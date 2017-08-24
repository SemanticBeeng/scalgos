package codility

/**
  * https://codesays.com/2014/solution-to-perm-missing-elem-by-codility
  */
object Solution_Perm_Missing_Elem {

  def solution(A: Array[Int]) : Int = {
    var res: Int = 0

    for(i <- A.indices)
      res = res ^ A(i) ^ (i + 1)

    res ^ (A.length + 1)
  }

  def main (args: Array[String]) {

    println(solution(Array(2, 3, 1, 5)) == 4)
  }

}

