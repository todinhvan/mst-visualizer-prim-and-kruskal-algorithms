package com.mst.models;

import javafx.scene.control.Label;

public class Vertex {
    private Label vertexLabel;
    private boolean isVisited;

    public Vertex() {
        this.vertexLabel = new Label();
        this.isVisited = false;
    }

    public Vertex(Vertex vertex) {
        this.vertexLabel = vertex.getVertexLabel();
        this.isVisited = vertex.isVisited();
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public Label getVertexLabel() {
        return vertexLabel;
    }

    public void setVertexLabel(Label vertexLabel) {
        this.vertexLabel = vertexLabel;
    }

    public String getName() {
        return this.getVertexLabel().getText();
    }
}
