package de.thomasvolk.easy.core.model

case class Page(id: String, content: String, title: String)

object Page {
  def apply(id: String, content: String): Page = new Page(id, content, Option(id) match {
    case Some(str) if str.contains("/") => str.split("/").last
    case Some(str) => str
    case None => null
  })
}
