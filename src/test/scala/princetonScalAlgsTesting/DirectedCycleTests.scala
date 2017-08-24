package princetonScalAlgsTesting

import edu.princeton.cs.algs4.{Digraph, DirectedCycle, In}
import org.specs2.mutable.Specification
import DataDigraph._
import scala.collection.JavaConversions._


class DirectedCycleTests extends Specification {

     object State {

          def getState(filename: String): DirectedCycle = {
               val in: In = new In(filename)
               val digraph: Digraph = new Digraph(in)
               new DirectedCycle(digraph)
          }
     }

     import State._

     val cycleFinderNone: DirectedCycle = getState(tinyDAG)
     val cycleFinder: DirectedCycle = getState(tinyDG)


     "Only the first cycle found is returned, even if there are more after that" should {

          "-> cycle exists from 9 -> 10 -> 12 -> 9 but 4 -> 3 -> 5 -> 4 is found first" in {
               cycleFinder.cycle.toList shouldEqual List(3, 5, 4, 3)
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








     /*val firstFinder: DirectedCycle = getState(directedCycle_1)
     val thirdFinder: DirectedCycle = getState(directedCycle_3_20_23)
     val secondFinderFiveToZeroThenFiveToFour: DirectedCycle = getState(directedCycle_2_50_54)
     val secondFinderFiveToFourThenFiveToZero: DirectedCycle = getState(directedCycle_2_54_50)
     val thirdFinderTwoToThreeThenTwoToZero: DirectedCycle = getState(directedCycle_3_23_20)
     val fourthFinderFiveToZeroThenFiveToFour: DirectedCycle = getState(directedCycle_4_50_54)
     val fourthFinderFiveToFourThenFiveToZero: DirectedCycle = getState(directedCycle_4_54_50)*/

/*
     "In certain graphs, order in which stack keeps nodes does not affect which cycle is found" should {

          "-> graph 1: there is (5 -> 0) not (0 -> 5) so there is no cycle involving 0 2 4 5 and so " +
               "4 2 3 5 4 is returned instead" in {

               // todo help note link to see image
               firstFinder.cycle().toList shouldEqual List(4, 2, 3, 5, 4)
          }
     }

     "In certain graphs, order in which the stack keeps the nodes affects which cycle is found" should {

          "-> graph 2: placing 5 0 before 5 4 in file means stack holds 5: 4 0" in {
               secondFinderFiveToZeroThenFiveToFour.cycle().toList shouldEqual List(2, 3, 5, 4, 2)
          }
     }*/
}
