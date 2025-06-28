package main;

import items.*;

public class Main {
        public static void main(String[] args) {
                Table t = new Table("deneme.csv", "test table");
                t.addColumn("isim");
                t.addColumn("soyisim");
                t.addColumn("numara");
        }
}
