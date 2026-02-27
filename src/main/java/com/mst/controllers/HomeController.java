package com.mst.controllers;

import com.mst.Main;
import com.mst.models.Edge;
import com.mst.models.Graph;
import com.mst.models.Vertex;
import com.mst.services.FileService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {
    @FXML
    public Pane mainPane;
    @FXML
    public Canvas canvas;

    @FXML
    public Button addVertexBtn;
    @FXML
    public Button addEdgeBtn;
    @FXML
    public Button moveBtn;
    @FXML
    public Button deleteBtn;
    @FXML
    public Button resetBtn;
    @FXML
    public Button dfsBtn;
    @FXML
    public Button connectBtn;
    @FXML
    public Button mstBtn;
    @FXML
    public Button saveBtn;
    @FXML
    public Button openBtn;

    @FXML
    public TextField notifyField;

    private Graph graph;
    private boolean isEnableAddVertex;
    private boolean isEnableAddEdge;
    private boolean isEnableMove;
    private boolean isEnableDelete;
    private Label vertexSelectedLabel = null;
    private Line temporaryLine = null;

    private final int RADIUS = 30;

    private FileService fileService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graph = new Graph();
        setDefaultBtnGraph();
    }

    public void setDefaultBtnGraph() {
        isEnableAddVertex = false;
        isEnableAddEdge = false;
        isEnableMove = false;
        isEnableDelete = false;
    }

    public void addVertexClickBtn() {
        isEnableAddVertex = !isEnableAddVertex;
        if (isEnableAddVertex) {
            setEnabledBtnColor(addVertexBtn);
        } else {
            addVertexBtn.setStyle("-fx-background-color: #F2F2F2;");
        }
    }

    public void addEdgeClickBtn() {
        isEnableAddEdge = !isEnableAddEdge;
        if (isEnableAddEdge) {
            setEnabledBtnColor(addEdgeBtn);
        } else {
            addEdgeBtn.setStyle("-fx-background-color: #F2F2F2;");
        }
    }

    public void moveClickBtn() {
        isEnableMove = !isEnableMove;
        if (isEnableMove) {
            setEnabledBtnColor(moveBtn);
        } else {
            moveBtn.setStyle("-fx-background-color: #F2F2F2;");
        }
    }

    public void deleteClickBtn() {
        isEnableDelete = !isEnableDelete;
        if (isEnableDelete) {
            setEnabledBtnColor(deleteBtn);
        } else {
            deleteBtn.setStyle("-fx-background-color: #F2F2F2;");
        }
    }

    public void resetClickBtn() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Làm mới đồ thị");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có muốn đặt lại đồ thị?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                graph.resetGraph();
                mainPane.getChildren().clear();
                mainPane.getChildren().add(canvas);
                notifyField.clear();
                setDefaultBtnColor(null);
                setDefaultBtnGraph();
            }
        }
    }

