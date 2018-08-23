package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{Event, MouseEvent, KeyboardEvent, ImageData}
import scalatags.JsDom.all._

@JSExportTopLevel("Paint")
object Paint {
  type Ctx2D = dom.CanvasRenderingContext2D
  val document = g.document

  @JSExport
  def createPainter(parent: html.Body, width: Int, height: Int): Unit = {
    val title         = h2("Simple Painter").render
    val (canvas, ctx) = createCanvas(width, height)
    val toolbar       = div(fontSize := "small", marginBottom := "3px").render
    toolbar.appendChild(Paint())
    toolbar.appendChild(Color(ctx))
    toolbar.appendChild(BrushSize(ctx))
    toolbar.appendChild(Alpha(ctx))
    parent.appendChild(div(title, toolbar, canvas).render)
  }

  def createCanvas(canvasWidth: Int,
                   canvasHeight: Int): (html.Canvas, Ctx2D) = {
    val can = canvas(id := "canvas",
                     // width := canvasWidth,
                     // height := canvasHeight,
                     border := "1px solid gray",
                     cursor := "pointer").render
    can.width = canvasWidth
    can.height = canvasHeight
    val ctx = can.getContext("2d").asInstanceOf[Ctx2D]

    can.addEventListener(
      "mousedown",
      (e: MouseEvent) => {
        val event = document.createEvent("HTMLEvents").asInstanceOf[Event]
        event.initEvent("change", false, true)
        Color.colorInput.dispatchEvent(event)
        Paint.paintTool match {
          case "Brush"      => Brush.draw(e, ctx)
          case "Line"       => Line.draw(e, ctx)
          case "Circle"     => Circle.draw(e, ctx)
          case "CircleFill" => CircleFill.draw(e, ctx)
        }
        g.console.log(ctx)
      },
      false
    )
    (can, ctx)
  }

  def relativePosition(event: MouseEvent,
                       element: html.Element): (Double, Double) = {
    val rect = element.getBoundingClientRect()
    (Math.floor(event.clientX - rect.left),
     Math.floor(event.clientY - rect.top))
  }

  sealed trait PaintTools {
    val name: String

    def draw(event: MouseEvent, ctx: Ctx2D)

    def setDragListeners(
        ctx: Ctx2D,
        img: ImageData,
        draw: js.Function1[js.Tuple2[Double, Double], Unit]): Unit = {

      def mousemoveEventListener(): js.Function1[MouseEvent, Any] =
        (e: MouseEvent) => {
          ctx.putImageData(img, 0, 0)
          draw(relativePosition(e, ctx.canvas))
        }

      val mouseMoveEvent = mousemoveEventListener()

      document.addEventListener("mousemove", mouseMoveEvent, false)

      lazy val mouseUpEvent: js.Function1[MouseEvent, Unit] =
        (e: MouseEvent) => {
          ctx.putImageData(img, 0, 0)
          draw(relativePosition(e, ctx.canvas))

          document.removeEventListener("mousemove", mouseMoveEvent, false)
          document.removeEventListener("mouseup", mouseUpEvent, false)
        }

      document.addEventListener("mouseup", mouseUpEvent, false)
    }
  }

  case object Brush extends PaintTools {
    override val name = "Brush"

    override def draw(e: MouseEvent, ctx: Ctx2D) {
      val img    = ctx.getImageData(0, 0, ctx.canvas.width, ctx.canvas.height)
      val (x, y) = relativePosition(e, ctx.canvas)
      ctx.lineCap = "round"
      ctx.lineJoin = " round"
      ctx.beginPath()
      ctx.moveTo(x, y)
      setDragListeners(ctx, img, q => {
        g.console.log(s"x = ${x} y = ${y} qx = ${q._1}, qy = ${q._2}")
        ctx.lineTo(q._1, q._2)
        ctx.stroke
      })
    }
  }
  case object Line extends PaintTools {
    override val name = "Line"

    override def draw(e: MouseEvent, ctx: Ctx2D) {
      val img    = ctx.getImageData(0, 0, ctx.canvas.width, ctx.canvas.height)
      val (x, y) = relativePosition(e, ctx.canvas)
      ctx.lineCap = "round"

      setDragListeners(ctx, img, q => {
        g.console.log(q._1 + " , " + q._2)
        ctx.beginPath()
        ctx.moveTo(x, y)
        ctx.lineTo(q._1, q._2)
        ctx.stroke
      })
    }
  }

