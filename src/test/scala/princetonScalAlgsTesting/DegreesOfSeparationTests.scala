package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._


trait DepSepState {

     def getState(filename: String, delimiter: String, source: String) ={
          val sg: SymbolGraph = new SymbolGraph(filename, delimiter)
          val graph: Graph = sg.G
          val s: Int = sg.index(source)
          val bfs: BreadthFirstPaths = new BreadthFirstPaths(graph, s)
          (sg, bfs)
     }
}



class DegreesOfSeparationTests extends Specification with DepSepState {

     val moviesDelim: String = "/"
     val moviesSource: String = "Bacon, Kevin"
     val moviesObj = getState(movies, moviesDelim, moviesSource)
     val moviesSG: SymbolGraph = moviesObj._1
     val moviesBFS: BreadthFirstPaths = moviesObj._2

     val routesDelim: String = " "
     val routesSource: String = "MCO"
     val routesObj = getState(routes, routesDelim, routesSource)
     val routesSG: SymbolGraph = routesObj._1
     val routesBFS: BreadthFirstPaths = routesObj._2



     "Shortest path" should {

          "-> from 'Bacon, Kevin' to 'Kidman, Nicole' should be as expected" in {

               val destinationNicoleKidman: String = "Kidman, Nicole"
               val path: List[String] = DegreesOfSeparation.getShortestPath(moviesSG, moviesBFS,
                    destinationNicoleKidman).toList

               path shouldEqual List("Bacon, Kevin", "Woodsman, The (2004)", "Grier, David Alan", "Bewitched (2005)",
                    "Kidman, Nicole")
          }

          "-> from 'Bacon, Kevin' to 'Eagle Has Landed' movie should be as expected" in {
               val destinationEagleLanded: String = "Eagle Has Landed, The (1976)"
               val path: List[String] = DegreesOfSeparation.getShortestPath(moviesSG, moviesBFS,
                    destinationEagleLanded).toList

               path shouldEqual List("Bacon, Kevin", "JFK (1991)", "Sutherland, Donald (I)", "Eagle Has Landed, The (1976)")
          }
     }

     "Shortest path" should {

          "-> from 'MCO' to 'LAX' should be as expected" in {

               val destinationLAX: String = "LAX"
               val path: List[String] = DegreesOfSeparation.getShortestPath(routesSG, routesBFS, destinationLAX).toList

               path shouldEqual List("MCO", "HOU", "DFW", "PHX", "LAX")
          }

          "-> from 'MCO' to 'PHX' should be as expected" in {
               val destinationPHX: String = "PHX"
               val path: List[String] = DegreesOfSeparation.getShortestPath(routesSG, routesBFS, destinationPHX).toList

               path shouldEqual List("MCO", "HOU", "DFW", "PHX")
          }
     }

}
