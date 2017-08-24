package princetonScalAlgsTesting


import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataDigraph._
import scala.collection.JavaConversions._



trait AcyclicState {
     def getState(filename: String, source: Int): AcyclicSP = {
          val in: In = new In(filename)
          val edgeWeightedDigraph: EdgeWeightedDigraph = new EdgeWeightedDigraph(in)
          new AcyclicSP(edgeWeightedDigraph, source)
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



class ShortestPathAcyclicSPTests extends Specification with AcyclicState {

     val source: Int = 5
     val mstEdges: List[Edge] = getStateMST(tinyEWDAG).edges().toList
     val directedShortestPathObj: AcyclicSP = getState(tinyEWDAG, source)
     val directedShortestPathTree: List[DirectedEdge] = List(
          new DirectedEdge(5, 4, 0.35),
          new DirectedEdge(4, 0, 0.38),
          new DirectedEdge(5, 1, 0.32),
          new DirectedEdge(5, 7, 0.28),
          new DirectedEdge(7, 2, 0.34),
          new DirectedEdge(1, 3, 0.29),
          new DirectedEdge(3, 6, 0.52)
     )
     // note same contents as above just different order to make comparing easier
     val directedShortestPathTreeMstOrd: List[DirectedEdge] = List(
          new DirectedEdge(5, 1, 0.32),
          new DirectedEdge(4, 0, 0.38),
          new DirectedEdge(1, 3, 0.29),
          new DirectedEdge(5, 4, 0.35),
          new DirectedEdge(5, 7, 0.28),
          new DirectedEdge(3, 6, 0.52),
          new DirectedEdge(7, 2, 0.34)
     )


     "Prerequisite: the graph for this algorithm" should {
          "-> not have cycles" in {
               val digraph: Digraph = new EdgeWeightedDigraph(new In(tinyEWDAG)).convertToDigraph()
               val digraphIsCyclic: Boolean = new DirectedCycle(digraph).hasCycle

               digraphIsCyclic shouldEqual false
          }
     }


     "All shortest paths" should {

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

     "Path weight from 5->6" should {

          "-> equal distTo(6)" in {
               roundTwo(directedShortestPathObj.distTo(6)) shouldEqual 1.13
               roundTwo(directedShortestPathObj.pathTo(6).toList.map(_.weight()).sum) shouldEqual 1.13
          }
     }

     "Path from 5->6" should {
          "-> equal pathTo(6)" in {
               val checkPath: List[DirectedEdge] = List(new DirectedEdge(5,1,0.32), new DirectedEdge(1,3,0.29),
                    new DirectedEdge(3, 6, 0.52))
               val foundPath: List[DirectedEdge] = directedShortestPathObj.pathTo(6).toList

               (0 to 2) foreach {i => foundPath(i) equals checkPath(i) shouldEqual true }
               success
          }
     }


     "Shortest path tree" should {

          "-> have no cycles" in {

               val shortestPathDigraph: Digraph = toEWDGraph(directedShortestPathTree).convertToDigraph()
               val shortestPathTreeHasCycle: Boolean = new DirectedCycle(shortestPathDigraph).hasCycle

               shortestPathTreeHasCycle shouldEqual false
          }
     }


     "Min spanning tree is not necessarily equal to shortest path tree" should {

          "-> be true for this example" in {
               equal(directedShortestPathTreeMstOrd(0), mstEdges(0)) shouldEqual true
               equal(directedShortestPathTreeMstOrd(2), mstEdges(2)) shouldEqual true
               equal(directedShortestPathTreeMstOrd(3), mstEdges(3)) shouldEqual true
               equal(directedShortestPathTreeMstOrd(4), mstEdges(4)) shouldEqual true
               equal(directedShortestPathTreeMstOrd(6), mstEdges(6)) shouldEqual true

               equal(directedShortestPathTreeMstOrd(1), mstEdges(1)) shouldEqual false
               directedShortestPathTreeMstOrd(1) equals new DirectedEdge(0, 4, 0.38) shouldEqual true
               mstEdges(1) equals new Edge(0, 2, 0.26) shouldEqual true

               equal(directedShortestPathTreeMstOrd(5), mstEdges(5)) shouldEqual false
               directedShortestPathTreeMstOrd(5) equals new DirectedEdge(3, 6, 0.52) shouldEqual true
               mstEdges(5) equals new Edge(6, 2, 0.40) shouldEqual true
          }
     }
}
