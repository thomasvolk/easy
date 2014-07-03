package de.thomasvolk.easy.web.actors

import de.thomasvolk.easy.core.model.Page
import argonaut._,Argonaut._

trait JsonEncoding {
  implicit def PageToJson: EncodeJson[Page] =
    EncodeJson((p: Page) =>
      ("id" := p.id) ->: ("title" := p.title) ->: ("content" := p.content) ->:
      ("parentPage" := p.parentPage.getOrElse(("",""))) ->: ("subPages" := p.subPages.toList) ->: jEmptyObject)

  def toJson(page: Page) = page.jencode

  def toJson(pages: Set[Page]) = pages.toList.jencode

  def toJson(map: Map[String, String]) = map.jencode

  def toJson(set: List[Map[String, String]]) = set.jencode

}
