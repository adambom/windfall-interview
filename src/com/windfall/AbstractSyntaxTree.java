package com.windfall;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class AbstractSyntaxTree {
    private Stack<FormulaOperator> operators = new Stack<FormulaOperator>();
    public LinkedList<FormulaToken> output = new LinkedList<FormulaToken>();
    public ArrayList<FormulaToken> references = new ArrayList<FormulaToken>();


    public AbstractSyntaxTree(String formula) {
        parse(formula);
    }


    private void parse(String formula) {
        int i = 0;

        ArrayList<FormulaToken> refs = new ArrayList<FormulaToken>();

        formula = Util.sanitizeFormula(formula);

        while (i < formula.length()) {
            FormulaToken token = readToken(formula.substring(i), i);

            if (token instanceof FormulaExpression) {
                addTokenToOutput(token);
            } else if (token instanceof FormulaOperator) {
                moveAllOperatorsToOutput();
                addTokenToOperators(token);
            } else {
                throw new Error("Invalid formula");
            }

            if (token instanceof FormulaCellReference) {
                references.add(token);
            }

            i += token.getLength();
        }

        moveAllOperatorsToOutput();
    }


    private FormulaToken readToken(CharSequence input, int index) {
        if (input.length() == 0) {
            return null;
        }

        String c = String.valueOf(input.charAt(0));


        if (Util.isAlpha(c)) {
            return parseTokenAsCellReference(input);
        }

        if (Util.isDigit(c)) {
            return parseTokenAsFloat(input);
        }

        if (Util.isOperator(c)) {
            if (checkForNegativeNumber(input, index)) {
                return parseTokenAsNegativeFloat(input);
            }

            return parseTokenAsOperator(input);
        }

        throw new Error(String.format("Invalid character, %s, in formula", c));
    }


    private FormulaCellReference parseTokenAsCellReference(CharSequence input) {
        StringBuilder ref = new StringBuilder();

        int i = 0;

        while (i < input.length() && Util.isAlpha(String.valueOf(input.charAt(i)))) {
            ref.append(input.charAt(i));
            i += 1;
        }

        while (i < input.length()) {
            char c = input.charAt(i);

            if (Util.isOperator(String.valueOf((c)))) {
                break;
            }

            ref.append(c);

            if (!Util.isDigit(String.valueOf(c))) {
                throw new Error(String.format("Invalid cell reference %s", ref.toString()));
            }

            i += 1;
        }

        return new FormulaCellReference(ref.toString());
    }


    private FormulaFloatExpression parseTokenAsFloat(CharSequence input) {
        StringBuilder digits = new StringBuilder();

        int i = 0;

        while (i < input.length() && Util.isDigit(String.valueOf(input.charAt(i)))) {
            digits.append(input.charAt(i));
            i += 1;
        }

        return new FormulaFloatExpression(digits.toString());
    }


    private Boolean checkForNegativeNumber(CharSequence input, int index) {
        if (input.length() < 2) {
            return false;
        }

        if (index == 0 && input.charAt(0) == '-' && Util.isDigit(String.valueOf(input.charAt(1)))) {
            return true;
        }

        return false;
    }


    private FormulaFloatExpression parseTokenAsNegativeFloat(CharSequence input) {
        StringBuilder digits = new StringBuilder();

        digits.append('-');

        int i = 1;

        while (i < input.length() && Util.isDigit(String.valueOf(input.charAt(i)))) {
            digits.append(input.charAt(i));
            i += 1;
        }

        return new FormulaFloatExpression(digits.toString());
    }


    private FormulaOperator parseTokenAsOperator(CharSequence input) {
        return FormulaOperator.factory(String.valueOf(input.charAt(0)));
    }


    private void addTokenToOutput(FormulaToken token) {
        output.add(token);
    }

    private void moveAllOperatorsToOutput() {
        while (!operators.empty()) {
            output.add(operators.pop());
        }
    }

    private void addTokenToOperators(FormulaToken token) {
        operators.push((FormulaOperator) token);
    }
}
