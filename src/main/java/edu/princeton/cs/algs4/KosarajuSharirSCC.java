/******************************************************************************
 *  Compilation:  javac KosarajuSharirSCC.java
 *  Execution:    java KosarajuSharirSCC filename.txt
 *  Dependencies: Digraph.java TransitiveClosure.java StdOut.java In.java
 *  Data files:   http://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *
 *  Compute the strongly-connected components of a digraph using the
 *  Kosaraju-Sharir algorithm.
 *
 *  Runs in O(E + V) time.
 *
 *  % java KosarajuSCC tinyDG.txt
 *  5 components
 *  1
 *  0 2 3 4 5
 *  9 10 11 12
 *  6 8
 *  7
 *
 *  % java KosarajuSharirSCC mediumDG.txt
 *  10 components
 *  21
 *  2 5 6 8 9 11 12 13 15 16 18 19 22 23 25 26 28 29 30 31 32 33 34 35 37 38 39 40 42 43 44 46 47 48 49
 *  14
 *  3 4 17 20 24 27 36
 *  41
 *  7
 *  45
 *  1
 *  0
 *  10
 *
 *  % java -Xss50m KosarajuSharirSCC mediumDG.txt
 *  25 components
 *  7 11 32 36 61 84 95 116 121 128 230   ...
 *  28 73 80 104 115 143 149 164 184 185  ...
 *  38 40 200 201 207 218 286 387 418 422 ...
 *  12 14 56 78 87 103 216 269 271 272    ...
 *  42 48 112 135 160 217 243 246 273 346 ...
 *  46 76 96 97 224 237 297 303 308 309   ...
 *  9 15 21 22 27 90 167 214 220 225 227  ...
 *  74 99 133 146 161 166 202 205 245 262 ...
 *  43 83 94 120 125 183 195 206 244 254  ...
 *  1 13 54 91 92 93 106 140 156 194 208  ...
 *  10 39 67 69 131 144 145 154 168 258   ...
 *  6 52 66 113 118 122 139 147 212 213   ...
 *  8 127 150 182 203 204 249 367 400 432 ...
 *  63 65 101 107 108 136 169 170 171 173 ...
 *  55 71 102 155 159 198 228 252 325 419 ...
 *  4 25 34 58 70 152 172 196 199 210 226 ...
 *  2 44 50 88 109 138 141 178 197 211    ...
 *  57 89 129 162 174 179 188 209 238 276 ...
 *  33 41 49 119 126 132 148 181 215 221  ...
 *  3 18 23 26 35 64 105 124 157 186 251  ...
 *  5 16 17 20 31 47 81 98 158 180 187    ...
 *  24 29 51 59 75 82 100 114 117 134 151 ...
 *  30 45 53 60 72 85 111 130 137 142 163 ...
 *  19 37 62 77 79 110 153 352 353 361    ...
 *  0 68 86 123 165 176 193 239 289 336   ...
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The <tt>KosarajuSharirSCC</tt> class represents a data type for
 *  determining the strong components in a digraph.
 *  The <em>id</em> operation determines in which strong component
 *  a given vertex lies; the <em>areStronglyConnected</em> operation
 *  determines whether two vertices are in the same strong component;
 *  and the <em>componentCount</em> operation determines the number of strong
 *  components.

 *  The <em>component identifier</em> of a component is one of the
 *  vertices in the strong component: two vertices have the same component
 *  identifier if and only if they are in the same strong component.

 *  <p>
 *  This implementation uses the Kosaraju-Sharir algorithm.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <em>id</em>, <em>componentCount</em>, and <em>areStronglyConnected</em>
 *  operations take constant time.
 *  For alternate implementations of the same API, see
 *  {@link TarjanSCC} and {@link GabowSCC}.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class KosarajuSharirSCC {
    private boolean[] marked;     // marked[v] = has vertex v been visited?
    private int[] id;             // id[v] = id of strong component containing v
    private int componentCount;   // number of strongly-connected components

    /**
     * Computes the strong components of the digraph <tt>G</tt>.
     *
     *
     * important summary idea: marks the digraph's vertices in the order of topologically sorted vertices.
     * So it uses the topological order of vertices to guide it in marking them. Also keeps
     * track of which component they are in and how many components there are. Once there are no
     * more connections on the thread you were following (so all are marked in that group)
     * then get out of dfs and increment componentCount. All indexes in id that have same
     * number are in the same component.
     *
     * @param digraph the digraph
     */
    public KosarajuSharirSCC(Digraph digraph) {

        // compute reverse postorder of reverse graph
        Digraph rev = digraph.reverse(); // note temporary variable so we don't debug rev all the time!
        DepthFirstOrder order = new DepthFirstOrder(rev);

        // run DFS on digraph, using reverse postorder to guide calculation
        marked = new boolean[digraph.V()];
        id = new int[digraph.V()];
        for (int v : order.reversePost()) {
            if (!marked[v]) {
                dfs(digraph, v);
                componentCount++; // increment this, means we are going into new component
            }
        }

        // check that id[] gives strong components
        assert check(digraph);
    }
    // note id has zeroes good and zeroes that should be None (implement in scala)
    // DFS on graph digraph
    private void dfs(Digraph digraph, int v) {
        marked[v] = true;
        id[v] = componentCount;
        for (int w : digraph.adj(v)) {
            if (!marked[w]) dfs(digraph, w);
        }
    }

    /**
     * Returns the number of strong components.
     * @return the number of strong components
     */
    public int count() {
        return componentCount;
    }


    // note made by me
    public int[] ids() { return id; }

    /**
     * Are vertices <tt>v</tt> and <tt>w</tt> in the same strong component?
     * @param v one vertex
     * @param w the other vertex
     * @return <tt>true</tt> if vertices <tt>v</tt> and <tt>w</tt> are in the same
     *     strong component, and <tt>false</tt> otherwise
     */
    public boolean areStronglyConnected(int v, int w) {
        return id[v] == id[w];
    }




    // note made by me
    /**
     * Computes list of vertices in the strongly connected components
     * Returns an array [] of Queue<Integer> objects.
     */
    public Queue<Integer>[] stronglyConnected(Digraph digraph) {

        // compute list of vertices in each strong component
        // making an array that contains queue<integer> objects.
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[componentCount];
        for (int i = 0; i < componentCount; i++) { //initializing
            components[i] = new Queue<Integer>();
        }
        for (int v = 0; v < digraph.V(); v++) { // filling
            components[id[v]].enqueue(v);
        }
        return components;
    }




    /**
     * Returns the component id of the strong component containing vertex <tt>v</tt>.
     * @param v the vertex
     * @return the component id of the strong component containing vertex <tt>v</tt>
     */
    public int id(int v) {
        return id[v];
    }



    // does the id[] array contain the strongly connected components?
    private boolean check(Digraph digraph) {
        TransitiveClosure tc = new TransitiveClosure(digraph);
        for (int v = 0; v < digraph.V(); v++) {
            for (int w = 0; w < digraph.V(); w++) {
                if (areStronglyConnected(v, w) != (tc.reachable(v, w) && tc.reachable(w, v)))
                    return false;
            }
        }
        return true;
    }




    /**
     * Unit tests the <tt>KosarajuSharirSCC</tt> data type.
     */
    // note help todo: for direct connections 2 -> 3 and 3 -> 2 this says 1 components, 0 as if zero was a vertex but
    // it's not! What is going on?

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        KosarajuSharirSCC scc = new KosarajuSharirSCC(digraph);

        // number of connected components
        int numStrongConn = scc.count();
        StdOut.println(numStrongConn + " components");
        Queue<Integer>[] components = scc.stronglyConnected(digraph);

        // print results
        for (int i = 0; i < numStrongConn; i++) {
            for (int v : components[i]) {
                StdOut.print(v + " ");
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
