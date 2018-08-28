package scalaJSExample

import scala.collection.mutable.LinkedHashMap
import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{
  Event,
  MouseEvent,
  KeyboardEvent,
  DragEvent,
  ImageData,
  FileList,
  FileReader,
  URL
}
import scalatags.JsDom.all._

@JSExportTopLevel("NewPainter")
object NewPainter {
  type Ctx2D = dom.CanvasRenderingContext2D
  val document                                               = g.document
  val controls: LinkedHashMap[String, (Ctx2D) => html.Label] = LinkedHashMap()
  val paintTools: LinkedHashMap[String, (MouseEvent, Ctx2D) => Unit] =
    LinkedHashMap()
  val filterTools: LinkedHashMap[String, (ImageData) => ImageData] =
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

  controls += "filter" -> { (ctx: Ctx2D) =>
    val DEFAULT_FILTER = 0
    val slt            = select().render
    slt.appendChild(option(value := "filter", "필터").render)
    filterTools.foreach {
      case (k, v) => slt.appendChild(option(value := k, k).render)
    }
    slt.selectedIndex = DEFAULT_FILTER
    slt.addEventListener(
      "change",
      (_: Event) => {
        val filterTool = slt.value
        val inputImage =
          ctx.getImageData(0, 0, ctx.canvas.width, ctx.canvas.height)
        val outputImage = filterTools(filterTool)(inputImage)
        ctx.putImageData(outputImage, 0, 0)
        slt.selectedIndex = DEFAULT_FILTER
      },
      false
    )
    label(" ", slt).render
  }

  controls += "save" -> { (ctx: Ctx2D) =>
    val ip = input(`type` := "button", value := "저장").render
    ip.addEventListener("click", (_: Event) => {
      val dataURL = ctx.canvas.toDataURL("image/jpeg")
      g.open(dataURL, "save")
    }, false)
    label(" ", ip).render
  }

  controls += "file" -> { (ctx: Ctx2D) =>
    val ip = input(`type` := "file").render
    ip.addEventListener(
      "change",
      (_: Event) => {
        val fs = ip.files.asInstanceOf[FileList]
        if (fs.length > 0) {
          val reader = new FileReader()
          // if (fs.length == 0) return
          reader.addEventListener("load", (_: Event) => {
            loadImageURL(ctx, reader.result)
          }, false)
          reader.readAsDataURL(fs(0))
        }
      },
      false
    )
    label(" ", ip).render
  }

  filterTools += "blur" -> { (inputImage: ImageData) =>
    val size = 2
    val w: List[List[Int]] =
      List.tabulate(2 * size + 1)(_ => List.tabulate(2 * size + 1)(_ => 1))
    weightedAverageFilter(inputImage, size, w, true, 0)
  }

  filterTools += "sharp" -> { (inputImage: ImageData) =>
    val w: List[List[Int]] =
      List(List(0, -1, 0), List(-1, 5, -1), List(0, -1, 0))
    weightedAverageFilter(inputImage, 1, w, false, 0)
  }

  filterTools += "emboss" -> { (inputImage: ImageData) =>
    val w: List[List[Int]] =
      List(List(-1, 0, 0), List(0, 0, 0), List(0, 0, 1))
    weightedAverageFilter(inputImage, 1, w, false, 128)
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

    can.addEventListener("dragover", (e: DragEvent) => e.preventDefault, false)

    can.addEventListener(
      "drop",
      (e: DragEvent) => {
        val files = e.dataTransfer.files
        if (files(0).`type`.substring(0, 6) == "image/") {
          loadImageURL(ctx, URL.createObjectURL(files(0)))
          e.preventDefault
        }
      }
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

  def loadImageURL(ctx: Ctx2D, url: Any): Unit = {
    val image = document.createElement("img").asInstanceOf[html.Image]
    image.addEventListener(
      "load",
      (_: Event) => {
        val width: Int        = image.width.asInstanceOf[Int]
        val height: Int       = image.height.asInstanceOf[Int]
        val canvasWidth: Int  = ctx.canvas.width
        val canvasHeight: Int = ctx.canvas.height
        val factor =
          Math.min(canvasWidth / width, canvasHeight / height)
        val wshift     = (canvasWidth - factor * width) / 2
        val hshift     = (canvasHeight - factor * height) / 2
        val savedColor = ctx.fillStyle
        ctx.fillStyle = "white"
        ctx.fillRect(0, 0, canvasWidth, canvasHeight)
        ctx.drawImage(image,
                      0,
                      0,
                      width,
                      height,
                      wshift,
                      hshift,
                      width * factor,
                      height * factor)
        ctx.fillStyle = savedColor
      },
      false
    )
    image.src = url.toString
  }

  def weightedAverageFilter(image: ImageData,
                            n: Int,
                            weight: List[List[Int]],
                            keepBrightness: Boolean,
                            offset: Int): ImageData = {
    val width  = image.width
    val height = image.height
    val outputImage =
      canvas.render
        .getContext("2d")
        .asInstanceOf[Ctx2D]
        .getImageData(0, 0, width, height)

    for (x <- 0 to width; y <- 0 to height) {
      val iR = 4 * (width * y + x)
      for (i <- 0 to 3) {
        var average   = 0
        var weightSum = 0
        for (ix <- -n to n) {
          val xp = x + ix
          if (xp >= 0 && xp < width) {
            for (iy <- -n to n) {
              val yp = y + iy
              if (yp > 0 && yp < height) {
                val w = weight(iy + n)(ix + n)
                weightSum += w
                average += w * image.data(4 * (width * yp + xp) + i)
              }
            }
          }
        }
        if (keepBrightness) average /= weightSum
        outputImage.data(iR + i) = average + offset
      }
      outputImage.data(iR + 3) = image.data(iR + 3)
    }
    outputImage
  }
}
