package com.windfall;

public class FormulaToken {
    public String value;
    private int length;

    public FormulaToken(String value) {
        this.value = value;
    }

    public int getLength() {
        return this.value.length();
    }
}
