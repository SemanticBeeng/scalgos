package princetonScalAlgsTesting

import edu.princeton.cs.algs4.{Bag, Digraph, DirectedDFS, In}
import org.specs2.mutable.Specification //{In, Digraph, Bag, DirectedDFS}
import DataDigraph._
import scala.collection.JavaConversions._

class DirectedDFSTests extends Specification {

     object State {

          def getState(filename: String, oneSource: Integer): DirectedDFS ={
               val in: In = new In(filename)
               val digraph: Digraph = new Digraph(in)
               val sources : Bag[Integer] = new Bag[Integer]
               sources.add(oneSource)
               new DirectedDFS(digraph, sources)
          }
     }

     import State._

     "Marks nodes that can be reached from a list of sources" should {

          "-> only (0) (1) (2) (3) (4) (5) are reachable from (2)" in {

               val directedDFS: DirectedDFS = getState(tinyDG, 2)


               //note the first 6 are true (meaning vertices 0,1,2,3,4,5 are reachable from 2)
               directedDFS.getMarked.take(6).forall(_ == true) mustEqual true

               // note another way to say it
               directedDFS.marked(0) mustEqual true
               directedDFS.marked(1) mustEqual true
               directedDFS.marked(2) mustEqual true
               directedDFS.marked(3) mustEqual true
               directedDFS.marked(4) mustEqual true
               directedDFS.marked(5) mustEqual true

               // note vertices that are not reachable from 2 are the rest: 6,7,8,9,10,11,12
               directedDFS.getMarked.drop(6).forall(_ == false) mustEqual true
          }

          "-> only (1) is reachable from (1) since it has no further descendants" in {

               val directedDFS: DirectedDFS = getState(tinyDG, 1)

               directedDFS.marked(0) mustEqual false
               directedDFS.marked(1) mustEqual true
               directedDFS.getMarked.drop(2).forall(_ == false) mustEqual true
          }
     }
}