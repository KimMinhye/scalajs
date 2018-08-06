package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.{html, document}

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
      document.getElementsByTagName("div").asInstanceOf[js.Array[js.Dynamic]]
    elements(2).innerHTML = "여기를 수정함"
  }
}
