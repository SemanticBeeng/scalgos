package princetonScalAlgsTesting


import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataDigraph._
import DataGraph._
import scala.collection.JavaConversions._



trait DijkstraState {
     def getStateD(filename: String, source: Int): DijkstraSP = {
          val in: In = new In(filename)
          val edgeWeightedDigraph: EdgeWeightedDigraph = new EdgeWeightedDigraph(in)
          new DijkstraSP(edgeWeightedDigraph, source)
     }

     def getStateU(filename: String, source: Int): DijkstraUndirectedSP = {
          val in: In = new In(filename)
          val edgeWeightedGraph: EdgeWeightedGraph = new EdgeWeightedGraph(in)
          new DijkstraUndirectedSP(edgeWeightedGraph, source)
     }

     def getStateMST(filename: String): PrimMST = {
          val in: In = new In(filename)
          val edgeWeightedGraph: EdgeWeightedGraph = new EdgeWeightedGraph(in)
          new PrimMST(edgeWeightedGraph)
     }

     def toEWGraph(edges: List[Edge]) = {
          val princetonQueue: edu.princeton.cs.algs4.Queue[Edge] = new edu.princeton.cs.algs4.Queue[Edge]
          edges.foreach(e => princetonQueue.enqueue(e))
          new EdgeWeightedGraph(princetonQueue)
     }

     def toEWDGraph(edges: List[DirectedEdge]): EdgeWeightedDigraph = {
          val princetonQueue: edu.princeton.cs.algs4.Queue[DirectedEdge] = new edu.princeton.cs.algs4.Queue[DirectedEdge]
          edges.foreach(e => princetonQueue.enqueue(e))
          new EdgeWeightedDigraph(princetonQueue)
     }

     def roundTwo(num: Double): Double = {
          (num * 100).round / 100.0
     }

     def equal(d: DirectedEdge, e: Edge): Boolean = {
          if (d == null && e == null) true
          else if (d == null || e == null) false
          else {
               val dv: Int = d.from()
               val dw: Int = d.to()
               val ev: Int = e.either()
               val ew: Int = e.other(ev)
               (dv == ev && dw == ew) || (dv == ew && dw == ev)
          }
     }
}



class ShortestPathDijkstraTests extends Specification with DijkstraState {

     val source: Int = 0
     val mstEdges: List[Edge] = getStateMST(tinyMSTvsSPTMineEWD).edges().toList
     val directedShortestPathObj: DijkstraSP = getStateD(tinyEWD, source)
     val undirectedShortestPathObj: DijkstraUndirectedSP = getStateU(tinyEWDUndirectedDijkstra, source)
     val undirectedShortestPathObjForCompare: DijkstraUndirectedSP = getStateU(tinyMSTvsSPTMineEWD, source)
     val directedShortestPathTree: List[DirectedEdge] = List(
          new DirectedEdge(0, 4, 0.38),
          new DirectedEdge(4, 5, 0.35),
          new DirectedEdge(5, 1, 0.32),
          new DirectedEdge(0, 2, 0.26),
          new DirectedEdge(2, 7, 0.34),
          new DirectedEdge(7, 3, 0.39),
          new DirectedEdge(3, 6, 0.52)
     )
     val undirectedShortestPathTree: List[Edge] = List (
          new Edge(0, 4, 0.38),
          new Edge(4, 5, 0.35),
          new Edge(5, 1, 0.32),
          new Edge(0, 2, 0.26),
          new Edge(2, 7, 0.34),
          new Edge(7, 3, 0.39),
          new Edge(6, 0, 0.58)
     )


     "Compare graphs: both directed and undirected graphs can have cycles, before using dijkstra" should {
          "-> be true" in {
               val digraph: Digraph = new EdgeWeightedDigraph(new In(tinyEWD)).convertToDigraph()
               val graph: Graph = new EdgeWeightedGraph(new In(tinyEWDUndirectedDijkstra)).convertToGraph()

               val digraphIsCyclic: Boolean = new DirectedCycle(digraph).hasCycle
               val graphIsCyclic: Boolean = new Cycle(graph).hasCycle

               digraphIsCyclic shouldEqual true
               graphIsCyclic shouldEqual true
          }
     }


     "Directed graph: All shortest paths" should {

          "-> be contained in shortest path tree" in {
               //val shortestPathTree =
               val allPaths: List[DirectedEdge] = List(
                    directedShortestPathObj.pathTo(0).toList,
                    directedShortestPathObj.pathTo(1).toList,
                    directedShortestPathObj.pathTo(2).toList,
                    directedShortestPathObj.pathTo(3).toList,
                    directedShortestPathObj.pathTo(4).toList,
                    directedShortestPathObj.pathTo(5).toList,
                    directedShortestPathObj.pathTo(6).toList,
                    directedShortestPathObj.pathTo(7).toList
               ).flatten.distinct

               //shortestPathTree.equals(allPaths) shouldEqual true
               (0 to 6) foreach {i => directedShortestPathTree(i) equals allPaths(i) shouldEqual true }
               success
          }
     }

