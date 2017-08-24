package princetonScalAlgsTesting


import edu.princeton.cs.algs4._
import org.specs2.mutable.Specification
import DataDigraph._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._



trait ArbitrageState {
     def getState(filename: String): (Int, EdgeWeightedDigraph, List[String]) = {
          val in: In = new In(filename)
          val V: Int = in.readLine.toInt
          val name: List[String] = Arbitrage.collectNames(V, filename).toList
          val ewd: EdgeWeightedDigraph = Arbitrage.buildArbitrageNetwork(filename)
          (V, ewd, name)
     }

     def roundThree(num: Double): Double = {
          (num * 1000).round / 1000.0
     }

     def roundFive(num: Double): Double={
          (num * 100000).round / 100000.0
     }
}


class ShortestPathArbitrageTests extends  Specification with ArbitrageState {

     val state = getState(rates)
     val V: Int = state._1
     val ewd: EdgeWeightedDigraph = state._2
     val name: List[String] = state._3
     val arbitragePath: List[Double] = Arbitrage.buildArbitragePath(ewd).asScala.map(_.doubleValue).toList
     val namePath: List[String] = Arbitrage.buildNamePath(ewd, name.toArray).toList


     "Arbitrage edge-weighted graph" should {

          "-> have negative exponent of edge weights be equal to original rates" in {
               roundThree(Math.exp(-ewd.getAdj(0).get(3).weight())) shouldEqual 0.741
               roundThree(Math.exp(-ewd.getAdj(1).get(0).weight())) shouldEqual 1.366
               roundThree(Math.exp(-ewd.getAdj(4).get(4).weight())) shouldEqual 0.995
          }
     }

     "Sum of exponentiated edge weights" should {
          "-> equal arbitrage profit plus original stake" in {

               val sumExpEdgeWeights: Double = 1000 * Math.exp(-(ewd.getAdj(0).get(3).weight() +
                    ewd.getAdj(1).get(0).weight() +
                    ewd.getAdj(4).get(4).weight()))

               roundFive(sumExpEdgeWeights) shouldEqual 1007.14497
          }
     }

     "Selling 1000 USQ -> buying 741 EUR -> selling 741 EUR -> buying 1012 CAD -> " +
          "selling 1012 CAD -> buying 1007.14497 USD" in {

          "-> result in 7.14497 USD dollar profit" in {

               val stake: Double = 1000.0
               val rateUsdEur: Double = roundThree(Math.exp(-ewd.getAdj(0).get(3).weight()))
               val rateEurCad: Double = roundThree(Math.exp(-ewd.getAdj(1).get(0).weight()))
               val rateCadUsd: Double = roundThree(Math.exp(-ewd.getAdj(4).get(4).weight()))

               rateUsdEur shouldEqual 0.741
               rateEurCad shouldEqual 1.366
               rateCadUsd shouldEqual 0.995

               val end: Double = roundFive(stake * rateUsdEur * rateEurCad * rateCadUsd)
               val profit = roundFive(end - stake)

               end shouldEqual 1007.14497
               profit shouldEqual 7.14497
          }
     }

     "Negative cycle USQ-EUR-CAD (which maps to vertices (0-1-4))" should {
          "-> mean arbitrage opportunity is present in that conversion" in {
               val spt: BellmanFordSP = new BellmanFordSP(ewd, 0)
               val negCycle: List[DirectedEdge] = spt.negativeCycle().toList
               val negCycleCheck: List[DirectedEdge] = List(new DirectedEdge(0,1,-Math.log(0.741)),
                    new DirectedEdge(1,4,-Math.log(1.366)), new DirectedEdge(4,0,-Math.log(0.995)))

               (0 to 2) foreach{ i => negCycle(i) equals negCycleCheck(i) shouldEqual true }
               success
          }
     }
}
