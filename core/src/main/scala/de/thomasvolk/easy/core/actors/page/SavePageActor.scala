package de.thomasvolk.easy.core.actors.page

import akka.actor.Actor
import de.thomasvolk.easy.core.Logging
import de.thomasvolk.easy.core.message.{PageNotFound, PageFound, PersistPage}
import de.thomasvolk.easy.core.persistence.PagePersistenceService

class SavePageActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {

  def receive = {
    case PersistPage(content) =>
      sender !  PageFound(pagePersistenceService.persist(content))
  }
}
