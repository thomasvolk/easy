package de.thomasvolk.easy.core.actors.page

import akka.actor.Actor
import de.thomasvolk.easy.core.Logging
import de.thomasvolk.easy.core.message.{PageNotFound, PageFound, PersistPage}
import de.thomasvolk.easy.core.persistence.PagePersistenceService

class SavePageActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {

  def receive = {
    case PersistPage(content) =>
      pagePersistenceService.persist(content)
      pagePersistenceService.loadPage(content.id) match {
        case Some(page) => sender ! PageFound(page)
        case None => sender ! PageNotFound(content.id)
      }

  }
}
