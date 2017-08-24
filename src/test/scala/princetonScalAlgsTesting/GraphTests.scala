package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._




trait GraphState {

     def getState(filename: String): Graph = {
          val in: In = new In(filename)
          new Graph(in)
     }
}




class GraphTests extends Specification with GraphState {

     val graph: Graph = getState(tinyG)
     val graphConnected: Graph = getState(tinyCG)


     "If there is v-w then w-v" should {

          "-> be true" in {
               graph.adj(4).toList shouldEqual List(5, 6, 3)

               graph.adj(5).toList contains 4
               graph.adj(6).toList contains 4
               graph.adj(3).toList contains 4
          }
     }
}
