package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.{window, html, document}
import scalatags.JsDom.all._

@JSExportTopLevel("Chap13_1")
object Chap13_1 {
  @JSExport
  def main(): Unit = {
    val openButton                    = input(`type` := "button", value := "open").render
    val closeButton                   = input(`type` := "button", value := "close").render
    var w: js.UndefOr[dom.raw.Window] = js.undefined

    document.body.appendChild(
      div(
        p(openButton, closeButton)
      ).render)

    openButton.onclick = _ => {
      w = window.open("newpage.html", "new page", "width=400, height=300")
    }

    closeButton.onclick = _ => {
      w.foreach(_.close())
    }

  }
}
