package princetonScalAlgsTesting


import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._



trait CCState {

     def getState(filename: String): CC = {
          val in: In = new In(filename)
          val graph: Graph = new Graph(in)
          new CC(graph)
     }
}



class CCTests extends  Specification with CCState {

     val cg = getState(tinyG)
     val cc = getState(tinyCG)

     "Connected components algorithm finds which vertices are connected" should {
          "-> be 0 to 6, 7,8 and 9 to 12 for a graph with several components" in {

               val componentsListOfQueues = cg.getComponents
               val queueToList: Queue[Integer] => List[Integer] = { q => q.toList }
               val components = componentsListOfQueues.toList.map(q => queueToList(q))

               components(0) shouldEqual List(0,1,2,3,4,5,6)
               components(1) shouldEqual List(7,8)
               components(2) shouldEqual List(9,10,11,12)
          }

          "-> be 0,1,2,3,4,5 for the completely connected graph" in {
               val componentsListOfQueues = cc.getComponents
               val queueToList: Queue[Integer] => List[Integer] = { q => q.toList }
               val components = componentsListOfQueues.toList.map(q => queueToList(q))

               components(0) shouldEqual List(0,1,2,3,4,5)
          }
     }

     "Connected components algorithm finds how many components there are" should {
          "-> be 3 for a graph with several components" in {
               cg.count() shouldEqual 3
          }

          "-> be 1 for a completely connected graph" in {
               cc.count() shouldEqual 1
          }
     }

     "There is a list of ids that shows the id number of the components" should {

          "-> apply to graph with several components" in {

               cg.id(0) shouldEqual 0
               cg.id(1) shouldEqual 0
               cg.id(2) shouldEqual 0
               cg.id(3) shouldEqual 0
               cg.id(4) shouldEqual 0
               cg.id(5) shouldEqual 0
               cg.id(6) shouldEqual 0
               cg.id(7) shouldEqual 1
               cg.id(8) shouldEqual 1
               cg.id(9) shouldEqual 2
               cg.id(10) shouldEqual 2
               cg.id(11) shouldEqual 2
               cg.id(12) shouldEqual 2

          }

          "-> apply to completely connected graph" in {

               cc.id(0) shouldEqual 0
               cc.id(1) shouldEqual 0
               cc.id(2) shouldEqual 0
               cc.id(3) shouldEqual 0
               cc.id(4) shouldEqual 0
               cc.id(5) shouldEqual 0
          }
     }

     "There is a list of sizes that holds component size for a component id (index)" should {

          "-> apply to graph with several components" in {
               cg.size(0) shouldEqual 7 //because there are 7 vertices in component of 0.
               cg.size(1) shouldEqual 7 //because there are 7 vertices in component of 1.
               cg.size(2) shouldEqual 7 //because there are 7 vertices in component of 2...
               cg.size(3) shouldEqual 7
               cg.size(4) shouldEqual 7
               cg.size(5) shouldEqual 7
               cg.size(6) shouldEqual 7
               cg.size(7) shouldEqual 2
               cg.size(8) shouldEqual 2
               cg.size(9) shouldEqual 4
               cg.size(10) shouldEqual 4
               cg.size(11) shouldEqual 4
               cg.size(12) shouldEqual 4
          }

          "-> apply to completely connected graph" in {
               cc.size(0) shouldEqual 6 // 0,1,2,3,4,5
               cc.size(1) shouldEqual 6
               cc.size(2) shouldEqual 6
               cc.size(3) shouldEqual 6
               cc.size(4) shouldEqual 6
               cc.size(5) shouldEqual 6
          }
     }
}
