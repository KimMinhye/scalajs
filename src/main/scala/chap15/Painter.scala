package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{Event, MouseEvent, KeyboardEvent, ImageData}
import scalatags.JsDom.all._

@JSExportTopLevel("Painter")
object Painter {
  sealed trait Controls
  case object Paint extends Controls {
    def apply(): html.Label = {
      val slt = select().render
      val lb  = label("그리기 도구 : ", slt).render
      lb
    }
  }
  case object Color extends Controls {
    def apply(): html.Label = {
      val colorInput = input(`type` := "color").render
      val lb         = label("색 : ", colorInput).render
      lb
    }
  }
  case object Brushsize extends Controls {
    def apply(): html.Label = {
      val size = Seq(1, 2, 3, 4, 5, 6, 8, 10, 12, 14, 16, 20, 24, 28)
      val slt  = select().render
      size.foreach(i =>
        slt.appendChild(option(value := i.toString, i.toString).render))
      val lb = label("선의 너비 : ", slt).render
      lb
    }
  }
  case object Alpha extends Controls {
    def apply(): html.Label = {
      val ip = input(`type` := "number",
                     min := "0",
                     max := "1",
                     step := "0.05",
                     value := 1).render
      val lb = label("투명도 : ", ip).render
      lb
    }
  }

  sealed trait PaintTools {
    val name: String
    def draw(event: MouseEvent, ctx: dom.CanvasRenderingContext2D)
    def setDragListeners(ctx: dom.CanvasRenderingContext2D,
                         img: ImageData,
                         draw: Function1[Tuple2[Double, Double], Any]): Unit = {
      val mousemoveEventListener = (e: MouseEvent) => {
        ctx.putImageData(img, 0, 0)
        draw(relativePosition(e, ctx.canvas))
      }
      document.addEventListener("mousemove", mousemoveEventListener, false)
      lazy val mouseupEvent: Function1[MouseEvent, Any] = (e: MouseEvent) => {
        document.addEventListener(
          "mouseup",
          (e: MouseEvent) => {
            ctx.putImageData(img, 0, 0)
            draw(relativePosition(e, ctx.canvas))
            document.removeEventListener("mousemove",
                                         mousemoveEventListener,
                                         false)
            document.removeEventListener("mouseup", mouseupEvent, false)
          }
        )
      }
    }
  }

  case object Brush extends PaintTools {
    override val name = "Brush"
    override def draw(e: MouseEvent, ctx: dom.CanvasRenderingContext2D) {
      val img    = ctx.getImageData(0, 0, ctx.canvas.width, ctx.canvas.height)
      val (x, y) = relativePosition(e, ctx.canvas)
      ctx.lineCap = "round"
      ctx.lineJoin = " round"
      ctx.beginPath()
      ctx.moveTo(x, y)
      setDragListeners(ctx, img, q => {
        ctx.lineTo(q._1, q._2)
        ctx.stroke
      })
    }
  }
  case object Line extends PaintTools {
    override val name = "Line"
    override def draw(event: MouseEvent, ctx: dom.CanvasRenderingContext2D) {}
  }
  case object Circle extends PaintTools {
    override val name = "Circle"
    override def draw(event: MouseEvent, ctx: dom.CanvasRenderingContext2D) {}
  }
  case object CircleFill extends PaintTools {
    override val name = "CircleFill"
    override def draw(event: MouseEvent, ctx: dom.CanvasRenderingContext2D) {}
  }

  val document = g.document

  @JSExport
  def main(parent: html.Body, width: Int, height: Int): Unit = {
    val title         = h2("Simple Painter").render
    val (canvas, ctx) = createCanvas(width, height)
    val toolbar       = div(fontSize := "small", marginBottom := "3px").render
    toolbar.appendChild(Paint())
    toolbar.appendChild(Color())
    toolbar.appendChild(Brushsize())
    toolbar.appendChild(Alpha())
    parent.appendChild(div(title, toolbar, canvas).render)
  }

  def createCanvas(
      canvasWidth: Int,
      canvasHeight: Int): (html.Canvas, dom.CanvasRenderingContext2D) = {
    val can = canvas(width := canvasWidth,
                     height := canvasHeight,
                     border := "1px soled gray",
                     cursor := "pointer").render
    val ctx =
      canvas.render.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    can.addEventListener("mousedown", (e: Event) => {
      val event = document.createEvent("HTMLEvents")
      event.initEvent("change", false, true)
    }, false)
    (can, ctx)
  }

  def relativePosition(event: MouseEvent,
                       element: html.Element): (Double, Double) = {
    val rect = element.getBoundingClientRect()
    (Math.floor(event.clientX - rect.left),
     Math.floor(event.clientY - rect.top))
  }

  // def setDragListeners(ctx: dom.CanvasRenderingContext2D,
  //                      img: ImageData,
  //                      draw: Function1[Tuple2[Double, Double], Any]): Unit = {}
}
