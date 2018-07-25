package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.HTMLImageElement

@JSExportTopLevel("Chap6_8")
object Chap6_8 {
  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    var img: HTMLImageElement =
      dom.document.createElement("img").asInstanceOf[HTMLImageElement]

    img.onload = _ => {
      ctx.drawImage(img, 0, 0)
    }
    img.src = "./image/cat.jpg"
  }

}