     "Undirected graph: All shortest paths" should {

          "-> be contained in shortest paths tree" in {
               val allPaths: List[Edge] = List(
                    undirectedShortestPathObj.pathTo(0).toList,
                    undirectedShortestPathObj.pathTo(1).toList,
                    undirectedShortestPathObj.pathTo(2).toList,
                    undirectedShortestPathObj.pathTo(3).toList,
                    undirectedShortestPathObj.pathTo(4).toList,
                    undirectedShortestPathObj.pathTo(5).toList,
                    undirectedShortestPathObj.pathTo(6).toList,
                    undirectedShortestPathObj.pathTo(7).toList
               ).flatten.distinct

               (0 to 6) foreach {i => undirectedShortestPathTree(i) equals allPaths(i) shouldEqual true }
               success
          }
     }

     "Directed graph: Path weight from 0->6" should {

          "-> equal distTo(6)" in {
               roundTwo(directedShortestPathObj.distTo(6)) shouldEqual 1.51
               roundTwo(directedShortestPathObj.pathTo(6).toList.map(_.weight()).sum) shouldEqual 1.51
          }
     }

     "Undirected graph: Path weight from 0->6" should {
          "-> equal distTo(6)" in {
               undirectedShortestPathObj.distTo(6) shouldEqual 0.58
               undirectedShortestPathObj.pathTo(6).toList.map(_.weight()).sum shouldEqual 0.58
          }
     }

     "Compare graphs: some paths" should {
          "-> be the same for directed and undirected dijkstra" in {
               val checkDirPathTo3: List[DirectedEdge] = List(new DirectedEdge(0, 2, 0.26), new DirectedEdge(2, 7, 0.34),
                    new DirectedEdge(7, 3, 0.39))
               val checkUndirPathTo3: List[Edge] = List(new Edge(0, 2, 0.26), new Edge(2, 7, 0.34), new Edge(7, 3, 0.39))
               val foundDirPathTo3: List[DirectedEdge] = directedShortestPathObj.pathTo(3).toList
               val foundUndirPathTo3: List[Edge] = undirectedShortestPathObj.pathTo(3).toList

               (0 until 3) foreach {i => foundDirPathTo3(i) equals checkDirPathTo3(i) shouldEqual true}
               success
               (0 until 3) foreach {i => foundUndirPathTo3(i) equals checkUndirPathTo3(i) shouldEqual true}
               success
          }

          "-> be different for directed and undirected dijkstra" in {
               val foundDirPathTo6: List[DirectedEdge] = directedShortestPathObj.pathTo(6).toList
               val checkDirPathTo6: List[DirectedEdge] = List(new DirectedEdge(0, 2, 0.26), new DirectedEdge(2, 7, 0.34),
                    new DirectedEdge(7, 3, 0.39), new DirectedEdge(3, 6, 0.52))
               val foundUndirPathTo6: List[Edge] = undirectedShortestPathObj.pathTo(6).toList
               val checkUndirPathTo6: List[Edge] = List(new Edge(6, 0, 0.58))

               foundUndirPathTo6.head equals checkUndirPathTo6.head shouldEqual true
               (0 until 4) foreach {i => foundDirPathTo6(i) equals checkDirPathTo6(i) shouldEqual true}
               success
          }
     }


     "Directed graph: Shortest path tree" should {

          "-> have no cycles" in {

               val shortestPathDigraph: Digraph = toEWDGraph(directedShortestPathTree).convertToDigraph()
               val shortestPathTreeHasCycle: Boolean = new DirectedCycle(shortestPathDigraph).hasCycle

               shortestPathTreeHasCycle shouldEqual false
          }
     }

     "Undirected graph: Shortest path tree" should {

          "-> have no cycles" in {
               val shortestPathGraph: Graph = toEWGraph(undirectedShortestPathTree).convertToGraph()
               val shortestPathTreeHasCycle: Boolean = new Cycle(shortestPathGraph).hasCycle

               shortestPathTreeHasCycle shouldEqual false
          }
     }

     "Undirected graph: Min spanning tree is not necessarily equal to shortest path tree" should {

          "-> be true for this example" in {

               undirectedShortestPathObjForCompare.pathTo(1).toList.head.equals(new Edge(0, 1, 5.00)) shouldEqual true
               undirectedShortestPathObjForCompare.pathTo(2).toList.head.equals(new Edge(0, 2, 9.00)) shouldEqual true

               mstEdges(0) equals new Edge(0, 1, 5.00) shouldEqual true
               mstEdges(1) equals new Edge(1, 2, 5.00) shouldEqual true
          }
     }
}
