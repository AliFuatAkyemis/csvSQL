package items;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Table implements Relation {
        private String tableName = "TableNamePlaceHolder";
        private String[] columns; 
        private Object[] records;

        //Constructor
        public Table(String filename){
                try {
                        BufferedReader reader = new BufferedReader(new FileReader(filename));
                        String str = reader.nextLine();
                        columns = str.split(",");
                        str = reader.nextLine();

                        while (str != null) {
                                String[] temp = str.split(",");
                                for (int i = 0; i < temp.lenght; i++) {
                                        
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        //Methods
        public void addColumn(String columnName, Class type) {}

        public void dropColumn(String columnName, Class type) {}

        public void insert(Object[] values) {}

        public void delete(String columnName, Object value) {}
}
