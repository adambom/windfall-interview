package com.windfall;

import java.util.ArrayList;

public class FormulaAdditionOperator extends FormulaOperator {
    public FormulaAdditionOperator(String value) {
        super(value);
    }

    public Float operate(Float... values) {
        assert values.length > 0;

        Float result = 0.0f;

        for (int i = 0; i < values.length; i++) {
            Float value = values[i];

            if (value == null) {
                continue;
            }

            result += value;
        }

        return result;
    }
}
