package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{Event, MouseEvent, Element, Attr}
import scalatags.JsDom.all._

@JSExportTopLevel("Chap14")
object Chap14 {
  val document = g.document

  @JSExport
  def getElementById(): Unit = {
    val element = document.getElementById("second")
    element.innerHTML = "여기를 수정함"
  }

  @JSExport
  def getElementsByTagName(): Unit = {
    val elements =
      document.getElementsByTagName("div").asInstanceOf[js.Array[html.Div]]
    elements(2).innerHTML = "여기를 수정함"
  }

  @JSExport
  def getFirstDivParas(): Unit = {
    val elements =
      document.getElementsByTagName("div").asInstanceOf[js.Array[html.Div]]
    val firstDivParas = elements(0).getElementsByTagName("p")
    g.console.log(firstDivParas)
  }

  @JSExport
  def getElementsByClassName(): Unit = {
    val cats =
      document.getElementsByClassName("cat").asInstanceOf[js.Array[html.Div]]
    for (i <- 0 to cats.length) {
      g.console.log(i + "번 째 고양이 : " + cats(i).innerHTML)
    }
  }

  @JSExport
  def getFirstParaImportants(): Unit = {
    val paras =
      document.getElementsByTagName("p").asInstanceOf[js.Array[html.Div]]
    val firstParaImportants = paras(0).getElementsByClassName("important")
    g.console.log(firstParaImportants)
  }

  @JSExport
  def getElementsByName(): Unit = {
    val dogs =
      document.getElementsByName("dog").asInstanceOf[js.Array[html.Input]]
    dogs(1).value = "corgi"
    dogs(1).nextElementSibling.innerHTML = "웰시 코기"
    for (i <- 0 to dogs.length - 1) {
      g.console.log(i + " 번째 값 : " + dogs(i).value)
    }
  }

  @JSExport
  def getAttribute(): Unit = {
    val fm   = document.getElementById("favorite")
    val list = fm.children.asInstanceOf[js.Array[html.Object]]
    val result = list
      .filter(o => o.nodeName == "INPUT" && o.`type` == "checkbox")
      .map(o => o.getAttribute("value"))
    g.console.log(result.join(","))
  }

  @JSExport
  def setAttribute(): Unit = {
    val anchor = document.getElementById("school")
    anchor.setAttribute("href", "http://www.gilbut.co.kr/")
    g.console.log(anchor)
  }

  @JSExport
  def attributes(): Unit = {
    val para = document.getElementById("controls")
    val list =
      para.firstElementChild.attributes.asInstanceOf[js.Array[Attr]]
    list.foreach(o => g.console.log(o.name + " : " + o.value))
  }

  @JSExport
  def getTextContent(): Unit = {
    val para = document.getElementById("cards")
    g.console.log(para.textContent)
  }

  @JSExport
  def createElement(): Unit = {
    val doglist = document.getElementById("doglist")
    val element = document.createElement("li")
    element.textContent = "불독"
    doglist.appendChild(element)
  }

  @JSExport
  def insertBefore(): Unit = {
    val doglist = document.getElementById("doglist")
    val element = document.createElement("li")
    element.textContent = "불독"
    doglist.insertBefore(element, doglist.children(1))
  }

  @JSExport
  def scrollTo(): Unit = {
    if (g.history.asInstanceOf[js.Object].hasOwnProperty("scrollRestoration"))
      g.history.scrollRestoration = "manual"

    val element = document.getElementById("sec3")
    val rect    = element.getBoundingClientRect()
    g.scrollTo(rect.left.asInstanceOf[Double] + GetScroll.getScrollLeft(),
               rect.top.asInstanceOf[Double] + GetScroll.getScrollTop())
  }

  @JSExport
  def htmlForm(): Unit = {
    val inputs =
      document.forms.form1.elements.sex.asInstanceOf[js.Array[html.Input]]
    for (i <- 0 to inputs.length - 1) {
      g.console.log(inputs(i).value)
    }
  }

  @JSExport
  def backgroundColor(): Unit = {
    val element = document.getElementById("title")
    element.onclick = (_: MouseEvent) => {
      element.style.backgroundColor = "pink"
    }
  }

  @JSExport
  def iconEditor(nx: Int, ny: Int): Unit = {
    val color                  = input(`type` := "color").render
    val clear                  = input(`type` := "button", value := "모두삭제").render
    lazy val (tableSet, cells) = makeTable

    def makeTable: (html.Table, Seq[html.TableCell]) = {
      val (rows, cells) =
        Seq
          .fill(ny)(Seq.fill(nx)(makeCell))
          .foldRight((Seq[html.TableRow](), Seq[html.TableCell]())) {
            (x, acc) =>
              (tr(x).render +: acc._1, x ++ acc._2)
          }
      (table(borderCollapse := "collapse", marginTop := "5px", rows).render,
       cells)
    }

    def makeCell: html.TableCell = {
      val cell =
        td(width := "15px", height := "15px", border := "1px solid gray").render
      cell.onclick = (e: MouseEvent) => {
        e.target.asInstanceOf[html.TableCell].style.backgroundColor =
          color.value
      }
      cell
    }

    clear.onclick = (_: MouseEvent) => {
      cells.foreach(_.style.backgroundColor = "white")
    }

    document.body.appendChild(color)
    document.body.appendChild(clear)
    document.body.appendChild(tableSet)
  }
}
