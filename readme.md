[<img alt="GitHub Workflow" src="https://img.shields.io/github/workflow/status/propensive/quarantine/Build/main?style=for-the-badge" height="24">](https://github.com/propensive/quarantine/actions)
[<img src="https://img.shields.io/badge/gitter-discuss-f00762?style=for-the-badge" height="24">](https://gitter.im/propensive/quarantine)
[<img src="https://img.shields.io/discord/633198088311537684?color=8899f7&label=DISCORD&style=for-the-badge" height="24">](https://discord.gg/CHCPjERybv)
[<img src="https://img.shields.io/matrix/propensive.quarantine:matrix.org?label=MATRIX&color=0dbd8b&style=for-the-badge" height="24">](https://app.element.io/#/room/#propensive.quarantine:matrix.org)
[<img src="https://img.shields.io/twitter/follow/propensive?color=%2300acee&label=TWITTER&style=for-the-badge" height="24">](https://twitter.com/propensive)
[<img src="https://img.shields.io/maven-central/v/com.propensive/quarantine-core_2.12?color=2465cd&style=for-the-badge" height="24">](https://search.maven.org/artifact/com.propensive/quarantine-core_2.12)
[<img src="https://img.shields.io/badge/vent-propensive%2Fquarantine-f05662?style=for-the-badge" height="24">](https://vent.dev)

<img src="/doc/images/github.png" valign="middle">

# Quarantine

Quarantine is a small library to make it easy to handle exceptions _safely_ and _exhaustively_, in particular when combining modules which may throw different exception types.

## Features

TBC


## Getting Started

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


## Status

Quarantine is classified as __fledgling__. Propensive defines the following five stability levels for open-source projects:

- _embryonic_: for experimental or demonstrative purposes only, without guarantee of longevity
- _fledgling_: of proven utility, seeking contributions, but liable to significant redesigns
- _maturescent_: major design decisions broady settled, seeking probatory adoption and refinement of designs
- _dependable_: production-ready, subject to controlled ongoing maintenance and enhancement; tagged as version `1.0` or later
- _adamantine_: proven, reliable and production-ready, with no further breaking changes ever anticipated

## Availability

Quarantine&rsquo;s source is available on GitHub, and may be built with [Fury](https://github.com/propensive/fury) by
cloning the layer `propensive/quarantine`.
```
fury layer clone -i propensive/quarantine
```
or imported into an existing layer with,
```
fury layer import -i propensive/quarantine
```
A binary is available on Maven Central as `com.propensive:quarantine-core_<scala-version>:0.6.0`. This may be added
to an [sbt](https://www.scala-sbt.org/) build with:
```
libraryDependencies += "com.propensive" %% "quarantine-core" % "0.6.0"
```

## Contributing

Contributors to Quarantine are welcome and encouraged. New contributors may like to look for issues marked
<a href="https://github.com/propensive/quarantine/labels/good%20first%20issue"><img alt="label: good first issue"
src="https://img.shields.io/badge/-good%20first%20issue-67b6d0.svg" valign="middle"></a>.

We suggest that all contributors read the [Contributing Guide](/contributing.md) to make the process of
contributing to Quarantine easier.

Please __do not__ contact project maintainers privately with questions, as other users cannot then benefit from
the answers.

## Author

Quarantine was designed and developed by [Jon Pretty](https://twitter.com/propensive), and commercial support and
training is available from [Propensive O&Uuml;](https://propensive.com/).



## License

Quarantine is copyright &copy; 2019-20 Jon Pretty & Propensive O&Uuml;, and is made available under the
[Apache 2.0 License](/license.md).
