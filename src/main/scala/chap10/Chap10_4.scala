package scalaJSExample

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.{html, document}

@JSExportTopLevel("Chap10_4")
object Chap10_4 {
  @JSExport
  def main(body: html.Body) {
    val TIME_INTERVAL                                  = 1500
    val DISPLAY_TIME                                   = 1050
    val molesState                                     = makeMoles(7, 4)
    val timer                                          = js.timers.setInterval(TIME_INTERVAL)(appearMole(molesState))
    var timer: js.UndefOr[js.timers.SetIntervalHandle] = js.undefined
    val element                                        = document.createElement("div").asInstanceOf[html.Div]

    def makeMoles(nx: Int, ny: Int): Set[html.Div] = {
      val molesState = Set[html.Div]()
      for (i <- 0 to nx; j <- 0 to ny) {
        molesState + makeMole(i, j)
      }
      return molesState
    }

    def makeMole(i: Int, j: Int): html.Div = {
      val W     = 50
      val SPACE = 10
      element.style.width = W + "px"
      element.style.height = W + "px"
      element.style.background = "url(./image/mole.jpg)"
      element.style.position = "absolute"
      element.style.opacity = 0.2.toString
      element.style.transition = "transform 0.5s ease-in-out, opacity 0.5s ease"
      body.appendChild(element)

      element.style.left = SPACE + i * (W + SPACE) + "px"
      element.style.top = 2 * SPACE + j * (W + SPACE) + "px"
      return element
    }

    def moleClickEvent(e: html.Dib): Unit = {
    }

    def appearMole(molesState: Set[html.Div]): Unit = {
      val n = Math.floor(Math.random() * molesState.size)
      if (molesState.size == 0) {
        js.timers.clearInterval(timer)
        body.innerHtml = "game over"
      }
      else {}
    }

    def hideMole(element: html.Div): Unit = {
      element.style.opacity = 0.2.toString
      element.style.transform = "translateY(0)"
    }
  }
}
