package com.windfall;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class SpreadsheetTest {
    private Spreadsheet sheet;

    @Before
    public void setUp() throws Exception {
        sheet = new Spreadsheet(2, 2);

        sheet.addNode(new SpreadsheetCell("A1", "B2+2"));
        sheet.addNode(new SpreadsheetCell("B1", "A1+A2"));
        sheet.addNode(new SpreadsheetCell("A2", "B2-3"));
        sheet.addNode(new SpreadsheetCell("B2", "7+5"));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void print() {
        Assert.assertEquals("14.00,23.00\n9.00,12.00", sheet.print());

        sheet = Spreadsheet.fromString(
                "4+9+2-2,-1+A1+22-9,4-A1,4-B3\n" +
                "B2+1   ,9         ,1-B1,A1\n" +
                "A2 + A4,          ,8+2 ,A3+A2\n" +
                "A1     ,C3 + C4   ,+A2+A3,B4\n");

        String expected = "13.00,25.00,-9.00,4.00\n" +
                "10.00,9.00,-24.00,13.00\n" +
                "23.00,,10.00,33.00\n" +
                "13.00,43.00,33.00,43.00";

        Assert.assertEquals(expected, sheet.print());
    }

    @Test
    public void eval() {
        Assert.assertEquals((Float)14.0f, sheet.eval("A1"));
        Assert.assertEquals((Float)23.0f, sheet.eval("B1"));
        Assert.assertEquals((Float)9.0f, sheet.eval("A2"));
        Assert.assertEquals((Float)12.0f, sheet.eval("B2"));
    }

    @Test
    public void getCell() {
        SpreadsheetCell a1 = sheet.getCell("A1");

        Assert.assertEquals("A1", a1.id);

        a1 = sheet.getCell("a1");

        Assert.assertEquals("A1", a1.id);
    }

    @Test
    public void setCell() {
        sheet.setCell("B2", "9+10");

        Assert.assertEquals((Float)21.0f, sheet.eval("A1"));
        Assert.assertEquals((Float)37.0f, sheet.eval("B1"));
        Assert.assertEquals((Float)16.0f, sheet.eval("A2"));
        Assert.assertEquals((Float)19.0f, sheet.eval("B2"));
    }

    @Test
    public void bootstrapCellEdges() {
        sheet.bootstrapCellEdges();

        Assert.assertEquals(4, sheet.edges.size());

        HashSet<Edge> expected = new HashSet<Edge>();
    }
}