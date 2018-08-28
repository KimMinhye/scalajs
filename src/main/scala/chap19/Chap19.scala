package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{File, FileList, FileReader, DragEvent, Event}

@JSExportTopLevel("Chap19")
object Chap19 {
  val document = g.document

  @JSExport
  def dragAndDrop(): Unit = {
    val dragBox = document.getElementById("dragbox")
    val dropBox = document.getElementById("dropbox")

    dropBox.addEventListener("dragenter", (e: DragEvent) => {
      e.target.asInstanceOf[html.Div].style.borderColor = "red"
    }, false)

    dropBox.addEventListener("dragleave", (e: DragEvent) => {
      e.target.asInstanceOf[html.Div].style.borderColor = "gray"
    }, false)

    dropBox.addEventListener("drop", (e: DragEvent) => {
      e.target.asInstanceOf[html.Div].style.borderColor = "gray"
    }, false)
  }

  @JSExport
  def dragAndDrop_1(): Unit = {
    val color   = document.getElementById("color")
    val dropBox = document.getElementById("dropbox")

    color.addEventListener("dragstart", (e: DragEvent) => {
      e.dataTransfer.setData("text/plain",
                             e.target.asInstanceOf[html.Input].value)
    }, false)

    dropBox.addEventListener("dragover", (e: DragEvent) => {
      e.preventDefault
    }, false)

    dropBox.addEventListener("drop", (e: DragEvent) => {
      e.preventDefault
      e.target.asInstanceOf[html.Div].style.backgroundColor =
        e.dataTransfer.getData("text/plain")
    }, false)
  }

  @JSExport
  def readFile(): Unit = {
    val input  = document.getElementById("input")
    val output = document.getElementById("output")
    input.addEventListener(
      "change",
      (_: dom.Event) => {
        val fs = input.files.asInstanceOf[FileList]
        if (fs.length == 0) return
        readTextFile(fs(0), (text: String) => output.innerHTML = text)
      },
      false
    )
  }

  def readTextFile(f: File, callback: String => Unit): Unit = {
    val reader = new FileReader()
    reader.addEventListener("load", (_: Event) => {
      callback(reader.result.toString)
    }, false)
    reader.addEventListener("error", (e: Event) => g.console.log(e), false)
    reader.readAsText(f)
  }
}
