## csvSQL
A database manager that is created for my personal use.

It has essential methods that is inspired from common sql dynamics.

## Table Management System

A simple Java implementation for managing tabular data with file persistence.

## Features

- Create and manage tables with columns and records
- Add/drop columns
- Insert/delete/update records
- Select and display data
- File-based persistence (CSV format)
- Utility methods for array operations

## Classes

## Table Class
Main class representing a table with:
- File operations (load/sync)
- Column management (add/drop)
- Record operations (insert/delete/update/select)
- Display functionality

## Utility Class
Helper class with static methods for:
- File operations (row/column counting)
- Array operations (1D and 2D copying)

## Usage

1. Create a table: `Table myTable = new Table("data.csv");`
2. Set table name: `myTable.setTableName("Employees");`
3. Add columns: `myTable.addColumn("Name");`
4. Insert records: `myTable.insert("John", "Doe", "30");`
5. Query data: `String[][] results = myTable.select();`

## File Format
Data is stored in CSV format:
- First line contains column names
- Subsequent lines contain records

## Limitations
- No data type validation
- Basic error handling
- No indexing or advanced query capabilities

## UML-Diagram
classDiagram
    class Utility {
        <<utility>>
        +getRowCount(String filename) int
        +getColumnCount(String filename) int
        +copyArray(String[] arr, String[] newArr) String[]
        +copyArray2D(String[][] arr, String[][] newArr) String[][]
    }

    class Table {
        -filename: String
        -tableName: String
        -columns: String[]
        -records: String[][]
        +Table(filename: String)
        +setTableName(name: String)
        +getTableName() String
        +addColumn(columnName: String)
        +dropColumn(columnName: String)
        +insert(values: String...)
        +delete(columnName: String, value: String)
        +update(columnName: String, value: String, updateParameter: String, updateValue: String)
        +select() String[][]
        +select(columnName: String[], values: String[]) String[][]
        +display()
        -display(attributes: String[], matrix: String[][])
        -loadTable()
        -syncTable()
    }

    Table --> Utility : uses
