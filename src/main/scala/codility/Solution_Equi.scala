import scala.collection.JavaConversions._
import scala.collection.mutable

/**
  * Correct solution: http://blog.codility.com/2011/03/solutions-for-task-equi.html
  */
object Solution_Equi {

  def isEqual(maybeInt: Option[Int], maybeInt1: Option[Int]): Boolean =
    maybeInt == maybeInt1 &&
      maybeInt.isDefined

  def solution(A: Array[Int]): Int = {
    // write your code in Scala 2.10

    if(A.length >= 3) // data is proper
    for(i <- 1 until A.length) {
      if(isEqual(sum(A, 0, i-1), sum(A, i+1, A.length - 1)))
        return i
    }
    -1
  }

  val memos : mutable.Map[(Integer, Integer), Option[Int]] = new mutable.HashMap[(Integer, Integer), Option[Int]]
  var calculations = 0
  var memoHits = 0

  def sum(A: Array[Int], x : Int, y : Int) : Option[Int] = {
    var res: Option[Int] = None

    println(s"Summing : from x=$x to y=$y")

    val res_memo = memos.get(Tuple2(x, y))
    if (res_memo.isEmpty) {
      res = None

      if (y == x)
        res = Some(A(x))
      else
      if (y == x + 1)
        res = Some(A(x) + A(y))
      else
        res = Some(sum(A, x, y - 1).get + A(y))

      println(s"Summed: from x=$x to y=$y, res = $res ")
      calculations = calculations + 1
      memos.put(Tuple2(x, y), res)
    } else {

      println(s"Memo hit: x=$x, y=$y, res = $res ")
      memoHits = memoHits + 1

      res = res_memo.get
    }

    res
  }

  def main (args: Array[String]) {

    val works = Array[Boolean] (

//      solution(Array()) == -1,
//      solution(Array(56)) == -1,
//      solution(Array(1, 2)) == -1,
//      solution(Array(1, 5, 1)) == 1,
//      solution(Array(1, -3, 2, -5, 3)) == 2,
      solution(Array(1, -3, 2, -5, 3, -3, 7, -7, 0)) == 3
    )

    println(s"calculations = $calculations")
    println(s"memo hists = $memoHits")

    works foreach print

  }

}

