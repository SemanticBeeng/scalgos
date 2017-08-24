package princetonScalAlgsTesting


import DataGraph._
import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable._



trait KruskalState {

     val mstObj: KruskalMST = getState(tinyEWG)
     val mstEdges: List[Edge] = /*mutable.Set() ++ */mstObj.edges().toList
     val mstGraph: Graph = new EdgeWeightedGraph(mstObj.edges()).convertToGraph()
     val mst: EdgeWeightedGraph = new EdgeWeightedGraph(mstObj.edges())
     val originalGraph: Graph = new EdgeWeightedGraph(new In(tinyEWG)).convertToGraph()
     val ewg: EdgeWeightedGraph = new EdgeWeightedGraph(new In(tinyEWG))


     def getState(filename: String): KruskalMST = {
          val in: In = new In(filename)
          val edgeWeightedGraph: EdgeWeightedGraph = new EdgeWeightedGraph(in)
          new KruskalMST(edgeWeightedGraph)
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

          val tempMstEdges: mutable.Set[Edge] = mutable.Set() ++ mstEdges

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

     def getVertices(ewg: EdgeWeightedGraph): mutable.Set[Integer] = {
          val vertices: mutable.Set[Integer] = mutable.Set()
          for (v <- 0 until ewg.V()){
               ewg.adj(v).toArray.foreach(e => vertices += e.either() += e.other(e.either()))
          }
          vertices
     }

     def getVertices(g: Graph): List[Integer] = {
          val vertices: mutable.Set[Integer] = mutable.Set()
          for (v <- 0 until g.V()){
               g.adj(v).toArray.foreach(w => vertices += w)
          }
          vertices.toList
     }
}



class MinSpanTreeKruskalTests extends Specification with KruskalState {

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

          val mstDupObj: KruskalMST = getState(tinyEWGDuplicateMine)
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

     "Kruskal's algorithm delivers edges in order of their weight" should {

          "-> be true for this tinyEWG graph" in {
               mstEdges.head.weight() shouldEqual 0.16
               mstEdges(1).weight() shouldEqual 0.17
               mstEdges(2).weight() shouldEqual 0.19
               mstEdges(3).weight() shouldEqual 0.26
               mstEdges(4).weight() shouldEqual 0.28
               mstEdges(5).weight() shouldEqual 0.35
               mstEdges(6).weight() shouldEqual 0.40
          }
     }
}
