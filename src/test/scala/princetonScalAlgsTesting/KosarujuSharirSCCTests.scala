package princetonScalAlgsTesting

import edu.princeton.cs.algs4
import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataDigraph._
import scala.collection.JavaConversions._



trait KosarujuState /*extends Scope*/ {

     def getState(filename: String) = {
          val in: In = new In(filename)
          val digraph: Digraph = new Digraph(in)
          val scc = new KosarajuSharirSCC(digraph)
          (digraph, scc)
     }

     def dgNoneConn: Digraph = getState(tinyDAG)._1
     def sccNoneConn: KosarajuSharirSCC = getState(tinyDAG)._2

     def dgSomeConn: Digraph = getState(tinyDG)._1
     def sccSomeConn: KosarajuSharirSCC = getState(tinyDG)._2

     def dgOneVertex: Digraph = getState(kosarujuOneVertex)._1
     def sccOneVertex: KosarajuSharirSCC = getState(kosarujuOneVertex)._2

     def dgOneConn: Digraph = getState(kosarujuOneConnVertices)._1
     def sccOneConn: KosarajuSharirSCC = getState(kosarujuOneConnVertices)._2

     def dgAllConn: Digraph = getState(kosarujuAllConn)._1
     def sccAllConn: KosarajuSharirSCC = getState(kosarujuAllConn)._2
}



class KosarujuSharirSCCTests extends Specification with KosarujuState {

     "A directed acyclic graph (DAG)" should {

          val numVertices = sccNoneConn.stronglyConnected(dgNoneConn).toList.length

          "-> have V strong connections" in {
               sccNoneConn.count() shouldEqual numVertices
          }

          "-> have between 1 and V strong connections" in {
               sccNoneConn.count() must beBetween(1, numVertices)
          }

          "-> have list showing to which components the vertices belong" in {
               val ids: List[Int] = sccNoneConn.ids.toList
               ids shouldEqual List(11, 10, 12, 9, 4, 5, 6, 7, 8, 3, 2, 1, 0)
          }

          "-> have groups of vertices that are strong connections" in {

               val componentsListOfQueues: Array[algs4.Queue[Integer]] = sccNoneConn.stronglyConnected(dgNoneConn)
               val queueToList: Queue[Integer] => List[Integer] = { q => q.toList }
               val components = componentsListOfQueues.toList.map(q => queueToList(q))

               components shouldEqual List(List(12), List(11), List(10), List(9), List(4), List(5), List(6), List(7),
                                             List(8), List(3), List(1), List(0), List(2))

               // note none are connected to each other
               for (v <- 0 to 12){
                    for (w <- 0 to 12){
                         if (v != w){
                              sccNoneConn.areStronglyConnected(v, w) shouldEqual false
                         }
                    }
               }
               success
          }

          "-> reflexive property: every vertex is connected to itself" in {

               (0 to 12) foreach {
                    i => sccNoneConn.areStronglyConnected(i, i) shouldEqual true
               }
               success
          }

          "-> symmetric property: if v -> w then w -> v" in {
               def areStronglyConnected(v: Int, w: Int): Boolean = sccNoneConn.areStronglyConnected(v, w)

               for (v <- 0 to 12){
                    for (w <- 0 to 12){
                         if (areStronglyConnected(v, w))
                              areStronglyConnected(w, v) shouldEqual true
                         else
                              areStronglyConnected(w, v) shouldEqual false

                    }
               }
               success
          }
          // note no example in tinyDAG because all are separate
          "-> transitive property: if v -> w and w -> x then v -> x, where -> means " +
               "strongly connected (one example)" in {

               // note 3 -> 5 and 5 -> 4 so then 3 -> 4 (transitivity)
               sccNoneConn.areStronglyConnected(3, 4) shouldEqual false
          }
     }


