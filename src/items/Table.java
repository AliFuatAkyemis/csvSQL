package items;

import utility.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Table {
        //Attributes
        private String filename = "(Empty)";
        private String tableName = "(Empty)";

        private String[] columns; //For column names
        private String[][] records; //For records of column attributes

        //Constructor
        public Table(String filename){ 
                this.filename = filename; //Initializing the table
                loadTable(); //Load informations from file
        }

        //----------------Required methods------------------//
        
        //Setter method for tableName
        public void setTableName(String tableName) {
                this.tableName = tableName;
        }

        //Getter method for tableName
        public String getTableName() {
                return this.tableName;
        }

        //Add method to add a new column to table
        public void addColumn(String columnName) { //Time Complexity -> O(n)
                loadTable(); //Refresh informations from file
                
                //Check for duplicate
                for (String str : columns)
                        if (str.equals(columnName)) return;

                //Addition part
                columns = Utility.copyArray(columns, new String[columns.length+1]);
                records = Utility.copyArray2D(records, new String[records.length][columns.length+1]);

                columns[columns.length-1] = columnName; //Update new column name
                
                syncTable(); //Apply changes to file
        }

        //Drop method to drop a column entirely
        public void dropColumn(String columnName) { //Time Complexity -> O(n^2)
                loadTable(); //Refresh informations from file

                if (columns.length == 0) return;

                //Search phase
                boolean state = false; //Switch state to control program flow during search action
                for (int i = 0; i < columns.length; i++) {
                        if (!state) state = columns[i].equals(columnName) ? true : false; //Check
                        else {
                                //Tilting rest of the columns through the deleted column's space
                                columns[i-1] = columns[i];
                                for (String[] record : records) {
                                        record[i-1] = record[i];
                                }
                        }
                }

                if (state) {
                        int col = columns.length-1;

                        //Size adjustment
                        columns = Utility.copyArray(columns, new String[col]);
                        records = Utility.copyArray2D(records, new String[records.length][col]);
                }

                syncTable(); //Apply changes to file
        }

        //Insert method to insert a new record
        public void insert(String... values) { //Time Complexity -> O(n)
                loadTable(); //Refresh informaitons from file

                if (values.length != columns.length) return; //If values size doesn't match with columns size then, exit

                records = Utility.copyArray2D(records, new String[records.length+1][columns.length]); //Resizing records array for new insertion
                records[records.length-1] = values; //Insertion part

                syncTable(); //Apply changes to file
        }

        //Delete method to delete a record
        public void delete(String columnName, String value) { //Time Complexity -> O(n)
                loadTable(); //Refresh informations from file

                if (records.length == 0) return; //If table is empty then, exit

                int k = -1; //A variable to point the desired column index with initial value of -1

                //Column search
                for (int i = 0; i < columns.length; i++) {
                        if (columns[i].equals(columnName)) {
                                k = i;
                                break;
                        }
                }

                if (k == -1) return; //If column not found then, exit

                //Row search
                for (int i = 0; i < records.length; i++) {
                        if (records[i][k].equals(value)) records[i] = null; //Define as null to delete later
                }

                //Restructuring the records array
                String[][] arr = new String[records.length][columns.length]; //Define a new array to use temporary space(This will replace the old array)
                int n = 0; //Null counter
                for (int i = 0, j = 0; i < records.length; i++) {
                        if (records[i] != null) arr[j++] = records[i]; //Fill the new array
                        else n++; //increment null counter
                }
                records = arr; //Replace

                records = Utility.copyArray2D(records, new String[records.length-n][columns.length]); //Resizing to shrink array size

                syncTable(); //Apply changes to file
        }

        //Update method to update informations
        public void update(String columnName, String value, String updateParameter, String updateValue) {
                loadTable(); //Refresh informations from file

                if (records.length == 0) return; //If table is empty then, exit

                int k = -1, n = -1; //Pointers for required columns
                for (int i = 0; i < columns.length; i++) {
                        //If found any match update parameters
                        if (k == -1) k = columns[i].equals(columnName) ? i : -1; 
                        if (n == -1) n = columns[i].equals(updateParameter) ? i : -1;
                }

                if (k == -1 || n == -1 || k == n) return; //Exit conditions (Not found etc.)

                //Update part
                for (String[] record : records) {
                        if (record[k].equals(value)) record[n] = updateValue;
                }

                syncTable(); //Apply changes to file
        }

        //Overload for select(String[] columnName, String[] values)
        public String[][] select() {
                loadTable(); //Refresh informations from file

                display(); //Display all the informations

                String[][] arr = Utility.copyArray2D(records, new String[records.length][columns.length]); //Clone records array to protect its reference from user

                return arr; //Return clone array
        }

        //Select method to get informations
        public String[][] select(String[] columnName, String[] values) {
                loadTable(); //Refresh informations from file
               
                if (columnName == null || values == null);

                int[] index = new int[columnName.length];
                int n = 0;
                for (int i = 0; i < columns.length; i++) {
                        for (int k = 0; k < columnName.length; k++) {
                                if (columns[i].equals(columnName[k])) index[n++] = i;
                        }
                }

                if (n < columnName.length) return null;

                String[][] response = new String[0][columns.length];
                for (int i = 0, k = 0; i < records.length; i++) {
                        boolean state = true;
                        for (int j = 0; j < index.length; j++) {
                                if (!records[i][index[j]].equals(values[j])) {
                                        state = false;
                                        break;
                                }
                        }
                        
                        if (state) {
                                response = Utility.copyArray2D(response, new String[response.length+1][columns.length]);
                                response[k++] = records[i];
                        }
                }
                
                display(columns, response);

                return response;
        }

        //Overload for display(String[][] matrix)
        public void display() {
                display(columns, records); //Assigned by default
        }

        //Display method for representation of data
        private void display(String[] attributes, String[][] matrix) { //Time Complexity -> O(n^2)
                loadTable(); //Refresh informations from file

                int max = 0; //Max row size variable
                
                //Start with header String lenght
                for (String str : attributes) max += str.length();

                //Search for row that has maximum String length
                for (String[] arr : matrix) {
                        int i = 0; //Counter for each row
                        
                        //Iterate for each row
                        for (String str : arr) i += str.length();

                        //Update counter
                        if (i > max) max = i;
                }

                //Calculate actual needed length by adding other symbols length
                max += 3*columns.length;

                //Display part
                //--------------Header----------------//
                System.out.println("-".repeat(max));
                String row = "| ";
                for (String str : attributes) row += (str + " | ");
                System.out.println(row.trim());
               
                //-------------Body------------------//
                System.out.println("-".repeat(max));
                for (String[] record : matrix) {
                        row = "| ";
                        for (String str : record) row += (str + " | ");
                        row = row.trim() + "\n";
                        System.out.println(row.trim());
                        System.out.println("-".repeat(max));
                }
        }

        //Table load method to update informations from file
        private void loadTable() { //Time Complexity -> O(n)
                try {
                        int col = Utility.getColumnCount(filename), row = Utility.getRowCount(filename); //Determining table size
        
                        //Initializing table space
                        columns = new String[col];
                        records = new String[row][col];

                        //Read and load part
                        BufferedReader reader = new BufferedReader(new FileReader(filename));
                        String str = reader.readLine();

                        if (str == null) {
                                reader.close();
                                return;
                        }

                        columns = str.split(",");
                        str = reader.readLine();

                        int i = 0;
                        while (str != null) {
                                records[i++] = str.split(",");
                                str = reader.readLine();
                        }
                        
                        reader.close();
                } catch (FileNotFoundException e) {
                        try {
                                new File(filename).createNewFile();
                        } catch (IOException e2) {
                                e.printStackTrace();
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        //Table syncronize method to update file content
        private void syncTable() { //Time Complexity -> O(n)
                try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                        String str = ""; //Text container
                        
                        str += String.join(",", columns) + "\n"; //Column names
                       
                        for (String[] row : records) {
                                str += String.join(",", row) + "\n"; //Records
                        }

                        writer.write(str.trim()); //Writting by trim()
                        writer.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