//    public void dfsClickBtn() {
//        try {
//            resetBackgroundColor();
//            resetLineColor();
//            String startVertexName = showInputVertexDialog();
//            Vertex startVertex = graph.getVertexByStringName(startVertexName);
//
//            notifyField.clear();
//            List<Vertex> dfs = graph.depthFirstSearch(startVertex);
//            for (Vertex vertex : dfs) {
//                vertex.getVertexLabel().setBackground(generateBackgroundColor(new Color(0.32, 1.0, 0.1, 1.0)));
//            }
//
//            notifyField.setText("Duyệt đồ thị theo chiều sâu (DFS): ");
//            for (Vertex vertex : dfs) {
//                if (vertex == dfs.getLast()) {
//                    notifyField.appendText(vertex.getName());
//                } else {
//                    notifyField.appendText(vertex.getName() + ", ");
//                }
//            }
//
//            resetVertexVisited();
//        } catch (Exception e) {
//            showAlert("Lỗi", "Số đỉnh của đồ thị phải lớn hơn 0!");
//        }
//    }

    public void connectClickBtn() {
        try {
            resetBackgroundColor();
            resetLineColor();
            notifyField.clear();

            Map<String, String> connected = getVertexConnected();
            Set<String> idConnected = getIdConnected(connected);
            List<Color> uniqueColorList = new ArrayList<>(graph.generateUniqueColor(idConnected.size() + 5));

            for (String id : idConnected) {
                String colorHex = colorToHex(uniqueColorList.getFirst());
                for (Map.Entry<String, String> entry : connected.entrySet()) {
                    if (entry.getValue().equals(id)) {
                        List<Edge> edgeList = graph.getAllEdgesByVertex(graph.getVertexByStringName(entry.getKey()));
                        for (Edge edge : edgeList) {
                            edge.getLineEdge().setStyle("-fx-stroke: #"+ colorHex +";-fx-stroke-width: 3px;");
                        }
                    }
                }
                uniqueColorList.removeFirst();
            }

            List<List<String>> listItem = new ArrayList<>();
            for (String id : idConnected) {
                List<String> item = new ArrayList<>();
                for (Map.Entry<String, String> entry : connected.entrySet()) {
                    if (entry.getValue().equals(id)) {
                        item.add(entry.getKey());
                    }
                }
                listItem.add(item);
            }

            notifyField.setText("Số bộ phận liên thông: " + idConnected.size() + ". Bao gồm: ");
            for(List<String> list : listItem) {
                notifyField.appendText("(");
                for (String item : list) {
                    if (item.equals(list.getLast())) {
                        notifyField.appendText(item);
                    } else {
                        notifyField.appendText(item + ", ");
                    }
                }
                if (list == listItem.getLast()) {
                    notifyField.appendText(")");
                } else {
                    notifyField.appendText(") và ");
                }
            }

            resetVertexVisited();
        } catch (Exception e) {
            showAlert("Lỗi", "Số đỉnh của đồ thị phải lớn hơn 0!");
        }
    }

    public Map<String, String> getVertexConnected() {
        Map<String, String> connected = new HashMap<>();
        int id = 1;

        for (Vertex vertex : graph.getVertexList()) {
            if (!vertex.isVisited()) {
                List<Vertex> dfs = graph.depthFirstSearch(vertex);
                for (Vertex vertexConnected : dfs) {
                    connected.put(vertexConnected.getName(), String.valueOf(id));
                }
                id++;
            }
        }

        return connected;
    }

    public Set<String> getIdConnected(Map<String, String> connected) {
        Set<String> idConnected = new HashSet<>();
        for(Map.Entry<String, String> entry : connected.entrySet()) {
            idConnected.add(entry.getValue());
        }

        return idConnected;
    }

    public String colorToHex(Color color) {
        return String.format("%02X%02X%02X", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));
    }

    public void primClickBtn() {
        try {
            Map<String, String> connected = getVertexConnected();
            Set<String> idConnected = getIdConnected(connected);
            if (idConnected.size() > 1)  {
                showAlert("Lỗi", "Đồ thị tồn tại " + idConnected.size() + " thành phần liên thông!");
                resetVertexVisited();
                return;
            }
            String startVertexName = showInputVertexDialog();
            if (startVertexName == null) {
                startVertexName = "1";
//                resetVertexVisited();
//                resetBackgroundColor();
//                resetLineColor();
//                notifyField.clear();
//                return;
            };
            resetBackgroundColor();
            resetLineColor();
            notifyField.clear();
            Vertex startVertex = graph.getVertexByStringName(startVertexName);


            List<Edge> mst = graph.primAlgorithm(startVertex);
            for (Edge edge : mst) {
                edge.getLineEdge().setStyle("-fx-stroke: red;-fx-stroke-width: 3px;");
            }

            int totalWeight = 0;
            notifyField.setText("Đỉnh bắt đầu duyệt: " + startVertexName);
            notifyField.appendText(". Cây khung tối tiểu (Prim): ");
            for (Edge edge : mst) {
                if (edge == mst.getLast()) {
                    notifyField.appendText("Cung("+ edge.getStartVertex().getName() +", "+ edge.getEndVertex().getName() +")");
                } else {
                    notifyField.appendText("Cung("+ edge.getStartVertex().getName() +", "+ edge.getEndVertex().getName() +"), ");
                }
                totalWeight += edge.getWeight();
            }
            notifyField.appendText(". Tổng trọng số: " + totalWeight);

            resetVertexVisited();
        } catch (Exception e) {
            showAlert("Lỗi", "Số đỉnh của đồ thị phải lớn hơn 0!");
        }
    }

    public void kruskalClickBtn(){
        try {
            Map<String, String> connected = getVertexConnected();
            Set<String> idConnected = getIdConnected(connected);
            if (idConnected.size() == 1) {
                resetBackgroundColor();
                resetLineColor();
                notifyField.clear();

                List<Edge> mst = graph.kruskal();
                for (Edge edge : mst) {
                    edge.getLineEdge().setStyle("-fx-stroke: orange;-fx-stroke-width: 3px;");
                }

                int totalWeight = 0;
                notifyField.setText("Cây khung tối tiểu (Kruskal): ");
                for (Edge edge : mst) {
                    if (edge == mst.getLast()) {
                        notifyField.appendText("Cung("+ edge.getStartVertex().getName() +", "+ edge.getEndVertex().getName() +")");
                    } else {
                        notifyField.appendText("Cung("+ edge.getStartVertex().getName() +", "+ edge.getEndVertex().getName() +"), ");
                    }
                    totalWeight += edge.getWeight();
                }
                notifyField.appendText(". Tổng trọng số: " + totalWeight);

                resetVertexVisited();
            } else {
                showAlert("Lỗi", "Đồ thị tồn tại " + idConnected.size() + " thành phần liên thông!");
                resetVertexVisited();
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Số đỉnh của đồ thị phải lớn hơn 0!");
        }
    }

    public void saveClickBtn(MouseEvent me) {
        fileService = new FileService();
        fileService.saveGraph(me, this.graph, notifyField);
    }

    public void openClickBtn(MouseEvent me) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(canvas);

        FileService fileService = new FileService(this, this.mainPane);
        fileService.openGraph(me, this.graph);

        resetVertexVisited();
    }

    public void resetBackgroundColor() {
        Background bg = new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10.0, true), null));
        for (Vertex vertex : graph.getVertexList()) {
            vertex.getVertexLabel().setBackground(bg);
        }
    }

    public void resetLineColor() {
        for (Edge edge : graph.getEdgeList()) {
            edge.getLineEdge().setStyle("-fx-stroke: black; -fx-stroke-width: 2px;");
        }
    }
    public void resetVertexVisited() {
        for (Vertex vertex : graph.getVertexList()) {
            vertex.setVisited(false);
        }
    }

    public String showInputVertexDialog() {
        List<Vertex> vertexes = graph.getVertexList();
        List<String> vertexNameStringList = new ArrayList<>();
        for (Vertex vertex : vertexes) {
            vertexNameStringList.add(vertex.getName());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(vertexNameStringList.getFirst(), vertexNameStringList);
        dialog.setTitle("Chọn đỉnh bắt đầu");
        dialog.setHeaderText(null);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        };
        return null;
    }

    public Background generateBackgroundColor(Color color)  {
        Background bg = new Background(new BackgroundFill(color, new CornerRadii(10.0, true), null));
        return bg;
    }

    public double[] getVertexCoordinates(double x, double y) {
        if (x <= 0.0) x = 0.0;
        if (x >= mainPane.getWidth() - 2.0 * RADIUS) {
            x = mainPane.getWidth() - 2.0 * RADIUS;
        }
        if (y <= 0.0) y = 0.0;
        if (y >= mainPane.getHeight() - 2.0 * RADIUS) {
            y = mainPane.getHeight() - 2.0 * RADIUS;
        }

        return new double[]{x, y};
    }

    public Label createVertexLabel(MouseEvent me) {
        double x = Math.min(me.getX() - RADIUS, mainPane.getWidth() - 2.0 * RADIUS);
        double y = Math.min(me.getY() - RADIUS, mainPane.getHeight() - 2.0 * RADIUS);
        double[] coordinate = getVertexCoordinates(x, y);

        Label vertexLabel = new Label(graph.getVertexNameList().getFirst());
        vertexLabel.setId("vertexLabel");
        vertexLabel.setLayoutX(coordinate[0]);
        vertexLabel.setLayoutY(coordinate[1]);
        graph.getVertexNameList().removeFirst();
        vertexLabel.setFont(new Font("System Bold", 24));

        return vertexLabel;
    }

    public Label addVertex(MouseEvent me) {
        Label vertexLabel = createVertexLabel(me);
        Vertex vertex = new Vertex();
        vertex.setVertexLabel(vertexLabel);
        vertex.setVisited(false);

        graph.getVertexList().add(vertex);
        return vertexLabel;
    }

    public void canvasOnClick(MouseEvent me) {
        if (isEnableAddVertex) {
            Label vertexLabel = addVertex(me);
            mainPane.getChildren().add(vertexLabel);
            addEventToVertex(vertexLabel);
        }
    }

    public void addEventToVertex(Label vertexLabel) {
        vertexLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                vertexOnClick(event);
            }
        });

        vertexLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                vertexOnPress(event);
            }
        });

        vertexLabel.setOnMouseDragged(this::vertexOnDrag);
        vertexLabel.setOnMouseReleased(this::vertexOnRelease);

    }

    public void addEventToEdge(Line edgeLine) {
        edgeLine.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && isEnableDelete) {
                deleteEdge(edgeLine);
            }
        });
    }

    public void addEventToWeight(Label weightLabel) {
        weightLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                changeWeight(weightLabel);
            }

            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && isEnableDelete) {
                Edge edge = graph.getEdgeByWeightLabel(weightLabel);
                deleteEdge(edge.getLineEdge());
            }
        });
    }

    public void changeWeight(Label label) {
        Edge e = graph.getEdgeByWeightLabel(label);
        mainPane.getChildren().remove(e.getWeightLabel());
//        showInputWeightDialog(edge.getStartVertex(), edge.getEndVertex());
        Vertex startVertex = e.getStartVertex();
        Vertex endVertex = e.getEndVertex();
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Nhập trọng số");
        dialog.setHeaderText(null);
        dialog.setContentText("Nhập trọng số của cung: ");

        dialog.setOnShown(ev -> {
            Platform.runLater(() -> dialog.getEditor().selectAll());
        });

        while (true) {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String value = result.get();
                try {
                    int weight = Integer.parseInt(value);
                    if (weight > 0) {
                        Edge edge = new Edge();
                        edge.setLineEdge(addEdge(startVertex.getVertexLabel(), endVertex.getVertexLabel()));
                        edge.setStartVertex(startVertex);
                        edge.setEndVertex(endVertex);
                        edge.setWeight(weight);

//                        graph.getEdgeList().add(edge);

                        double[] edgeCoordinates = calculateCoordinates(startVertex.getVertexLabel(), endVertex.getVertexLabel(), RADIUS);
                        Label weightLabel = new Label();
                        weightLabel.getStyleClass().add("weightLabel");
                        weightLabel.setText(String.valueOf(weight));
                        weightLabel.setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2.0);
                        weightLabel.setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2.0);

                        edge.setWeightLabel(weightLabel);
                        graph.getEdgeList().add(edge);

                        weightLabel.toFront();
                        mainPane.getChildren().add(weightLabel);
                        addEventToWeight(weightLabel);

                        break;
                    } else {
                        showAlert("Trọng số không hợp lệ", "Vui lòng nhập một số nguyên lớn hơn 0!");
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Trọng số không hợp lệ", "Vui lòng nhập một số nguyên!");
                }
            } else {
                Edge edgePrevious = graph.getEdgeByWeightLabel(label);

                double[] edgeCoordinates = calculateCoordinates(startVertex.getVertexLabel(), endVertex.getVertexLabel(), RADIUS);
                Label weightLabel = edgePrevious.getWeightLabel();
                weightLabel.getStyleClass().add("weightLabel");
                weightLabel.setText(String.valueOf(edgePrevious.getWeight()));
                weightLabel.setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2.0);
                weightLabel.setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2.0);
                weightLabel.toFront();

                mainPane.getChildren().add(weightLabel);
                addEventToWeight(weightLabel);
                break;
            }
        }
    }

    public void vertexOnClick(MouseEvent me) {
        if (isEnableDelete) {
            Label vertecClickedLabel = (Label) me.getSource();
            Vertex vertexClicked = graph.findVertexByLabel(vertecClickedLabel);
            List<Edge> edgeList = graph.getAllEdgesByVertex(vertexClicked);

            List<Line> edgeLineList = new ArrayList<>();
            List<Label> edgeWeightLabelList = new ArrayList<>();
            for (Edge edge : edgeList) {
                edgeLineList.add(edge.getLineEdge());
                edgeWeightLabelList.add(edge.getWeightLabel());
            }

            graph.getEdgeList().removeAll(edgeList);
            deleteVertex(vertecClickedLabel);

            mainPane.getChildren().remove(vertecClickedLabel);
            mainPane.getChildren().removeAll(edgeLineList);
            mainPane.getChildren().removeAll(edgeWeightLabelList);
        }
    }

    public void startDrawEdge(MouseEvent me) {
        vertexSelectedLabel = (Label) me.getSource();

        double startLineX = vertexSelectedLabel.getLayoutX() + vertexSelectedLabel.getWidth() / 2.0;
        double startLineY = vertexSelectedLabel.getLayoutY() + vertexSelectedLabel.getHeight() / 2.0;

        temporaryLine = new Line(startLineX, startLineY, startLineX, startLineY);

        mainPane.getChildren().add(temporaryLine);
    }

    public void drawEdge(MouseEvent me) {
        temporaryLine.setStartX(vertexSelectedLabel.getLayoutX() + vertexSelectedLabel.getWidth() / 2.0);
        temporaryLine.setStartY(vertexSelectedLabel.getLayoutY() + vertexSelectedLabel.getHeight() / 2.0);
        temporaryLine.setEndX(vertexSelectedLabel.getLayoutX() + me.getX());
        temporaryLine.setEndY(vertexSelectedLabel.getLayoutY() + me.getY());
    }

    public void endDrawEdge(MouseEvent me) {
        Label vertexReleaseLabel = getVertexReleaseLabel(me);
        if (vertexReleaseLabel != null && vertexReleaseLabel != vertexSelectedLabel) {
            Vertex startVertex = graph.findVertexByLabel(vertexSelectedLabel);
            Vertex endVertex = graph.findVertexByLabel(vertexReleaseLabel);

            showInputWeightDialog(startVertex, endVertex);
        }

        vertexSelectedLabel = null;
        mainPane.getChildren().remove(temporaryLine);
        temporaryLine = null;
    }

    public Label getVertexReleaseLabel(MouseEvent me) {
        Label vertexLabel;
        Bounds bounds;
        for (Vertex vertex : graph.getVertexList()) {
            vertexLabel = vertex.getVertexLabel();
            bounds = vertexLabel.getBoundsInParent();

            if (bounds.contains(vertexSelectedLabel.getLayoutX() + me.getX(),
                    vertexSelectedLabel.getLayoutY() + me.getY())) {
                return vertexLabel;
            }
        }
        return null;
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Line addEdge(Label startLabel, Label endLabel) {
        double[] labelCoordinates = calculateCoordinates(startLabel, endLabel, RADIUS);

        Line edgeLine = new Line();
        edgeLine.setStartX(labelCoordinates[0]);
        edgeLine.setStartY(labelCoordinates[1]);
        edgeLine.setEndX(labelCoordinates[2]);
        edgeLine.setEndY(labelCoordinates[3]);
        edgeLine.setStyle("-fx-stroke-width: 2px;");
        mainPane.getChildren().add(edgeLine);

        addEventToEdge(edgeLine);

        return edgeLine;
    }

    public void deleteEdge(Line edgeLine) {
        Edge edge = graph.getEdgeByEdgeLine(edgeLine);

        graph.getEdgeList().remove(edge);
        mainPane.getChildren().remove(edgeLine);
        mainPane.getChildren().remove(edge.getWeightLabel());
    }

    public void showInputWeightDialog(Vertex startVertex, Vertex endVertex) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Nhập trọng số");
        dialog.setHeaderText(null);
        dialog.setContentText("Nhập trọng số của cung: ");

        dialog.setOnShown(e -> {
            Platform.runLater(() -> dialog.getEditor().selectAll());
        });

        while (true) {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String value = result.get();
                try {
                    int weight = Integer.parseInt(value);
                    if (weight > 0) {
                        Edge edge = new Edge();
                        edge.setLineEdge(addEdge(startVertex.getVertexLabel(), endVertex.getVertexLabel()));
                        edge.setStartVertex(startVertex);
                        edge.setEndVertex(endVertex);
                        edge.setWeight(weight);

//                        graph.getEdgeList().add(edge);

                        double[] edgeCoordinates = calculateCoordinates(startVertex.getVertexLabel(), endVertex.getVertexLabel(), RADIUS);
                        Label weightLabel = new Label();
                        weightLabel.getStyleClass().add("weightLabel");
                        weightLabel.setText(String.valueOf(weight));
                        weightLabel.setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2.0);
                        weightLabel.setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2.0);

                        edge.setWeightLabel(weightLabel);
                        graph.getEdgeList().add(edge);

                        weightLabel.toFront();
                        mainPane.getChildren().add(weightLabel);
                        addEventToWeight(weightLabel);

                        break;
                    } else {
                        showAlert("Trọng số không hợp lệ", "Vui lòng nhập một số nguyên lớn hơn 0!");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Trọng số không hợp lệ", "Vui lòng nhập một số nguyên!");
                }
            } else {
                int weight = 1;
                if (weight > 0) {
                    Edge edge = new Edge();
                    edge.setLineEdge(addEdge(startVertex.getVertexLabel(), endVertex.getVertexLabel()));
                    edge.setStartVertex(startVertex);
                    edge.setEndVertex(endVertex);
                    edge.setWeight(weight);

//                        graph.getEdgeList().add(edge);

                    double[] edgeCoordinates = calculateCoordinates(startVertex.getVertexLabel(), endVertex.getVertexLabel(), RADIUS);
                    Label weightLabel = new Label();
                    weightLabel.getStyleClass().add("weightLabel");
                    weightLabel.setText(String.valueOf(weight));
                    weightLabel.setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2.0);
                    weightLabel.setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2.0);

                    edge.setWeightLabel(weightLabel);
                    graph.getEdgeList().add(edge);

                    weightLabel.toFront();
                    mainPane.getChildren().add(weightLabel);
                    addEventToWeight(weightLabel);

                    break;
                } else {
                    showAlert("Trọng số không hợp lệ", "Vui lòng nhập một số nguyên lớn hơn 0!");
                }
            }
        }
    }

    public void startMoveVertex(MouseEvent me) {
        vertexSelectedLabel = (Label) me.getSource();
    }

    public void moveVertex(MouseEvent me) {
        vertexSelectedLabel = (Label) me.getSource();
        double layoutX = vertexSelectedLabel.getLayoutX() + me.getX() - RADIUS;
        double layoutY = vertexSelectedLabel.getLayoutY() + me.getY() - RADIUS;

        double[] vertexLayout = getVertexCoordinates(layoutX, layoutY);
        vertexSelectedLabel.setLayoutX(vertexLayout[0]);
        vertexSelectedLabel.setLayoutY(vertexLayout[1]);

        Vertex vertex = graph.findVertexByLabel(vertexSelectedLabel);
        List<Edge> edgeList = graph.getAllEdgesByVertex(vertex);
        for (Edge edge : edgeList) {
            Label startVertexLabel = edge.getStartVertex().getVertexLabel();
            Label endVertexLabel = edge.getEndVertex().getVertexLabel();

            double[] edgeCoordinates = calculateCoordinates(startVertexLabel, endVertexLabel, RADIUS);
            edge.getLineEdge().setStartX(edgeCoordinates[0]);
            edge.getLineEdge().setStartY(edgeCoordinates[1]);
            edge.getLineEdge().setEndX(edgeCoordinates[2]);
            edge.getLineEdge().setEndY(edgeCoordinates[3]);

            edge.getWeightLabel().setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2.0);
            edge.getWeightLabel().setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2.0);
        }
    }

    public double[] calculateCoordinates(Label startVertexLabel, Label endVertexLabel, int radius) {
        double angle = Math.atan2(endVertexLabel.getLayoutY() - startVertexLabel.getLayoutY(),
                endVertexLabel.getLayoutX() - startVertexLabel.getLayoutX());
        double startX = (startVertexLabel.getLayoutX() + radius) + (radius * Math.cos(angle));
        double startY = (startVertexLabel.getLayoutY() + radius) + (radius * Math.sin(angle));
        double endX = (endVertexLabel.getLayoutX() + radius) - (radius * Math.cos(angle));
        double endY = (endVertexLabel.getLayoutY() + radius) - (radius * Math.sin(angle));

        return new double[]{startX, startY, endX, endY};

    }

    public void endMoveVertex() {vertexSelectedLabel = null;}

    public void vertexOnPress(MouseEvent me) {
        if (isEnableAddEdge) {
            startDrawEdge(me);
        }
        if (isEnableMove) {
            startMoveVertex(me);
        }
    }

    public void vertexOnDrag(MouseEvent me) {
        if (isEnableAddEdge) {
            drawEdge(me);
        }
        if (isEnableMove) {
            moveVertex(me);
        }
    }

    public void vertexOnRelease(MouseEvent me) {
        if (isEnableAddEdge) {
            endDrawEdge(me);
        }
        if (isEnableMove) {
            endMoveVertex();
        }
    }

    public void deleteVertex(Label label) {
        Vertex vertex = graph.findVertexByLabel(label);
        graph.getVertexNameList().add(vertex.getName());

        graph.getVertexNameList().sort((s1, s2) -> Integer.compare(Integer.parseInt(s1), Integer.parseInt(s2)));
        graph.getVertexList().remove(vertex);
    }

    public void setEnabledBtnColor(Button btn) {
        setDefaultBtnColor(btn);
        btn.setStyle("-fx-background-color: #A2A2A2;");
    }

    public void setDefaultBtnColor(Button btn) {
        if (Objects.equals(btn, addVertexBtn)) {
            isEnableAddEdge = false;
            isEnableMove = false;
            isEnableDelete = false;
        }
        if (Objects.equals(btn, addEdgeBtn)) {
            isEnableAddVertex = false;
            isEnableMove = false;
            isEnableDelete = false;
        }
        if (Objects.equals(btn, moveBtn)) {
            isEnableAddVertex = false;
            isEnableAddEdge = false;
            isEnableDelete = false;
        }
        if (Objects.equals(btn, deleteBtn)) {
            isEnableAddVertex = false;
            isEnableAddEdge = false;
            isEnableMove = false;
        }

        addVertexBtn.setStyle("-fx-background-color: #F2F2F2;");
        addEdgeBtn.setStyle("-fx-background-color: #F2F2F2;");
        moveBtn.setStyle("-fx-background-color: #F2F2F2;");
        deleteBtn.setStyle("-fx-background-color: #F2F2F2;");
    }

    public void helpClickBtn(ActionEvent e) throws IOException {
        FXMLLoader fxml = new FXMLLoader(Main.class.getResource("views/HelpView.fxml"));
        Scene helpScene = new Scene(fxml.load());

        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(helpScene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
}
