package de.thomasvolk.easy.web.actors

import akka.actor.{ReceiveTimeout, ActorRef}
import de.thomasvolk.easy.core.message._
import de.thomasvolk.easy.core.model.Page
import de.thomasvolk.easy.web.utils.PollingWriteListener


class PersistPageStreamActor(pageActor: ActorRef, writeListener: PollingWriteListener)
  extends AbstractStreamActor(writeListener) {

  def receive = {
    case pp: PersistPage =>
      pageActor ! pp
    case rp: PageFound =>
      complete(toJson(rp.page).toString())
    case PageNotFound(id) =>
      onError(new RuntimeException("saved page not found: " + id))
    case ReceiveTimeout ⇒
      onError(new RuntimeException("received timeout"))
    case _ =>
  }
}
