package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataDigraph._



trait TransitiveClosureState /*extends Scope*/ {

     def getState(filename: String) = {
          val in: In = new In(filename)
          val digraph: Digraph = new Digraph(in)
          val tc: TransitiveClosure = new TransitiveClosure(digraph)
          (digraph, tc)
     }

     def reconstructTransClosArray(tc: TransitiveClosure, numVertices: Int): List[List[Boolean]] = {
          val transClosMatrix = Array.ofDim[Boolean](numVertices, numVertices)

          for (v <- 0 until numVertices){
               for (w <- 0 until numVertices) {
                    transClosMatrix(v)(w) = tc.reachable(v, w) // boolean value
               }
          }
          transClosMatrix.toList.map(_.toList)
     }

     def allTrue(list: List[Boolean]): Boolean = { list.forall(_ == true) }
     def allFalse(list: List[Boolean]): Boolean = { list.forall(_ == false) }



     def dCyclic = getState(tinyDG)._1
     def tcCyclic = getState(tinyDG)._2

     def dAcyclic = getState(tinyDAG)._1
     def tcAcyclic = getState(tinyDAG)._2
}


class TransitiveClosureTests extends Specification with TransitiveClosureState {

     val T = true
     val F = false

     "Transitive closure for a cyclic digraph" should {
          "-> mark all direct and indirect connections as true" in {
               val transClos: List[List[Boolean]] = reconstructTransClosArray(tcCyclic, dCyclic.V())

               allTrue(transClos.head.take(6)) shouldEqual true
               allFalse(transClos.head.drop(6)) shouldEqual true

               transClos(1)(0) shouldEqual false
               transClos(1)(1) shouldEqual true
               allFalse(transClos(1).drop(2)) shouldEqual true

               allTrue(transClos(2).take(6)) shouldEqual true
               allFalse(transClos(2).drop(6)) shouldEqual true

               allTrue(transClos(3).take(6)) shouldEqual true
               allFalse(transClos(3).drop(6)) shouldEqual true

               allTrue(transClos(4).take(6)) shouldEqual true
               allFalse(transClos(4).drop(6)) shouldEqual true

               allTrue(transClos(5).take(6)) shouldEqual true
               allFalse(transClos(5).drop(6)) shouldEqual true

               allTrue(transClos(6).take(7)) shouldEqual true
               transClos(6)(7) shouldEqual false
               allTrue(transClos(6).drop(8)) shouldEqual true

               allTrue(transClos(7)) shouldEqual true

               allTrue(transClos(8).take(7)) shouldEqual true
               transClos(8)(7) shouldEqual false
               allTrue(transClos(8).drop(8)) shouldEqual true

               allTrue(transClos(9).take(6)) shouldEqual true
               allFalse(transClos(9).drop(6).take(3)) shouldEqual true
               allTrue(transClos(9).drop(9)) shouldEqual true

               allTrue(transClos(10).take(6)) shouldEqual true
               allFalse(transClos(10).drop(6).take(3)) shouldEqual true
               allTrue(transClos(10).drop(9)) shouldEqual true

               allTrue(transClos(11).take(6)) shouldEqual true
               allFalse(transClos(11).drop(6).take(3)) shouldEqual true
               allTrue(transClos(11).drop(9)) shouldEqual true

               allTrue(transClos(12).take(6)) shouldEqual true
               allFalse(transClos(12).drop(6).take(3)) shouldEqual true
               allTrue(transClos(12).drop(9)) shouldEqual true
          }
     }

     "Transitive closure for an acyclic digraph" should {
          "-> mark all direct and indirect connections as true" in {
               val transClos: List[List[Boolean]] = reconstructTransClosArray(tcAcyclic, dAcyclic.V())

               transClos(0)(0) shouldEqual true
               transClos(0)(1) shouldEqual true
               transClos(0)(2) shouldEqual false
               transClos(0)(3) shouldEqual false
               allTrue(transClos(0).drop(4).take(3)) shouldEqual true
               transClos(0)(7) shouldEqual false
               transClos(0)(8) shouldEqual false
               allTrue(transClos(0).drop(9)) shouldEqual true

               transClos(1)(0) shouldEqual false
               transClos(1)(1) shouldEqual true
               allFalse(transClos(1).drop(2)) shouldEqual true

               allTrue(transClos(2).take(7)) shouldEqual true
               transClos(2)(7) shouldEqual false
               transClos(2)(8) shouldEqual false
               allTrue(transClos(2).drop(9)) shouldEqual true

               allFalse(transClos(3).take(3)) shouldEqual true
               transClos(3)(3) shouldEqual true
               transClos(3)(4) shouldEqual true
               transClos(3)(5) shouldEqual true
               allFalse(transClos(3).drop(6)) shouldEqual true

               transClos(4)(4) shouldEqual true
               allFalse(transClos(4).take(4)) shouldEqual true
               allFalse(transClos(4).drop(5)) shouldEqual true

               transClos(5)(4) shouldEqual true
               transClos(5)(5) shouldEqual true
               allFalse(transClos(5).take(4)) shouldEqual true
               allFalse(transClos(5).drop(6)) shouldEqual true

               allFalse(transClos(6).take(4)) shouldEqual true
               transClos(6)(4) shouldEqual true
               transClos(6)(5) shouldEqual false
               transClos(6)(6) shouldEqual true
               transClos(6)(7) shouldEqual false
               transClos(6)(8) shouldEqual false
               allTrue(transClos(6).drop(9)) shouldEqual true

               allFalse(transClos(7).take(4)) shouldEqual true
               transClos(7)(4) shouldEqual true
               transClos(7)(5) shouldEqual false
               transClos(7)(6) shouldEqual true
               transClos(7)(7) shouldEqual true
               transClos(7)(8) shouldEqual false
               allTrue(transClos(7).drop(9)) shouldEqual true

               allFalse(transClos(8).take(4)) shouldEqual true
               transClos(8)(4) shouldEqual true
               transClos(8)(5) shouldEqual false
               allTrue(transClos(8).drop(6)) shouldEqual true

               allFalse(transClos(9).take(9)) shouldEqual true
               allTrue(transClos(9).drop(9)) shouldEqual true

               allFalse(transClos(10).take(10)) shouldEqual true
               transClos(10)(10) shouldEqual true
               transClos(10)(11) shouldEqual false
               transClos(10)(12) shouldEqual false

               allFalse(transClos(11).take(11)) shouldEqual true
               transClos(11)(11) shouldEqual true
               transClos(11)(12) shouldEqual true

               allFalse(transClos(12).take(12)) shouldEqual true
               transClos(12)(12) shouldEqual true
          }
     }
}
