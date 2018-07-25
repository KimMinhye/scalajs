package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.html

@JSExportTopLevel("Chap7_10")
object Chap7_10 {
  @JSExport
  def main(): Unit = {
    val n   = g.prompt("n?").asInstanceOf[String].toInt
    val p   = List()
    val max = Math.floor(Math.sqrt(n))
    val x   = 2
    while (x <= max) {}
  }
}
