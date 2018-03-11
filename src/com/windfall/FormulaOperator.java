package com.windfall;

import java.util.ArrayList;

public abstract class FormulaOperator extends FormulaToken {
    public FormulaOperator(String value) {
        super(value);
    }


    public static FormulaOperator factory(String operator) {
        assert Util.isOperator(operator);

        switch (operator) {
            case "+":
                return new FormulaAdditionOperator(operator);
            case "-":
                return new FormulaSubtractionOperator(operator);
            default:
                return null;
        }
    }

    public abstract Float operate(Float... values);
}