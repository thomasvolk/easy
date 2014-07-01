package de.thomasvolk.easy.core.message

import de.thomasvolk.easy.core.model.Page
import de.thomasvolk.easy.core.model.Content

case class DeletePage(id: String)
case class PersistPageContent(content: Content)
case class FindPage(id: String)
case class PageFound(page: Page)
case class PageNotFound(id: String)
case class PageSaved(page: Page)
case class PageDeleted(id: String)

