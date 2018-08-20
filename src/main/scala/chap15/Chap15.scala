package scalaJSExample

import scala.util.{Success}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.timers._
import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{Event, MouseEvent, KeyboardEvent}
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

  @JSExport
  def showKey(): Unit = {
    val display = document.getElementById("display")
    val showKey = (e: KeyboardEvent) => {
      val stringBuilder = StringBuilder.newBuilder
      stringBuilder.append("<br> altKey : " + e.altKey)
      stringBuilder.append("<br> ctrlKey : " + e.ctrlKey)
      stringBuilder.append("<br> shiftKey : " + e.shiftKey)
      stringBuilder.append("<br> metaKey : " + e.metaKey)
      stringBuilder.append("<br> key : " + e.key)
      stringBuilder.append(
        "<br> keyCode : " + e.keyCode + " -> " + e.keyCode.toChar)
      display.innerHTML = stringBuilder.toString
    }
    document.addEventListener("keydown", showKey, false)
  }

  @JSExport
  def eventStep(): Unit = {
    val outer  = document.getElementById("outer")
    val inner2 = document.getElementById("inner2")
    outer.addEventListener("click",
                           (_: MouseEvent) => console.log("outer bubbling"),
                           false)
    outer.addEventListener("click",
                           (_: MouseEvent) => console.log("outer capturing"),
                           true)
    inner2.addEventListener("click",
                            (_: MouseEvent) => console.log("inner2 bubbling"),
                            false)
  }

  @JSExport
  def stopPropagation(): Unit = {
    val outer  = document.getElementById("outer")
    val inner2 = document.getElementById("inner2")
    outer.addEventListener("click",
                           (_: MouseEvent) => console.log("outer bubbling"),
                           false)
    outer.addEventListener("click",
                           (_: MouseEvent) => console.log("outer capturing"),
                           true)
    inner2.addEventListener("click", (e: MouseEvent) => {
      console.log("inner2 (1)")
      e.stopPropagation
    }, false)
    inner2.addEventListener("click", (e: MouseEvent) => {
      console.log("inner2 (2)")
    }, false)

  }

  @JSExport
  def thisListener(): Unit = {
    val person = Person("Tom")
    val button = document.getElementById("button")
    button.addEventListener("click", (_: MouseEvent) => person.sayHello, false)
  }

  @JSExport
  def promise(): Unit = {
    Future {
      setTimeout(1000)((_: html.Object) => g.console.log("A"))
    } andThen {
      case Success(_) => g.console.log("B")
    }
  }

  case class Person(name: String) extends SayHello {
    override def sayHello(): Unit = {
      console.log("Hello! " + name)
    }
  }

  trait SayHello {
    def sayHello(): Unit
  }
}
