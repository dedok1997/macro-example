package com.github.dedok1997

import scala.annotation.{StaticAnnotation, tailrec}
import scala.reflect.macros.blackbox
import scala.language.experimental.macros

object Lenses {

  def lens[A, B](a: A)(path: A => B): (B => B) => A = macro lensImpl[A, B]




  def lensImpl[A, B](c: blackbox.Context)(a: c.Expr[A])(path: c.Expr[A => B]): c.Tree = {
    import c.universe._

    @tailrec def splitPath(p: c.Tree, acc: List[c.TermName] = List.empty): List[c.TermName] = p match {
      // . is right associative
      case q"$prefix.$last" =>
        splitPath(prefix, last :: acc)
      case x: Ident => acc
    }

    def generateCopy(obj: c.Tree, tail: List[c.TermName], transformation: TermName): c.Tree = tail match {
      case Nil => q"$transformation($obj)"
      case x :: xs => q"$obj.copy($x = ${generateCopy(q"$obj.$x", xs, transformation)})"
    }

    path.tree match {
      case q"($x) => $field" =>
        val transformationFunc = TermName(c.freshName())
        val transformationFuncParam = q"val $transformationFunc = ${q""}"
        val result = generateCopy(a.tree, splitPath(field), transformationFunc)
        q"{$transformationFuncParam => $result}"

      case _ => c.abort(c.enclosingPosition, "Use _.x.y.z... template")
    }
  }

}
