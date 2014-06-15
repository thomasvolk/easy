package de.thomasvolk.easy.core.message

import de.thomasvolk.easy.core.model.Page

case class DeletePage(page: Page)
case class PersistPage(page: Page)
case class FindPage(id: String)
case class PageFound(page: Page)
case class PageNotFound(id: String)
case class PageSaved(page: Page)
case class PageDeleted(page: Page)
case class FindSubPages(id: String)
case class ChildPagesFound(pages: Set[Page])
case class FindParentPage(id: String)
case class ParentPageFound(page: Page)
case class ParentPageNotFound(id: String)

