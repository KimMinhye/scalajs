package scalaJSExample

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js.Date
import scala.scalajs.js.timers._
import org.scalajs.dom
import org.scalajs.dom.{document, html}
import org.scalajs.dom.raw.{MouseEvent}

@JSExportTopLevel("StopWatchExercise")
object StopWatchExercise {
  @JSExport
  def main(): Unit = {
    val startButton = document.getElementById("start").asInstanceOf[html.Button]
    val stopButton = document.getElementById("stop").asInstanceOf[html.Button]
    val display = document.getElementById("display")
    var timer: js.UndefOr[js.timers.SetIntervalHandle] = js.undefined

    startButton.onclick = (_: MouseEvent) => start()

    def start(): Unit = {
      startButton.onclick = null
      stopButton.onclick = (_: MouseEvent) => stop()
      val startTime = new Date().getTime
      timer = setInterval(10) {
        val now = new Date().getTime
        display.innerHTML = "%.2f".format((now - startTime) / 1000)
      }
    }

    def stop(): Unit = {
      timer foreach clearInterval
      startButton.onclick = (_: MouseEvent) => start()

    }
  }
}
