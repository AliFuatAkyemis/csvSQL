package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Utility {
        //---------------Static Utility Methods--------------//

        //Table row counter
        public static int getRowCount(String filename) {
                try {
                        BufferedReader reader = new BufferedReader(new FileReader(filename));
                        String str = reader.readLine();

                        int i = 0;
                        while (str != null) {
                                i++;
                                str = reader.readLine();
                        }

                        reader.close();
                        
                        return i;
                } catch(IOException e) {
                        e.printStackTrace();
                }

                return -1;
        }

        //Table column counter
        public static int getColumnCount(String filename) {
                try {
                        BufferedReader reader = new BufferedReader(new FileReader(filename));
                        String str = reader.readLine();

                        reader.close();

                        return str.split(",").length;
                } catch(IOException e) {
                        e.printStackTrace();
                }
                
                return -1;
        }

        //Array copy method for 1D arrays
        public static void copyArray(String[] arr, String[] newArr) {
                for (int i = 0; i < arr.length && i < newArr.length; i++) {
                        newArr[i] = arr[i];
                }
        }

        //Array copy method for 2D arrays
        public static void copyArray2D(String[][] arr, String[][] newArr) {
                for (int i = 0; i < arr.length && i < newArr.length; i++) {
                        copyArray(arr[i], newArr[i]);
                }
        }
}
