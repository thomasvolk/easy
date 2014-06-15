package de.thomasvolk.easy.core.actors.page

import akka.actor.Actor
import de.thomasvolk.easy.core.Logging
import de.thomasvolk.easy.core.message._
import de.thomasvolk.easy.core.persistence.PagePersistenceService

class ParentPageActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {
  def receive = {
    case FindParentPage(id) =>
      pagePersistenceService.getParentPage(id) match {
        case Some(page) => sender ! ParentPageFound(page)
        case None => sender ! ParentPageNotFound(id)
      }
  }
}
