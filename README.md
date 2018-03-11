Windfall CSV Project
====================

This package implements my solution to the Windfall Data coding challenge.

# Approach

The Spreadsheet class is implemented as a directed graph, where the nodes represent cells in the sheet, and the edges
represent cell references. Edges point from the referencing cell to the referenced cell.

When a cell is instantiated, it builds an Abstract Syntax Tree (AST) representation of the formula it contains and
converts that representation to postfix notation, e.g. "2 + 3" becomes "2 3 +". I used Dijkstra's Shunting-yard
algorithm to build the AST.

To evaluate a cell, you can use the `Spreadsheet.eval` method, which takes the id of the cell you want to evaluate. This
method dequeues two operands from the cell's postfix queue, and recursively evaluates them both. It then combines the
values it has resolved using the specified operator. To improve efficiency of evaluating cell references, cells can
cache their value so that they don't need to be evaluated twice.

We can facilitate fast reads by using cached values, but this introduces the problem of invalidating the cache when a
cell's formula is updated. This is where the graph representation comes in. When we update a cell's formula, we do a
reverse breadth-first-search, looking at subsequent layers of parents, invalidating the cached values at each layer and
recomputing the values for each dependent node in the graph.

To load a file, you can use `Spreadsheet.load(path)`. You can also manually construct a spreadsheet. For an example of
the latter, see Main.java.

As far as testing goes, I wrote some really basic tests. I would have liked to be more thorough, but I've already put
plenty of time into this and a full test suite would probably add another couple of hours to the time it takes to
complete the project.