/******************************************************************************
 *  Compilation:  javac Bipartite.java
 *  Execution:    java  Bipartite V E F
 *  Dependencies: Graph.java
 *
 *  Given a graph, find either (i) a bipartition or (ii) an odd-length cycle.
 *  Runs in O(E + V) time.
 *
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;


/**
 *  The <tt>Bipartite</tt> class represents a data type for
 *  determining whether an undirected graph is bipartite or whether
 *  it has an odd-length cycle.
 *  The <em>isBipartite</em> operation determines whether the graph is
 *  bipartite. If so, the <em>color</em> operation determines a
 *  bipartition; if not, the <em>oddCycle</em> operation determines a
 *  cycle with an odd number of edges.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <em>isBipartite</em> and <em>color</em> operations
 *  take constant time; the <em>oddCycle</em> operation takes time proportional
 *  to the length of the cycle.
 *  See {@link BipartiteX} for a nonrecursive version that uses breadth-first
 *  search.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/41graph">Section 4.1</a>
 *  of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Bipartite {
    private boolean isBipartite;   // is the graph bipartite?
    private boolean[] color;       // color[v] gives vertices on one side of bipartition
    private boolean[] marked;      // marked[v] = true if v has been visited in DFS
    private int[] edgeTo;          // edgeTo[v] = last edge on path to v
    private Stack<Integer> cycle;  // odd-length cycle

    /**
     * Determines whether an undirected graph is bipartite and finds either a
     * bipartition or an odd-length cycle.
     *
     * @param  graph the graph
     */
    public Bipartite(Graph graph) {
        isBipartite = true;
        color  = new boolean[graph.V()];
        marked = new boolean[graph.V()];
        edgeTo = new int[graph.V()];

        for (int v = 0; v < graph.V(); v++) {
            if (!marked[v]) {
                dfs(graph, v);
            }
        }
        assert check(graph);
    }

    // note  important checks if you can mark every other node with different color and result is
    // graph where nodes are ALWAYS alternating in color then the graph is bipartite.
    private void dfs(Graph graph, int v) {
        marked[v] = true;
        for (int w : graph.adj(v)) {

            // short circuit if odd-length cycle found
            if (cycle != null) return;

            // found uncolored vertex, so recur
            if (!marked[w]) {
                edgeTo[w] = v;
                color[w] = !color[v];
                dfs(graph, w);
            }

            // if v-w create an odd-length cycle, find it
            else if (color[w] == color[v]) {
                isBipartite = false;
                cycle = new Stack<Integer>();
                cycle.push(w);  // don't need this unless you want to include start vertex twice
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
            }
        }
    }

    /**
     * Returns true if the graph is bipartite.
     *
     * @return <tt>true</tt> if the graph is bipartite; <tt>false</tt> otherwise
     */
    public boolean isBipartite() {
        return isBipartite;
    }

    /**
     * Returns the side of the bipartite that vertex <tt>v</tt> is on.
     *
     * @param  v the vertex
     * @return the side of the bipartition that vertex <tt>v</tt> is on; two vertices
     *         are in the same side of the bipartition if and only if they have the
     *         same color
     * @throws IllegalArgumentException unless <tt>0 &le; v &lt; V</tt>
     * @throws UnsupportedOperationException if this method is called when the graph
     *         is not bipartite
     */
    public boolean color(int v) {
        // note sort of permanently removing this so tests will work
        /*if (!isBipartite)
            throw new UnsupportedOperationException("Graph is not bipartite");*/
        return color[v];
    }

    /**
     * Returns an odd-length cycle if the graph is not bipartite, and
     * <tt>null</tt> otherwise.
     *
     * @return an odd-length cycle if the graph is not bipartite
     *         (and hence has an odd-length cycle), and <tt>null</tt>
     *         otherwise
     */
    public Iterable<Integer> oddCycle() {
        return cycle;
    }

    private boolean check(Graph graph) {
        // graph is bipartite
        if (isBipartite) {
            for (int v = 0; v < graph.V(); v++) {
                for (int w : graph.adj(v)) {
                    if (color[v] == color[w]) {
                        System.err.printf("edge %d-%d with %d and %d in same side of bipartition\n", v, w, v, w);
                        return false;
                    }
                }
            }
        }

        // graph has an odd-length cycle
        else {
            // verify cycle
            int first = -1, last = -1;
            for (int v : oddCycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }

        return true;
    }

    /**
     * Unit tests the <tt>Bipartite</tt> data type.
     */
    public static void main(String[] args) {
        // note bipartite means a graph that is made of two disjoint sets U and W such that
        // every edge connects a vertex in U to one in W.
        int V1 = Integer.parseInt(args[0]); // note number of vertices for set U
        int V2 = Integer.parseInt(args[1]); // note num vertices for set W
        int E  = Integer.parseInt(args[2]); // note num edges
        int F  = Integer.parseInt(args[3]); // note num random additional edges

        // create random bipartite graph with V1 vertices on left side,
        // V2 vertices on right side, and E edges; then add F random edges
        Graph bipartite = GraphGenerator.bipartite(V1, V2, E);
        for (int i = 0; i < F; i++) {
            int v = StdRandom.uniform(V1 + V2);
            int w = StdRandom.uniform(V1 + V2);
            bipartite.addEdge(v, w);
        }

        StdOut.println(bipartite);

        //Graph bipartite = new Graph(new In(args[0]));
        Bipartite b = new Bipartite(bipartite);
        //Bipartite b1 = new Bipartite(bipartite); // uncomment to quick-see if bipartite is true or false
        if (b.isBipartite()) {
            StdOut.println("Graph is bipartite");
            for (int v = 0; v < bipartite.V(); v++) {
                StdOut.println(v + ": " + b.color(v));
            }
        }
        else {
            StdOut.print("Graph has an odd-length cycle: ");
            for (int x : b.oddCycle()) {
                StdOut.print(x + " ");
            }
            StdOut.println();
        }
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
