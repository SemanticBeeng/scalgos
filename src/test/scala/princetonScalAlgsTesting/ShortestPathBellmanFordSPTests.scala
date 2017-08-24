package princetonScalAlgsTesting


import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataDigraph._
import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer


trait BellmanFordState {
     def getState(filename: String, source: Int): BellmanFordSP = {
          val in: In = new In(filename)
          val edgeWeightedDigraph: EdgeWeightedDigraph = new EdgeWeightedDigraph(in)
          new BellmanFordSP(edgeWeightedDigraph, source)
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

     def toEdges(ewg: EdgeWeightedDigraph): List[DirectedEdge] = {
          val edges: ArrayBuffer[DirectedEdge] = ArrayBuffer()
          val vertices: ArrayBuffer[Integer] = getVertices(ewg)
          for (v <- vertices){
               ewg.adj(v).toArray.foreach(e => edges += e)
          }
          edges.toList.distinct
     }

     def getVertices(ewg: EdgeWeightedDigraph): ArrayBuffer[Integer] = {
          val vertices: ArrayBuffer[Integer] = ArrayBuffer()
          for (v <- 0 until ewg.V()){
               ewg.adj(v).toArray.foreach(e => vertices += e.from() += e.to())
          }
          vertices.distinct
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



class ShortestPathBellmanFordSPTests extends Specification with BellmanFordState {

     val source: Int = 0
     val mstEdges: List[Edge] = getStateMST(tinyEWD).edges().toList
     //help: does primMST work for graph with negative edges?
     val mstNegEdges: List[Edge] = getStateMST(tinyEWDn).edges().toList
     val dspObj: BellmanFordSP = getState(tinyEWD, source)
     val dspNegEdgeObj: BellmanFordSP = getState(tinyEWDn, source)
     val dspNegCycleObj: BellmanFordSP = getState(tinyEWDnc, source)
     val dspTree: List[DirectedEdge] = List(
          new DirectedEdge(0, 4, 0.38),
          new DirectedEdge(4, 5, 0.35),
          new DirectedEdge(5, 1, 0.32),
          new DirectedEdge(0, 2, 0.26),
          new DirectedEdge(2, 7, 0.34),
          new DirectedEdge(7, 3, 0.39),
          new DirectedEdge(3, 6, 0.52)
     )
     val dspNegEdgeTree: List[DirectedEdge] = List(
          new DirectedEdge(0, 2, 0.26),
          new DirectedEdge(2, 7, 0.34),
          new DirectedEdge(7, 3, 0.39),
          new DirectedEdge(3, 6, 0.52),
          new DirectedEdge(6, 4, -1.25),
          new DirectedEdge(4, 5, 0.35),
          new DirectedEdge(5, 1, 0.32)
     )
     // note same contents as dirShortPathTree just different order to make comparing easier
     val dspTreeUsingMstOrder: List[DirectedEdge] = List(
          new DirectedEdge(5, 1, 0.32),
          new DirectedEdge(4, 0, 0.38),
          new DirectedEdge(1, 3, 0.29),
          new DirectedEdge(5, 4, 0.35),
          new DirectedEdge(5, 7, 0.28),
          new DirectedEdge(3, 6, 0.52),
          new DirectedEdge(7, 2, 0.34)
     )
     // note same contents as dirShortPathNegEdgeTree just different order to make comparing easier
     val dspNegEdgeTreeUsingMstOrder: List[DirectedEdge] = List(
          new DirectedEdge(5, 1, 0.32),
          new DirectedEdge(3, 6, 0.52), //6-2
          new DirectedEdge(4, 5, 0.35), //1-3
          new DirectedEdge(6, 4, -1.25),
          new DirectedEdge(0, 2, 0.26), //7-5
          new DirectedEdge(7, 3, 0.39), //6-0
          new DirectedEdge(2, 7, 0.34)
     )


     "Prerequisite: the graph for this algorithm" should {
          "-> be allowed to have negative edges" in {
               val ewg: EdgeWeightedDigraph = new EdgeWeightedDigraph(new In(tinyEWDn)) //.convertToDigraph()
               val hasNegEdges: Boolean = toEdges(ewg).exists(e => e.weight() < 0)

               hasNegEdges shouldEqual true
          }

          "-> might contain negative cycles" in {
               val ewg: EdgeWeightedDigraph = new EdgeWeightedDigraph(new In(tinyEWDnc))
               val hasNegCycles: Boolean = new BellmanFordSP(ewg, source).hasNegativeCycle

               hasNegCycles shouldEqual true
          }
     }


     "All shortest paths to particular target vertices" should {

          "-> be contained in shortest path tree from a positive-weighted graph" in {

               val allPaths: List[DirectedEdge] = List(
                    dspObj.pathTo(0).toList,
                    dspObj.pathTo(1).toList,
                    dspObj.pathTo(2).toList,
                    dspObj.pathTo(3).toList,
                    dspObj.pathTo(4).toList,
                    dspObj.pathTo(5).toList,
                    dspObj.pathTo(6).toList,
                    dspObj.pathTo(7).toList
               ).flatten.distinct

               (0 to 6) foreach {i => dspTree(i) equals allPaths(i) shouldEqual true }
               success
          }

          "-> be contained in shortest path tree from a negative-weighted graph" in {

               val allPathsNeg: List[DirectedEdge] = List(
                    dspNegEdgeObj.pathTo(0).toList,
                    dspNegEdgeObj.pathTo(1).toList,
                    dspNegEdgeObj.pathTo(2).toList,
                    dspNegEdgeObj.pathTo(3).toList,
                    dspNegEdgeObj.pathTo(4).toList,
                    dspNegEdgeObj.pathTo(5).toList,
                    dspNegEdgeObj.pathTo(6).toList,
                    dspNegEdgeObj.pathTo(7).toList
               ).flatten.distinct

               (0 to 6) foreach {i => dspNegEdgeTree(i) equals allPathsNeg(i) shouldEqual true }
               success
          }
     }

     "Shortest path" should {
          "-> never have negative cycles, when made from positive-weighted graph" in {
               dspObj.hasNegativeCycle shouldEqual false
          }

          "-> never have negative cycles, even when made from negative-weighted graph" in {
               dspNegEdgeObj.hasNegativeCycle shouldEqual false
          }
     }

     "Negative cycle" should {
          "-> be found if graph has negative cycle" in {
               val checkNegCycle: List[DirectedEdge] = List(new DirectedEdge(4,5,0.35), new DirectedEdge(5,4,-0.66))
               val foundNegCycle: List[DirectedEdge] = dspNegCycleObj.negativeCycle().toList

               dspNegCycleObj.hasNegativeCycle shouldEqual true
               (0 to 1) foreach {i => foundNegCycle(i) equals checkNegCycle(i) shouldEqual true }
               success
          }

          "-> have weight equal to -0.31" in {
               roundTwo(dspNegCycleObj.negativeCycle().toList.map(_.weight()).sum) shouldEqual -0.31
          }
     }

     "Path weight from 0->1" should {

          "-> equal distTo(1) for a positive-weighted graph" in {
               roundTwo(dspObj.distTo(1)) shouldEqual 1.05
               roundTwo(dspObj.pathTo(1).toList.map(_.weight()).sum) shouldEqual 1.05
          }

          "-> equal distTo(1) for a negative-weighted graph" in {
               roundTwo(dspNegEdgeObj.distTo(1)) shouldEqual 0.93
               roundTwo(dspNegEdgeObj.pathTo(1).toList.map(_.weight()).sum) shouldEqual 0.93
          }

          "-> be possibly smaller for negative-weighted graph than for positive-weighted graph" +
               "because the algorithm seeks to build the shortest path of a negative-weighted" +
               "graph using the negative edges in particular." in {
               dspNegEdgeObj.distTo(1) < dspObj.distTo(1) shouldEqual true
          }
     }

     "Path from 0->1" should {
          "-> equal pathTo(1) for a positive-weighted graph" in {
               val checkPath: List[DirectedEdge] = List(new DirectedEdge(0,4,0.38), new DirectedEdge(4,5,0.35),
                    new DirectedEdge(5,1,0.32))
               val foundPath: List[DirectedEdge] = dspObj.pathTo(1).toList

               (0 to 2) foreach {i => foundPath(i) equals checkPath(i) shouldEqual true }
               success
          }


          "-> equal pathTo(1) for a negative-weighted graph" in {
               val checkPath: List[DirectedEdge] = List(new DirectedEdge(0,2,0.26), new DirectedEdge(2,7,0.34),
                    new DirectedEdge(7,3,0.39), new DirectedEdge(3, 6, 0.52), new DirectedEdge(6,4,-1.25),
                    new DirectedEdge(4,5,0.35), new DirectedEdge(5,1,0.32))
               val foundPath: List[DirectedEdge] = dspNegEdgeObj.pathTo(1).toList

               (0 to 2) foreach {i => foundPath(i) equals checkPath(i) shouldEqual true }
               success
          }

          "-> possibly contain more edges for negative-weighted graph than for positive-weighted graph" in {
               dspNegEdgeObj.pathTo(1).size > dspObj.pathTo(1).size shouldEqual true
          }
     }


     "Shortest path tree" should {

          "-> have no cycles when made from a positive-weighted graph" in {

               val shortPathDigraph: Digraph = toEWDGraph(dspTree).convertToDigraph()
               val shortPathTreeHasCycle: Boolean = new DirectedCycle(shortPathDigraph).hasCycle

               shortPathTreeHasCycle shouldEqual false
          }

          "-> have no cycles when made from a negative-weighted graph" in {

               val shortPathDigraph: Digraph = toEWDGraph(dspNegEdgeTree).convertToDigraph()
               val shortPathTreeWithNegEdgesHasCycle: Boolean = new DirectedCycle(shortPathDigraph).hasCycle

               shortPathTreeWithNegEdgesHasCycle shouldEqual false
          }
     }


     "Min spanning tree is not necessarily equal to shortest path tree" should {

          "-> be true for shortest path from positive-weighted graph" in {
               equal(dspTreeUsingMstOrder(0), mstEdges(0)) shouldEqual true
               equal(dspTreeUsingMstOrder(2), mstEdges(2)) shouldEqual true
               equal(dspTreeUsingMstOrder(3), mstEdges(3)) shouldEqual true
               equal(dspTreeUsingMstOrder(4), mstEdges(4)) shouldEqual true
               equal(dspTreeUsingMstOrder(6), mstEdges(6)) shouldEqual true

               equal(dspTreeUsingMstOrder(1), mstEdges(1)) shouldEqual false
               dspTreeUsingMstOrder(1) equals new DirectedEdge(0, 4, 0.38) shouldEqual true
               mstEdges(1) equals new Edge(0, 2, 0.26) shouldEqual true

               equal(dspTreeUsingMstOrder(5), mstEdges(5)) shouldEqual false
               dspTreeUsingMstOrder(5) equals new DirectedEdge(3, 6, 0.52) shouldEqual true
               mstEdges(5) equals new Edge(6, 2, 0.40) shouldEqual true
          }

          "-> be true for shortest path from negative-weighted graph" in {
               equal(dspNegEdgeTreeUsingMstOrder(0), mstNegEdges(0)) shouldEqual true
               equal(dspNegEdgeTreeUsingMstOrder(3), mstNegEdges(3)) shouldEqual true
               equal(dspNegEdgeTreeUsingMstOrder(6), mstNegEdges(6)) shouldEqual true

               equal(dspNegEdgeTreeUsingMstOrder(1), mstNegEdges(1)) shouldEqual false
               dspNegEdgeTreeUsingMstOrder(1) equals new DirectedEdge(3, 6, 0.52) shouldEqual true
               mstNegEdges(1) equals new Edge(6, 2, -1.20) shouldEqual true

               equal(dspNegEdgeTreeUsingMstOrder(2), mstNegEdges(2)) shouldEqual false
               dspNegEdgeTreeUsingMstOrder(2) equals new DirectedEdge(4,5, 0.35) shouldEqual true
               mstNegEdges(2) equals new Edge(1,3,0.29) shouldEqual true

               equal(dspNegEdgeTreeUsingMstOrder(4), mstNegEdges(4)) shouldEqual false
               dspNegEdgeTreeUsingMstOrder(4) equals new DirectedEdge(0, 2, 0.26) shouldEqual true
               mstNegEdges(4) equals new Edge(7,5,0.28) shouldEqual true

               equal(dspNegEdgeTreeUsingMstOrder(5), mstNegEdges(5)) shouldEqual false
               dspNegEdgeTreeUsingMstOrder(5) equals new DirectedEdge(7,3,0.39) shouldEqual true
               mstNegEdges(5) equals new Edge(6, 0, -1.40) shouldEqual true
          }
     }
}
