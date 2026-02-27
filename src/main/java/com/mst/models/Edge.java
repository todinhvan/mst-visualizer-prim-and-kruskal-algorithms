package com.mst.models;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class Edge {
    private Vertex startVertex;
    private Vertex endVertex;
    private int weight;

    private Label weightLabel;
    private Line lineEdge;

    public Edge() {
        this.startVertex = new Vertex();
        this.endVertex = new Vertex();
        this.weight = 1;
        this.weightLabel = new Label("1");
        this.lineEdge = new Line();
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public void setEndVertex(Vertex endVertex) {
        this.endVertex = endVertex;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Label getWeightLabel() {
        return weightLabel;
    }

    public void setWeightLabel(Label weightLabel) {
        this.weightLabel = weightLabel;
    }

    public Line getLineEdge() {
        return lineEdge;
    }

    public void setLineEdge(Line lineEdge) {
        this.lineEdge = lineEdge;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "startVertex=" + startVertex +
                ", endVertex=" + endVertex +
                ", weight=" + weight +
                ", weightLabel=" + weightLabel +
                ", lineEdge=" + lineEdge +
                '}';
    }
}
