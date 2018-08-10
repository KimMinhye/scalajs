package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{Event, MouseEvent}
import scalatags.JsDom.all._

@JSExportTopLevel("Chap15")
object Chap15 {
  val document = g.document
  val console  = g.console

  @JSExport
  def addEventListener(): Unit = {
    val changeColor = (e: Event) =>
      e.currentTarget.asInstanceOf[html.Div].style.backgroundColor = "red"

    val element = document.getElementById("box")
    element.addEventListener("click", changeColor, false)
  }

  @JSExport
  def callee(): Unit = {
    lazy val removeEvent: Function1[Event, Any] = (e: Event) => {
      console.log("Hello")
      e.currentTarget
        .asInstanceOf[html.Button]
        .removeEventListener("click", removeEvent, false)
    }

    val element = document.getElementById("button")
    element.addEventListener("click", removeEvent, false)
  }

  @JSExport
  def mouseEvent(): Unit = {
    val box = div(display := "inline-block",
                  position := "absolute",
                  padding := "10px",
                  backgroundColor := "blue",
                  color := "white",
                  cursor := "pointer",
                  "JavaScript").render

    document.body.appendChild(box)
    box.addEventListener(
      "mousedown",
      (e: MouseEvent) => {
        val boxStyle = g.getComputedStyle(box)
        val offsetX = e.clientX - boxStyle.left
          .asInstanceOf[String]
          .dropRight(2)
          .toDouble
        val offsetY = e.clientY - boxStyle.top
          .asInstanceOf[String]
          .dropRight(2)
          .toDouble
        val listener = mouseMoveListener(offsetX, offsetY)
        document.addEventListener("mousemove", listener, false)
        document.addEventListener("mouseup", (e: Event) => {
          document.removeEventListener("mousemove", listener, false)
        }, false)
      },
      false
    )

    def mouseMoveListener(offsetX: Double,
                          offsetY: Double): js.Function1[MouseEvent, Any] =
      (e: MouseEvent) => {
        box.style.left = e.pageX - offsetX + "px"
        box.style.top = e.pageY - offsetY + "px"
      }
  }
}
