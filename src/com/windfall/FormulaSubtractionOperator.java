package com.windfall;

import java.util.ArrayList;

public class FormulaSubtractionOperator extends FormulaOperator {
    public FormulaSubtractionOperator(String value) {
        super(value);
    }

    public Float operate(Float... values) {
        assert values.length > 1;

        Float result = values[values.length - 1];

        if (result == null) {
            result = 0.0f;
        }

        for (int i = 0; i < values.length - 1; i++) {
            Float value = values[i];

            if (value == null) {
                continue;
            }

            result -= value;
        }

        return result;
    }
}