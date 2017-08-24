/*
package com.github.pathikrit.scalgos

import org.specs2.mutable.Specification
import org.specs2.specification.dsl.mutable.ExampleDsl

class SegmentTreeSpec extends Specification with ExampleDsl {
  "segment-tree" should {
    "match naive implementation" in {
      val data = RandomData.seq()
      val functions: Seq[(Int, Int) => Int] = Seq(_ + _, _ min _)

      eg {
        for {
          f <- functions
          s1 = SegmentTree.apply(data)(f)
          s2 = SegmentTree.naive(data)(f)
          i <- data.indices
          j <- i + 1 until data.length
        } s1.fold(Int.MaxValue)(i, j) must be equalTo s2.fold(Int.MaxValue)(i, j)
        success
      }
    }
  }
}
*/
