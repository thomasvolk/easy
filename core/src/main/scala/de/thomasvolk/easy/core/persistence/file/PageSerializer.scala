package de.thomasvolk.easy.core.persistence.file

import de.thomasvolk.easy.core.model.Page
import org.jsoup.Jsoup

trait PageSerializer {
  def serialize(page: Page): String
  def deserialize(content: String): Page
}


class HtmlPageSerializer(htmlTemplate: String) extends PageSerializer {
  def serialize(page: Page): String = {
    val templateDocument = Jsoup.parse(htmlTemplate)
    templateDocument.head().select("title").html(page.title).attr("data-id", page.id)
    templateDocument.body().select("header h1").html(page.title)
    templateDocument.body().select("section article").html(page.content)
    templateDocument.toString
  }

  def deserialize(content: String): Page = {
    val doc = Jsoup.parse(content)
    Page(doc.head().select("title").attr("data-id"),
      doc.body().select("section article").html(),
      doc.head().select("title").html() )
  }
}
