package com.windfall;

public class Main {

    public static void main(String[] args) {
        Spreadsheet s = new Spreadsheet(3, 2);

        s.addNode(new SpreadsheetCell("A1", "B2+2"));
        s.addNode(new SpreadsheetCell("B1", "A1+A2"));
        s.addNode(new SpreadsheetCell("A2", "B2-3"));
        s.addNode(new SpreadsheetCell("B2", "7+5"));
        s.addNode(new SpreadsheetCell("A3", "A1+B1-A2"));

        s.bootstrapCellEdges();

        System.out.println(s.print());

        s.setCell("B2", "9+9");

        System.out.println(s.print());
    }
}
