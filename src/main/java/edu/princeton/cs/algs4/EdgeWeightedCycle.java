/*
package edu.princeton.cs.algs4;



// note made by me so that [[MinSpanningTreeLazyPrimTests]] would work.
// note amend: not really needed, just convert ewgraph into normal graph and then call Cycle.java

public class EdgeWeightedCycle {
    private boolean[] marked;             // marked[v] = has vertex v been marked?
    private int[] edgeTo;        // edgeTo[v] = previous edge on path to v
    private boolean[] onStack;            // onStack[v] = is vertex on the stack?
    //private Stack<Edge> cycleEdges;    // cycle (or null if no such cycle)
    private Stack<Integer> cycle; // actual vertices in cycle


    public EdgeWeightedCycle(EdgeWeightedGraph edgeWeightedGraph) {
        Graph graph = edgeWeightedGraph.convertToGraph();

        if (hasSelfLoop(graph)) return;
        if (hasParallelEdges(graph)) return;
        marked = new boolean[graph.V()];
        edgeTo = new int[graph.V()];
        for (int v = 0; v < graph.V(); v++)
            if (!marked[v])
                dfs(graph, -1, v);
    }


    private void dfs(Graph graph, int u, int v) {
        marked[v] = true;
        for (int w : graph.adj(v)) {

            // short circuit if cycle already found
            if (cycle != null) return;

            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(graph, v, w);
            }

            // check for cycle (but disregard reverse of edge leading to v)
            else if (w != u) { // note if marked and w not equals v then there is a cycle.
                cycle = new Stack<Integer>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
    }




    // does this graph have a self loop?
    // side effect: initialize cycle to be self loop
    private boolean hasSelfLoop(Graph graph) {
        for (int v = 0; v < graph.V(); v++) {
            for (int w : graph.adj(v)) {
                if (v == w) {
                    cycle = new Stack<Integer>();
                    cycle.push(v);
                    cycle.push(v);
                    return true;
                }
            }
        }
        return false;
    }

    // does this graph have two parallel edges?
    // side effect: initialize cycle to be two parallel edges
    private boolean hasParallelEdges(Graph graph) {
        marked = new boolean[graph.V()];

        for (int v = 0; v < graph.V(); v++) {

            // check for parallel edges incident to v
            for (int w : graph.adj(v)) {
                if (marked[w]) {
                    cycle = new Stack<Integer>();
                    cycle.push(v);
                    cycle.push(w);
                    cycle.push(v);
                    return true;
                }
                marked[w] = true;
            }

            // reset so marked[v] = false for all v
            for (int w : graph.adj(v)) {
                marked[w] = false;
            }
        }
        return false;
    }



    */
/*public Iterable<Edge> getCycleEdges(int w) {
        cycleEdges = new Stack<Edge>();
        int t = w;
        do {
            Edge edge = edgeTo[w];
            cycleEdges.push(edge);
            t = edge.other(t);
        } while (t != w);
        return cycleEdges;
    }*//*



    */
/**
     * Does the edge-weighted graph have a cycle?
     * @return <tt>true</tt> if the edge-weighted graph has a cycle,
     * <tt>false</tt> otherwise
     *//*

    public boolean hasCycle() {
        return cycle != null;
    }

    */
/**
     * Returns a cycle if the edge-weighted graph has a cycle,
     * and <tt>null</tt> otherwise.
     * @return a cycle (as an iterable) if the edge-weighted graph
     *    has a cycle, and <tt>null</tt> otherwise
     *//*

    public Iterable<Integer> cycle() {
        return cycle;
    }



    */
/**
     * Unit tests the <tt>EdgeWeightedCycle</tt> data type.
     *//*

    public static void main(String[] args) {

        In in = new In(args[0]);
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(in);
        StdOut.println(edgeWeightedGraph);

        EdgeWeightedCycle finder = new EdgeWeightedCycle(edgeWeightedGraph);

        if (finder.hasCycle()) {
            StdOut.print("Cycle: ");
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("No cycle");
        }
    }
}
*/
