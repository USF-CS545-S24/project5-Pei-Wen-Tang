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

    private Pair[] dijkstraTable;  // Pair: (shortest, parent)

    // Inner class for storing data with two elements
    private static class Pair{
        Object first;
        Object second;

        public Pair(Object first, Object second){
            this.first = first;
            this.second = second;
        }
    }

    // Inner class implementing priority queue for Dijkstra
    private static class PriorityQueue{
        private int[] positions;
        private Pair[] heap;
        private int size;

        public PriorityQueue(int maxSize){
            this.size = 0;
            this.heap = new Pair[maxSize];
            this.heap[0] = new Pair(-1, Integer.MIN_VALUE);
            this.positions = new int[maxSize - 1];
        }

        public boolean isEmpty(){
            return size == 0;
        }

        private int parent(int pos) {
            return pos / 2;
        }

        private void swap(int pos1, int pos2) {
            Pair tmp = heap[pos1];
            heap[pos1] = heap[pos2];
            heap[pos2] = tmp;
            int positionTmp = positions[pos1 - 1];
            positions[pos1 - 1] = positions[pos2 - 1];
            positions[pos2 - 1] = positionTmp;
        }

        public void insert(int nodeId, int priority){
            size++;
            heap[size] = new Pair(nodeId, priority);
            positions[size - 1] = nodeId + 1;

            int current = size;
            // FILL IN CODE: bubble up if the value of current < value of the parent
            while((int)heap[current].second < (int)heap[parent(current)].second){
                swap(current, parent(current));
                current = parent(current);
            }
        }

        public int removeMin(){
            swap(1, size); // swap the end of the heap into the root
            size--;  	   // removed the end of the heap
            // fix the heap property - push down as needed
            if (size != 0) {
                pushdown(1);
            }

            return (int)heap[size + 1].first;
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
                    if((int)heap[smallestChild + 1].second < (int)heap[smallestChild].second){
                        smallestChild = smallestChild + 1;
                    }
                }
                if((int)heap[position].second <= (int)heap[smallestChild].second){
                    return;
                }else{
                    swap(position, smallestChild);
                }
                position = smallestChild;
            }
        }

        public void reduceKey(int nodeId, int newPriority){
            heap[nodeId] = new Pair(nodeId, newPriority);
            while(!isLeaf(nodeId)){
                int leftChildPosition = nodeId * 2;
                int rightChildePosition = nodeId * 2 + 1;
                if(rightChildePosition <= size){
                    if((int)heap[leftChildPosition].second > (int)heap[rightChildePosition].second){
                        if((int)heap[nodeId].second > (int)heap[rightChildePosition].second){
                            swap(nodeId, rightChildePosition);
                            nodeId = rightChildePosition;
                        }else{
                            break;
                        }
                    }else{
                        if((int)heap[nodeId].second > (int)heap[leftChildPosition].second){
                            swap(nodeId, leftChildPosition);
                            nodeId = leftChildPosition;
                        }else{
                            break;
                        }
                    }
                }else{
                    if((int)heap[nodeId].second > (int)heap[leftChildPosition].second){
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
        this.dijkstraTable = new Pair[this.graph.numNodes()];
        for(int i = 1; i < this.graph.numNodes(); i++){
            this.dijkstraTable[i] = new Pair(-1, Integer.MAX_VALUE);
        }
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
        ;
        while(!pq.isEmpty()){
            int currNodeId = pq.removeMin();
            int[] neighbors = graph.getNeighbors(currNodeId);
            for(int neighbor: neighbors){

            }
        }
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