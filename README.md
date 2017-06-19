# Kotlin fantasy land

[![Fantasy Land](https://raw.githubusercontent.com/fantasyland/fantasy-land/master/logo.png)][fantasy-land]

[Kotlin][kotlin] is interesting language to play with. This is [Fantasy][fantasy-land] experiment with Kotlin infix extension functions and with a little taste of unicode names. Propositions for better symbols are very welcome. Or maybe special font if this is not _inspiring_ enough. :smile:

```kotlin
// Maybe type with Just( value ) or Nothing
fun <A> pureM (a: A) = Maybe.Just(a)
val p = pureM(42)
val q = pureM(24)
val u = pureM(84)
val plus3 = fun (a: Int) = fun (b: Int) = fun (c: Int) = a + b + c

// Maybe Applicative
val rm : Maybe<Int> = plus3 ⵙ p ⴲ q ⴲ u
                            ^        ^
                           map     apply

// List type with Cons( head, rest ) or Nil as empty list
val m = List.Cons(42, List.Cons(24, List.Nil))
val n = List...
val o = List...

// List Applicative
val rl : List<Int> = plus3 ⵙ m ⴲ n ⴲ o

// Monad laws
fun f (x: Int) = pureM(x + 42)
fun g (x: Int) = pureM(x + 24)

val associativity  = p ᐒ (fun (x) = f(x) ᐒ ::g) == p ᐒ ::f ᐒ ::g
val left_identity  = p ᐒ ::f == f(42)
val right_identity = p ᐒ ::pureM == p
                       ^
                      bind
```

Well, there is a problem. We must define operations (extension functions) on specific data type (Maybe, List). This hurts a lot, we don't have a way to specify some sort of a _contract_ for extension functions that we need to implement. Or from other end we cannot define functions or types that depend on such a _contract_.
For example `Traversable` _interface_ defined in [Idris][idris-traversable] prelude.

```idris
interface (Functor t, Foldable t) => Traversable (t : Type -> Type) where
  traverse : Applicative f => (a -> f b) -> t a -> f (t b)
```

Hm, can we use Kotlin interfaces? I was not satisfied with the first few attempts but exploration will continue. Also [Type-Safe Builders][kotlin-builders] invites to be (mis)used. ;)


[kotlin]: http://kotlinlang.org
[kotlin-builders]: https://kotlinlang.org/docs/reference/type-safe-builders.html
[fantasy-land]: https://github.com/fantasyland/fantasy-land
[idris-traversable]: https://github.com/idris-lang/Idris-dev/blob/master/libs/prelude/Prelude/Traversable.idr#L25
