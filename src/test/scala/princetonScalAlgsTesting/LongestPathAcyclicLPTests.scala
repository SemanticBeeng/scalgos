package princetonScalAlgsTesting


import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataDigraph._
import scala.collection.JavaConversions._



trait AcyclicLPState {
     def getStateL(filename: String, source: Int): AcyclicLP = {
          val in: In = new In(filename)
          val edgeWeightedDigraph: EdgeWeightedDigraph = new EdgeWeightedDigraph(in)
          new AcyclicLP(edgeWeightedDigraph, source)
     }

     def getStateS(filename: String, source: Int): AcyclicSP = {
          val in: In = new In(filename)
          val edgeWeightedDigraph: EdgeWeightedDigraph = new EdgeWeightedDigraph(in)
          new AcyclicSP(edgeWeightedDigraph, source)
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
}



class LongestPathAcyclicLPTests extends Specification with AcyclicLPState {

     val source: Int = 5
     val directedShortestPathObj: AcyclicSP = getStateS(tinyEWDAG, source)
     val directedLongestPathObj: AcyclicLP = getStateL(tinyEWDAG, source)
     val directedLongestPathTree: List[DirectedEdge] = List(
          new DirectedEdge(5, 1, 0.32),
          new DirectedEdge(1, 3, 0.29),
          new DirectedEdge(3, 6, 0.52),
          new DirectedEdge(6, 4, 0.93),
          new DirectedEdge(4, 0, 0.38),
          new DirectedEdge(4, 7, 0.37),
          new DirectedEdge(7, 2, 0.34)
     )

     "Prerequisite: the graph for this algorithm" should {
          "-> not have cycles" in {
               val digraph: Digraph = new EdgeWeightedDigraph(new In(tinyEWDAG)).convertToDigraph()
               val digraphIsCyclic: Boolean = new DirectedCycle(digraph).hasCycle

               digraphIsCyclic shouldEqual false
          }
     }


     "All longest paths" should {

          "-> be contained in longest path tree" in {
               val allPaths: List[DirectedEdge] = List(
                    directedLongestPathObj.pathTo(0).toList,
                    directedLongestPathObj.pathTo(1).toList,
                    directedLongestPathObj.pathTo(2).toList,
                    directedLongestPathObj.pathTo(3).toList,
                    directedLongestPathObj.pathTo(4).toList,
                    directedLongestPathObj.pathTo(5).toList,
                    directedLongestPathObj.pathTo(6).toList,
                    directedLongestPathObj.pathTo(7).toList
               ).flatten.distinct

               //shortestPathTree.equals(allPaths) shouldEqual true
               (0 to 6) foreach {i => directedLongestPathTree(i) equals allPaths(i) shouldEqual true }
               success
          }
     }

     "Path weight from 5->7" should {

          "-> equal distTo(7)" in {
               roundTwo(directedLongestPathObj.distTo(7)) shouldEqual 2.43
               roundTwo(directedLongestPathObj.pathTo(7).toList.map(_.weight()).sum) shouldEqual 2.43
          }
     }

     "Path from 5->7" should {
          "-> equal pathTo(7)" in {
               val checkPath: List[DirectedEdge] = List(new DirectedEdge(5,1,0.32), new DirectedEdge(1,3,0.29),
                    new DirectedEdge(3, 6, 0.52), new DirectedEdge(6, 4, 0.93), new DirectedEdge(4, 7, 0.37))
               val foundPath: List[DirectedEdge] = directedLongestPathObj.pathTo(7).toList

               (0 to 4) foreach {i => foundPath(i) equals checkPath(i) shouldEqual true }
               success
          }
     }

     "Path from 5->7" should {
          "-> be shorter for shortest path tree" in {
               directedShortestPathObj.pathTo(7).toList.length shouldEqual 1
          }

          "-> be longer for longest path tree" in {
               directedLongestPathObj.pathTo(7).toList.length shouldEqual 5
          }
     }


     "Longest path tree" should {

          "-> have no cycles" in {

               val longestPathDigraph: Digraph = toEWDGraph(directedLongestPathTree).convertToDigraph()
               val longestPathTreeHasCycle: Boolean = new DirectedCycle(longestPathDigraph).hasCycle

               longestPathTreeHasCycle shouldEqual false
          }
     }

}
