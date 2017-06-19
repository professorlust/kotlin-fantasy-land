fun main(args: Array<String>) {
  println(res)
}

// data Maybe.kt a = Just a | Nothing

// data Maybe.kt : (a: Type) -> Type where
//   Just : (x: a) -> Maybe.kt a
//   None : Maybe.kt a

// pure : {A: Type} -> (a: A) -> Maybe.kt A

// Maybe.kt
sealed class Maybe<out A> {
  data class Just<A>(val x: A) : Maybe<A>()
  object None : Maybe<Nothing>() {
    override fun toString(): String = "Nothing"
  }
}

// map : (a -> b) -> f a -> f b
infix fun <A, B> ((A) -> B).ⵙ(a: Maybe<A>): Maybe<B> {
  when (a) {
    is Maybe.Just<A> -> {
      return Maybe.Just(this(a.x))
    }
    is Maybe.None -> {
      return Maybe.None
    }
  }
}

// pure : a -> f a
fun <A> pure(a: A) = Maybe.Just(a)

// apply : f (a -> b) -> f a -> f b
infix fun <A, B> Maybe<(A) -> B>.ⴲ(a: Maybe<A>): Maybe<B> {
  when (this) {
    is Maybe.Just<(A) -> B> -> {
      val f = this.x
      return f ⵙ a
    }
    is Maybe.None -> return Maybe.None
  }
}

infix fun <A, B> Maybe<(A) -> B>.ⴲ1(a: Maybe<A>): Maybe<B> =
  this ᐒ { f ->
  a ᐒ { x ->
  pure( f(x) )}}

infix fun <A, B> Maybe<(A) -> B>.ⴲ2(a: Maybe<A>): Maybe<B> =
  a ᐒ fun(x) =
  this ᐒ fun(f) =
  pure( f(x) )

// bind : m a -> (a -> m b) -> m b
infix fun <A, B> Maybe<A>.ᐒ(f: (A) -> Maybe<B>): Maybe<B> {
  when (this) {
    is Maybe.Just<A> -> return f(this.x)
    is Maybe.None    -> return Maybe.None
  }
}

val p = pure(42)
val q = pure(42)
val u = Maybe.None //pure(42)

val plus = { a: Int -> { b: Int -> a + b } }
val plus3 = fun(a: Int) = fun(b: Int) = fun(c: Int) = a + b + c
val plus10M = { a: Int -> pure("$a + 10 = ${a + 10}") }

val res1 = plus ⵙ p ⴲ q
val res2 = plus3 ⵙ p ⴲ q ⴲ u
val res2a = pure(plus3) ⴲ p ⴲ q ⴲ u
val res3 = p ᐒ plus10M

val res = Triple(res1, res2, res3)

