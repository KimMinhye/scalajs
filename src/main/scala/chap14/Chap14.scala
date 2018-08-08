package scalaJSExample

import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.dom
import org.scalajs.dom.{html, document, DOMList}
import org.scalajs.dom.raw.{HTMLCollection, Attr}

@JSExportTopLevel("Chap14")
object Chap14 {
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
}
