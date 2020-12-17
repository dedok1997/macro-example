package com.github.dedok1997

object LensesTest extends App {

  case class A(x: Int, y: Int)
  case class B(x: Int, y: A, z: A)
  val b = B(1, A(2, 3), A(4, 5))
  require(Lenses.lens(b)(_.x)(_ + 4) == b.copy(x = b.x + 4))
  require(Lenses.lens(b)(_.y.x)(_ + 4) == b.copy(y = b.y.copy(x = b.y.x + 4)))



}
