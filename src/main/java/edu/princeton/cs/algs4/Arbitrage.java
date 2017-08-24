/******************************************************************************
 *  Compilation:  javac Arbitrage.java
 *  Execution:    java Arbitrage < input.txt
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge.java
 *                BellmanFordSP.java
 *  Data file:    http://algs4.cs.princeton.edu/44sp/rates.txt
 *
 *  Arbitrage detection.
 *
 *  % more rates.txt
 *  5
 *  USD 1      0.741  0.657  1.061  1.005
 *  EUR 1.349  1      0.888  1.433  1.366
 *  GBP 1.521  1.126  1      1.614  1.538
 *  CHF 0.942  0.698  0.619  1      0.953
 *  CAD 0.995  0.732  0.650  1.049  1
 *
 *  % java Arbitrage < rates.txt
 *  1000.00000 USD =  741.00000 EUR
 *   741.00000 EUR = 1012.20600 CAD
 *  1012.20600 CAD = 1007.14497 USD
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.ArrayList;

/**
 *  The <tt>Arbitrage</tt> class provides a client that finds an arbitrage
 *  opportunity in a currency exchange table by constructing a
 *  complete-digraph representation of the exchange table and then finding
 *  a negative cycle in the digraph.
 *  <p>
 *  This implementation uses the Bellman-Ford algorithm to find a
 *  negative cycle in the complete digraph.
 *  The running time is proportional to <em>V</em><sup>3</sup> in the
 *  worst case, where <em>V</em> is the number of currencies.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Arbitrage {

    // this class cannot be instantiated
    private Arbitrage() { }


    public static String[] collectNames(int V, String file) {
        In in = new In(file);
        in.readLine(); // throw away num vertices
        String[] name = new String[V];
        for (int v = 0; v < V; v++) {
            String[] line = in.readLine().split("\\s+");
            name[v] = line[0];  //StdIn.readString();

        }
        return name;
    }

    public static EdgeWeightedDigraph buildArbitrageNetwork(String file){

        In in = new In(file);
        int V = Integer.parseInt(in.readLine()); //throw away since we already have it
        String[] name = collectNames(V, file);

        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++) {
            String[] line = in.readLine().split("\\s+");
            name[v] = line[0];  //StdIn.readString();

            for (int w = 0; w < V; w++) {
                double rate = Double.parseDouble(line[w+1]);
                // note using logarithm we can add the logged weights instead of multiplying the rates.
                DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate));
                edgeWeightedDigraph.addEdge(e);
            }
        }
        return edgeWeightedDigraph;
    }


    public static ArrayList<Double> buildArbitragePath(EdgeWeightedDigraph ewg){
        BellmanFordSP spt = new BellmanFordSP(ewg, 0);
        double originalStake = 1000.0;
        ArrayList<Double> path = new ArrayList<Double>();

        if (spt.hasNegativeCycle()) {
            double stake = originalStake;
            for (DirectedEdge e : spt.negativeCycle()) {
                path.add(stake);
                stake *= Math.exp(-e.weight());
            }
            path.add(stake);
        }
        return path;
    }


    public static ArrayList<String> buildNamePath(EdgeWeightedDigraph ewg, String[] name){
        BellmanFordSP spt = new BellmanFordSP(ewg, 0);
        ArrayList<String> namePath = new ArrayList<String>();

        if (spt.hasNegativeCycle()) {
            for (DirectedEdge e : spt.negativeCycle()) {
                namePath.add(name[e.from()]);
            }
            int firstEdgeVertex = spt.negativeCycle().iterator().next().from();
            String currency = name[firstEdgeVertex];
            namePath.add(currency); //first name = last name in arraylist
        }
        return namePath;
    }



    /**
     *  Reads the currency exchange table from standard input and
     *  prints an arbitrage opportunity to standard output (if one exists).
     */
    public static void main(String[] args) {

        // note just for reading top of file (num currencies)
        In in = new In(args[0]);

        // V currencies
        int V = Integer.parseInt(in.readLine());
        String[] name = collectNames(V, args[0]);

        // create complete network
        EdgeWeightedDigraph edgeWeightedDigraph = buildArbitrageNetwork(args[0]);
        StdOut.println(edgeWeightedDigraph);

        // find negative cycle
        ArrayList<Double> arbitragePath = buildArbitragePath(edgeWeightedDigraph);
        ArrayList<String> namePath = buildNamePath(edgeWeightedDigraph, name);

        StdOut.println("Arbitrage path: ");
        if (arbitragePath.size() != 0) {
            for (int i = 0; i < arbitragePath.size() - 1; i++) {
                StdOut.printf("%10.5f %s ", arbitragePath.get(i), namePath.get(i));
                StdOut.printf("= %10.5f %s\n", arbitragePath.get(i+1), namePath.get(i+1));
            }
            int lastIndex = arbitragePath.size() - 1;
            double profit = arbitragePath.get(lastIndex) - arbitragePath.get(0);
            StdOut.println("\nProfit made: " + profit + " " + namePath.get(0));
        }
        else {
            StdOut.println("No arbitrage opportunity");
        }
    }



        /*In in = new In(args[0]);

        // V currencies
        int V = Integer.parseInt(in.readLine());
        String[] name = new String[V];

        // create complete network
        EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++) {
            String[] line = in.readLine().split("\\s+");
            name[v] = line[0];  //StdIn.readString();

            for (int w = 0; w < V; w++) {
                double rate = Double.parseDouble(line[w+1]);
                // note using logarithm we can add the logged weights instead of multiplying the rates.
                DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate));
                edgeWeightedDigraph.addEdge(e);
            }
        }

        // find negative cycle
        BellmanFordSP spt = new BellmanFordSP(edgeWeightedDigraph, 0);
        StdOut.println("Arbitrage path: ");
        double originalStake = 1000.0;
        double finalStake = 0;
        if (spt.hasNegativeCycle()) {
            double stake = originalStake;
            for (DirectedEdge e : spt.negativeCycle()) {
                StdOut.printf("%10.5f %s ", stake, name[e.from()]);
                stake *= Math.exp(-e.weight());
                StdOut.printf("= %10.5f %s\n", stake, name[e.to()]);
            }
            finalStake = stake;
            String currency = name[spt.negativeCycle().iterator().next().from()];
            StdOut.println("\nProfit made: " + (finalStake - originalStake) + " " + currency);
        }
        else {
            StdOut.println("No arbitrage opportunity");
        }
    }*/

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
