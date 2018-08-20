package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom.{html}
import org.scalajs.dom.raw.{Event, XMLHttpRequest}

@JSExportTopLevel("Chap16")
object Chap16 {
  @JSExport
  def onreadystatechange(): Unit = {
    val req = new XMLHttpRequest()

    req.onreadystatechange = (_: Event) => {
      if (req.readyState.toInt == 4) {
        if (req.status == 200) {
          g.document.getElementById("view").innerHTML = req.responseText
        }
      }
    }
    req.open("GET", "../data/data.txt")
    req.send()
  }
}
