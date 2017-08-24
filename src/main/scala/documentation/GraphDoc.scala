package documentation


/// note http://visualgo.net/dfsbfs for visualizing graphs (handy for moving graph around to see how program
// traverses) and for translation from adj to graph without tangles.

object GraphDocumentation {

     /**
       * done-[[edu.princeton.cs.algs4.Graph]]
       * done-[[edu.princeton.cs.algs4.GraphClient]]
       *
       * done-[[edu.princeton.cs.algs4.DepthFirstSearch]]
       * done-[[edu.princeton.cs.algs4.DepthFirstPaths]]
       * done-[[edu.princeton.cs.algs4.BreadthFirstPaths]]
       *
       * done-[[edu.princeton.cs.algs4.CC]]
       *
       * done-[[edu.princeton.cs.algs4.Cycle]]
       *
       * done-[[edu.princeton.cs.algs4.Bipartite]]
       *
       * done-[[edu.princeton.cs.algs4.SymbolGraph]]
       *
       * done-[[edu.princeton.cs.algs4.DegreesOfSeparation]]
       *
       * done-[[edu.princeton.cs.algs4.LazyPrimMST]]
       * done-[[edu.princeton.cs.algs4.PrimMST]]
       * done-[[edu.princeton.cs.algs4.KruskalMST]]
       *
       *
       * done-[[edu.princeton.cs.algs4.DijkstraSP]]
       * done-[[edu.princeton.cs.algs4.DijkstraUndirectedSP]]
       * ////// not really needed [[edu.princeton.cs.algs4.DijkstraAllPairsSP]]
       * done-[[edu.princeton.cs.algs4.AcyclicSP]]
       * done-[[edu.princeton.cs.algs4.CPM]]
       * done-[[edu.princeton.cs.algs4.BellmanFordSP]]
       * done-[[edu.princeton.cs.algs4.Arbitrage]]
       */

}


object GraphTestingIdeas {
     // note need to have this with note as comment here for other highlights to appear!

