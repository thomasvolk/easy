package de.thomasvolk.easy.core.actors

import akka.actor.{Props, Actor}
import de.thomasvolk.easy.core.actors.page._

import de.thomasvolk.easy.core.persistence.PagePersistenceService
import de.thomasvolk.easy.core.message._
import de.thomasvolk.easy.core.model.Page
import de.thomasvolk.easy.core.Logging

class PageActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {
  val loadPageActor = context.actorOf(Props(new LoadPageActor(pagePersistenceService)))
  val savePageActor = context.actorOf(Props(new SavePageActor(pagePersistenceService)))
  val deletePageActor = context.actorOf(Props(new DeletePageActor(pagePersistenceService)))

  context watch(loadPageActor)
  context watch(savePageActor)
  context watch(deletePageActor)

  def receive = {
    case lp: FindPage =>
      loadPageActor.!(lp)(sender)
    case sp: PersistPage =>
     savePageActor.!(sp)(sender)
    case dp: DeletePage =>
      deletePageActor.!(dp)(sender)
  }
}
