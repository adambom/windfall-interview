package com.windfall;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class Util {
    public static String normalizeCellRef(String ref) {
        return ref.toUpperCase();
    }


    public static String sanitizeFormula(String formula) {
        return clearLeadingPlus(eliminateDuplicateOperators(clearWhitespace(formula)));
    }


    public static String clearWhitespace(String input) {
        return input.replaceAll("\\s", "");
    }


    public static Boolean isAlpha(String s) {
        return s.matches("[A-z]+");
    }


    public static Boolean isDigit(String s) {
        return s.matches("[0-9]+");
    }


    public static Boolean isOperator(String s) {
        return s.matches("\\+|\\-");
    }

    public static String toPrecision(Float f, int precision) {
        if (f == null) {
            return "";
        }

        StringBuilder format = new StringBuilder();

        format.append("0");

        if (precision > 0) {
            format.append(".");
        }

        for (int i = 0; i < precision; i++) {
            format.append("0");
        }

        DecimalFormat formatter = new DecimalFormat(format.toString());

        return formatter.format(f);
    }

    private static final Pattern duplicateOperatorsPattern = Pattern.compile("\\-\\-|\\-\\+|\\+\\-|[+]{2,}");

    // Could be made a lot more efficient
    public static String eliminateDuplicateOperators(String formula) {
        while (duplicateOperatorsPattern.matcher(formula).find()) {
            formula = formula.replaceAll("\\-\\+", "-");
            formula = formula.replaceAll("\\+\\-", "-");
            formula = formula.replaceAll("[+]{2,}", "+");
            formula = formula.replaceAll("\\-\\-", "+");
        }

        return formula;
    }


    public static String clearLeadingPlus(String formula) {
        if (formula == null || formula.length() < 1) {
            return formula;
        }
        if (formula.charAt(0) == '+') {
            return formula.substring(1);
        }
        return formula;
    }
}
