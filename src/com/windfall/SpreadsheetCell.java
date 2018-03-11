package com.windfall;

import java.util.ArrayList;
import java.util.Queue;

public class SpreadsheetCell extends Node {
    private Float cachedValue;
    private String formula;
    private AbstractSyntaxTree ast;

    public SpreadsheetCell(String id, String formula) {
        super(Util.normalizeCellRef(id), null);
        this.formula = formula;
        this.ast = new AbstractSyntaxTree(formula);
    }


    public void update(String formula) {
        clearCache();
        this.formula = formula;
        ast = new AbstractSyntaxTree(formula);
    }


    public void cache(float value) {
        cachedValue = value;
    }


    public Float getCachedValue() {
        return cachedValue;
    }


    public void clearCache() {
        cachedValue = null;
    }


    public Boolean hasCachedValue() {
        return cachedValue != null;
    }


    public ArrayList<String> getReferences() {
        ArrayList<String> references = new ArrayList<String>();

        for (int i = 0; i < ast.references.size(); i++) {
            references.add(ast.references.get(i).value);
        }

        return references;
    }


    public Queue<FormulaToken> postfixQueue() {
        return (Queue<FormulaToken>)ast.output.clone();
    }
}