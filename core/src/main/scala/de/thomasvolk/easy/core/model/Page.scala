package de.thomasvolk.easy.core.model

case class Page(reference: Reference, pageContent: Content, parentPage: Option[Reference], subPages: Set[Reference]) {
  def id = reference.id
  def title = reference.title
  def content = pageContent.content
}

object Page {
  def apply(id: String, content: String): Page = {
    val title = Option(id) match {
      case Some(str) if str.contains("/") => str.split("/").last
      case Some(str) => str
      case None => null
    }
    apply(Reference(id, title), Content(id, content))
  }

  def apply(reference: Reference, content: Content): Page = new Page(reference, content, None, Set.empty)
}