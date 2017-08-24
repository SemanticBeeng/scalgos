/******************************************************************************
 *  Compilation:  javac LazyPrimMST.java
 *  Execution:    java LazyPrimMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                MinPQ.java UF.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using a lazy version of Prim's
 *  algorithm.
 *
 *  %  java LazyPrimMST tinyEWG.txt
 *  0-7 0.16000
 *  1-7 0.19000
 *  0-2 0.26000
 *  2-3 0.17000
 *  5-7 0.28000
 *  4-5 0.35000
 *  6-2 0.40000
 *  1.81000
 *
 *  % java LazyPrimMST mediumEWG.txt
 *  0-225   0.02383
 *  49-225  0.03314
 *  44-49   0.02107
 *  44-204  0.01774
 *  49-97   0.03121
 *  202-204 0.04207
 *  176-202 0.04299
 *  176-191 0.02089
 *  68-176  0.04396
 *  58-68   0.04795
 *  10.46351
 *
 *  % java LazyPrimMST largeEWG.txt
 *  ...
 *  647.66307
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The <tt>LazyPrimMST</tt> class represents a data type for computing a
 *  <em>minimum spanning tree</em> in an edge-weighted graph.
 *  The edge weights can be positive, zero, or negative and need not
 *  be distinct. If the graph is not connected, it computes a <em>minimum
 *  spanning forest</em>, which is the union of minimum spanning trees
 *  in each connected component. The <tt>mstWeight()</tt> method returns the
 *  mstWeight of a minimum spanning tree and the <tt>edges()</tt> method
 *  returns its edges.
 *  <p>
 *  This implementation uses a lazy version of <em>Prim's algorithm</em>
 *  with a binary heap of edges.
 *  The constructor takes time proportional to <em>E</em> log <em>E</em>
 *  and extra space (not including the graph) proportional to <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <tt>mstWeight()</tt> method takes constant time
 *  and the <tt>edges()</tt> method takes time proportional to <em>V</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  For alternate implementations, see {@link PrimMST}, {@link KruskalMST},
 *  and {@link BoruvkaMST}.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class LazyPrimMST {
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private double mstWeight;                            // total mstWeight of MST
    private Queue<Edge> mstEdges;                        // edges in the MST
    private boolean[] mstMarkedVertices;                 // mstMarkedVertices[v] = true if v on tree
    private MinPQ<Edge> crossingAndIneligibleEdges;      // edges with one endpoint in tree

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param edgeWeightedGraph the edge-weighted graph
     */
    public LazyPrimMST(EdgeWeightedGraph edgeWeightedGraph) {
        mstEdges = new Queue<Edge>();
        crossingAndIneligibleEdges = new MinPQ<Edge>();
        mstMarkedVertices = new boolean[edgeWeightedGraph.V()];
        for (int v = 0; v < edgeWeightedGraph.V(); v++)     // run Prim from all vertices to
            if (!mstMarkedVertices[v]) prim(edgeWeightedGraph, v);     // get a minimum spanning forest

        // check optimality conditions
        assert check(edgeWeightedGraph);
    }

    // run Prim's algorithm
    // note: first adds adjacent edges, then chooses min crossing edge (any one on the pq) and
    // checks if it's ineligible. Eager prim first deletes vertex then scans for adding adjacents.
    private void prim(EdgeWeightedGraph edgeWeightedGraph, int s) {
        scan(edgeWeightedGraph, s);
        while (!crossingAndIneligibleEdges.isEmpty()) {                        // better to stop when mstEdges has V-1 edges
            Edge ineligibleEdgeMaybe = crossingAndIneligibleEdges.delMin();                      // smallest edge on crossingAndIneligibleEdges
            int v = ineligibleEdgeMaybe.either();
            int w = ineligibleEdgeMaybe.other(v);        // two endpoints
            assert mstMarkedVertices[v] || mstMarkedVertices[w];
            if (mstMarkedVertices[v] && mstMarkedVertices[w]) continue;      // lazy, both v and w already scanned

            // note if we continue, then edge is a crossing edge so rename it (not necessary, just for clarity)
            Edge crossingEdge = ineligibleEdgeMaybe;
            mstEdges.enqueue(crossingEdge);                            // add e to MST
            mstWeight += crossingEdge.weight();
            if (!mstMarkedVertices[v]) scan(edgeWeightedGraph, v);               // v becomes part of tree
            if (!mstMarkedVertices[w]) scan(edgeWeightedGraph, w);               // w becomes part of tree
        }
    }

    // note add all edges e incident to v onto crossingAndIneligibleEdges if the other endpoint has not yet been scanned
    private void scan(EdgeWeightedGraph edgeWeightedGraph, int v) {
        assert !mstMarkedVertices[v];
        mstMarkedVertices[v] = true;
        for (Edge e : edgeWeightedGraph.adj(v))
            if (!mstMarkedVertices[e.other(v)])
                crossingAndIneligibleEdges.insert(e);
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     *    an iterable of edges
     */
    public Iterable<Edge> edges() {
        return mstEdges;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        return mstWeight;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph edgeWeightedGraph) {

        // check mstWeight
        double totalWeight = 0.0;
        for (Edge e : edges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal mstWeight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(edgeWeightedGraph.V());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : edgeWeightedGraph.edges()) {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges()) {

            // all edges in MST except e
            uf = new UF(edgeWeightedGraph.V());
            for (Edge f : mstEdges) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }

            // check that e is min mstWeight edge in crossing cut
            for (Edge f : edgeWeightedGraph.edges()) {
                int x = f.either(), y = f.other(x);
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }


    /**
     * Unit tests the <tt>LazyPrimMST</tt> data type.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph edgeWeightedGraph = new EdgeWeightedGraph(in);
        LazyPrimMST mst = new LazyPrimMST(edgeWeightedGraph);
        for (Edge e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }

}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
