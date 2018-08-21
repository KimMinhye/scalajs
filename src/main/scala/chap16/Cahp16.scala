package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import scala.scalajs.js.JSON
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{Event, XMLHttpRequest, MessageEvent}

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

  @JSExport
  def responseType(): Unit = {
    val req = new XMLHttpRequest()

    req.addEventListener("load", (_: Event) => {
      val jsonObj = req.response
      g.console.log(jsonObj)
    }, false)
    req.responseType = "json"
    req.open("GET", "../data/data.json")
    req.send()
  }

  @JSExport
  def jsonp(): Unit = {
    val url: String = "../data/jsonp.js"
    val script      = g.document.createElement("script").asInstanceOf[html.Script]
    script.setAttribute("src", url)
    // g.document.getElementsByTagName("head")(0).appendChild(script)
    dom.document.getElementsByTagName("head")(0).appendChild(script)
  }

  @JSExport
  def postMessage(): Unit = {
    val frameWindow  = g.document.getElementById("frame").contentWindow
    val message      = g.document.getElementById("message")
    val button       = g.document.getElementById("button")
    val targetOrigin = "/"
    button.addEventListener("click", (_: Event) => {
      frameWindow.postMessage(message.value, targetOrigin)
    }, false)
  }

  @JSExport
  def otherDomain(): Unit = {
    val display = g.document.getElementById("dispaly").asInstanceOf[html.Div]
    g.window.addEventListener("message", (e: MessageEvent) => {
      val sourceOrigin = g.location.origin
      if (e.origin == sourceOrigin) return
      display.textContent = e.data.toString
    }, false)
  }
}
