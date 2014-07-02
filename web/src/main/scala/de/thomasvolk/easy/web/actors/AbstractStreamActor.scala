package de.thomasvolk.easy.web.actors

import de.thomasvolk.easy.web.utils.PollingWriteListener
import akka.actor.{Actor}
import scala.concurrent.duration._

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
