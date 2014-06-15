package de.thomasvolk.easy.core.actors.page

import akka.actor.Actor
import de.thomasvolk.easy.core.Logging
import de.thomasvolk.easy.core.message._
import de.thomasvolk.easy.core.persistence.PagePersistenceService

class SubPagesActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {
  def receive = {
    case FindSubPages(id) =>
      sender ! ChildPagesFound(pagePersistenceService.getSubpages(id))
  }
}