  case object Circle extends PaintTools {
    override val name = "Circle"

    override def draw(e: MouseEvent, ctx: Ctx2D) {
      val img    = ctx.getImageData(0, 0, ctx.canvas.width, ctx.canvas.height)
      val (x, y) = relativePosition(e, ctx.canvas)
      setDragListeners(ctx, img, q => {
        val dx = q._1 - x
        val dy = q._2 - y
        val r  = Math.sqrt(dx * dx + dy * dy)
        ctx.beginPath()
        ctx.arc(x, y, r, 0, 2 * Math.PI, false)
        ctx.stroke
      })
    }
  }

  case object CircleFill extends PaintTools {
    override val name = "CircleFill"

    override def draw(e: MouseEvent, ctx: Ctx2D) {
      val img    = ctx.getImageData(0, 0, ctx.canvas.width, ctx.canvas.height)
      val (x, y) = relativePosition(e, ctx.canvas)
      setDragListeners(ctx, img, q => {
        val dx = q._1 - x
        val dy = q._2 - y
        val r  = Math.sqrt(dx * dx + dy * dy)
        ctx.beginPath()
        ctx.arc(x, y, r, 0, 2 * Math.PI, false)
        ctx.fill
      })
    }
  }

  sealed trait Controls

  case object Paint extends Controls {
    val DEFAULT_TOOL = 0
    val slt          = select().render

    slt.appendChild(option(value := Brush.name, Brush.name).render)
    slt.appendChild(option(value := Line.name, Line.name).render)
    slt.appendChild(option(value := Circle.name, Circle.name).render)
    slt.appendChild(option(value := CircleFill.name, CircleFill.name).render)
    slt.selectedIndex = DEFAULT_TOOL
    var paintTool: String = slt.value
    slt.addEventListener("change", (e: Event) => {
      paintTool = slt.value
    }, false)

    def apply(): html.Label = {
      label("그리기 도구 : ", slt).render
    }
  }

  case object Color extends Controls {
    val colorInput = input(`type` := "color").render

    def apply(ctx: Ctx2D): html.Label = {
      val lb = label("색 : ", colorInput).render
      colorInput.addEventListener("change", (_: Event) => {
        ctx.strokeStyle = colorInput.value
        ctx.fillStyle = colorInput.value
      }, false)
      lb
    }
  }

  case object BrushSize extends Controls {
    def apply(ctx: Ctx2D): html.Label = {
      val size = Seq(1, 2, 3, 4, 5, 6, 8, 10, 12, 14, 16, 20, 24, 28)
      val slt  = select().render
      size.foreach(i =>
        slt.appendChild(option(value := i.toString, i.toString).render))
      slt.selectedIndex = 2
      ctx.lineWidth = slt.value.toDouble
      slt.addEventListener("change", (_: Event) => {
        ctx.lineWidth = slt.value.toDouble
      }, false)
      val lb = label("선의 너비 : ", slt).render
      lb
    }
  }

  case object Alpha extends Controls {
    def apply(ctx: Ctx2D): html.Label = {
      val ip = input(`type` := "number",
                     min := "0",
                     max := "1",
                     step := "0.05",
                     value := 1).render
      val lb = label("투명도 : ", ip).render

      ip.addEventListener("change", (_: Event) => {
        ctx.globalAlpha = ip.value.toDouble
      }, false)
      lb
    }
  }
}
