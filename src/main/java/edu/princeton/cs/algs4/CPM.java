/******************************************************************************
 *  Compilation:  javac CPM.java
 *  Execution:    java CPM < input.txt
 *  Dependencies: EdgeWeightedDigraph.java AcyclicDigraphLP.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/44sp/jobsPC.txt
 *
 *  Critical path method.
 *
 *  % java CPM < jobsPC.txt
 *   job   start  finish
 *  --------------------
 *     0     0.0    41.0
 *     1    41.0    92.0
 *     2   123.0   173.0
 *     3    91.0   127.0
 *     4    70.0   108.0
 *     5     0.0    45.0
 *     6    70.0    91.0
 *     7    41.0    73.0
 *     8    91.0   123.0
 *     9    41.0    70.0
 *  Finish time:   173.0
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  The <tt>CPM</tt> class provides a client that solves the
 *  parallel precedence-constrained job scheduling problem
 *  via the <em>critical path method</em>. It reduces the problem
 *  to the longest-paths problem in edge-weighted DAGs.
 *  It builds an edge-weighted digraph (which must be a DAG)
 *  from the job-scheduling problem specification,
 *  finds the longest-paths tree, and computes the longest-paths
 *  lengths (which are precisely the start times for each job).
 *  <p>
 *  This implementation uses {@link AcyclicLP} to find a longest
 *  path in a DAG.
 *  The running time is proportional to <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of jobs and <em>E</em> is the
 *  number of precedence constraints.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class CPM {

    // this class cannot be instantiated
    private CPM() { }


    public static EdgeWeightedDigraph buildCriticalPathNetwork(int N, int source, int sink, In in){
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(2*N + 2);
        for (int i = 0; i < N; i++) {
            String[] line = in.readLine().split("\\s+");
            double duration = Double.parseDouble(line[0]); //StdIn.readDouble();
            g.addEdge(new DirectedEdge(i, i+N, duration));
            g.addEdge(new DirectedEdge(source, i, 0.0));
            g.addEdge(new DirectedEdge(i+N, sink, 0.0));

            // precedence constraints
            for (int j = 1; j < line.length; j++) {
                int successor = Integer.parseInt(line[j]); //StdIn.readInt();
                g.addEdge(new DirectedEdge(i+N, successor, 0.0));
            }
        }
        return g;
    }



    public static List<List<Double>> computeLongestPaths(EdgeWeightedDigraph g, int N, int source, int sink) {

        AcyclicLP lp = new AcyclicLP(g, source);

        List<Double> startTimes = new ArrayList<Double>(N);
        List<Double> finishTimes = new ArrayList<Double>(N);
        List<Double> totalTime = new ArrayList<Double>(Arrays.asList(lp.distTo(sink)));

        for (int i = 0; i < N; i++){
            startTimes.add(lp.distTo(i));
            finishTimes.add(lp.distTo(i+N));
        }
        List<List<Double>> group = new ArrayList<List<Double>>();
        group.add(startTimes);
        group.add(finishTimes);
        group.add(totalTime);

        return group;
    }


    /**
     *  Reads the precedence constraints from standard input
     *  and prints a feasible schedule to standard output.
     */
    public static void main(String[] args) {

        // note for visualizing graphs: http://graphonline.ru/en/
        In in = new In(args[0]);

        // number of jobs
        int N = Integer.parseInt(in.readLine()); //StdIn.readInt(); StdIn.readLine(); //go to next line.

        // source and sink
        int source = 2*N;
        int sink   = 2*N + 1;

        // build network
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(2*N + 2);
        for (int i = 0; i < N; i++) {
            String[] line = in.readLine().split("\\s+");
            double duration = Double.parseDouble(line[0]); //StdIn.readDouble();
            g.addEdge(new DirectedEdge(i, i+N, duration));
            g.addEdge(new DirectedEdge(source, i, 0.0));
            g.addEdge(new DirectedEdge(i+N, sink, 0.0));

            // precedence constraints
            for (int j = 1; j < line.length; j++) {
                int successor = Integer.parseInt(line[j]); //StdIn.readInt();
                g.addEdge(new DirectedEdge(i+N, successor, 0.0));
            }
        }

        //show graph
        StdOut.println(g);


        // compute longest path
        AcyclicLP lp = new AcyclicLP(g, source);

        // print results
        StdOut.println(" job   start  finish");
        StdOut.println("--------------------");
        for (int i = 0; i < N; i++) {
            StdOut.printf("%4d %7.1f %7.1f\n", i, lp.distTo(i), lp.distTo(i+N));
        }
        StdOut.printf("Finish time: %7.1f\n", lp.distTo(sink));


        /*// build network
        EdgeWeightedDigraph g = buildCriticalPathNetwork(N, source, sink, in);
        StdOut.println(g); //show graph


        // compute longest path
        List<List<Double>> times = computeLongestPaths(g, N, source, sink);
        Double[] startTimes = times.get(0).toArray(new Double[N]);
        Double[] finishTimes = times.get(1).toArray(new Double[N]);
        Double totalTime = times.get(2).get(0);


        // print results
        StdOut.println(" job   start  finish");
        StdOut.println("--------------------");
        for (int i = 0; i < N; i++) {
            StdOut.printf("%4d %7.1f %7.1f\n", i, startTimes[i], finishTimes[i]);
        }
        StdOut.printf("Finish time: %7.1f\n", totalTime);*/
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
