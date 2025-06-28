package items;

import utility.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

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
                if (new File(filename).exists()) loadTable();
                else createTable();
        }

        //----------------Required methods------------------//
       
        //Add method to add a new column to table
        public void addColumn(String columnName) {

        }

        //Drop method to drop a column entirely
        public void dropColumn(String columnName) {}

        //Insert method to insert a new record
        public void insert(String[] values) {}

        //Delete method to delete a record
        public void delete(String columnName, String value) {}

        //Table load function to update informations from file
        private void loadTable() {
                int col = Utility.getColumnCount(filename), row = Utility.getRowCount(filename); //Determining table size

                //Initializing table space
                columns = new String[col];
                records = new String[col][row];

                //Read and load part
                try {
                        BufferedReader reader = new BufferedReader(new FileReader(filename));

                        String str = reader.readLine();
                        columns = str.split(",");
                        str = reader.readLine();

                        int i = 0;
                        while (str != null) {
                                records[i++] = str.split(",");
                                str = reader.readLine();
                        }

                        reader.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        //Table create method for the sitch of non-existence
        private void createTable() {
                
        }

        //Table syncronize method to update .csv file
        private void syncTable() {
                try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                        writer.write(String.join(",", columns));
                        
                        for (int i = 0; i < records.length; i++) {
                                writer.write(String.join(",", records[i]));
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
