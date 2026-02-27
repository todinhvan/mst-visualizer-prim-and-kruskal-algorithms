package com.mst.services;

import com.mst.controllers.HomeController;
import com.mst.models.Edge;
import com.mst.models.Graph;
import com.mst.models.Vertex;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class FileService {
    public static HomeController home;
     Pane mainPane;

    public FileService() {
        home = null;
        mainPane = null;
    }

    public FileService(HomeController home, Pane mainPane) {
        this.home = home;
        this.mainPane = mainPane;
    }

    public void saveGraph(MouseEvent me, Graph graph, TextField notifyField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu Đồ Thị");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));

        File fileSelected = fileChooser.showSaveDialog(((Node)me.getSource()).getScene().getWindow());
        if (fileSelected != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileSelected));

                writer.write("Số đỉnh: " + graph.getVertexList().size() + "\n");
                writer.write("Số cung: " + graph.getEdgeList().size() + "\n");
                writer.newLine();

                writer.write("============= Đỉnh =============\n");
                for (Vertex vertex : graph.getVertexList()) {
                    writer.write(vertex.getName() + "\t" + vertex.getVertexLabel().getLayoutX() + "\t" + vertex.getVertexLabel().getLayoutY() + "\n");
                }
                writer.newLine();

                writer.write("========== Cung và Trọng Số ==========\n");
                for (Edge edge : graph.getEdgeList()) {
                    writer.write(edge.getStartVertex().getName() + "\t" + edge.getEndVertex().getName() + "\t" + edge.getWeight() + "\n");
                }

                if (notifyField != null) {
                    writer.newLine();

                    writer.write("========== Kết quả thuật toán ==========\n");
                    writer.write(notifyField.getText() + "\n");
                }

                showAlert(Alert.AlertType.INFORMATION, "Save Graph File", null, fileSelected.getName() + " đã được lưu thành công!");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    public void openGraph(MouseEvent me, Graph graph) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Mở Đồ Thị Từ File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));

        File fileSelected = fileChooser.showOpenDialog(((Node)me.getSource()).getScene().getWindow());
        if (fileSelected != null) {
            try {
                graph.resetGraph();

                BufferedReader reader = new BufferedReader(new FileReader(fileSelected));

                String line;
                int vertexNumber = 0;
                int edgeNumber = 0;
                int lineCount = 0;

                while ((line = reader.readLine()) != null) {
                    if (lineCount == 0) {
                        vertexNumber = Integer.parseInt(line.split(" ")[line.split(" ").length - 1]);
                    }else if (lineCount == 1) {
                        edgeNumber = Integer.parseInt(line.split(" ")[line.split(" ").length - 1]);
                    }else if (lineCount >= 4 && lineCount <= vertexNumber + 3) {
                        String[] parts = line.split("\t");
                        graph.getVertexList().add(initVertexFromFile(parts));
                        Label vertexLabel = graph.getVertexList().getLast().getVertexLabel();
                        mainPane.getChildren().add(vertexLabel);
                        home.addEventToVertex(vertexLabel);

                        graph.getVertexNameList().remove(parts[0]);
                    } else if (lineCount >= (vertexNumber + 6) && lineCount <= (edgeNumber + vertexNumber + 5)){
                        String[] parts = line.split("\t");
                        addEdge(graph, parts);
                    }
                    lineCount++;
                }

                reader.close();
                showAlert(Alert.AlertType.INFORMATION, "Open Graph File", null, fileSelected.getName() + " đã được mở thành công!");
            } catch(IOException e) {
                throw new RuntimeException();
            }
        }
    }

    public void addEdge(Graph graph, String[] parts) {
        Vertex startVertex = graph.getVertexByStringName(parts[0].trim());
        Vertex endVertex = graph.getVertexByStringName(parts[1].trim());

        if (startVertex != null && endVertex != null) {
            int weight = Integer.parseInt(parts[2].trim());

            double[] edgeCoordinates = home.calculateCoordinates(startVertex.getVertexLabel(), endVertex.getVertexLabel(), 30);

            Line edgeLine = new Line();
            edgeLine.setStartX(edgeCoordinates[0]);
            edgeLine.setStartY(edgeCoordinates[1]);
            edgeLine.setEndX(edgeCoordinates[2]);
            edgeLine.setEndY(edgeCoordinates[3]);
            edgeLine.setStyle("-fx-stroke-width: 2px;");

            Edge edge = new Edge();
            edge.setLineEdge(edgeLine);
            edge.setStartVertex(startVertex);
            edge.setEndVertex(endVertex);
            edge.setWeight(weight);

            Label weightLabel = new Label();
            weightLabel.getStyleClass().add("weightLabel");
            weightLabel.setText(String.valueOf(weight));
            weightLabel.setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2.0);
            weightLabel.setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2.0);

            edge.setWeightLabel(weightLabel);

            graph.getEdgeList().add(edge);

            mainPane.getChildren().add(edgeLine);
            home.addEventToEdge(edgeLine);
            mainPane.getChildren().add(weightLabel);
            home.addEventToWeight(weightLabel);
        }
    }

    public Vertex initVertexFromFile(String[] parts) {
        Label vertexLabel = new Label(parts[0]);
        vertexLabel.setId("vertexLabel");
        vertexLabel.setFont(new Font("System Bold", 24));
        vertexLabel.setLayoutX(Double.parseDouble(parts[1]));
        vertexLabel.setLayoutY(Double.parseDouble(parts[2]));

        Vertex vertex = new Vertex();
        vertex.setVertexLabel(vertexLabel);
        vertex.setVisited(false);

        return vertex;
    }

    public void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
