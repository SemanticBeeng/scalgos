package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable._




trait LazyVsEagerState {
     val lazyPrim: LazyPrimMST = getLazyPrimState(tinyEWG)
     val eagerPrim: PrimMST = getEagerPrimState(tinyEWG)
     val mstEdges: List[Edge] = lazyPrim.edges().toList
     val ewg: EdgeWeightedGraph = new EdgeWeightedGraph(new In(tinyEWG))

     def getEagerPrimState(filename: String): PrimMST = {
          val in: In = new In(filename)
          val edgeWeightedGraph: EdgeWeightedGraph = new EdgeWeightedGraph(in)
          new PrimMST(edgeWeightedGraph)
     }

     def getLazyPrimState(filename: String): LazyPrimMST = {
          val in: In = new In(filename)
          val edgeWeightedGraph: EdgeWeightedGraph = new EdgeWeightedGraph(in)
          new LazyPrimMST(edgeWeightedGraph)
     }

     def lazyPrimState(limit: Int): (List[Edge], List[Boolean]) = {
          val mstMarkedVertices: ArrayBuffer[Boolean] = ArrayBuffer.fill(ewg.V())(false)
          val crossingAndIneligibleEdges: edu.princeton.cs.algs4.MinPQ[Edge] = new edu.princeton.cs.algs4.MinPQ[Edge]
          var edgesOfMst: ArrayBuffer[Edge] = mutable.ArrayBuffer()
          var counter: Int = 0

          def prim(): (List[Edge], List[Boolean]) = {
               scan(0) // source = 0
               while (!crossingAndIneligibleEdges.isEmpty){
                    val ineligibleEdgeMaybe: Edge = crossingAndIneligibleEdges.delMin
                    val v: Int = ineligibleEdgeMaybe.either
                    val w: Int = ineligibleEdgeMaybe.other(v)

                    if (!(mstMarkedVertices(v) && mstMarkedVertices(w))){
                         val crossingEdge: Edge = ineligibleEdgeMaybe
                         edgesOfMst += crossingEdge
                         //mstWeight += crossingEdge.weight
                         if (counter == limit) {
                              return new Tuple2(crossingAndIneligibleEdges.toList, mstMarkedVertices.toList)
                         }
                         if (!mstMarkedVertices(v)) scan(v)
                         if (!mstMarkedVertices(w)) scan(w)
                    }
               }
               new Tuple2(crossingAndIneligibleEdges.toList, mstMarkedVertices.toList)
          }

          def scan(v: Int): Unit = {
               counter += 1
               mstMarkedVertices(v) = true
               for (e <- ewg.adj(v)){
                    if (!mstMarkedVertices(e.other(v))){
                         crossingAndIneligibleEdges.insert(e)
                    }
               }
          }

          prim()
     }


     def eagerPrimState(limit: Int): (List[Edge], List[Boolean]) = {
          val edgeTo: ArrayBuffer[Edge] = ArrayBuffer.fill(ewg.V())(null)
          val distTo: ArrayBuffer[Double] = ArrayBuffer.fill(ewg.V())(Double.PositiveInfinity)
          val mstMarkedVertices: ArrayBuffer[Boolean] = ArrayBuffer.fill(ewg.V())(false)
          val crossingEdges: edu.princeton.cs.algs4.IndexMinPQ[java.lang.Double] = new edu.princeton.cs.algs4
          .IndexMinPQ[java.lang.Double](ewg.V())
          var counter: Int = 0

          def prim(): (List[Edge], List[Boolean]) = {
               distTo(0) = 0.0
               crossingEdges.insert(0, 0.0)
               while (!crossingEdges.isEmpty) {
                    val v: Int = crossingEdges.delMin()
                    if (counter == limit) {
                         return new Tuple2(edgeTo.toList, mstMarkedVertices.toList)
                    }
                    scan(v)
               }
               new Tuple2(edgeTo.toList, mstMarkedVertices.toList)
          }

          def scan(v: Int): Unit = {
               counter += 1
               mstMarkedVertices(v) = true
               for (e <- ewg.adj(v)){
                    val w: Int = e.other(v)
                    if (!mstMarkedVertices(w)){
                         if (e.weight() < distTo(w)) {
                              distTo(w) = e.weight()
                              edgeTo(w) = e
                              if (crossingEdges.contains(w))
                                   crossingEdges.decreaseKey(w, distTo(w))
                              else
                                   crossingEdges.insert(w, distTo(w))
                         }
                    }
               }
          }

          prim()
     }
}


class LazyVsEagerPrim extends Specification with LazyVsEagerState {

     "LazyPrim vs Eager prim: lazy prim keeps ineligible edges longer on priority queue than does eager prim" should {

          "-> apply to example 1: lazy prim keeps both edges (4-7) and (0-4) on crossingEdges priority queue " +
               "while eager prim replaces (0-4) with (4-7). Both edges are ineligible in the end" in {

               val end: Int = 10
               val crossEdgesLazy: List[Edge] = lazyPrimState(2)._1
               val crossEdgesEager: List[Edge] = eagerPrimState(2)._1
               val edge04: Edge = new Edge(0, 4, 0.38)
               val edge47: Edge = new Edge(4, 7, 0.47)

               crossEdgesEager.exists(e => edge04.equals(e)) shouldEqual false
               crossEdgesEager.exists(e => edge47.equals(e)) shouldEqual true

               crossEdgesLazy.exists(e => edge04.equals(e)) shouldEqual true
               crossEdgesLazy.exists(e => edge47.equals(e)) shouldEqual true

               // note both edges are ineligible in the end
               mstEdges.exists(e => edge04.equals(e)) shouldEqual false
               mstEdges.exists(e => edge47.equals(e)) shouldEqual false

               mstEdges.exists(e => edge04.equals(e)) shouldEqual false
               mstEdges.exists(e => edge47.equals(e)) shouldEqual false
          }

          "-> apply to example 2: lazy prim keeps both edges (6-0) and (6-2) on crossingEdges priority queue " +
               "while eager prim replaces (6-0) with (6-2). Edge (6-2) becomes part of mst" in {

               val crossEdgesLazy: List[Edge] =  lazyPrimState(4)._1
               val crossEdgesEager: List[Edge] = eagerPrimState(4)._1
               val edge06: Edge = new Edge(0, 6, 0.38)
               val edge62: Edge = new Edge(6, 2, 0.47)

               crossEdgesEager.exists(e => edge06.equals(e)) shouldEqual false
               crossEdgesEager.exists(e => edge62.equals(e)) shouldEqual true

               crossEdgesLazy.exists(e => edge06.equals(e)) shouldEqual true
               crossEdgesLazy.exists(e => edge62.equals(e)) shouldEqual true

               // note only edge 0-6 is ineligible in the end, 6-2 is good.
               mstEdges.exists(e => edge62.equals(e)) shouldEqual true //so edge 6-2 was a min weight crossing edge
               mstEdges.exists(e => edge06.equals(e)) shouldEqual false // so edge 0-6 was ineligible
          }
     }
}

