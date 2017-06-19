package Main

import Function.Curry.*
import Data.Maybe.*
import Data.Maybe.pure as pureM
import Data.List.*
import Data.List.pure as pureL
import Data.List.List as List // hiding java List

fun main(args: Array<String>) {
  println(maybeSample())
  println(listSample())
}

val plus2 = { a: Int -> { b: Int -> a + b } }
val plus3 = fun (a: Int) = fun (b: Int) = fun (c: Int) = a + b + c

fun plus3a_ (a: Int, b: Int, c: Int) = "$a + $b + $c"
val plus3a = curry(::plus3a_)

fun maybeSample() : Any {
  val p = pureM(21)
  val q = pureM(42)
  val u = Maybe.None
  val v = pureM(84)

  val res1 = plus2  ⵙ p ⴲ q
  val res2 = plus3  ⵙ p ⴲ q ⴲ v
  val res3 = plus3a ⵙ p ⴲ q ⴲ u
  val res4 = pureM(plus3) ⴲ p ⴲ q ⴲ u

  val strM = { a: Int -> pureM("$a + 5 = ${a+10}") }
  val res5 = p ᐒ strM
  val res6 = p ᐒ { a -> pureM("$a + 5 = ${a+10}") }

  return Triple(res2, res3, res6)
}

fun listSample() : Any {
  val p = List.Cons(21, pureL(11))
  val q = pureL(42)
  val u = List.Nil
  val v = pureL(84)

  val res1 = plus2  ⵙ p ⴲ q
  val res2 = plus3  ⵙ p ⴲ q ⴲ v
  val res3 = plus3a ⵙ p ⴲ q ⴲ u
  val res4 = pureL(plus3) ⴲ p ⴲ q ⴲ u

  val strM = fun (a: Int) = pureL("$a + 5 = ${a+10}")
  val res5 = p ᐒ strM
  val res6 = p ᐒ fun (a) = pureL("$a + 5 = ${a+10}")

  return Triple(res2, res3, res6)
}
