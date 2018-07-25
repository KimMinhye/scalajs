package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.html

@JSExportTopLevel("Chap6_5")
object Chap6_5 {
  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    ctx.strokeRect(50, 60, 200, 100)
  }
}
