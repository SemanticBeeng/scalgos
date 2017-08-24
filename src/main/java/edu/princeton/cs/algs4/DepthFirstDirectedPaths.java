/******************************************************************************
 *  Compilation:  javac DepthFirstDirectedPaths.java
 *  Execution:    java DepthFirstDirectedPaths G source
 *  Dependencies: Digraph.java Stack.java
 *
 *  Determine reachability in a digraph from a given vertex using
 *  depth first search.
 *  Runs in O(E + V) time.
 *
 *  % tinyDG.txt 3
 *  3 to 0:  3-5-4-2-0
 *  3 to 1:  3-5-4-2-0-1
 *  3 to 2:  3-5-4-2
 *  3 to 3:  3
 *  3 to 4:  3-5-4
 *  3 to 5:  3-5
 *  3 to 6:  not connected
 *  3 to 7:  not connected
 *  3 to 8:  not connected
 *  3 to 9:  not connected
 *  3 to 10:  not connected
 *  3 to 11:  not connected
 *  3 to 12:  not connected
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The <tt>DepthFirstDirectedPaths</tt> class represents a data type for finding
 *  directed paths from a source vertex <em>source</em> to every
 *  other vertex in the digraph.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  It uses extra space (not including the graph) proportional to <em>V</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class DepthFirstDirectedPaths {
    private boolean[] marked;  // marked[v] = true if v is reachable from source
    private int[] edgeTo;      // edgeTo[v] = last edge on path from source to v
    private final int source;       // source vertex

    /**
     * Computes a directed path from <tt>source</tt> to every other vertex in digraph <tt>G</tt>.
     * @param digraph the digraph
     * @param src the source vertex
     */
    public DepthFirstDirectedPaths(Digraph digraph, int src) {
        marked = new boolean[digraph.V()];
        edgeTo = new int[digraph.V()];
        this.source = src;
        dfs(digraph, src);
    }

    /**
     * note dfs() marks all vertices connected to a source.
     * @param digraph
     * @param src
     */
    private void dfs(Digraph digraph, int src) {
        marked[src] = true;
        for (int w : digraph.adj(src)) {
            if (!marked[w]) {
                edgeTo[w] = src;
                dfs(digraph, w);
            }
        }
    }

    /**
     * Is there a directed path from the source vertex <tt>source</tt> to vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a directed path from the source
     *   vertex <tt>source</tt> to vertex <tt>v</tt>, <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }


    /**
     * Returns a directed path from the source vertex <tt>source</tt> to vertex <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a directed path from the source vertex
     *   <tt>source</tt> to vertex <tt>v</tt>, as an Iterable
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != source; x = edgeTo[x])
            path.push(x);
        path.push(source);
        return path;
    }


    //note made by me
    public int edgeTo(int i){
        return edgeTo[i];
    }

    /**
     * Unit tests the <tt>DepthFirstDirectedPaths</tt> data type.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        // StdOut.println(G);

        int src = Integer.parseInt(args[1]);
        DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(digraph, src);

        for (int v = 0; v < digraph.V(); v++) {
            if (dfs.hasPathTo(v)) {
                StdOut.printf("%d to %d:  ", src, v);
                for (int x : dfs.pathTo(v)) {
                    if (x == src) StdOut.print(x);
                    else        StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d:  not connected\n", src, v);
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
