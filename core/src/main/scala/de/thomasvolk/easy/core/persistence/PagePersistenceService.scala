package de.thomasvolk.easy.core.persistence

import de.thomasvolk.easy.core.model.{Content, Page}


trait PagePersistenceService {
  def loadPage(id: String): Option[Page]
  def persist(content: Content): Unit
  def deletePage(id: String): Unit
  def getSubpages(id: String): Set[Page]
  def getParentPage(id: String): Option[Page]
}
