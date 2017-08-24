package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._


trait CycleState {
     def getState(filename: String): Cycle = {
          val in: In = new In(filename)
          val graph: Graph = new Graph(in)
          new Cycle(graph)
     }
}


class CycleTests extends Specification with CycleState {

     val cycleFinderNone: Cycle = getState(cycleNoneMine)
     val cycleFinder: Cycle = getState(tinyG)

     "Only the first cycle found is returned, even if there are more after that" should {

          "-> cycle exists from 9 -> 11 -> 12 -> 9 but 3 -> 4 -> 5 -> 3 is found first" in {
               cycleFinder.cycle.toList shouldEqual List(3, 4, 5, 3)
          }
     }

     "Confirm when cycle is present or not" should {

          "-> cycle should be found" in {
               cycleFinder.hasCycle shouldEqual true
          }
          "-> cycle should not be found" in {
               cycleFinderNone.hasCycle shouldEqual false
          }
     }
}