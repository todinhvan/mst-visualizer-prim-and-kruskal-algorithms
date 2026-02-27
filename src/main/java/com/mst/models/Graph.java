package com.mst.models;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.*;

public class Graph {
    protected List<Vertex> vertexList;
    protected List<Edge> edgeList;

    protected List<String> vertexNameList;
    protected Set<Color> colorSet;

    private int[] sz = new int[100001];
    private int[] parent = new int[100001];

    public Graph() {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        this.vertexNameList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            this.vertexNameList.add(String.valueOf(i));
        }
        this.colorSet = new HashSet<>();
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public List<String> getVertexNameList() {
        return vertexNameList;
    }

    public void resetGraph() {
        vertexList.clear();
        edgeList.clear();
        vertexNameList.clear();
        for (int i = 1; i <= 1000; i++) {
            vertexNameList.add(String.valueOf(i));
        }
        colorSet.clear();
    }

    public List<Vertex> depthFirstSearch(Vertex startVertex) {
        List<Vertex> dfs = new ArrayList<>();
        Stack<Vertex> vertexStack = new Stack<>();

        if (!vertexList.isEmpty()) {
            vertexStack.push(startVertex);
        }

        while (!vertexStack.isEmpty()) {
            Vertex currentVertex = vertexStack.pop();

            if (!currentVertex.isVisited()) {
                dfs.add(currentVertex);
                List<Vertex> neighbors = getUnvisitedNeighbors(currentVertex);
                neighbors.sort((v1, v2) -> {
                    return Integer.parseInt(v2.getName()) - Integer.parseInt(v1.getName());
                });
                for (Vertex neighbor : neighbors) {
                    vertexStack.push(neighbor);
                }
                currentVertex.setVisited(true);
            }
        }
        return dfs;
    }


    public List<Edge> primAlgorithm(Vertex startVertex) {
        List<Edge> mst = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();

        visited.add(startVertex);

        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                return Integer.compare(e1.getWeight(), e2.getWeight());
            }
        });

        edgeQueue.addAll(getAllEdgesByVertex(startVertex));

        while (!edgeQueue.isEmpty()) {
            Edge e = edgeQueue.poll();
            Vertex u = e.getStartVertex();
            Vertex v = e.getEndVertex();

            if (visited.contains(u) && visited.contains(v)) {
                continue;
            }

            mst.add(e);

            if (!visited.contains(u)) {
                visited.add(u);
                edgeQueue.addAll(getAllEdgesByVertex(u));
            }
            if (!visited.contains(v)) {
                visited.add(v);
                edgeQueue.addAll(getAllEdgesByVertex(v));
            }
        }

        return mst;
    }

    public void initUnion() {
        for (int i = 1; i <= vertexList.size(); i++) {
            sz[i] = 1;
            parent[i] = i;
        }
    }

//    public int findUnion(int u) {
//        while (u != parent[u]) {
//            u = parent[u];
//        }
//        return u;
//    }
    public int findUnion(int u) {
        if (parent[u] == u) {
            return u;
        } else {
            return parent[u] = findUnion(parent[u]);
        }
    }

    public boolean union(int u, int v) {
        u = findUnion(u);
        v = findUnion(v);
        if (u == v) {
            return false;
        }
        if (sz[u] > sz[v]) {
            parent[v] = u;
            sz[u] += sz[v];
        } else {
            parent[u] = v;
            sz[v] += sz[u];
        }
        return true;
    }

    public List<Edge> kruskal() {
        initUnion();
        List<Edge> mst = new ArrayList<>();
        edgeList.sort((o1, o2) -> o1.getWeight() - o2.getWeight());

        for (Edge edge : edgeList) {
            Vertex u = edge.getStartVertex();
            Vertex v = edge.getEndVertex();
            if (mst.size() == vertexList.size() - 1) break;
            if (union(Integer.parseInt(u.getName()), Integer.parseInt(v.getName()))) {
                mst.add(edge);
            }
        }

        return mst;
    }

    public List<Vertex> getUnvisitedNeighbors(Vertex vertex) {
        List<Vertex> neighbors = new ArrayList<>();
        List<Edge> edges = getAllEdgesByVertex(vertex);
        for (Edge edge : edges) {
            if (edge.getStartVertex() != vertex && !edge.getStartVertex().isVisited()) {
                neighbors.add(edge.getStartVertex());
            } else if (edge.getEndVertex() != vertex && !edge.getEndVertex().isVisited()) {
                neighbors.add(edge.getEndVertex());
            }
        }

        return neighbors;
    }

    public Vertex findVertexByLabel(Label label) {
        Vertex result = new Vertex();

        for (Vertex vertex : vertexList) {
            if (vertex.getVertexLabel() == label) {
                result = vertex;
            }
        }
        return result;
    }

    public Edge getEdgeByEdgeLine(Line edgeLine) {
        for (Edge edge : getEdgeList()) {
            if (edge.getLineEdge() == edgeLine) {
                return edge;
            }
        }
        return null;
    }

    public Edge getEdgeByWeightLabel(Label weightLabel) {
        for (Edge edge : getEdgeList()) {
            if (edge.getWeightLabel().equals(weightLabel)) {
                return edge;
            }
        }
        return null;
    }

    public List<Edge> getAllEdgesByVertex(Vertex vertex) {
        List<Edge> edgeList = new ArrayList<>();
        for (Edge edge : getEdgeList()) {
            if (edge.getStartVertex() == vertex || edge.getEndVertex() == vertex) {
                edgeList.add(edge);
            }
        }
        return edgeList;
    }

    public Vertex getVertexByStringName(String name) {
        Vertex result = new Vertex();
        for (Vertex vertex : getVertexList()) {
            if (vertex.getName().equals(name)) {
                result = vertex;
            }
        }
        return result;
    }

    public Set<Color> generateUniqueColor(int n) {
        Set<Color> colorList = new HashSet<>();
        Random rd = new Random();
        for (int i = 0; i < n; i++) {
            colorList.add(new Color(rd.nextDouble(), rd.nextDouble(), rd.nextDouble(), 1.0));
        }
        return colorList;
    }
}
