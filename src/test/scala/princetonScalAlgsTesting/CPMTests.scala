package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataDigraph._
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


trait CPMState {

     val stateCPM = getState(jobsPC)
     val criticalPathGraph: EdgeWeightedDigraph = stateCPM._1
     val acyclicLP: AcyclicLP = stateCPM._2

     def getState(filename: String): (EdgeWeightedDigraph, AcyclicLP) ={
          val in: In = new In(filename)
          val N: Int = in.readLine.toInt // number of jobs
          val source: Int = 2 * N // source and sink
          val sink: Int = 2 * N + 1
          val graph: EdgeWeightedDigraph = CPM.buildCriticalPathNetwork(N, source, sink, in)
          val lp: AcyclicLP = new AcyclicLP(graph, source)
          (graph, lp)
     }

     def makePath(finishVertex: Int): String = {

          val foundPath: List[DirectedEdge] = acyclicLP.pathTo(finishVertex).toList
          var pathVertices: ArrayBuffer[Int] = ArrayBuffer()
          foundPath.foreach(e => pathVertices += e.from() += e.to())
          pathVertices = pathVertices.distinct
          pathVertices.mkString(" -> ")
     }
}


class CPMTests extends Specification with CPMState {

     "Start-finish times are taken from the critical path graph" should {

          "-> apply to example 1: job 4 starts at 70.0 and finishes at 108.0" in {
               val startVertex: Int = 4
               val finishVertex: Int = 14
               val path: String = makePath(finishVertex)

               acyclicLP.pathTo(startVertex).toList.map(_.weight()).sum shouldEqual 70.0
               acyclicLP.pathTo(finishVertex).toList.map(_.weight()).sum shouldEqual 108.0
               path shouldEqual "20 -> 0 -> 10 -> 9 -> 19 -> 4 -> 14"
          }

          "-> apply to example 2: job 2 starts at 123.0 and finishes at 173.0" in {
               val startVertex: Int = 2
               val finishVertex: Int = 12
               val path: String = makePath(finishVertex)

               acyclicLP.pathTo(startVertex).toList.map(_.weight()).sum shouldEqual 123.0
               acyclicLP.pathTo(finishVertex).toList.map(_.weight()).sum shouldEqual 173.0
               path shouldEqual "20 -> 0 -> 10 -> 9 -> 19 -> 6 -> 16 -> 8 -> 18 -> 2 -> 12"
          }
     }

     "Critical path graph has 4 types of edge connections" should {

          "-> apply to case 1: duration-weight edge between start and finish vertices" in {

               criticalPathGraph.getAdj(0).get(0).equals(new DirectedEdge(0, 10, 41)) shouldEqual true
               criticalPathGraph.getAdj(1).get(0).equals(new DirectedEdge(1, 11, 51)) shouldEqual true
               criticalPathGraph.getAdj(2).get(0).equals(new DirectedEdge(2, 12, 50)) shouldEqual true
               criticalPathGraph.getAdj(3).get(0).equals(new DirectedEdge(3, 13, 36)) shouldEqual true
               criticalPathGraph.getAdj(4).get(0).equals(new DirectedEdge(4, 14, 38)) shouldEqual true
               criticalPathGraph.getAdj(5).get(0).equals(new DirectedEdge(5, 15, 45)) shouldEqual true
               criticalPathGraph.getAdj(6).get(0).equals(new DirectedEdge(6, 16, 21)) shouldEqual true
               criticalPathGraph.getAdj(7).get(0).equals(new DirectedEdge(7, 17, 32)) shouldEqual true
               criticalPathGraph.getAdj(8).get(0).equals(new DirectedEdge(8, 18, 32)) shouldEqual true
               criticalPathGraph.getAdj(9).get(0).equals(new DirectedEdge(9, 19, 29)) shouldEqual true
          }

          "-> apply to case 2: zero-weighted edge between source and start vertices" in {

               criticalPathGraph.getAdj(20).get(9).equals(new DirectedEdge(20, 0, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(20).get(8).equals(new DirectedEdge(20, 1, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(20).get(7).equals(new DirectedEdge(20, 2, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(20).get(6).equals(new DirectedEdge(20, 3, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(20).get(5).equals(new DirectedEdge(20, 4, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(20).get(4).equals(new DirectedEdge(20, 5, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(20).get(3).equals(new DirectedEdge(20, 6, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(20).get(2).equals(new DirectedEdge(20, 7, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(20).get(1).equals(new DirectedEdge(20, 8, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(20).get(0).equals(new DirectedEdge(20, 9, 0.00)) shouldEqual true
          }

          "-> apply to case 3: zero-weight edge between finish and sink vertices" in {
               criticalPathGraph.getAdj(10).get(3).equals(new DirectedEdge(10, 21, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(11).get(1).equals(new DirectedEdge(11, 21, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(12).get(0).equals(new DirectedEdge(12, 21, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(13).get(0).equals(new DirectedEdge(13, 21, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(14).get(0).equals(new DirectedEdge(14, 21, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(15).get(0).equals(new DirectedEdge(15, 21, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(16).get(2).equals(new DirectedEdge(16, 21, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(17).get(2).equals(new DirectedEdge(17, 21, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(18).get(1).equals(new DirectedEdge(18, 21, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(19).get(2).equals(new DirectedEdge(19, 21, 0.00)) shouldEqual true
          }

          "-> apply to case 4: zero-weight edge between finish and successor vertices" in {
               criticalPathGraph.getAdj(10).get(2).equals(new DirectedEdge(10, 1, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(10).get(1).equals(new DirectedEdge(10, 7, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(10).get(0).equals(new DirectedEdge(10, 9, 0.00)) shouldEqual true

               criticalPathGraph.getAdj(11).get(0).equals(new DirectedEdge(11, 2, 0.00)) shouldEqual true

               criticalPathGraph.getAdj(16).get(1).equals(new DirectedEdge(16, 3, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(16).get(0).equals(new DirectedEdge(16, 8, 0.00)) shouldEqual true

               criticalPathGraph.getAdj(17).get(1).equals(new DirectedEdge(17, 3, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(17).get(0).equals(new DirectedEdge(17, 8, 0.00)) shouldEqual true

               criticalPathGraph.getAdj(18).get(0).equals(new DirectedEdge(18, 2, 0.00)) shouldEqual true

               criticalPathGraph.getAdj(19).get(1).equals(new DirectedEdge(19, 4, 0.00)) shouldEqual true
               criticalPathGraph.getAdj(19).get(0).equals(new DirectedEdge(19, 6, 0.00)) shouldEqual true
          }
     }
}
