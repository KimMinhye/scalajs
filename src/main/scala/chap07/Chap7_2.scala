package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import org.scalajs.dom
import org.scalajs.dom.html
import scalatags.JsDom.all._

@JSExportTopLevel("Chap7_2")
object Chap7_2 {
  @JSExport
  def main(target: html.Div): Unit = {
    val highpressure = input(`type` := "number").render
    val lowpressure  = input(`type` := "number").render
    val judgement    = label("이곳에 판정 결과가 표시됩니다.").render
    val button       = input(`type` := "button", value := "확인하기").render

    target.appendChild(
      div(
        p("수축기 혈압(최고 혈압) : ", highpressure),
        p("이완기 혈압(최저 혈압) : ", lowpressure),
        p(judgement),
        button
      ).render)

    button.onclick = _ => {
      val hp = highpressure.value.toFloat
      val lp = lowpressure.value.toFloat
      if (hp < 120 && lp < 80)
        judgement.innerHTML = "당신의 혈압은 정상입니다."
      else if (hp < 139 && lp < 89)
        judgement.innerHTML = "당신의 혈압은 다소 높습니다."
      else
        judgement.innerHTML = "당신은 고혈압입니다."
    }
  }
}
