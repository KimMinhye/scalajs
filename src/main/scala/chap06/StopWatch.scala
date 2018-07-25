package ScalaJSExamples

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import org.scalajs.dom
import org.scalajs.dom.html
import scala.scalajs.js
import org.scalajs.dom.raw.{Event, MouseEvent, Element, Document}
import scalatags.JsDom.all._

@JSExportTopLevel("StopWatch")
object StopWatch {
  @JSExport
  def main01(): Unit = {
    val startButton                                    = js.Dynamic.global.document.getElementById("start")
    val stopButton                                     = js.Dynamic.global.document.getElementById("stop")
    val display                                        = js.Dynamic.global.document.getElementById("display")
    var timer: js.UndefOr[js.timers.SetIntervalHandle] = js.undefined

    startButton.onclick = (_: MouseEvent) => start()

    def start() {
      startButton.onclick = null
      stopButton.onclick = (_: MouseEvent) => stop()
      val startTime = js.Date.now()
      timer = js.timers.setInterval(10)(run(startTime))
    }

    def run(start: Double) = {
      val now = js.Date.now()
      display.innerHTML = "%.2f".format((now - start) / 1000)
    }

    def stop() = {
      startButton.onclick = (_: MouseEvent) => start()
      timer foreach js.timers.clearInterval
    }
  }

  @JSExport
  def main02(target: html.Div): Unit = {
    val startButton                                    = input(`type` := "button", value := "start").render
    val stopButton                                     = input(`type` := "button", value := "stop").render
    val display                                        = p("0.00").render
    var timer: js.UndefOr[js.timers.SetIntervalHandle] = js.undefined

    target.appendChild(div(display, startButton, stopButton).render)

    startButton.onclick = _ => start()

    def start() {
      startButton.onclick = null
      stopButton.onclick = _ => stop()
      val startTime = js.Date.now()
      timer = js.timers.setInterval(10)(run(startTime))
    }

    def run(start: Double) = {
      val now = js.Date.now()
      display.innerHTML = "%.2f".format((now - start) / 1000)
    }

    def stop() = {
      startButton.onclick = _ => start()
      timer foreach js.timers.clearInterval
    }
  }
}
