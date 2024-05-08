package graph;

/** Class Dijkstra. Implementation of Dijkstra's algorithm for finding the shortest path
 * between the source vertex and other vertices in the graph.
 *  Fill in code. It is ok to add additional helper methods / classes.
 *  Must add a class representing a Priority queue.
 */

import java.awt.*;
import java.util.List;

public class Dijkstra {
    private Graph graph; // stores the graph of CityNode-s and edges connecting them
    private List<Integer> shortestPath = null; // nodes that are part of the shortest path

    private int[][] dijkstraTable;

    private static class PriorityQueue{
        private int[] positions;
        private int[] distances;
        private int size;

        public PriorityQueue(int maxSize){
            this.size = 0;
            this.distances = new int[maxSize];
            this.distances[0] = Integer.MIN_VALUE;
            this.positions = new int[maxSize];
            for(int i = 0; i < maxSize; i++){
                this.positions[i] = i + 1;
            }
            System.out.println("Good");
        }

        public boolean isEmpty(){
            return size == 0;
        }

        private int parent(int pos) {
            return pos / 2;
        }

        private void swap(int pos1, int pos2) {
            int tmp = distances[pos1];
            distances[pos1] = distances[pos2];
            distances[pos2] = tmp;
            tmp = positions[pos1 - 1];
            positions[pos1 - 1] = positions[pos2 - 1];
            positions[pos2 - 1] = tmp;
        }

        public void insert(int nodeId, int priority){
            size++;
            distances[size] = priority;
            //positions[size - 1] = nodeId;

            int current = size;
            // FILL IN CODE: bubble up if the value of current < value of the parent
            while(distances[current] < distances[parent(current)]){
                swap(current, parent(current));
                current = parent(current);
            }
        }

        public void removeMin(){
            swap(1, size); // swap the end of the heap into the root
            size--;  	   // removed the end of the heap
            // fix the heap property - push down as needed
            if (size != 0)
                pushdown(1);

        }

        private boolean isLeaf(int pos) {
            return ((pos > size / 2) && (pos <= size));
        }

        private int leftChild(int pos) {
            return 2 * pos;
        }

        private void pushdown(int position) {
            int smallestChild;
            while (!isLeaf(position)) {
                smallestChild = leftChild(position);
                if(smallestChild + 1 <= size){
                    if(distances[smallestChild + 1] < distances[smallestChild]){
                        smallestChild = smallestChild + 1;
                    }
                }
                if(distances[position] < distances[smallestChild]){
                    return;
                }else{
                    swap(position, smallestChild);
                }
                position = smallestChild;
            }
        }

        public void reduceKey(int nodeId, int newPriority){
            distances[nodeId] = newPriority;
            while(!isLeaf(nodeId)){
                int leftChildPosition = nodeId * 2;
                int rightChildePosition = nodeId * 2 + 1;
                if(rightChildePosition <= size){
                    if(distances[leftChildPosition] > distances[rightChildePosition]){
                        if(distances[nodeId] > distances[rightChildePosition]){
                            swap(nodeId, rightChildePosition);
                            nodeId = rightChildePosition;
                        }else{
                            break;
                        }
                    }else{
                        if(distances[nodeId] > distances[leftChildPosition]){
                            swap(nodeId, leftChildPosition);
                            nodeId = leftChildPosition;
                        }else{
                            break;
                        }
                    }
                }else{
                    if(distances[nodeId] > distances[leftChildPosition]){
                        swap(nodeId, leftChildPosition);
                        nodeId = leftChildPosition;
                    }else{
                        break;
                    }
                }
            }
        }
    }


    /** Constructor
     *
     * @param filename name of the file that contains info about nodes and edges
     * @param graph graph
     */
    public Dijkstra(String filename, Graph graph) {
        this.graph = graph;
        graph.loadGraph(filename);
        this.dijkstraTable = new int[this.graph.numNodes()][2];
    }

    /**
     * Returns the shortest path between the origin vertex and the destination vertex.
     * The result is stored in shortestPathEdges.
     * This function is called from GUIApp, when the user clicks on two cities.
     * @param origin source node
     * @param destination destination node
     * @return the ArrayList of nodeIds (of nodes on the shortest path)
     */
    public List<Integer> computeShortestPath(CityNode origin, CityNode destination) {

        // FILL IN CODE

        // Create and initialize Dijkstra's table
        // Create and initialize a Priority Queue - you need to implement your own, NOT use a built in one!
        PriorityQueue pq = new PriorityQueue(graph.numNodes() + 1);
        pq.insert(0, 0);
        for(int i = 1; i < graph.numNodes(); i++){
            pq.insert(i, Integer.MAX_VALUE);
        }

        // Run Dijkstra

        // Compute the nodes on the shortest path by "backtracking" using the table

        // The result should be in an instance variable called "shortestPath" and
        // should also be returned by the method
        return null; // don't forget to change it
    }

    /**
     * Return the shortest path as a 2D array of Points.
     * Each element in the array is another array that has 2 Points:
     * these two points define the beginning and end of a line segment.
     * @return 2D array of points
     */
    public Point[][] getPath() {
        if (shortestPath == null)
            return null;
        return graph.getPath(shortestPath); // delegating this task to the Graph class
    }

    /** Set the shortestPath to null.
     *  Called when the user presses Reset button.
     */
    public void resetPath() {
        shortestPath = null;
    }

}