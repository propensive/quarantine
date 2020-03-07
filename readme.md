# Quarantine

## About

Quarantine is a small library to make it easy to handle exceptions _safely_ and _exhaustively_, in particular
when combining modules which may through different exception types.

## The problem

Methods in Java may return a value, or throw an exception. It is possible to write very concise and readable
code using Java exceptions, but only if you neglect to handle those exceptions; the `try`/`catch` handling code
is verbose and ugly.

A type such as `scala.util.Try[T]` may capture a successful result, or an `Exception`. But it does not
distinguish between different subtypes of `Exception`, so it can be difficult to know what exception types may
be contained within a `Try`, and thus need handling.

`Either[L, R]` can be used to capture successful `R` values or exceptions of type `L`. If `L` is a sealed type,
the compiler can use its exhaustivity-checking algorithm to warn if we have forgotten to match any subtypes of
`L`. But it is verbose to have to write the full type-signature (including the `L` parameter) every time.

Quarantine offers, `domain.Result[T]`, a parameterized, path-dependent type which captures succesful instances
of type `T`, and exceptions of a type, which would ideally be _sealed_, determined by a `domain`. It would be
typical to import the `domain`,
```
import domain._
```
so that the `Result[T]` type can be used without a prefix. So although the type prefix is an essential part of
the type, from which its exception type will be inferred, imports can elide it from the syntax within a
particular scope, while it would be common for a consistent prefix of `Result[T]` to be used within one such
scope.

The types `domain.Result[T]` and `domain2.Result[T]` are distinct type, so using an instance of one where the
other is expected results in a type error. This makes it impossible for a result containing an exception type
from one domain to be accidentally encapsulated in another result type whose consumers are not expecting to
handle it. Instead, it must be converted, manually or semi-automatically.

### Surprises

Exceptions can be thrown at any time on the JVM, and unless they are caught, they will bubble up through the
call stack. Apart from exceptions representing error states which are best resolved by terminating the JVM, we
usually want to capture any exceptions we were not expecting and store them in the result type.

## Transforming `Result`s

`Result[T]` provides a number of methods for transforming a result type.