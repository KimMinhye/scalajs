package scalaJSExample

import scala.scalajs.js.annotation._
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.{html, document}
import scala.annotation.tailrec

@JSExportTopLevel("Chap7_8")
object Chap7_8 {
  @JSExport
  def main(): Unit = {
    val EPSILON = 1.0e-10
    val a       = g.prompt("정수를 입력하십시오").asInstanceOf[String].toFloat
    val xinit   = if (a > 1) a else 1.0

    document.write("sqrt(" + a + ") = " + newtonRaphson(xinit))

    @tailrec
    def newtonRaphson(xold: Double): Double = {
      val xnew = xold - (xold * xold - a) / (2.0 * xold)
      val d    = (xold - xnew) / xold
      if (d < EPSILON) xnew
      else newtonRaphson(xnew)
    }
  }
}
