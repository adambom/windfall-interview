package com.windfall;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Spreadsheet extends Graph {
    public int rows;
    public int columns;

    private static final String[] LABELS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");

    public static void main(String[] args) {
        assert args.length == 1;
        Spreadsheet sheet = Spreadsheet.load(args[0]);

        System.out.println(sheet.print());
    }

    public Spreadsheet(int rows, int columns) {
        super();

        if (columns > 26) {
            throw new Error("Spreadsheet only supports up to 26 columns");
        }

        this.rows = rows;
        this.columns = columns;
    }


    public static Spreadsheet load(String path) {
        BufferedReader reader = null;
        StringBuilder lines = new StringBuilder();

        try {
            reader = new BufferedReader(new FileReader(path));

            String line;

            while ((line = reader.readLine()) != null) {
                lines.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return Spreadsheet.fromString(lines.toString());
    }


    public static Spreadsheet fromString(String input) {
        List<List<String>> rows = parseInput(input);

        if (rows.size() == 0) {
            return null;
        }

        int n = rows.size();
        int m = rows.get(0).size();

        Spreadsheet sheet = new Spreadsheet(n, m);

        fillSheet(sheet, rows);

        return sheet;
    }


    private static List<List<String>> parseInput(String input) {
        List<List<String>> rows = new ArrayList<List<String>>();

        int numCols = -1;

        for (String line : input.split("\n")) {
            String[] row = line.split(",");

            if (numCols == -1) {
                numCols = row.length;
            } else {
                if (row.length != numCols) {
                    throw new Error("All columns must have the same number of rows");
                }
            }

            rows.add(Arrays.asList(row));
        }

        return rows;
    }


    private static void fillSheet(Spreadsheet s, List<List<String>> rows) {
        for (int i = 1; i <= s.rows; i++) {
            for (int j = 0; j < s.columns; j++) {
                String columnName = LABELS[j];
                String cellReference = String.format("%s%d", columnName, i);

                s.addNode(new SpreadsheetCell(cellReference, rows.get(i - 1).get(j)));
            }
        }

        s.bootstrapCellEdges();
    }


    public String print() {
        String[] rows = new String[this.rows];

        for (int i = 1; i <= this.rows; i++) {
            String[] row = new String[this.columns];

            for (int j = 0; j < this.columns; j++) {
                String label = LABELS[j];
                Float value = eval(String.format("%s%d", label, i));

                row[j] = Util.toPrecision(value, 2);
            }

            rows[i - 1] = String.join(",", row);
        }

        return String.join("\n", rows);
    }


    public Float eval(String cellReference) {
        HashSet<String> visited = new HashSet<String>();

        return eval(cellReference, visited);
    }


    private Float eval(String cellReference, Set<String> visited) {
        cellReference = Util.normalizeCellRef(cellReference);

        if (visited.contains(cellReference)) {
            throw new Error("Circular reference in cell formula detected. Exiting.");
        }

        visited.add(cellReference);

        SpreadsheetCell cell = (SpreadsheetCell)nodes.get(cellReference);

        if (cell == null) {
            return null;
        }

        if (cell.hasCachedValue()) {
            return cell.getCachedValue();
        }

        Float value = computeCellValue(cell, visited);

        if (value != null) {
            cell.cache(value);
        }

        return value;
    }


    private Float computeCellValue(SpreadsheetCell cell, Set<String> visited) {
        Stack<Float> operandStack = new Stack<Float>();
        Queue<FormulaToken> postfixQueue = cell.postfixQueue();

        if (postfixQueue.size() == 0) {
            return null;
        }

        while (postfixQueue.size() > 0) {
            FormulaToken token = postfixQueue.remove();

            if (token instanceof FormulaFloatExpression) {
                operandStack.push(((FormulaFloatExpression) token).getValueAsFloat());
            }

            if (token instanceof FormulaCellReference) {
                operandStack.push(this.eval(token.value, visited));
            }

            if (token instanceof FormulaOperator) {
                Float operand1 = operandStack.pop();
                Float operand2 = operandStack.pop();

                operandStack.push(((FormulaOperator) token).operate(operand1, operand2));
            }
        }

        return operandStack.pop();
    }


    public SpreadsheetCell getCell(String cellReference) {
        cellReference = Util.normalizeCellRef(cellReference);

        if (nodes.containsKey(cellReference)) {
            return (SpreadsheetCell)nodes.get(cellReference);
        }

        return null;
    }


    private SpreadsheetCell createOrUpdateCell(String cellReference, String formula) {
        SpreadsheetCell cell = getCell(cellReference);

        if (cell != null) {
            clearEdgesForCell(cell);
            cell.update(formula);
        } else {
            cell = new SpreadsheetCell(cellReference, formula);
            addNode(cell);
        }

        addEdgesForCell(cell);

        return cell;
    }


    private void clearEdgesForCell(SpreadsheetCell cell) {
        cell.edges.forEach((Edge edge) -> {
            removeEdge(edge.source, edge.target);
        });
    }


    private void addEdgesForCell(SpreadsheetCell src) {
        src.getReferences().forEach((String ref) -> {
            SpreadsheetCell tgt = getCell(ref);

            if (tgt == null) {
                throw new Error(String.format("Invalid cell reference in formula: %s", ref));
            }

            addEdge(src, tgt, null);
        });
    }


    public void setCell(String cellReference, String formula) {
        cellReference = Util.normalizeCellRef(cellReference);

        SpreadsheetCell cell = createOrUpdateCell(cellReference, formula);

        LinkedList<Node> q = new LinkedList<Node>();

        HashSet<String> visited = new HashSet<String>();

        eval(cell.id);
        visited.add(cell.id);

        q.add(cell);

        while (q.size() > 0) {
            Node root = q.remove();

            root.parents().forEach((Node node) -> {
                if (!visited.contains(node.id)) {
                    ((SpreadsheetCell)node).clearCache();
                    eval(node.id);
                    visited.add(node.id);

                    q.add(node);
                }
            });
        }
    }

    public void bootstrapCellEdges() {
        for (Node node : nodes.values()) {
            addEdgesForCell((SpreadsheetCell) node);
        }
    }
}
