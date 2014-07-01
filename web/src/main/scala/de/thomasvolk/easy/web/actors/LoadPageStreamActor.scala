package de.thomasvolk.easy.web.actors

import akka.actor.{ReceiveTimeout, ActorRef}
import de.thomasvolk.easy.core.message._
import de.thomasvolk.easy.core.model.Page
import de.thomasvolk.easy.web.utils.PollingWriteListener


class LoadPageStreamActor(pageActor: ActorRef, writeListener: PollingWriteListener)
  extends AbstractStreamActor(writeListener) {

  def receive = {
    case fp: FindPage =>
      pageActor ! fp
    case rp: PageFound =>
      complete(toJson(rp.page).toString())
    case PageNotFound(id) =>
      complete(toJson(Page(id, "Edit new content here ...")).toString())
    case ReceiveTimeout ⇒
      onError(new RuntimeException("received timeout"))
    case _ =>
  }
}
