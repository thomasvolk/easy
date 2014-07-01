package de.thomasvolk.easy.core.actors.page

import akka.actor.Actor
import de.thomasvolk.easy.core.Logging
import de.thomasvolk.easy.core.message.{DeletePage, PageSaved, PersistPageContent}
import de.thomasvolk.easy.core.persistence.PagePersistenceService

class DeletePageActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {

  def receive = {
    case DeletePage(page) =>
      pagePersistenceService.deletePage(page)
      sender ! PageSaved(page)
  }
}
