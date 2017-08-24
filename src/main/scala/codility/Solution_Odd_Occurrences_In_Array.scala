package codility

import scala.collection.mutable

/**
  * https://codesays.com/2015/solution-to-odd-occurrences-in-array-by-codility/
  */
object Solution_Odd_Occurrences_In_Array {

  def solution(A: Array[Int]) : Int = {
    var res: Int = 0

    for(i <- A.indices)
      res ^= A(i)

    res
  }

  def main (args: Array[String]) {

    println(solution(Array(1, 2, 5, 2, 5, 4, 1)) == 4)
    println(solution(Array(1, 2, 5, 2, 5, 3, 4, 1)) == 7)
  }

}

