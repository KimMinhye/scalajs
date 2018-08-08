package scalaJSExample

import scala.scalajs.js
import js.Dynamic.{global => g}

object GetScroll {
  val window   = g.window
  val document = g.document

  def getScrollTop(): Double = {
    val pageYOffset: js.UndefOr[js.Dynamic] = window.pageYOffset
    val elementScrollTop: js.UndefOr[js.Dynamic] =
      document.documentElement.scrollTop
    val bodyScrollTop: js.UndefOr[js.Dynamic] = document.body.scrollTop

    js.Math.max(
      pageYOffset.fold(0.0)(_.asInstanceOf[Double]),
      elementScrollTop.fold(0.0)(_.asInstanceOf[Double]),
      bodyScrollTop.fold(0.0)(_.asInstanceOf[Double])
      )
  }

  def getScrollLeft(): Double = {
    val pageXOffset: js.UndefOr[js.Dynamic] = window.pageXOffset
    val elementScrollLeft: js.UndefOr[js.Dynamic] =
      document.documentElement.scrollLeft
    val bodyScrollLeft: js.UndefOr[js.Dynamic] = document.body.scrollLeft

    js.Math.max(
      pageXOffset.fold(0.0)(_.asInstanceOf[Double]),
      elementScrollLeft.fold(0.0)(_.asInstanceOf[Double]),
      bodyScrollLeft.fold(0.0)(_.asInstanceOf[Double])
      )
  }
}
