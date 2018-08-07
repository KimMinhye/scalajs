package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.{html, document, DOMList}
import org.scalajs.dom.raw.HTMLCollection

@JSExportTopLevel("Chap14")
object Chap14 {
  @JSExport
  def getElementById(): Unit = {
    val element = document.getElementById("second")
    element.innerHTML = "여기를 수정함"
  }

  @JSExport
  def getElementsByTagName(): Unit = {
    val elements =
      document.getElementsByTagName("div").asInstanceOf[js.Array[html.Div]]
    elements(2).innerHTML = "여기를 수정함"
  }

  @JSExport
  def getFirstDivParas(): Unit = {
    val elements =
      document.getElementsByTagName("div").asInstanceOf[js.Array[html.Div]]
    val firstDivParas = elements(0).getElementsByTagName("p")
    g.console.log(firstDivParas)
  }

  @JSExport
  def getElementsByClassName(): Unit = {
    val cats =
      document.getElementsByClassName("cat").asInstanceOf[js.Array[html.Div]]
    for (i <- 0 to cats.length) {
      g.console.log(i + "번 째 고양이 : " + cats(i).innerHTML)
    }
  }

  @JSExport
  def getFirstParaImportants(): Unit = {
    val paras =
      document.getElementsByTagName("p").asInstanceOf[js.Array[html.Div]]
    val firstParaImportants = paras(0).getElementsByClassName("important")
    g.console.log(firstParaImportants)
  }

  @JSExport
  def getElementsByName(): Unit = {
    val dogs =
      g.document.getElementsByName("dog").asInstanceOf[HTMLCollection]
    dogs(1).asInstanceOf[html.Input].value = "corgi"
    dogs(1).nextElementSibling.innerHTML   = "웰시 코기<br>"
    for (i <- 0 to dogs.length - 1) {
      g.console.log(i + "번째의 값 : " + dogs(i).getAttribute("value"))

}
    // val dogs =
    //   document.getElementsByName("dog").asInstanceOf[js.Array[html.Input]]
    // dogs(1).value = "corgi"
    // dogs(1).nextElementSibling.innerHTML = "웰시 코기"
    // for (i <- 0 to dogs.length - 1) {
    //   g.console.log(i + " 번째 값 : " + dogs(i).value)
    // }
  }
}
