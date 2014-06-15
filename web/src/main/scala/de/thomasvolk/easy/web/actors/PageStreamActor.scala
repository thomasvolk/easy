package de.thomasvolk.easy.web.actors

import de.thomasvolk.easy.web.utils.PollingWriteListener
import de.thomasvolk.easy.core.message._
import akka.actor.{ActorRef, Actor, ReceiveTimeout}
import scala.concurrent.duration._
import de.thomasvolk.easy.core.message.FindPage
import de.thomasvolk.easy.core.message.PersistPage
import de.thomasvolk.easy.core.message.PageFound
import de.thomasvolk.easy.core.message.PageSaved
import de.thomasvolk.easy.core.model.Page

class PageStreamActor(pageActor: ActorRef, writeListener: PollingWriteListener) extends Actor with JsonEncoding {

  context.setReceiveTimeout(5 seconds)

  def receive = {
    case fp: FindPage =>
      pageActor ! fp
    case fsp: FindParentPage =>
      pageActor ! fsp
    case pp: PersistPage =>
      pageActor ! pp
    case ps: PageSaved =>
      writeListener.put("SAVED")
      writeListener.complete()
      context.stop(self)
    case rp: PageFound =>
      writeListener.put(toJson(rp.page).toString())
      writeListener.complete()
      context.stop(self)
    case rp: ParentPageFound =>
      writeListener.put(toJson(Map[String, String]("id" -> rp.page.id, "title" -> rp.page.title)).toString())
      writeListener.complete()
      context.stop(self)
    case PageNotFound(id) =>
      writeListener.put(toJson(Page(id, "Edit new content here ...")).toString())
      writeListener.complete()
      context.stop(self)
    case ParentPageNotFound(id) =>
      writeListener.put(toJson(Map[String, String]()).toString())
      writeListener.complete()
      context.stop(self)
    case fsp: FindSubPages =>
      pageActor ! fsp
    case ChildPagesFound(pages) =>
      writeListener.put(toJson( pages.map { p => Map[String, String]("id" -> p.id, "title" -> p.title) }.toList ).toString())
      writeListener.complete()
      context.stop(self)
    case ReceiveTimeout ⇒
      writeListener.onError(new RuntimeException("received timeout"))
      context.stop(self)
    case _ =>
  }
}
