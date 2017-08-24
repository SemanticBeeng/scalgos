package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._



trait DFSState {

     def getState(filename: String, source: Int): DepthFirstSearch = {
          val in: In = new In(filename)
          val graph: Graph = new Graph(in)
          new DepthFirstSearch(graph, source)
     }
}


class DepthFirstSearchTests extends Specification with DFSState {


     val dfsSourceTwo: DepthFirstSearch = getState(tinyG, 2)

     "Reachable from (2)" should {
          "-> be 0,1,3,5,4,6" in {
               dfsSourceTwo.reachable().toList shouldEqual List(0, 1, 2, 3, 4, 5, 6)
          }
     }

     "Not reachable from (2)" should {
          "-> be 7,8,9,10,11,12" in {
               dfsSourceTwo.notReachable().toList shouldEqual List(7,8,9,10,11,12)
          }
     }
}
