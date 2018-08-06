package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.{html, document}

@JSExportTopLevel("Chap14_2_1")
object Chap14_2_1 {
  @JSExport
  def getElementById(): Unit = {
    val element = document.getElementById("second")
    element.innerHTML = "여기를 수정함"
  }
}
