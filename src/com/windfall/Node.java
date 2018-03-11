package com.windfall;

import java.util.ArrayList;

public class Node {
    public String id;
    public ArrayList<Edge> edges = new ArrayList<Edge>();
    public Object data;

    public Node(String id, Object data) {
        this.id = id;
        this.data = data;
    }


    public void addEdge(Edge edge) {
        if (!this.hasEdge(edge)) {
            edges.add(edge);
        }
    }


    public Boolean hasEdge(Edge edge) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).equals(edge)) {
                return true;
            }
        }

        return false;
    }


    public ArrayList<Node> children() {
        ArrayList<Node> children = new ArrayList<Node>();

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            if (edge.source.id == id) {
                children.add(edge.target);
            }
        }

        return children;
    }


    public ArrayList<Node> parents() {
        ArrayList<Node> parents = new ArrayList<Node>();

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            if (edge.target.id == id) {
                parents.add(edge.source);
            }
        }

        return parents;
    }


    public Boolean equals(Node node) {
        return node.id == id;
    }
}
