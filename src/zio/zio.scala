package quarantine

import zio._

object zioBoxing {
  implicit object zio extends Box {
    type Wrap[+E <: Throwable, +T] = IO[E, T]
    def fail[E <: Throwable](error: => E): IO[E, Nothing] = IO.fail(error)
    def succeed[T](value: => T): IO[Nothing, T] = IO.succeed(value)
  }
}
