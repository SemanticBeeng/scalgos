package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification

import scala.collection.JavaConversions._
import scalaz.Functor
import scalaz.std.list._
import DataDigraph._


class TopologicalTests extends Specification { // note uses mutable specification

     object State {

          def getState(filename : String, delimiter : String) = {
               val sg: SymbolDigraph = new SymbolDigraph(filename, delimiter)
               val topological: Topological = new Topological(sg.G)
               (sg, topological)
          }

          def getState(filename: String) : DepthFirstOrder = {
               val in: In = new In(tinyDAG)
               val digraph: Digraph = new Digraph(in)
               new DepthFirstOrder(digraph)
          }

          def getStateReverse(filename: String): DepthFirstOrder = {
               val in: In = new In(tinyDAG)
               val digraph: Digraph = new Digraph(in)
               new DepthFirstOrder(digraph.reverse())
          }


          val delimiter: String = "/"

          val acyclicInfo = getState (tinyDAGDelimiter, delimiter)
          val cyclicInfo = getState (tinyDGDelimiter, delimiter)
          val dfs = getState (tinyDAG)
          val dfsReverse = getStateReverse(tinyDAG)
          val acyclicTwoProngs = getState(twoProngs, delimiter)
     }


     import State._

     "Directed acyclic graph" should {
          "have a topological sort" in {

               val symDigraph: SymbolDigraph = acyclicInfo._1
               val topological: Topological = acyclicInfo._2
               val vs: List[Integer] = topological.order.toList
               val topologicalSort = Functor[List].map(vs)({v => symDigraph.name(v)}).filterNot(_.isEmpty)

               topological.isDAG mustEqual true
               topologicalSort mustEqual List("8","7","2","3","0","6","9","10","11","12","1","5","4")
          }
     }

     "Directed cylic graph" should {
          "have no topological sort" in {

               val topological: Topological = cyclicInfo._2

               topological.isDAG mustEqual false
          }
     }

     "Reverse postorder" should {
          "equal a topological sort" in {

               val reversePostOrder = dfs.reversePost().toList.map(_.toString)
               val symDigraph: SymbolDigraph = acyclicInfo._1
               val topological: Topological = acyclicInfo._2
               val vs: List[Integer] = topological.order.toList
               val topologicalSort = Functor[List].map(vs)({v => symDigraph.name(v)}).filterNot(_.isEmpty)

               reversePostOrder mustEqual topologicalSort
          }
     }

     "Two-pronged connection" should {
          "result in highest node (1) being before other nodes (2) in topological sort" in {
               val symDigraph: SymbolDigraph = acyclicTwoProngs._1
               val topological: Topological = acyclicTwoProngs._2
               val vs: List[Integer] = topological.order.toList
               val topologicalSort = Functor[List].map(vs)({v => symDigraph.name(v)}).filterNot(_.isEmpty)

               topologicalSort mustEqual List("3","4","5","6","1","2")
          }
     }

     "Postorder and preorder: means nodes should finish/start in certain orders" should {

          val postOrder/*: List[Int] */= dfs.post().toList
          val preOrder/*: List[Int]*/ = dfs.pre().toList


          "1. (4) should finish before (5) because it comes after it and has no more descendants" in {
               val nodeBeingTested: Int = preOrder(2).toInt

               // note 4 finishes before 5 means that 4 comes at earlier index in postorder list
               // note 4 finishes first means it is first on postorder list.
               nodeBeingTested mustEqual 4
               preOrder(2) mustEqual postOrder(0)
          }

          "(5) finishes after (4) since it has one descendant" in {
               postOrder(0) mustEqual 4
               postOrder(1) mustEqual 5
          }

          "(5) is in same location in both postorder and preorder lists" in {
               val nodeBeingTested: Int = preOrder(1).toInt

               nodeBeingTested mustEqual 5
               preOrder(1) mustEqual postOrder(1)
          }

          "(6) starts then keeps going until it hits the end at (12)" in {

               preOrder(4) mustEqual 6  // starting 6
               preOrder(7) mustEqual 12 // gone in 12
               postOrder(3) mustEqual 12 // hit end (12)
          }

          "(11) is done after (12) because it was marked when heading to (12)" in {
               preOrder(6) mustEqual 11 // marked when heading to 12
               preOrder(7) mustEqual 12 // 12 is reached

               postOrder(3) mustEqual 12 // 12 finished before 11
               postOrder(4) mustEqual 11 // 11 is finished after 12
          }

          "(0) is done after (6), its directed descendant, is done" in {
               postOrder(7) mustEqual 6
               postOrder(8) mustEqual 0
          }

          "there is no (0) after (2) in preorder because (0) was already marked when first heading into it" in {
               val nodesAfterTwo = preOrder.drop(10)

               nodesAfterTwo.count(_ == 0) mustEqual 0
          }
     }

     /*"There are properties involving the original graph and the reversed graph" should {

          val originalGraphReversePostOrder = dfs.reversePost().toList.map(_.toString)
          val reverseGraphPostOrder = dfsReverse.post().toList.map(_.toString)

          val symDigraph: SymbolDigraph = acyclicInfo._1
          val topological: Topological = acyclicInfo._2
          val vs: List[Integer] = topological.order.toList
          val topologicalSort = Functor[List].map(vs)({v => symDigraph.name(v)}).filterNot(_.isEmpty)

--- help???
// postorder of reverse graph is topological sort
// reverse postorder of (original) graph is topological sort

          "-> topological sort is the reverse post order of original graph" in {
               originalGraphReversePostOrder shouldEqual topologicalSort
          }

// note todo not true! investigate later if it's really the case

          "-> topological sort is post order of reversed graph" in {
               reverseGraphPostOrder shouldEqual topologicalSort
          }
     }*/

}