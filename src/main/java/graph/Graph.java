package graph;


/** A class that represents a graph where nodes are cities (CityNode).
 * The cost of each edge connecting two cities is the distance between the cities.
 * Fill in code in this class. You may add additional methods and variables.
 * You are required to implement a priority queue (min heap) from scratch to get full credit.
 */

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Graph {
    public final int EPS_DIST = 5;
    private int numNodes;     // total number of nodes
    private int numEdges; // total number of edges
    private CityNode[] nodes; // array of nodes of the graph
    private Edge[] adjacencyList; // adjacency list; for each vertex stores a linked list of edges
    private Map<String, Integer> labelsToIndices; // a HashMap that maps each city to the corresponding node id
    private int[] neighborsCount;

    /**
     * Read graph info from the given file, and create nodes and edges of
     * the graph.
     *
     * @param filename name of the file that has nodes and edges
     */
    public void loadGraph(String filename) {
        // FILL IN CODE
        // Add edge : SF to LA and also LA to SF !!!!!
        this.labelsToIndices = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // Load nodes
            String line = br.readLine();
            if(!line.equals("NODES")){
                System.out.println("Illegal Input");
            }
            this.numNodes = Integer.parseInt(br.readLine());
            this.nodes = new CityNode[this.numNodes];
            this.neighborsCount = new int[this.numNodes];
            for(int i = 0; i < this.numNodes; i++){
                line = br.readLine();
                String[] strs = line.split(" ");
                this.labelsToIndices.put(strs[0], i);
                addNode(new CityNode(strs[0], Double.parseDouble(strs[1]), Double.parseDouble(strs[2])));
            }

            // Load edges
            this.adjacencyList = new Edge[this.numNodes];
            if(!br.readLine().equals("ARCS")){
                System.out.println("Illegal Input");
            }
            while((line = br.readLine()) != null){
                String[] strs = line.split(" ");
                int idx1 = this.labelsToIndices.get(strs[0]);
                int idx2 = this.labelsToIndices.get(strs[1]);
                int cost = Integer.parseInt(strs[2]);
                addEdge(idx1, new Edge(idx2, cost));
                addEdge(idx2, new Edge(idx1, cost));
                this.numEdges += 2;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Add a node to the array of nodes.
     * Increment numNodes variable.
     * Called from loadGraph.
     *
     * @param node a CityNode to add to the graph
     */
    public void addNode(CityNode node) {
        // FILL IN CODE
        this.nodes[getId(node)] = node;
    }

    /**
     * Return the number of nodes in the graph
     * @return number of nodes
     */
    public int numNodes() {
        return numNodes;
    }

    /**
     * Adds the edge to the linked list for the given nodeId
     * Called from loadGraph.
     *
     * @param nodeId id of the node
     * @param edge edge to add
     */
    public void addEdge(int nodeId, Edge edge) {
        // FILL IN CODE
        if(this.adjacencyList[nodeId] == null){
            this.adjacencyList[nodeId] = edge;
        }else{
            edge.setNext(this.adjacencyList[nodeId]);
            this.adjacencyList[nodeId] = edge;
        }
        this.neighborsCount[nodeId]++;
    }

    /**
     * Returns an integer id of the given city node
     * @param city node of the graph
     * @return its integer id
     */
    public int getId(CityNode city) {
        return this.labelsToIndices.get(city.getCity()); // Don't forget to change this
    }

    /**
     * Return the edges of the graph as a 2D array of points.
     * Called from GUIApp to display the edges of the graph.
     *
     * @return a 2D array of Points.
     * For each edge, we store an array of two Points, v1 and v2.
     * v1 is the source vertex for this edge, v2 is the destination vertex.
     * This info can be obtained from the adjacency list
     */
    public Point[][] getEdges() {
        if (adjacencyList == null || adjacencyList.length == 0) {
            //System.out.println("Adjacency list is empty. Load the graph first.");
            return null;
        }
        Point[][] edges2D = new Point[numEdges][2];
        // FILL IN CODE
        int n = 0;
        for(int i = 0; i < this.adjacencyList.length; i++){
            Edge edge = this.adjacencyList[i];
            Point v1 = this.nodes[i].getLocation();
            while(edge != null){
                Point v2 = this.nodes[edge.getNeighbor()].getLocation();
                edges2D[n][0] = v1;
                edges2D[n][1] = v2;
                n++;
                edge = edge.getNext();
            }
        }
        return edges2D;
    }

    /**
     * Get the nodes of the graph as a 1D array of Points.
     * Used in GUIApp to display the nodes of the graph.
     * @return a list of Points that correspond to nodes of the graph.
     */
    public Point[] getNodes() {
        if (nodes == null) {
            // System.out.println("Array of nodes is empty. Load the graph first.");
            return null;
        }
        Point[] nodes = new Point[this.nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = this.nodes[i].getLocation();
        }
        return nodes;
    }

    /**
     * Used in GUIApp to display the names of the airports.
     * @return the list that contains the names of cities (that correspond
     * to the nodes of the graph)
     */
    public String[] getCities() {
        if (this.nodes == null) {
            System.out.println("Graph has no nodes. Write loadGraph method first. ");
            return null;
        }
        String[] labels = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            labels[i] = nodes[i].getCity();
        }
        return labels;
    }

    /** Take a list of node ids on the path and return an array where each
     * element contains two points (an edge between two consecutive nodes)
     * @param pathOfNodes A list of node ids on the path
     * @return array where each element is an array of 2 points
     */
    public Point[][] getPath(List<Integer> pathOfNodes) {
        Point[][] edges2D = new Point[pathOfNodes.size()-1][2];
        // Each "edge" is an array of size two (one Point is origin, one Point is destination)
        // FILL IN CODE
        for(int i = 0; i < pathOfNodes.size() - 1; i++){
            edges2D[i][0] = this.nodes[pathOfNodes.get(i)].getLocation();
            edges2D[i][1] = this.nodes[pathOfNodes.get(i + 1)].getLocation();
        }
        return edges2D;
    }

    /**
     * Return the CityNode for the given nodeId
     * @param nodeId id of the node
     * @return CityNode
     */
    public CityNode getNode(int nodeId) {
        return nodes[nodeId];
    }

    /**
     * Take the location of the mouse click as a parameter, and return the node
     * of the graph at this location. Needed in GUIApp class. No need to modify.
     * @param loc the location of the mouse click
     * @return reference to the corresponding CityNode
     */
    public CityNode getNode(Point loc) {
        if (nodes == null) {
            System.out.println("No node at this location. ");
            return null;
        }
        for (CityNode v : nodes) {
            Point p = v.getLocation();
            if ((Math.abs(loc.x - p.x) < EPS_DIST) && (Math.abs(loc.y - p.y) < EPS_DIST))
                return v;
        }
        return null;
    }

    public Edge[] getAdjacencyList(){
        return this.adjacencyList;
    }
}