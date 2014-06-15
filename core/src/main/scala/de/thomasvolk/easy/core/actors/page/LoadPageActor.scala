package de.thomasvolk.easy.core.actors.page

import akka.actor.Actor
import de.thomasvolk.easy.core.Logging
import de.thomasvolk.easy.core.message.{FindPage, PageFound, PageNotFound}
import de.thomasvolk.easy.core.persistence.PagePersistenceService

class LoadPageActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {

  def receive = {
    case FindPage(id) =>
      pagePersistenceService.loadPage(id) match {
        case Some(page) => sender ! PageFound(page)
        case None => sender ! PageNotFound(id)
      }

  }
}
