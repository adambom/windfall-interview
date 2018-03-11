package com.windfall;

public class Edge {
    public Node source;
    public Node target;
    public Object data;

    public Edge(Node source, Node target, Object data) {
        this.source = source;
        this.target = target;
        this.data = data;
    }


    public Boolean equals(Edge edge) {
        return edge.source.id == source.id && edge.target.id == target.id;
    }
}
