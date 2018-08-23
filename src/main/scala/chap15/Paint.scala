package scalaJSExample

import scala.collection.mutable.LinkedHashMap
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
  val document                                               = g.document
  val controls: LinkedHashMap[String, (Ctx2D) => html.Label] = LinkedHashMap()
  val paintTools: LinkedHashMap[String, (MouseEvent, Ctx2D) => Unit] =
    LinkedHashMap()
  val colorInput        = input(`type` := "color").render
  var paintTool: String = ""

  paintTools += "Brush" -> { (e: MouseEvent, ctx: Ctx2D) =>
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

  paintTools += "Line" -> { (e: MouseEvent, ctx: Ctx2D) =>
    val img    = ctx.getImageData(0, 0, ctx.canvas.width, ctx.canvas.height)
    val (x, y) = relativePosition(e, ctx.canvas)
    ctx.lineCap = "round"

    setDragListeners(ctx, img, q => {
      ctx.beginPath()
      ctx.moveTo(x, y)
      ctx.lineTo(q._1, q._2)
      ctx.stroke
    })
  }

  paintTools += "Circle" -> { (e: MouseEvent, ctx: Ctx2D) =>
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

  paintTools += "CircleFill" -> { (e: MouseEvent, ctx: Ctx2D) =>
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

  controls += "painter" -> { (_: Ctx2D) =>
    {
      val DEFAULT_TOOL = 0
      val slt          = select().render

      paintTools foreach {
        case (k, _) => slt.appendChild(option(value := k, k).render)
      }

      slt.selectedIndex = DEFAULT_TOOL
      paintTool = slt.value

      slt.addEventListener("change", (e: Event) => {
        paintTool = slt.value
      }, false)

      label("그리기 도구 : ", slt).render
    }
  }

  controls += "color" -> { (ctx: Ctx2D) =>
    colorInput.addEventListener("change", (_: Event) => {
      ctx.strokeStyle = colorInput.value
      ctx.fillStyle = colorInput.value
    }, false)
    label("색 : ", colorInput).render
  }

  controls += "brushsize" -> { (ctx: Ctx2D) =>
    val size = Seq(1, 2, 3, 4, 5, 6, 8, 10, 12, 14, 16, 20, 24, 28)
    val slt  = select().render
    size.foreach(i =>
      slt.appendChild(option(value := i.toString, i.toString).render))
    slt.selectedIndex = 2
    ctx.lineWidth = slt.value.toDouble
    slt.addEventListener("change", (_: Event) => {
      ctx.lineWidth = slt.value.toDouble
    }, false)
    label("선의 너비 : ", slt).render
  }

  controls += "alpha" -> { (ctx: Ctx2D) =>
    val ip = input(`type` := "number",
                   min := "0",
                   max := "1",
                   step := "0.05",
                   value := 1).render

    ip.addEventListener("change", (_: Event) => {
      ctx.globalAlpha = ip.value.toDouble
    }, false)
    label("투명도 : ", ip).render
  }

  @JSExport
  def createPainter(parent: html.Body, width: Int, height: Int): Unit = {
    val title         = h2("Simple Painter").render
    val (canvas, ctx) = createCanvas(width, height)
    val toolbar       = div(fontSize := "small", marginBottom := "3px").render
    controls foreach { case (k, v) => toolbar.appendChild(v(ctx)) }
    parent.appendChild(div(title, toolbar, canvas).render)
  }

  def createCanvas(canvasWidth: Int,
                   canvasHeight: Int): (html.Canvas, Ctx2D) = {
    val can = canvas(id := "canvas",
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
        colorInput.dispatchEvent(event)
        paintTools(paintTool)(e, ctx)
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
