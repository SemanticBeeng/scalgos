package com.github.pathikrit.scalgos

import org.specs2.mutable.Specification

import Implicits.Crossable
import BitHacks._
import org.specs2.specification.core.Fragments
import org.specs2.specification.dsl.mutable.ExampleDsl

class BitHacksSpec extends Specification with ExampleDsl {

  "flipCase" should {
    "work for characters" in todo
//    {
//      val lower = "abcdefghijklmnopqrstuvwxyz"
//      val upper = lower map flipCase
//      upper must be equalTo lower.toUpperCase
//      val lower2 = upper map flipCase
//      lower2 must be equalTo lower
//    }

    "fail for non characters" in todo
  }

  "swap" should {
    "work" in {

      eg {
        for (v <- (-100 to 100) X (-100 to 100)) {
          xorSwap(v) must be equalTo v.swap
          noTempSwap(v) must_== v.swap
        }
        success
      }
    }

    "work for Int.MaxValue & Int.MinValue" in todo
  }

  "gcd" should {
    "match euclid's algorithm" in {

//        eg {
//          for ((x,y) <- (-100 to 100) X (-100 to 100) if x!=0 || y!=0) {
//            val g = gcd(x,y)
//            x%g must be equalTo 0
//            y%g must be equalTo 0
//            g must_== NumberTheory.gcd(x,y)
//          }
//          success
//        }
        val seq:Traversable[(Int, Int)] = for ((x,y) <- (-100 to 100) X (-100 to 100) if x!=0 || y!=0) yield (x, y)

        Fragments.foreach(seq.toSeq) { case (x, y) =>

          "example for pair "+(x, y) ! {

              val g = gcd(x,y)
              x%g must be equalTo 0
              y%g must be equalTo 0
              g must_== NumberTheory.gcd(x,y)
          }
        }: Fragments
    }

    "fail for (0,0)" in {
      gcd(0, 0) must throwA[IllegalArgumentException]
    }

    "work for Int.MaxValue and Int.MinValue" in todo
  }
}
