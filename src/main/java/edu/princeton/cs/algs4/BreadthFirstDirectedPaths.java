/******************************************************************************
 *  Compilation:  javac BreadthFirstDirectedPaths.java
 *  Execution:    java BreadthFirstDirectedPaths V E
 *  Dependencies: Digraph.java Queue.java Stack.java
 *
 *  Run breadth first search on a digraph.
 *  Runs in O(E + V) time.
 *
 *  % java BreadthFirstDirectedPaths tinyDG.txt 3
 *  3 to 0 (2):  3->2->0
 *  3 to 1 (3):  3->2->0->1
 *  3 to 2 (1):  3->2
 *  3 to 3 (0):  3
 *  3 to 4 (2):  3->5->4
 *  3 to 5 (1):  3->5
 *  3 to 6 (-):  not connected
 *  3 to 7 (-):  not connected
 *  3 to 8 (-):  not connected
 *  3 to 9 (-):  not connected
 *  3 to 10 (-):  not connected
 *  3 to 11 (-):  not connected
 *  3 to 12 (-):  not connected
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;


/**
 *  The <tt>BreadthDirectedFirstPaths</tt> class represents a data type for finding
 *  shortest paths (number of edges) from a source vertex <em>s</em>
 *  (or set of source vertices) to every other vertex in the digraph.
 *  <p>
 *  This implementation uses breadth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  It uses extra space (not including the digraph) proportional to <em>V</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class BreadthFirstDirectedPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s->v path?
    private int[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
    private int[] distTo;      // distTo[v] = length of shortest s->v path

    /**
     * Computes the shortest path from <tt>s</tt> and every other vertex in graph <tt>G</tt>.
     * @param digraph the digraph
     * @param src the source vertex
     */
    public BreadthFirstDirectedPaths(Digraph digraph, int src) {
        marked = new boolean[digraph.V()];
        distTo = new int[digraph.V()];
        edgeTo = new int[digraph.V()];
        for (int v = 0; v < digraph.V(); v++)
            distTo[v] = INFINITY;
        bfs(digraph, src);
    }

    /**
     * Computes the shortest path from any one of the source vertices in <tt>sources</tt>
     * to every other vertex in graph <tt>G</tt>.
     * @param digraph the digraph
     * @param sources the source vertices
     */
    public BreadthFirstDirectedPaths(Digraph digraph, Iterable<Integer> sources) {
        marked = new boolean[digraph.V()];
        distTo = new int[digraph.V()];
        edgeTo = new int[digraph.V()];
        for (int v = 0; v < digraph.V(); v++)
            distTo[v] = INFINITY;
        bfs(digraph, sources);
    }

    // BFS from single source
    private void bfs(Digraph digraph, int src) {
        Queue<Integer> sourceQueue = new Queue<Integer>();
        marked[src] = true;
        distTo[src] = 0;
        sourceQueue.enqueue(src);
        while (!sourceQueue.isEmpty()) {
            int s = sourceQueue.dequeue();
            for (int w : digraph.adj(s)) {
                if (!marked[w]) {
                    edgeTo[w] = s;
                    distTo[w] = distTo[s] + 1;
                    marked[w] = true;
                    sourceQueue.enqueue(w);
                }
            }
        }
    }

    // BFS from multiple sources
    private void bfs(Digraph digraph, Iterable<Integer> sources) {
        Queue<Integer> sourceQueue = new Queue<Integer>();
        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            sourceQueue.enqueue(s);
        }
        while (!sourceQueue.isEmpty()) {
            int s = sourceQueue.dequeue();
            for (int w : digraph.adj(s)) {
                if (!marked[w]) {
                    edgeTo[w] = s;
                    distTo[w] = distTo[s] + 1;
                    marked[w] = true;
                    sourceQueue.enqueue(w);
                }
            }
        }
    }

    /**
     * Is there a directed path from the source <tt>s</tt> (or sources) to vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a directed path, <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * Returns the number of edges in a shortest path from the source <tt>s</tt>
     * (or sources) to vertex <tt>v</tt>?
     * @param v the vertex
     * @return the number of edges in a shortest path
     */
    public int distTo(int v) {
        return distTo[v];
    }


    // note by me
    /*public ArrayList<Iterable<Integer>> getPaths() {
        for (int i = 0; i <)
    }*/

    /**
     * Returns a shortest path from <tt>s</tt> (or sources) to <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }


    //note made by me
    public int edgeTo(int i){
        return edgeTo[i];
    }


    /**
     * Unit tests the <tt>BreadthFirstDirectedPaths</tt> data type.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        // StdOut.println(G);

        int src = Integer.parseInt(args[1]);
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(digraph, src);

        for (int v = 0; v < digraph.V(); v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", src, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == src) StdOut.print(x);
                    else        StdOut.print("->" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d (-):  not connected\n", src, v);
            }

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