     "A directed cyclic graph (DG)" should {

          val numVertices = sccSomeConn.stronglyConnected(dgNoneConn).toList.length

          "-> have between 1 and V strong connections" in {
               sccSomeConn.count() must beBetween(1, numVertices)
               sccSomeConn.count() shouldEqual 5
          }

          "-> have list showing to which components the vertices belong" in {
               val ids: List[Int] = sccSomeConn.ids.toList
               ids shouldEqual List(1, 0, 1, 1, 1, 1, 3, 4, 3, 2, 2, 2, 2)
          }

          "-> have groups of vertices that are strong connections" in {
               val componentsListOfQueues = sccSomeConn.stronglyConnected(dgSomeConn)
               val queueToList: Queue[Integer] => List[Integer] = { q => q.toList }
               val components = componentsListOfQueues.toList.map(q => queueToList(q))

               components shouldEqual List(List(1), List(0, 2, 3, 4, 5), List(9, 10, 11, 12), List(6, 8), List(7))

               // note list of some connected components
               sccSomeConn.areStronglyConnected(1, 1) shouldEqual true
               sccSomeConn.areStronglyConnected(1, 0) shouldEqual false
               sccSomeConn.areStronglyConnected(0, 2) shouldEqual true
               sccSomeConn.areStronglyConnected(2, 3) shouldEqual true
               sccSomeConn.areStronglyConnected(4, 5) shouldEqual true
               sccSomeConn.areStronglyConnected(4, 3) shouldEqual true
               sccSomeConn.areStronglyConnected(9, 10) shouldEqual true
               sccSomeConn.areStronglyConnected(10, 11) shouldEqual true
               sccSomeConn.areStronglyConnected(1, 9) shouldEqual false
               sccSomeConn.areStronglyConnected(7, 1) shouldEqual false
          }

          "-> reflexive property: every vertex is connected to itself" in {

               (0 to 12) foreach {
                    i => sccSomeConn.areStronglyConnected(i, i) shouldEqual true
               }
               success
          }

          "-> symmetric property: if v -> w then w -> v" in {
               def areStronglyConnected(v: Int, w: Int): Boolean = sccSomeConn.areStronglyConnected(v, w)

               for (v <- 0 to 12){
                    for (w <- 0 to 12){
                         if (areStronglyConnected(v, w))
                              areStronglyConnected(w, v) shouldEqual true
                         else
                              areStronglyConnected(w, v) shouldEqual false

                    }
               }
               success
          }

          "-> transitive property: if v -> w and w -> x then v -> x, where -> means " +
               "strongly connected (one example)" in {

               // note 3 -> 5 and 5 -> 4 so then 3 -> 4 (transitivity)
               sccSomeConn.areStronglyConnected(3, 4) shouldEqual true
               // note 9 -> 11 and 11 -> 12 means 9 -> 12
               sccSomeConn.areStronglyConnected(9, 12) shouldEqual true
               // note 9 -> 10 and 10 -> 12 means 12 -> 9
               sccSomeConn.areStronglyConnected(12, 9) shouldEqual true
          }
     }



     "A directed cyclic graph (DG) with all components connected" should {

          val numVertices = sccAllConn.stronglyConnected(dgAllConn).toList.length

          "-> have between 1 and V strong connections" in {
               sccAllConn.count() must beBetween(1, numVertices)
               sccAllConn.count() shouldEqual 1
          }

          "-> have list showing to which components the vertices belong" in {
               val ids: List[Int] = sccAllConn.ids.toList
               ids shouldEqual List(0, 0, 0, 0, 0)
          }

          "-> have groups of vertices that are strong connections" in {
               val componentsListOfQueues = sccAllConn.stronglyConnected(dgAllConn)
               val queueToList: Queue[Integer] => List[Integer] = { q => q.toList }
               val components = componentsListOfQueues.toList.map(q => queueToList(q))

               components shouldEqual List(List(0, 1, 2, 3, 4))

               // note list of all connected components
               sccAllConn.areStronglyConnected(0, 1) shouldEqual true
               sccAllConn.areStronglyConnected(0, 2) shouldEqual true
               sccAllConn.areStronglyConnected(0, 3) shouldEqual true
               sccAllConn.areStronglyConnected(0, 4) shouldEqual true
               sccAllConn.areStronglyConnected(1, 2) shouldEqual true
               sccAllConn.areStronglyConnected(1, 3) shouldEqual true
               sccAllConn.areStronglyConnected(1, 4) shouldEqual true
               sccAllConn.areStronglyConnected(2, 3) shouldEqual true
               sccAllConn.areStronglyConnected(2, 4) shouldEqual true
               sccAllConn.areStronglyConnected(3, 4) shouldEqual true
          }

          "-> reflexive property: every vertex is connected to itself" in {

               (0 to 4) foreach {
                    i => sccAllConn.areStronglyConnected(i, i) shouldEqual true
               }
               success
          }

          "-> symmetric property: if v -> w then w -> v" in {
               def areStronglyConnected(v: Int, w: Int): Boolean = sccAllConn.areStronglyConnected(v, w)

               // note v and w are the to and from vertices
               for (v <- 0 to 4){
                    for (w <- 0 to 4){
                         if (areStronglyConnected(v, w))
                              areStronglyConnected(w, v) shouldEqual true
                         else
                              areStronglyConnected(w, v) shouldEqual false

                    }
               }
               success
          }

          "-> transitive property: if v -> w and w -> x then v -> x, where -> means " +
               "strongly connected (one example)" in {

               // note 2 -> 0 and 0 -> 1 so then 1 -> 2 (transitivity) (even if 1 -> 4 -> 2 and not directly 1 -> 2
               sccAllConn.areStronglyConnected(1, 2) shouldEqual true
               // note 2 -> 3 and 3 -> 2
               sccAllConn.areStronglyConnected(3, 2) shouldEqual true
               // note 9 -> 10 and 10 -> 12 means 12 -> 9
               sccAllConn.areStronglyConnected(1, 3) shouldEqual true
          }
     }




