package de.thomasvolk.easy.core.persistence.file

import scala.collection.JavaConversions._

import de.thomasvolk.easy.core.model.{Page}
import org.jsoup.Jsoup

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
      templateDocument.body().select("nav ul").html(s"<li class='parent'><a href='${parent._1}'>${parent._2}</a></li>")
    }
    page.subPages.foreach { sub =>
      templateDocument.body().select("nav ul").html(s"<li><a href='${sub._1}'>${sub._2}</a></li>")
    }
    templateDocument.toString
  }

  def deserialize(content: String): Page = {
    val doc = Jsoup.parse(content)
    val id = doc.head().select("title").attr("data-easy-page-id")
    val title = doc.head().select("title").html()
    val article =  doc.body().select("section article").html()
    val parentPage = doc.body().select("nav ul li").find(_.hasClass("parent")).collect {
      case e => (e.select("a").attr("href"), e.select("a").text()) }
    val subPages = doc.body().select("nav ul li").filter(!_.hasClass("parent")).map { e =>
      (e.select("a").attr("href"), e.select("a").text())
    }
    Page(id, title, article, parentPage, subPages)
  }
}
