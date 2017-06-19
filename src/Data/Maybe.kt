package Data.Maybe

/*
data Maybe a = Just a | Nothing

data Maybe : (a: Type) -> Type where
    Just : (x: a) -> Maybe a
    None : Maybe a
*/

// Maybe
sealed class Maybe<out A> {
  data class Just<out A>(val x: A) : Maybe<A>() { override fun toString() = "Just(${x})" }
  object None : Maybe<Nothing>()                { override fun toString() = "Nothing" }
}

// map : (a -> b) -> Maybe a -> Maybe b
infix fun <A, B> ((A) -> B). ⵙ (a: Maybe<A>) : Maybe<B> = when (a) {
  is Maybe.Just<A> -> Maybe.Just(this(a.x))
  is Maybe.None    -> Maybe.None
}

// pure : a -> Maybe a
fun <A> pure (a: A) = Maybe.Just(a)

// apply : Maybe (a -> b) -> Maybe a -> Maybe b
infix fun <A, B> Maybe<(A) -> B>. ⴲ (a: Maybe<A>) : Maybe<B> = when (this) {
  is Maybe.Just<(A) -> B> -> this.x ⵙ a
  is Maybe.None           -> Maybe.None
}

// bind : Maybe a -> (a -> Maybe b) -> Maybe b
infix fun <A, B> Maybe<A>. ᐒ (f: (A) -> Maybe<B>) : Maybe<B> = when (this) {
  is Maybe.Just<A> -> f(this.x)
  is Maybe.None    -> Maybe.None
}





// `apply` using `bind`
infix fun <A, B> Maybe<(A) -> B>. ⴲ1 (a: Maybe<A>) : Maybe<B> =
  this ᐒ { f ->
  a ᐒ { x ->
  pure( f(x) ) } }

infix fun <A, B> Maybe<(A) -> B>. ⴲ2 (a: Maybe<A>) : Maybe<B> =
  a ᐒ fun (x) =
  this ᐒ fun (f) =
  pure( f(x) )

infix fun <A, B> ((A) -> B).map (a: Maybe<A>) = this.ⵙ(a)
