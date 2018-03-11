package com.windfall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph {
    public HashMap<String, Node> nodes = new HashMap<String, Node>();
    public ArrayList<Edge> edges = new ArrayList<Edge>();

    public Node addNode(Node node) {
        if (this.hasNode(node)) {
            return nodes.get(node.id);
        }

        nodes.put(node.id, node);

        return nodes.get(node.id);
    }


    public Boolean hasNode(Node node) {
        return nodes.containsKey(node.id);
    }


    public Boolean hasNode(String id) {
        return nodes.containsKey(id);
    }


    public Edge addEdge(Node src, Node tgt, Object data) {
        Edge edge = new Edge(src, tgt, data);

        if (this.hasEdge(src, tgt)) {
            return null;
        }

        src.addEdge(edge);
        tgt.addEdge(edge);

        edges.add(edge);

        return edge;
    }


    public Boolean hasEdge(Node node1, Node node2) {
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            if (edge.source.id == node1.id && edge.target.id == node2.id) {
                return true;
            }
        }

        return false;
    }


    public void removeEdge(Node src, Node tgt) {
        ArrayList<Edge> filtered = new ArrayList<Edge>();

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            if (edge.target.id == tgt.id && edge.source.id == src.id) {
                filtered.add(edge);
            }
        }

        for (Node node : nodes.values()) {
            ArrayList<Edge> filteredForNode = new ArrayList<Edge>();

            for (int i = 0; i < node.edges.size(); i++) {
                filteredForNode.add(node.edges.get(i));
            }
        }
    }
}
