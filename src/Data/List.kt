package Data.List

/*
data List a = Cons a (List a) | Nil

data List : (a: Type) -> Type where
    Cons : (x: a) -> (xs: List a) -> List a
    Nil  : List a
*/

// List
sealed class List<out A> {
  data class Cons<out A>(val x: A, val xs: List<A>) : List<A>() { override fun toString() = "[$x, ${xs.toString()}]" }
  object Nil : List<Nothing>()                                  { override fun toString() = "[]" }
}

// map : (a -> b) -> f a -> f b
infix fun <A, B> ((A) -> B). ⵙ (a: List<A>) : List<B> = when (a) {
  is List.Cons<A> -> List.Cons(this(a.x), this ⵙ a.xs)
  is List.Nil     -> List.Nil
}

// apply : f (a -> b) -> f a -> f b
infix fun <A, B> List<(A) -> B>. ⴲ (a: List<A>) : List<B> = when (this) {
  is List.Cons<(A) -> B> -> {
    val xs = this.x ⵙ a
    val ys = this.xs ⴲ a
    xs ᚌ ys
  }
  is List.Nil -> List.Nil
}

// pure : a -> f a
fun <A> pure (a: A) = List.Cons(a, List.Nil)

// bind : m a -> (a -> m b) -> m b
infix fun <A, B> List<A>. ᐒ (f: (A) -> List<B>) : List<B> = when (this) {
  is List.Cons<A> -> f(this.x) ᚌ (this.xs ᐒ f)
  is List.Nil     -> List.Nil
}

// append (concat) : f a -> f a -> f a
infix fun <A> List<A>. ᚌ (ys: List<A>) : List<A> = when (this) {
  is List.Cons<A> -> List.Cons(this.x, this.xs ᚌ ys)
  is List.Nil     -> ys
}

// empty : f a
val empty = List.Nil
