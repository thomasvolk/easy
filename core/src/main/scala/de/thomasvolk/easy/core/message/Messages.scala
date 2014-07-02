package de.thomasvolk.easy.core.message

import de.thomasvolk.easy.core.model.Page

case class DeletePage(id: String)
case class PersistPage(page: Page)
case class FindPage(id: String)
case class PageFound(page: Page)
case class PageNotFound(id: String)

