package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._


trait DBState {
     def getState(filename: String, source: Int) = {
          val in: In = new In(filename)
          val graph: Graph = new Graph(in)
          val dfp: DepthFirstPaths = new DepthFirstPaths(graph, source)
          val bfp: BreadthFirstPaths = new BreadthFirstPaths(graph, source)
          (bfp, dfp)
     }

     // note utility functions
     def makeBreadthPathTo(destination: Integer): String = {
          bfp.pathTo(destination).toList.map(_.toString).mkString(" -> ")
     }

     def makeDepthPathTo(destination: Integer): String = {
          dfp.pathTo(destination).toList.map(_.toString).mkString(" -> ")
     }

     // note specifying source = 2
     val bfp: BreadthFirstPaths = getState(tinyG, 2)._1
     val dfp: DepthFirstPaths = getState(tinyG, 2)._2
}


class DBFirstPathsTests extends Specification with DBState {


     "Some breadth-first paths" should {

          "-> be shorter than depth-first paths" in {

               val depthPathTo3: String = makeDepthPathTo(3)
               val breadthPathTo3: String = makeBreadthPathTo(3)


               dfp.pathTo(3).toList.length shouldEqual 6
               bfp.pathTo(3).toList.length shouldEqual 4

               depthPathTo3 shouldEqual "2 -> 0 -> 6 -> 4 -> 5 -> 3"
               breadthPathTo3 shouldEqual "2 -> 0 -> 5 -> 3"

               dfp.pathTo(3) shouldNotEqual bfp.pathTo(4)
          }

          "-> be same length as depth-first paths" in {

               val depthPathTo4: String = makeDepthPathTo(4)
               val breadthPathTo4: String = makeBreadthPathTo(4)

               dfp.pathTo(4).toList.length shouldEqual 4
               bfp.pathTo(4).toList.length shouldEqual 4

               depthPathTo4 shouldEqual "2 -> 0 -> 6 -> 4"
               breadthPathTo4 shouldEqual "2 -> 0 -> 6 -> 4"

               dfp.pathTo(4) shouldEqual bfp.pathTo(4)
          }

          "-> be same length as depth-first paths, which is always true when path is to itself" in {
               dfp.pathTo(2).toList shouldEqual List(2)
               bfp.pathTo(2).toList shouldEqual List(2)
          }
     }


     "Breadth and depth-first paths" should {

          "-> be nonexistent from the same indices" in {

               (7 to 12) foreach {
                    i => dfp.hasPathTo(i) shouldEqual false
                         bfp.hasPathTo(i) shouldEqual false
               }
               success
          }
     }


     "The edgeTo array" should {

          "-> be used to create the path" in {

               val depthPathTo3: String = makeDepthPathTo(3)
               val breadthPathTo3: String = makeBreadthPathTo(3)

               depthPathTo3 shouldEqual "2 -> 0 -> 6 -> 4 -> 5 -> 3"
               dfp.edgeTo(3) shouldEqual 5
               dfp.edgeTo(5) shouldEqual 4
               dfp.edgeTo(4) shouldEqual 6
               dfp.edgeTo(6) shouldEqual 0
               dfp.edgeTo(0) shouldEqual 2


               breadthPathTo3 shouldEqual "2 -> 0 -> 5 -> 3"
               bfp.edgeTo(3) shouldEqual 5
               bfp.edgeTo(5) shouldEqual 0
               bfp.edgeTo(0) shouldEqual 2
          }
     }
}
