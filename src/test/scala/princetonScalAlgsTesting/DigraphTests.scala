package princetonScalAlgsTesting

import edu.princeton.cs.algs4.{Digraph, In}
import org.specs2.mutable.Specification
import DataDigraph._
import scala.collection.JavaConversions._




class DigraphTests extends Specification {

     object State {

          def getState(filename: String): Digraph ={
               val in: In = new In(filename)
               new Digraph(in)
          }
     }

     import State._

     val digraph: Digraph =  getState(tinyDG)
     val reversed: Digraph = digraph.reverse()


     "Digraph construction" should {

          "-> (6,9), (6,4), (6,8) in file should equal 6 : 9 4 8 0 in digraph" in {

               digraph.adj(6).size mustEqual 4
               digraph.adj(6).toList mustEqual List(9, 4, 8, 0)
          }

          "-> if there are no descendants for a node, list underneath it should be empty" in {

               digraph.adj(1).size mustEqual 0
               digraph.adj(1).toList mustEqual List()
          }
     }

     "Reversing digraph" should {


          "-> the tail node is now the head and its tail is made of its previous heads" in {

               digraph.adj(5).exists(_ == 4) mustEqual true
               digraph.adj(6).exists(_ == 4) mustEqual true
               digraph.adj(11).exists(_ == 4) mustEqual true

               reversed.adj(4).toList mustEqual List(11, 6, 5)
          }
     }
}
