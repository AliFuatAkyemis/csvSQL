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
        public void update() {}

        //Select method to get informations
        public void select() {}

        //Display method for representation of data
        public void display() {
                int max = 0;
                for (String[] record : records) {
                        int i = 0;
                        for (String str : record) {
                                i += str.length();
                        }
                        if (i > max) max = i;
                }
                max += 3*columns.length-2;
                System.out.println("-".repeat(max));
                String row = "| ";
                for (String str : columns) row += (str + " | ");
                System.out.println(row.trim());
                System.out.println("-".repeat(max));
                for (String[] record : records) {
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
