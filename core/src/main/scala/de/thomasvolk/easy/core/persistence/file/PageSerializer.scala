package de.thomasvolk.easy.core.persistence.file

import scala.collection.JavaConversions._

import de.thomasvolk.easy.core.model.{Page}
import org.jsoup.Jsoup

import scala.io.Source

trait PageSerializer {
  def serialize(page: Page): String
  def deserialize(content: String): Page
}


class HtmlPageSerializer(htmlTemplate: String) extends PageSerializer {
  def serialize(page: Page): String = {
    val templateDocument = Jsoup.parse(htmlTemplate)
    templateDocument.head().select("title").html(page.title).attr("data-easy-page-id", page.id)
    templateDocument.body().select("header h1").html(page.title)
    templateDocument.body().select("section article").html(page.content)
    if(page.parentPage.isDefined) {
      val parent = page.parentPage.get
      val href = "../" + HtmlPageSerializer.getFilename(
        parent._1.drop(parent._1.lastIndexOf("/") + 1)).dropWhile( c => c == '/')
      templateDocument.body().select("nav ul").append(
        s"<li class='parent'><a data-easy-page-id='${parent._1}' href='${href}'>${parent._2}</a></li>")
    }
    page.subPages.foreach { sub =>
      val href = page.name + "/" + HtmlPageSerializer.getFilename(sub._1.drop(page.id.size)).dropWhile( c => c == '/')
      templateDocument.body().select("nav ul").append(
        s"<li><a data-easy-page-id='${sub._1}' href='${href}'>${sub._2}</a></li>")
    }
    templateDocument.toString
  }

  def deserialize(content: String): Page = {
    val doc = Jsoup.parse(content)
    val id = doc.head().select("title").attr("data-easy-page-id")
    val title = doc.head().select("title").html()
    val article =  doc.body().select("section article").html()
    val parentPage = doc.body().select("nav ul li").find(_.hasClass("parent")).collect {
      case e => (e.select("a").attr("data-easy-page-id"), e.select("a").text()) }
    val subPages = doc.body().select("nav ul li").filter(!_.hasClass("parent")).map { e =>
      (e.select("a").attr("data-easy-page-id"), e.select("a").text())
    }
    Page(id, title, article, parentPage, subPages)
  }
}

object HtmlPageSerializer {
  def apply(): HtmlPageSerializer = {
    new HtmlPageSerializer(Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("HtmlPageTemplate.html")).mkString)
  }
  def getFilename(id: String): String = {
    id + ".html"
  }
}