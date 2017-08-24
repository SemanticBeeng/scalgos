package princetonScalAlgsTesting

import edu.princeton.cs.algs4.{BreadthFirstDirectedPaths, DepthFirstDirectedPaths, Digraph, In}
import org.specs2.mutable.Specification
import DataDigraph._
import scala.collection.JavaConversions._



class DBFirstDirectedPathsTests extends Specification {

     object State {
          // note state function
          def getState(filename: String, oneSource: Integer) ={
               val in: In = new In(filename)
               val digraph: Digraph = new Digraph(in)
               val bfp = new BreadthFirstDirectedPaths(digraph, oneSource)
               val dfp = new DepthFirstDirectedPaths(digraph, oneSource)
               (bfp, dfp)
          }

          // note utility functions
          def makeBreadthPathCyclicTo(destination: Integer): String = {
               breadthCyclic.pathTo(destination).toList.map(_.toString).mkString(" -> ")
          }

          def makeDepthPathCyclicTo(destination: Integer): String = {
               depthCyclic.pathTo(destination).toList.map(_.toString).mkString(" -> ")
          }

          def makeBreadthPathAcyclicTo(destination: Integer): String = {
               breadthAcyclic.pathTo(destination).toList.map(_.toString).mkString(" -> ")
          }

          def makeDepthPathAcyclicTo(destination: Integer): String = {
               depthAcyclic.pathTo(destination).toList.map(_.toString).mkString(" -> ")
          }

          // note should matcher functions
          def mustBeShorterThan(path1: Iterable[Integer], path2: Iterable[Integer]): Boolean = {
               path1.size < path2.size
          }
     }


     import State._

     val source: Integer = 2

     val breadthAcyclic: BreadthFirstDirectedPaths = getState(tinyDAG, source)._1
     val depthAcyclic: DepthFirstDirectedPaths = getState(tinyDAG, source)._2

     val breadthCyclic: BreadthFirstDirectedPaths = getState(tinyDG, source)._1
     val depthCyclic: DepthFirstDirectedPaths = getState(tinyDG, source)._2


     // note using tinyDAG.txt data
     // help verify if this is an axiom/theorem/always true.
     "Depth and breadth first directed paths" should {

          "-> be the same for a DAG (directed acyclic graph)" in {

               Range(0, 12) foreach {
                    i => breadthAcyclic.pathTo(i) shouldEqual depthAcyclic.pathTo(i)
               }
               success
          }
     }

     // note using tinyDG.txt data
     "Breadth first paths may be shorter than depth first paths" should {

          "-> path (2) to (4) should be same in depth and breadth paths because there is no other" +
               "path" in {

               val bothPathsMade: String = makeBreadthPathCyclicTo(4)

               bothPathsMade shouldEqual "2 -> 0 -> 5 -> 4"
               breadthCyclic.pathTo(4) shouldEqual depthCyclic.pathTo(4)
          }

          "-> path (2) to (2) is always the same" in {

               val bothPathsMade: String = makeBreadthPathCyclicTo(2)

               bothPathsMade shouldEqual "2"
               breadthCyclic.pathTo(2) shouldEqual depthCyclic.pathTo(2)

          }

          "-> path (2) to (3) differs for depth because it went down first path while breadth chose" +
               "the shortest path" in {

               val depthPathMade: String = makeDepthPathCyclicTo(3)
               val breadthPathMade: String = makeBreadthPathCyclicTo(3)

               depthPathMade shouldEqual "2 -> 0 -> 5 -> 4 -> 3"
               breadthPathMade shouldEqual "2 -> 3"

               breadthCyclic.pathTo(3) shouldNotEqual depthCyclic.pathTo(3)
               //todo help how to make this infix? help and why is the number white?
               mustBeShorterThan(breadthCyclic.pathTo(3), depthCyclic.pathTo(3))
          }

          "-> there are no paths from (2) to (6) to (2) to (12)" in {

               Range(6, 12) foreach {
                    i => breadthCyclic.pathTo(i) shouldEqual null
                         depthCyclic.pathTo(i) shouldEqual null
               }
               success
          }
     }



     "The edgeTo array is used to create the path" should {

          "-> index-hopping in edgeTo array creates the path from a directed cyclic graph" in {

               val breadthPath: String = makeBreadthPathCyclicTo(3)
               val depthPath: String = makeDepthPathCyclicTo(3)

               depthPath shouldEqual "2 -> 0 -> 5 -> 4 -> 3"
               depthCyclic.edgeTo(3) shouldEqual 4
               depthCyclic.edgeTo(4) shouldEqual 5
               depthCyclic.edgeTo(5) shouldEqual 0
               depthCyclic.edgeTo(0) shouldEqual 2

               breadthPath shouldEqual "2 -> 3"
               breadthCyclic.edgeTo(3) shouldEqual 2
          }

          "-> index-hopping in edgeTo array creates the path from a directed acyclic graph" in {

               val breadthPath: String = makeBreadthPathAcyclicTo(3)
               val depthPath: String = makeDepthPathAcyclicTo(3)

               depthPath shouldEqual "2 -> 3"
               depthAcyclic.edgeTo(3) shouldEqual 2

               breadthPath shouldEqual "2 -> 3"
               breadthAcyclic.edgeTo(3) shouldEqual 2
          }
     }
}
