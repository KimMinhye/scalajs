package scalaJSExample

import scala.util.Random
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.{html, document}
import org.scalajs.dom.raw.{MouseEvent}

@JSExportTopLevel("Chap10_4")
object Chap10_4 {
  type Mole = html.Div
  val TIME_INTERVAL                                  = 1500
  val DISPLAY_TIME                                   = 1050
  var timer: js.UndefOr[js.timers.SetIntervalHandle] = js.undefined
  var moles                                          = Set[Mole]()

  @JSExport
  def main(): Unit = {
    makeMoles(7, 4)
    timer = js.timers.setInterval(TIME_INTERVAL)(startGame)
  }

  def makeMoles(nx: Int, ny: Int): Set[Mole] = {
    for (i <- 0 to nx - 1; j <- 0 to ny - 1) {
      val mole = makeMole(i, j)
      document.body.appendChild(mole)
      moles += mole
    }
    moles
  }

  def makeMole(i: Int, j: Int): Mole = {
    val W     = 50
    val SPACE = 10
    val mole  = document.createElement("div").asInstanceOf[Mole]

    mole.style.width = W + "px"
    mole.style.height = W + "px"
    mole.style.background = "url(../images/mole.jpg)"
    mole.style.position = "absolute"
    mole.style.opacity = 0.2.toString
    mole.style.transition = "transform 0.5s ease-in-out, opacity 0.5s ease"
    mole.style.left = SPACE + i * (W + SPACE) + "px"
    mole.style.top = 2 * SPACE + j * (W + SPACE) + "px"
    moleClickEvent(mole)
    mole
  }

  def moleClickEvent(m: Mole): Unit = {
    m.onclick = (e: MouseEvent) => {
      val mole = e.currentTarget.asInstanceOf[Mole]
      if (mole.style.opacity.toDouble > 0.5) {
        document.body.removeChild(mole)
        moles -= mole
      }
    }
  }

  def startGame: Unit = {
    if (moles.size == 0) gameOver
    else continueGame
  }

  def gameOver: Unit = {
    timer foreach js.timers.clearInterval
    document.body.innerHTML = "game over"
  }

  def continueGame: Unit = {
    val mole = moles.toList(Random.nextInt(moles.size))
    appearMole(mole)
    js.timers.setTimeout(DISPLAY_TIME)(hideMole(mole))
  }

  def appearMole(mole: Mole): Unit = {
    mole.style.opacity = 1.toString
    mole.style.transform = "translateY(-10px)"
  }

  def hideMole(mole: Mole): Unit = {
    mole.style.opacity = 0.2.toString
    mole.style.transform = "translateY(0)"
  }
}
