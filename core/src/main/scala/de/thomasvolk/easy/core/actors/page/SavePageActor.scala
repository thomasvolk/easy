package de.thomasvolk.easy.core.actors.page

import akka.actor.Actor
import de.thomasvolk.easy.core.Logging
import de.thomasvolk.easy.core.message.{PageSaved, PersistPageContent}
import de.thomasvolk.easy.core.persistence.PagePersistenceService

class SavePageActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {

  def receive = {
    case PersistPageContent(content) =>
      pagePersistenceService.persist(content)
      sender ! PageSaved(pagePersistenceService.loadPage(content.id))
  }
}
