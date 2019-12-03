package quarantine

import language.higherKinds

import scala.concurrent._
import scala.util._, control._
import scala.annotation.unchecked.{uncheckedVariance => uv}

sealed trait !!!
sealed trait ![E <: Throwable] extends !!!

sealed trait Result[-E <: !!!, +T] extends Product with Serializable
case class Answer[T](value: T) extends Result[!!!, T]
case class Failed[E <: Throwable](error: E) extends Result[![E], Nothing]

object boxing {
  implicit object id extends Box {
    type Wrap[+E <: Throwable, +T] = T
    def fail[E <: Throwable](error: => E): Wrap[E, Nothing] = throw error
    def succeed[T](value: => T): Wrap[Nothing, T] = value
  }

  implicit object option extends Box {
    type Wrap[+E <: Throwable, +T] = Option[T]
    def fail[E <: Throwable](error: => E): Wrap[E, Nothing] = None
    def succeed[T](value: => T): Wrap[Nothing, T] = Some(value)
  }
  
  implicit object either extends Box {
    type Wrap[+E <: Throwable, +T] = Either[E, T]
    def fail[E <: Throwable](error: => E): Wrap[E, Nothing] = Left(error)
    def succeed[T](value: => T): Wrap[Nothing, T] = Right(value)
  }
 
  implicit object result extends Box {
    type Wrap[+E <: Throwable, +T] = Result[![E @uv], T]
    def fail[E <: Throwable](error: => E): Wrap[E, Nothing] = Failed(error)
    def succeed[T](value: => T): Wrap[Nothing, T] = Answer(value)
  }
}

object Box {
  implicit object `try` extends Box {
    type Wrap[+E <: Throwable, +T] = Try[T]
    def fail[E <: Throwable](error: => E): Wrap[E, Nothing] = Failure(error)
    def succeed[T](value: => T): Wrap[Nothing, T] = Success(value)
  }

  def apply[T](fn: => T)(implicit box: Box): box.Wrap[Throwable, T] = box.nonFatal[T](fn)

  def fromTry[E <: Throwable, T](`try`: => Try[T])(implicit box: Box): box.Wrap[Throwable, T] =
    if(`try`.isSuccess) box.succeed(`try`.get) else box.fail(`try`.failed.get)

  def fromOption[E <: Throwable, T](option: => Option[T])(implicit box: Box): box.Wrap[Throwable, T] =
    option match {
      case None => box.fail(new NoSuchElementException())
      case Some(v) => box.succeed[T](v)
    }

  def fromEither[E <: Throwable, T](either: => Either[E, T])(implicit box: Box): box.Wrap[E, T] = either match {
    case Left(left) => box.fail[E](left)
    case Right(right) => box.succeed[T](right)
  }
}

trait Box {
  type Wrap[+_ <: Throwable, +_]
  def fail[E <: Throwable](error: => E): Wrap[E, Nothing]
  def succeed[T](value: => T): Wrap[Nothing, T]

  def nonFatal[T](value: => T): Wrap[Throwable, T] = try succeed[T](value) catch { case NonFatal(e) => fail(e) }

  def capture[E <: Throwable: scala.reflect.ClassTag, T](value: T) =
    try succeed[T](value) catch { case e: E => fail(e) }

  def recapture[E <: Throwable: scala.reflect.ClassTag, E2 <: Throwable, T](value: T)(fn: E => E2): Wrap[E2, T] =
    try succeed[T](value) catch { case e: E => fail(fn(e)) }
}

