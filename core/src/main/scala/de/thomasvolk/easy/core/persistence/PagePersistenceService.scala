package de.thomasvolk.easy.core.persistence

import de.thomasvolk.easy.core.model.Page


trait PagePersistenceService {
  def loadPage(id: String): Option[Page]
  def persist(page: Page): Page
  def deletePage(id: String): Unit
  def getSubpages(id: String): Seq[Page]
  def getParentPage(id: String): Option[Page]
}
