fun main(args: Array<String>) {
  println(res)
}

interface IMonad<A> {
  // Functor
  // map : (a -> b) -> f a -> f b
  infix fun <B> mapRev(f: (A) -> B): IMonad<B>

  // Applicative
  // apply : f (a -> b) -> f a -> f b
  infix fun <B> applyRev(f: (IMonad<(A) -> B>)): IMonad<B>

  // Monad
  // bind : m a -> (a -> m b) -> m b
  infix fun <B> ᐒ(f: (A) -> IMonad<B>): IMonad<B>
}

// map : (a -> b) -> f a -> f b
infix fun <A, B> ((A) -> B). ⵙ (a: IMonad<A>): IMonad<B> = a mapRev this

// apply : f (a -> b) -> f a -> f b
infix fun <A, B> (IMonad<(A) -> B>). ⴲ (a: IMonad<A>): IMonad<B> = a applyRev this

// pure : a -> f a
fun <A> pure(a: A): Maybe<A> = Just(a)

interface Maybe<A> : IMonad<A> {}

class Just<A>(val Value: A) : Maybe<A> {
  infix override fun <B> mapRev(f: (A) -> B): Maybe<B> = pure(f(Value))
  infix override fun <B> applyRev(fa: (IMonad<(A) -> B>)): Maybe<B> {
    when (fa) {
      is Just<*> -> {
        val f = fa.Value as ((A) -> B)
        return this mapRev f
      }
      else -> return Nothing()
    }
  }

  infix override fun <B> ᐒ(f: ((A) -> IMonad<B>)): IMonad<B> {
    when (this) {
      is Just<*> -> return f(Value)
      else       -> return Nothing()
    }
  }

  override fun toString(): String = Value.toString()
}

class Nothing<A> : Maybe<A> {
  infix override fun <B> mapRev(f: (A) -> B): Maybe<B> = Nothing()
  infix override fun <B> applyRev(fa: (IMonad<(A) -> B>)): Maybe<B> = Nothing()
  infix override fun <B> ᐒ(f: ((A) -> IMonad<B>)): IMonad<B> = Nothing()
  override fun toString(): String = "Nothing"
}

val p = pure(42)
val q = pure(42)
val u = Nothing<Int>() //pure(42)

val plus: (Int) -> (Int) -> Int = { a: Int -> { b: Int -> a + b } }
val plus3: (Int) -> (Int) -> (Int) -> Int = { a: Int -> { b: Int -> { c: Int -> a + b + c } } }
fun plus10M(a: Int): Maybe<String> = Just("$a + 10 = ${a + 10}")

val res1 = plus ⵙ p ⴲ q
val res2 = plus3 ⵙ p ⴲ q ⴲ u
val res2a = Just(plus3) ⴲ p ⴲ q ⴲ u
val res3 = p ᐒ ::plus10M

val res = Triple(res1, res2, res3)

