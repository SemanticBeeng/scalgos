package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._



trait SymbolGraphState {

     def getState(filename: String, delimiter: String): SymbolGraph = {
          new SymbolGraph(filename, delimiter)
     }
}


class SymbolGraphTests extends Specification with SymbolGraphState {

     val sgMovies: SymbolGraph = getState(movies, "/")
     val sgRoutes: SymbolGraph = getState(routes, " ")


     /* done-testing that given a movie, it has a particular actor, and given that actor, we get the original movie
       * example: Final Fantasy: The Spirits Within (2001) gives Demita, John, and when we say his name, we should
       * get Final Fantasy movie again.
       * testing that that for routes.txt, the index JFK yields 4 neighbors: ORD, ATL, MCO. (test 2 more examples and
       * same for movies.txt)
       * testing (first) that DFW has 3 neighbors (PHX, HOU, ORD)
       */

     "Given a movie, yield some actors. Giving one of those actors" should {
          "-> result in a list containing that original movie" in {

               sgMovies.nearestNeighbors("First Knight (1995)").toList contains "Zucker, Kate"
               sgMovies.nearestNeighbors("Zucker, Kate").toList contains "First Knight (1995)"
          }
     }

     "Movie: 101 Dalmations" should {
          "-> have 25 actors (graph neighbors)" in {
               sgMovies.nearestNeighbors("101 Dalmatians (1996)").toList.length shouldEqual 25
          }
     }

     "Actor: Benfield, John" should {
          "-> have 4 movies (graph neighbors)" in {
               sgMovies.nearestNeighbors("Benfield, John").toList.length shouldEqual 4
          }
     }

     "Airport: ORD" should {
          "-> have 6 direct graph neighbors" in {

               sgRoutes.nearestNeighbors("ORD").toList.length shouldEqual 6
               sgRoutes.nearestNeighbors("ORD").toList shouldEqual List("DEN", "HOU", "DFW", "PHX", "JFK", "ATL")
          }
     }


     "Given a route, yield neighboring routes. Giving one of those result routes" should {
          "-> result in a list containing the original route" in {

               sgRoutes.nearestNeighbors("JFK").toList contains "ORD"
               sgRoutes.nearestNeighbors("ORD").toList contains "JFK"
          }
     }
}
