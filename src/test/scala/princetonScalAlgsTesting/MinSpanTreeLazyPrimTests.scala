package princetonScalAlgsTesting

import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataGraph._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._ //use if I want to convert scala arraybuffer into edu.princeton queue.
import scala.collection.mutable
import scala.collection.mutable._
import util.control.Breaks._


trait LazyPrimState {

     val mstObj: LazyPrimMST = getState(tinyEWG)
     val mstEdges: List[Edge] = /*mutable.Set() ++ */mstObj.edges().toList
     val mstGraph: Graph = new EdgeWeightedGraph(mstObj.edges()).convertToGraph()
     val mst: EdgeWeightedGraph = new EdgeWeightedGraph(mstObj.edges())
     val originalGraph: Graph = new EdgeWeightedGraph(new In(tinyEWG)).convertToGraph()
     val ewg: EdgeWeightedGraph = new EdgeWeightedGraph(new In(tinyEWG))


     def getState(filename: String): LazyPrimMST = {
          val in: In = new In(filename)
          val edgeWeightedGraph: EdgeWeightedGraph = new EdgeWeightedGraph(in)
          new LazyPrimMST(edgeWeightedGraph)
     }

     def toEWGraph(edges: List[Edge]) = {
          val princetonQueue: edu.princeton.cs.algs4.Queue[Edge] = new edu.princeton.cs.algs4.Queue[Edge]
          edges.foreach(e => princetonQueue.enqueue(e))
          new EdgeWeightedGraph(princetonQueue)
     }

     def removeNeededEdgeThenDisconnected(edgeIndex: Int): Boolean = {
          val edgeToRemove: Edge = mstEdges.to[ArrayBuffer].remove(edgeIndex)
          val tempGraph: Graph = new EdgeWeightedGraph(mstObj.edges(), edgeToRemove).convertToGraph()

          !new CC(tempGraph).isConnected
     }


     def addNeedlessEdgeThenHasCycle(sourceVertex: Int): Boolean = {

          val tempMstEdges: List[Edge] = mstEdges

          def getConnections(): Set[Integer] = {
               var connections: Set[Integer] = Set(sourceVertex)

               def getConn(edge: Edge): Unit = {
                    if (edge.connectsVertex(sourceVertex))
                         connections = connections + edge.other(sourceVertex)
               }

               tempMstEdges.foreach(e => getConn(e))
               connections
          }

          def makeNewGraph(vertex: Int): Graph = {
               val tempGraph: Graph = mstGraph
               tempGraph.addEdge(sourceVertex, vertex)
               tempGraph
          }

          var vertices: mutable.Set[Integer] = mutable.Set() ++ getVertices(mstGraph)
          vertices = vertices -- getConnections()

          val graphs: List[Graph] = (for (v <- vertices)  yield makeNewGraph(v)).toList
          val allCyclic: List[Boolean] = for (g <- graphs) yield new Cycle(g).hasCycle
          allCyclic.forall(_ == true)
     }



     def getVertices(ewg: EdgeWeightedGraph): List[Integer] = {
          val vertices: mutable.Set[Integer] = mutable.Set()
          for (v <- 0 until ewg.V()){
               ewg.adj(v).toArray.foreach(e => vertices += e.either() += e.other(e.either()))
          }
          vertices.toList
     }


     def getVertices(g: Graph): List[Integer] = {
          val vertices: mutable.Set[Integer] = mutable.Set()
          for (v <- 0 until g.V()){
               g.adj(v).toArray.foreach(w => vertices += w)
          }
          vertices.toList
     }

     def checkIfSpanningTree(maybeTree: Graph): Boolean = {
          val vertices: List[Integer] = getVertices(maybeTree)

          val numEdgesIsOneLessThanNumVertices: Boolean = maybeTree.E() == maybeTree.V() - 1
          val acyclic: Boolean = !new Cycle(maybeTree).hasCycle
          val connected: Boolean = new CC(maybeTree).isConnected
          val minimallyConnected: Boolean = (for(edgeIndex <- 0 until (maybeTree.V() - 1)) yield
               removeNeededEdgeThenDisconnected(edgeIndex)).forall(_ == true)
          val maximallyAcyclic: Boolean = (for (v <- vertices) yield
               addNeedlessEdgeThenHasCycle(v)).forall(_ == true)

          numEdgesIsOneLessThanNumVertices && acyclic && connected && minimallyConnected && maximallyAcyclic
     }


