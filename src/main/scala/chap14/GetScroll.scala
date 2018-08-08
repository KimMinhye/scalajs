package scalaJSExample

import scala.scalajs.js
import js.Dynamic.{global => g}

object GetScroll {
  val window = g.window
  val document = g.document
  val pageYOffset = window.pageYOffset
}
