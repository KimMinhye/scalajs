package scalajsExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom.html
import org.scalajs.dom.raw.{XMLHttpRequest, Event}
import scalatags.JsDom.all._

@JSExportTopLevel("LifeGame")
object LifeGame {
  @JSExport
  def main(): Unit = {
    readFile("../chap17/patterns.json", (jsonObj: Any, error: Boolean) => {
      g.console.log(error)
    })
    createLifeGame(g.document.body.asInstanceOf[html.Body], 78, 60, 780, 600)
  }

  def readFile(fileName: String,
               callback: (Any, Boolean) => js.Dynamic): Unit = {
    val req = new XMLHttpRequest()
    req.addEventListener("load", (_: Event) => {
      callback(req.response, false)
    }, false)
    req.addEventListener("error", (_: Event) => {
      callback(null, true)
    }, false)
    req.open("GET", fileName)
    req.responseType = "json"
    req.send()
  }

  def createLifeGame(parent: html.Body,
                     nx: Int,
                     ny: Int,
                     width: Int,
                     height: Int) {
    val title = h1(`class` := "title", "Life Game").render
  }
}

case class State(nx: Int, ny: Int) {}

case class View(nx: Int, ny: Int, w: Int, h: Int) {
  val rayer0          = canvas(id := "rayer0", width := w, height := h).render
  val rayer1          = canvas(id := "rayer1", width := w, height := h).render
  val generation      = span(id := "generation").render
  val statuspanel     = div(`class` := "status", "세대 : ", generation).render
  val cellWidth: Int  = rayer0.width / nx
  val cellHeight: Int = rayer0.height / ny
  val markRadius      = Math.min(cellWidth, cellHeight) / 2.5 + 0.5
}