     /**
       * done-[[edu.princeton.cs.algs4.Graph]]
       * done-testing test that for v - w there is w - v
       * done-testing if graph is connected: tinyG is not but tinyCG is
       *
       * done-[[edu.princeton.cs.algs4.DepthFirstSearch]]
       * done-testing that from 2, these 7, 8, 9, 10, 11, 12 are not reachable
       * while 1, 2, 6, 5, 3, 4 are reachable
       * done-testing that from 9, _ are reachable while _ are not.
       *
       *
       * done-help investigate: how are these different from directed breadth and directed depth first paths?
       * [[edu.princeton.cs.algs4.DepthFirstPaths]]
       * done-testing that path from 2 to 3 is: 2-0-6-4-5-3
       * done-testing that path from 2 to 7 is: not connected (hasPath is false)
       * done-testing test edgeTO
       *
       * done-[[edu.princeton.cs.algs4.BreadthFirstPaths]]
       * done-testing that path from
       *
       *
       * done-[[edu.princeton.cs.algs4.CC]]
       * done-testing whether graph is connected (tinyG and tinyCG)
       * done-testing how many components
       * done-testing size array, id array...
       *
       * done-[[edu.princeton.cs.algs4.Cycle]]
       * done-testing confirm whether cycle exists or not
       * done-testing that one cycle is found even if others are left.
       *
       * done-[[edu.princeton.cs.algs4.Bipartite]]
       * done-testing use edgeTo to show there is an odd length cycle in bipartite-not.txt
       * done-testing use color to show that 6 and 7 have same color and are next to each other, defying bipartiteness
       * done-testing isBipartite = false
       * done-testing cycle = 7 1 4 2 6 7
       * done-testing use color to show there are always alternating colors in each consecutive vertex in bipartite-yes.txt
       * done-testing isBipartite = true
       * done-testing show for both that marked is all true for both graphs
       *
       *
       *
       * done-[[edu.princeton.cs.algs4.SymbolGraph]]
       * done-testing that given a movie, it has a particular actor, and given that actor, we get the original movie
       * example: Final Fantasy: The Spirits Within (2001) gives Demita, John, and when we say his name, we should
       * get Final Fantasy movie again.
       * done-testing that that for routes.txt, the index JFK yields 4 neighbors: ORD, ATL, MCO. (test 2 more examples and
       * same for movies.txt)
       * done-testing (first) that DFW has 3 neighbors (PHX, HOU, ORD)
       *
       *
       * done-[[edu.princeton.cs.algs4.DegreesOfSeparation]]
       *  note answers the question: how to get from "source" to "input" in the shortest possible way?
       *  testing: source = Bacon, Kevin, input = Kidman, Nicole
       *
       *
       *
       *
       * General MST testing:
       * done-testing the tree properties of the MST
       * done- //if graph is tree if either is true:
       * done- //1. graph has V - 1 edges.
       * done- //2. graph is acyclic.  (Use Cycle.java)
       * done- //3. graph is connected. (Use CC.java count of connected components)
       * done- //4. removing any edge disconnects it
       * done- //5. adding any edge makes it cyclic.
       * done- //6. exactly one simple path connects each pair of vertices in the graph.
       *
       * done-testing MST spans all vertices so make sure numvertices is same as original graph
       * done-testing if same edge weight occurs more than once in the graph then the graph can
       * have multiple MSTS (but the weight of each MST would be unique) (testing by making msts manually then saying
       * weight is same but their edges are different, do test on page 605 minie graph)
       * help does this mean that graphs with all different weights have one unique MST?
       * --- testing graph can have many spanning trees (undirected, unweighted graph)
       * done-testing Cut Property: after separating vertices into two implicit disjoint groups,
       * the crossing edge of minimum weight must be included in the MST of this graph.
       * done-testing that scan in LazyPrimMst puts all edges incident to v onto crossingEdges priority queue if the
       * other endpoint hasn't already been scanned.
       * done-testing  find way to test that lazyprim is lazy vs just primmst
       * (think lazyprim first scans then deletes, while prim deletes then scans).
       *
       *
       * done-[[edu.princeton.cs.algs4.KruskalMST]]
       *       done-testing general mst things
       *       done-testing that mstEdges are delivered in order of their weight, increasing.
       * done-[[edu.princeton.cs.algs4.PrimMST]]
       * done-[[edu.princeton.cs.algs4.LazyPrimMST]]
       *
       *
       *
       *
       *
       *
       * General shortest paths testing:
       * testing: paths are directed.
       * testing: graph can be unconnected, can have parallel edges, self-loops
       * done-testing: graphs and digraphs can be cyclic
       * done-testing: shortest path has no cycles
       * help-testing: may be more than one shortest path
       * done-testing: that shortest path is not equal to min span tree: (compare Prim and Dijkstra)
       *  (Sp is concerned with shortest path not weight while prim is concerned with weight not shortest path)
       *  example: A-B (5), B-C (5), A-C (9) Then min tree is: A-B (5), B-C (5) while shortest path: A-C (9)
       *
       *
       * done-[[edu.princeton.cs.algs4.DijkstraSP]]
       * done-testing: particular case with source = 0. and above general things.
       * done-testing: path weight from v->w equals distTo[w] (example 0 to 6 weight is 1.51) and see how it sums up
       * using the path itself.
       *
       * done-[[edu.princeton.cs.algs4.DijkstraUndirectedSP]]
       * done-testing particular case with source = 0 and above general things.
       * done-testing: path weight from v->w equals distTo[w] (example 0 to 6 weight is 1.51) and see how it sums up
       * using the path itself.
       *
       * //////////////[[edu.princeton.cs.algs4.DijkstraAllPairsSP]]
       * //////////////testing: that all shortest path trees are equal to images in snagit
       *
       *
       * done-[[edu.princeton.cs.algs4.AcyclicSP]]
       * done-testing: things from dijkstra tests
       *
       * done-[[edu.princeton.cs.algs4.AcyclicLP]]
       * done-testing: things from above just for max paths
       * done-testing: compare longest path with shortest from above
       *
       *
       * done-[[edu.princeton.cs.algs4.CPM]]
       * help: why does topological sort then acycliclp result in the answer to critical path? (intuition?)
       * done-testing: explaining how start-finish times are extracted from longest paths graph.
       *       example 1: job 4 start and finish is 70 and 108 (distTo[4] and distTo[14] since 14 is finish)
       *       Equivalent to edge-hopping 0 -> 9 -> 4 sum is 41 + 29 + 38
       *            path is actually: 0-10 (41), 10-9 (0), 9-19 (29), 19-4 (0), 4-14 (38)
       *            so start 4 is: 41 + 29 = 70
       *            so end 4 is : 70 + 38 = 108
       *       example 2: job 2 start and finish is 123 and 173 (distTo[2] and distTo[12]) since 12 is the finish)
       *       Equivalent to edge-hopping path 0 -> 9 -> 6 -> 8 -> 2
       *            path is actually: 0-10(41), 10-9, 9-19(29), 19-6, 6-16(21), 16-8, 8-18(32), 18-2, 2-12(50)
       *            so start 2 is: 41 + 29 + 21 + 32 = 123
       *            so end 2 is: 123 + 50 = 173
       * done-testing how ewdag is created:
       *       case 1: duration-weight edge between start and finish
       *            example: 0 -> 10 = 41, 1 -> 11 = 51, 2 -> 12 = 50
       *       case 2: the 0-weighted edge between source and start
       *            example: 20 -> 0, 20 -> 1, 20 -> 2 ... 9
       *       case 3: the 0 -weighted edge between finish and sink
       *            example: 10 -> 21, 11 -> 21, 12 -> 21, 13 -> 21
       *       case 4: the 0-weighted edge between finish and successors
       *            example: 10 -> 1, 10 -> 7, 10 -> 9
       *            example: 19 -> 4, 19 -> 6
       *            example: 17 -> 3, 17 -> 8
       *
       *
       *
       * [[edu.princeton.cs.algs4.BellmanFordSP]]
       * done-testing: find no SP for tinyEWDnc
       * done-testing: find SP with no neg cycles for tinyEWDn
       * done-testing: find SP with no neg cycles for tinyEWD
       * done-testing: that when negative weights are present, SP paths have more edges than
       * when there are only pos weights (tinyEWDn vs tinyEWD with bellmanford compare)
       * done-testing: the shortest path result has no negative cycles (in principle we avoid them)
       * done-testing: prerequisite: graph can have directed negative cycles
       * done-testing: all shortest paths are contained in shortest path tree
       * done-testing: path is..
       * done-testing: path weight is..
       * done-testing: mst not equal to spt
       * help: does primMST work for graph with negative edges?
       * help why are negative cycles such a problem? Are simple cycles a problem too?
       * help why check for neg cycle only at Vth multiple?
       * help why put vertices on queue?
       *       answer: must keep track of vertices from distTo[] neighbor that changed previously
       *       because those vertices could lead to change in distTo[]. (page 672)
       *
       *
       *
       *
       *
       * [[edu.princeton.cs.algs4.Arbitrage]]
       * done-testing that individual weights 0-1,1-4,4-0 to exp are the original rates
       * done-testing that sum of 0-1-4 to exp is the profit:
       * Math.exp(-(ewg.getAdj(0).get(3).weight() + ewg.getAdj(1).get(0).weight() + ewg.getAdj(4).get(4).weight()))
       *  * 1000 = 1007.14497
       * done-testing: usd-eur-cad conversion: 0.741*1.366*0.995 *stake = 1007.14497 profit
       * done-testing: graph has negative cycle (0-1-4) = (usd-eur-cad) which means profit above
       */
}

