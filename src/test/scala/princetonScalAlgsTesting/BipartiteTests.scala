package princetonScalAlgsTesting


import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._





/// note http://visualgo.net/dfsbfs for visualizing graphs (handy for moving graph around to see how program
// traverses) and for translation from adj to graph without tangles.

trait BipartiteState {
     def getState(filename: String): Bipartite  = {
          val in: In = new In(filename)
          val graph: Graph = new Graph(in)
          new Bipartite(graph)
     }
}


class BipartiteTests extends Specification with BipartiteState {

     val bipartite: Bipartite = getState(bipartiteYesMine)
     val notBipartite: Bipartite = getState(bipartiteNotMine)
     val BLUE: Boolean = true
     val ORANGE: Boolean = false

     "A bipartite graph" should {

          "-> say it is bipartite" in {
               bipartite.isBipartite shouldEqual true
          }

          "-> always have alternating colors on vertices at all times. Or in other words, from every vertex A" +
               "the direct neighbors should have opposite colors" in {

               bipartite.color(4) shouldEqual BLUE
               bipartite.color(0) shouldEqual ORANGE
               bipartite.color(5) shouldEqual BLUE
               bipartite.color(6) shouldEqual ORANGE
               bipartite.color(7) shouldEqual BLUE
               bipartite.color(8) shouldEqual ORANGE
               bipartite.color(1) shouldEqual BLUE
               bipartite.color(3) shouldEqual ORANGE
               bipartite.color(2) shouldEqual BLUE
          }

          "-> not have an odd-length cycle" in {
               bipartite.oddCycle() shouldEqual null
          }
     }

     "A non-bipartite graph" should {

          "-> say it is not bipartite" in {
               notBipartite.isBipartite shouldEqual false
          }

          "-> otherwise have an odd-length cycle" in {
               notBipartite.oddCycle().toList shouldEqual List(7, 1, 8, 5, 6, 7)
          }

          "-> have color[] array that has at least one pair of consecutive vertices that are " +
               "the same color, meaning graph is not bipartite" in {

               // note (6)  and (7) are consecutive or right next to each other and since they
               // have same color, graph is not bipartite
               notBipartite.color(0) shouldEqual ORANGE
               notBipartite.color(7) shouldEqual BLUE
               notBipartite.color(6) shouldEqual BLUE
          }
     }

}
