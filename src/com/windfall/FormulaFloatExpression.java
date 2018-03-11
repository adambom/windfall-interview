package com.windfall;


public class FormulaFloatExpression extends FormulaExpression {
    public FormulaFloatExpression(String value) {
        super(value);
    }

    public Float getValueAsFloat() {
        return Float.parseFloat(value);
    }
}
