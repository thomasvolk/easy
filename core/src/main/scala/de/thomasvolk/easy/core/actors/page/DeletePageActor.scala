package de.thomasvolk.easy.core.actors.page

import akka.actor.Actor
import de.thomasvolk.easy.core.Logging
import de.thomasvolk.easy.core.message.{PageNotFound, DeletePage}
import de.thomasvolk.easy.core.persistence.PagePersistenceService

class DeletePageActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {

  def receive = {
    case DeletePage(id) =>
      pagePersistenceService.deletePage(id)
      sender ! PageNotFound(id)
  }
}
