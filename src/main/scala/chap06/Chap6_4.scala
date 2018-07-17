package scalaJSExample

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js.timers._
import org.scalajs.dom
import org.scalajs.dom.{document, html}
import org.scalajs.dom.raw.{MouseEvent}
import scalatags.JsDom.all._

@JSExportTopLevel("Chap6_4")
object Chap6_4 {
  @JSExport
  def main(target: html.Div): Unit = {
    val height = input(`type` := "number").render
    val weight = input(`type` := "number").render
    val bmi = label(0.0).render
    val button = input(`type` := "button", value := "계산").render

    target.appendChild(
      div(
        p("키:", height, "m"),
        p("몸무게:", weight, "kg"),
        p("당신의 체질량 지수는 ", bmi, "입니다."),
        button
      ).render
    )

    button.onclick = _ => {
      val h = height.value.toFloat
      val w = weight.value.toFloat
      bmi.innerHTML = "%.1f".format(w / h / h)
    }
  }
}
