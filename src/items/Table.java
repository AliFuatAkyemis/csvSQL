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
        public Table(String filename, String tableName){
                //Initializing the table
                this.filename = filename;
                this.tableName = tableName;
                
                //Check for file existence
                loadTable();
        }

        //----------------Required methods------------------//
       
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

                //Size adjustment
                columns = Utility.copyArray(columns, new String[columns.length-1]);
                records = Utility.copyArray2D(records, new String[records.length][columns.length-1]);

                syncTable(); //Apply changes to file
        }

        //Insert method to insert a new record
        public void insert(String[] values) {}

        //Delete method to delete a record
        public void delete(String columnName, String value) {}

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
                        writer.write(String.join(",", columns) + "\n"); //Column names
                       
                        //Records
                        for (String[] row : records) {
                                writer.write(String.join(",", row) + "\n");
                        }

                        writer.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
