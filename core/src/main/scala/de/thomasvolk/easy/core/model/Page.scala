package de.thomasvolk.easy.core.model

case class Page(id: String, title: String, content: String, parentPage: Option[(String, String)], subPages: Seq[(String, String)]) {
  def ref = (id, title)
}

object Page {
  def apply(id: String, content: String): Page = {
    val title = Option(id) match {
      case Some(str) if str.contains("/") => str.split("/").last
      case Some(str) => str
      case None => null
    }
    new Page(id = id, title = title, content = content, parentPage = None, subPages = Seq.empty)
  }
}