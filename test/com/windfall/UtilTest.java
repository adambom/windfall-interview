package com.windfall;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void normalizeCellRef() {
        Assert.assertEquals(Util.normalizeCellRef("a1+b2+b3"), "A1+B2+B3");
    }

    @Test
    public void clearWhitespace() {
        Assert.assertEquals(Util.clearWhitespace("    a b c + d     "), "abc+d");
    }

    @Test
    public void isAlpha() {
        Assert.assertTrue(Util.isAlpha("a"));
        Assert.assertTrue(Util.isAlpha("A"));
        Assert.assertTrue(Util.isAlpha("b"));

        Assert.assertFalse(Util.isAlpha("+"));
        Assert.assertFalse(Util.isAlpha("-"));
        Assert.assertFalse(Util.isAlpha("1"));
        Assert.assertFalse(Util.isAlpha("2"));
    }

    @Test
    public void isDigit() {
        Assert.assertTrue(Util.isDigit("1"));
        Assert.assertTrue(Util.isDigit("2"));
        Assert.assertTrue(Util.isDigit("3"));

        Assert.assertFalse(Util.isDigit("+"));
        Assert.assertFalse(Util.isDigit("-"));
        Assert.assertFalse(Util.isDigit("a"));
        Assert.assertFalse(Util.isDigit("A"));
    }

    @Test
    public void isOperator() {
        Assert.assertTrue(Util.isOperator("+"));
        Assert.assertTrue(Util.isOperator("-"));

        Assert.assertFalse(Util.isOperator("1"));
        Assert.assertFalse(Util.isOperator("2"));
        Assert.assertFalse(Util.isOperator("a"));
        Assert.assertFalse(Util.isOperator("A"));
    }

    @Test
    public void toPrecision() {
        Assert.assertEquals(Util.toPrecision(9.9f, 2), "9.90");
        Assert.assertEquals(Util.toPrecision(9.2922f, 2), "9.29");
        Assert.assertEquals(Util.toPrecision(1029.0f, 2), "1029.00");
        Assert.assertEquals(Util.toPrecision(null, 2), "");

        Assert.assertEquals(Util.toPrecision(9.9f, 0), "10");
        Assert.assertEquals(Util.toPrecision(9.2922f, 0), "9");
        Assert.assertEquals(Util.toPrecision(1029.0f, 0), "1029");
        Assert.assertEquals(Util.toPrecision(null, 0), "");

        Assert.assertEquals(Util.toPrecision(9.9f, 3), "9.900");
        Assert.assertEquals(Util.toPrecision(9.2922f, 3), "9.292");
        Assert.assertEquals(Util.toPrecision(1029.0f, 3), "1029.000");
        Assert.assertEquals(Util.toPrecision(null, 3), "");
    }

    @Test
    public void sanitizeFormula() {
        Assert.assertEquals("-1+A1+A2+3-2+4-3-0+7", Util.sanitizeFormula("  -1 +A1 + A2 ++3 + -2 --4+-3---0+--+7 "));
    }

    @Test
    public void clearLeadingPlus() {
        Assert.assertEquals("1+2+3", Util.clearLeadingPlus("+1+2+3"));
    }
}