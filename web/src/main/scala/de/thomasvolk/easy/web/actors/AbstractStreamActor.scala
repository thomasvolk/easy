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

abstract class AbstractStreamActor(writeListener: PollingWriteListener) extends Actor with JsonEncoding {

  context.setReceiveTimeout(20 seconds)

  def put(item: AnyRef): Unit = writeListener.put(item)

  def complete(item: AnyRef): Unit = {
    put(item)
    complete()
  }

  def complete(): Unit = {
    writeListener.complete()
    context.stop(self)
  }

  def onError(t: Throwable): Unit = {
    writeListener.onError(t)
    context.stop(self)
  }

  def sendError(code: Int, message: String): Unit = writeListener.sendError(code, message)

}
