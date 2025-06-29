package utility;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Utility {
        //---------------Static Utility Methods--------------//

        //Table row counter
        public static int getRowCount(String filename) { //Time Complexity -> O(n)
                try {
                        //File existence check
                        File file = new File(filename);
                        if (!file.exists()) return 0;

                        //Counting part
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        reader.readLine();
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

                return 0;
        }

        //Table column counter
        public static int getColumnCount(String filename) { //Time Complexity -> O(1)
                try {
                        //File existence check
                        File file = new File(filename);
                        if (!file.exists()) return 0;
                        
                        //Counting part
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String str = reader.readLine();

                        reader.close();
 
                        return str == null ? 0 : str.split(",").length; //If file is empty then, return 0
                } catch(IOException e) {
                        e.printStackTrace();
                }
                
                return 0;
        }

        //Array copy method for 1D arrays
        public static String[] copyArray(String[] arr, String[] newArr) { //Time Complexity -> O(n)
                for (int i = 0; i < arr.length && i < newArr.length; i++) {
                        newArr[i] = arr[i]; //Updating the new array
                }

                return newArr; //return new array
        }

        //Array copy method for 2D arrays
        public static String[][] copyArray2D(String[][] arr, String[][] newArr) { //Time Complexity -> O(n^2)
                for (int i = 0; i < arr.length && i < newArr.length; i++) {
                        newArr[i] = copyArray(arr[i], newArr[i]); //Use previous method to upscale the solution
                }

                return newArr; //return new array
        }
}
