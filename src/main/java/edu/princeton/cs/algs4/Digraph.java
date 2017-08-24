/******************************************************************************
 *  Compilation:  javac Digraph.java
 *  Execution:    java Digraph filename.txt
 *  Dependencies: Bag.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *
 *  A graph, implemented using an array of lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java Digraph tinyDG.txt
 *  13 vertices, 22 edges
 *  0: 5 1
 *  1:
 *  2: 0 3
 *  3: 5 2
 *  4: 3 2
 *  5: 4
 *  6: 9 4 8 0
 *  7: 6 9
 *  8: 6
 *  9: 11 10
 *  10: 12
 *  11: 4 12
 *  12: 9
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 *  The <tt>Digraph</tt> class represents a directed graph of vertices
 *  named 0 through <em>V</em> - 1.
 *  It supports the following two primary operations: add an edge to the digraph,
 *  iterate over all of the vertices adjacent from a given vertex.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which
 *  is a vertex-indexed array of {@link Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the vertices adjacent from a given vertex, which takes
 *  time proportional to the number of such vertices.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class Digraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;           // number of vertices in this digraph
    private int E;                 // number of edges in this digraph
    private Bag<Integer>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;        // indegree[v] = indegree of vertex v

    /**
     * Initializes an empty digraph with <em>V</em> vertices.
     *
     * @param  numVertices the number of vertices
     * @throws IllegalArgumentException if V < 0
     */
    public Digraph(int numVertices) {
        if (numVertices < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = numVertices;
        this.E = 0;
        indegree = new int[numVertices];
        adj = (Bag<Integer>[]) new Bag[numVertices];
        for (int v = 0; v < numVertices; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    /**
     * Initializes a digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices, with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IndexOutOfBoundsException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public Digraph(In in) {
        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
            indegree = new int[V];
            adj = (Bag<Integer>[]) new Bag[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new Bag<Integer>();
            }
            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                this.addEdge(v, w);
            }
        }
        catch (NoSuchElementException e) {
            throw new InputMismatchException("Invalid input format in Digraph constructor");
        }
    }

    /**
     * Initializes a new digraph that is a deep copy of the specified digraph.
     *
     * @param  g the digraph to copy
     */
    public Digraph(Digraph g) {
        this(g.V());
        this.E = g.E();
        for (int v = 0; v < V; v++)
            this.indegree[v] = g.indegree(v);
        for (int v = 0; v < g.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Integer> reverse = new Stack<Integer>();
            for (int w : g.adj[v]) {
                reverse.push(w);
            }
            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }


    // note made by me so that I can know what adj(v) contains, since Bag has no contains method.
    // need this so I can make EdgeWCcyle program by converting ewg to simple graph.
    public ArrayList<Integer> getAdj(int v) {
        validateVertex(v);
        ArrayList<Integer> newAdj = new ArrayList<>();
        for (int w : adj[v]) {
            newAdj.add(w);
        }
        return newAdj;
    }


    /**
     * Returns the number of vertices in this digraph.
     *
     * @return the number of vertices in this digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this digraph.
     *
     * @return the number of edges in this digraph
     */
    public int E() {
        return E;
    }


    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the directed edge v->w to this digraph.
     *
     * @param  v the tail vertex
     * @param  w the head vertex
     * @throws IndexOutOfBoundsException unless both 0 <= v < V and 0 <= w < V
     */
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        indegree[w]++;
        E++;
    }

    /**
     * Returns the vertices adjacent from vertex <tt>v</tt> in this digraph.
     * Example: adj(v) contains all the ws to which v is connected at the head.
     * So (6,4), (6,9), (6,8) translates to: adj (6) == 4, 9, 8
     * In other words: edge v -> w is represented as list node containing w
     * in the linked list corresponding to v.
     *
     * @param  v the vertex
     * @return the vertices adjacent from vertex <tt>v</tt> in this digraph, as an iterable
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex <tt>v</tt>.
     * This is known as the <em>outdegree</em> of vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the outdegree of vertex <tt>v</tt>
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns the number of directed edges incident to vertex <tt>v</tt>.
     * This is known as the <em>indegree</em> of vertex <tt>v</tt>.
     *
     * @param  v the vertex
     * @return the indegree of vertex <tt>v</tt>
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    /**
     * Returns the reverse of the digraph.
     *Example: 0: 1, 2 3 and 3: 0, 4, 5 and 4:0 become:
     * 1:0, 2:0, 3:0, 0: 3,4,   4:3, 5:3
     *
     * @return the reverse of the digraph
     */
    public Digraph reverse() {
        Digraph r = new Digraph(V);
        for (int v = 0; v < V; v++) {
            for (int w : adj(v)) {
                r.addEdge(w, v);
            }
        }
        return r;
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(String.format("%d: ", v));
            for (int w : adj[v]) { // note for this head (v) print all tails (w)
                s.append(String.format("%d ", w));
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Unit tests the <tt>Digraph</tt> data type.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        // note prints all the connections. So if there is (6,9), (6,4), (6,8)
        // then it will print --- >  6 : 9 4 8 0
        Digraph digraph = new Digraph(in);
        StdOut.println("The digraph: " + digraph);
        StdOut.println("\nReversed digraph: " + digraph.reverse());
    }

    /*
    * Input file:
    *   13
        22
         4  2
         2  3
         3  2
         6  0
         0  1
         2  0
        11 12
        12  9
         9 10
         9 11
         7  9
        10 12
        11  4
         4  3
         3  5
         6  8
         8  6
         5  4
         0  5
         6  4
         6  9
         7  6

    * */
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
