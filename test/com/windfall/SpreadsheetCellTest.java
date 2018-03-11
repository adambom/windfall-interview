package com.windfall;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class SpreadsheetCellTest {
    private  SpreadsheetCell cell;

    @Before
    public void setUp() throws Exception {
        cell = new SpreadsheetCell("A1", "3+5");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void update() {
        cell.update("4+9");

        Queue<FormulaToken> queue = cell.postfixQueue();

        FormulaToken token = queue.remove();

        Assert.assertEquals("4", token.value);

        token = queue.remove();

        Assert.assertEquals("9", token.value);

        token = queue.remove();

        Assert.assertEquals("+", token.value);
    }

    @Test
    public void cache() {
        cell.cache(10.0f);

        Assert.assertTrue(cell.getCachedValue() == 10.0f);
    }

    @Test
    public void getCachedValue() {
        cell.cache(10.0f);

        Assert.assertTrue(cell.getCachedValue() == 10.0f);
    }

    @Test
    public void clearCache() {
        cell.cache(10.0f);

        cell.clearCache();

        Assert.assertFalse(cell.hasCachedValue());
    }

    @Test
    public void hasCachedValue() {
        Assert.assertFalse(cell.hasCachedValue());

        cell.cache(10.0f);

        Assert.assertTrue(cell.hasCachedValue());
    }

    @Test
    public void getReferences() {
        cell.update("A1+A2");

        List<String> references = cell.getReferences();

        String ref = references.get(0);

        Assert.assertEquals("A1", ref);

        ref = references.get(1);

        Assert.assertEquals("A2", ref);

    }
}