     "A graph with one single vertex" should {

          val numVertices = sccOneVertex.stronglyConnected(dgOneVertex).toList.length

          "-> have between 1 and V strong connections" in {
               sccOneVertex.count() must beBetween(1, numVertices)
               sccOneVertex.count() shouldEqual 1
          }

          "-> have list showing to which components the vertices belong" in {
               val ids: List[Int] = sccOneVertex.ids.toList
               ids shouldEqual List(0)
          }

          "-> have 1 group of strong connections with the one vertex as the only member" in {
               val componentsListOfQueues = sccOneVertex.stronglyConnected(dgOneVertex)
               val queueToList: Queue[Integer] => List[Integer] = { q => q.toList }
               val components = componentsListOfQueues.toList.map(q => queueToList(q))

               components shouldEqual List(List(0)) // should be 2

               // note list of all connected components
               sccOneVertex.areStronglyConnected(0, 0) shouldEqual true
          }

          "-> reflexive property: every vertex is connected to itself" in {

               sccOneVertex.areStronglyConnected(0, 0) shouldEqual true
          }
     }



     /** FOR edge cases kosaruju1v and 2 v txts
       * testing for kosaruju2v.txt that count == 1, strongConn = [2,3]...
       * note help todo problem: result of Kosaruju for file 2connvertices (below data) is
       * 1 component, 0 which does not seem correct - can't it handle direct connections
       * like 2 -> 3 and 3 -> 2?
       * note commenting out below until figure out the answer.
       */

     "A directed cyclic graph (DG) with one connection" should {

          val numVertices = sccOneConn.stronglyConnected(dgOneConn).toList.length

          "-> have between 1 and V strong connections" in {
               sccOneConn.count() must beBetween(1, numVertices)
               sccOneConn.count() shouldEqual 3
          }

          "-> have list showing to which components the vertices belong" in {
               val ids: List[Int] = sccOneConn.ids.toList
               ids shouldEqual List(2, 2, 1, 0)
          }

          "-> have groups of vertices that are strong connections" in {
               val componentsListOfQueues = sccOneConn.stronglyConnected(dgOneConn)
               val queueToList: Queue[Integer] => List[Integer] = { q => q.toList }
               val components = componentsListOfQueues.toList.map(q => queueToList(q))

               components shouldEqual List(List(3), List(2), List(0, 1))

               // note list of all connected components
               sccOneConn.areStronglyConnected(3, 2) shouldEqual false
               sccOneConn.areStronglyConnected(0, 1) shouldEqual true
               sccOneConn.areStronglyConnected(0, 3) shouldEqual false
          }

          "-> reflexive property: every vertex is connected to itself" in {
               sccOneConn.areStronglyConnected(0, 0)
               sccOneConn.areStronglyConnected(1, 1)
               sccOneConn.areStronglyConnected(2, 2)
               sccOneConn.areStronglyConnected(3, 3)
          }

          "-> symmetric property: if v -> w then w -> v" in {
               sccOneConn.areStronglyConnected(0, 1)
               sccOneConn.areStronglyConnected(1, 0)
          }
     }
}