     // running prim's algorithm up to a point, when edge 2-3 is added to arraybuffer and no more.
     def lazyPrimState(limit: Int): (List[Edge], List[Boolean]) = {
          val mstMarkedVertices: ArrayBuffer[Boolean] = ArrayBuffer.fill(ewg.V())(false)
          val crossingAndIneligibleEdges: edu.princeton.cs.algs4.MinPQ[Edge] = new edu.princeton.cs.algs4.MinPQ[Edge]
          // note don't really need this variable here, just for the sake of showing algorithm, since we have
          // mstEdges at top.
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

          // this is the result of this function cutProperty
          prim()
     }
}




class MinSpanTreeLazyPrimTests extends Specification with LazyPrimState {

     //import LazyPrimState._


     // note technically book says that either of the printed conditions should be true, bu I "and-ed" them... (pg 520)
     "A minimum spanning tree" should {
          val vertices: List[Integer] = getVertices(mstGraph)

          val mstNumEdgesIsOneLessThanNumVertices: Boolean = mstGraph.E() == mstGraph.V() - 1
          val mstIsAcyclic: Boolean = !new Cycle(mstGraph).hasCycle
          val mstIsConnected: Boolean = new CC(mstGraph).isConnected
          val mstIsMinimallyConnected: Boolean = (for(edgeIndex <- 0 until (mstGraph.V() - 1)) yield
               removeNeededEdgeThenDisconnected(edgeIndex)).forall(_ == true)
          val mstIsMaximallyAcyclic: Boolean = (for (v <- vertices) yield
               addNeedlessEdgeThenHasCycle(v)).forall(_ == true)

          "-> have V - 1 edges" in { mstNumEdgesIsOneLessThanNumVertices shouldEqual true }

          "-> be acyclic" in { mstIsAcyclic shouldEqual true }

          "-> be connected" in { mstIsConnected shouldEqual true }

          "-> be such that removing any edge disconnects the mst" in { mstIsMinimallyConnected shouldEqual true }

          "-> be such that adding any edge creates a cycle" in { mstIsMaximallyAcyclic shouldEqual true }
     }

     "A minimum spanning tree" should {
          "-> have same vertices as original graph" in {
               val mstVertices: List[Integer] = getVertices(mstGraph)
               val originalVertices: List[Integer] = getVertices(originalGraph)

               mstVertices shouldEqual originalVertices
               mstVertices.size shouldEqual originalVertices.size
          }
     }

     "A minimum spanning tree with more than one duplicated edge weight" should {

          val mstDupObj: LazyPrimMST = getState(tinyEWGDuplicateMine)
          val edgesOne: List[Edge] = mstDupObj.edges().toList
          val mstPathOne: EdgeWeightedGraph = toEWGraph(edgesOne)
          val edgesTwo: List[Edge] = List(new Edge(0, 2, 0.50), new Edge(2, 3, 0.50), new Edge(1, 3, 1.00))
          val mstPathTwo: EdgeWeightedGraph = toEWGraph(edgesTwo)

          "-> have more than one MST with unique weights" in {
               mstPathOne.totalWeight() shouldEqual 2.00
               mstPathTwo.totalWeight() shouldEqual 2.00

               edgesOne shouldNotEqual edgesTwo
          }
     }

     "Cut property" should {

          "-> verify that minimum-weight edge 2-3 exists in the final minimum spanning tree" in {

               val cutPropertyState = lazyPrimState(4)

               val edgesMst: List[Edge] = cutPropertyState._1
               val marked: List[Boolean] = cutPropertyState._2
               val neighborsOfTwo: List[Edge] = ewg.getAdj(2).toList
               val minimumWeightEdge: Edge = edgesMst(3)

               marked(0) shouldEqual true
               marked(1) shouldEqual true
               marked(2) shouldEqual true
               marked(7) shouldEqual true
               (3 to 6).foreach(i => marked(i) shouldEqual false)

               neighborsOfTwo.exists(e => e.equals(new Edge(1, 2, 0.36)))
               neighborsOfTwo.exists(e => e.equals(new Edge(2, 7, 0.34)))
               neighborsOfTwo.exists(e => e.equals(new Edge(0, 2, 0.26)))
               neighborsOfTwo.exists(e => e.equals(new Edge(6, 2, 0.40)))
               neighborsOfTwo.exists(e => e.equals(minimumWeightEdge))

               edgesMst contains minimumWeightEdge shouldEqual true
          }
     }

     "Lazy prim's scan function" should {

          "-> avoid putting onto priority queue the edges whose end vertices are not marked" in {

               val scanState = lazyPrimState(2)
               val crossingEdges: List[Edge] = scanState._1
               val marked: List[Boolean] = scanState._2

               marked(0) shouldEqual true
               marked(7) shouldEqual true
               (1 to 6).foreach(v => marked(v) shouldEqual false)

               // note, in the priority queue, there is no edge 0-7 because 0 has already been marked.
               crossingEdges.count(e => e.equals(new Edge(0, 7, 0.16))) shouldEqual 0
          }
     }
}
