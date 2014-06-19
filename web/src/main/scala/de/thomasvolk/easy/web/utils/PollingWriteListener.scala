package de.thomasvolk.easy.web.utils

import java.util.concurrent.{TimeUnit, LinkedBlockingQueue}
import javax.servlet.http.HttpServletResponse
import akka.actor.PoisonPill
import javax.servlet.{WriteListener, AsyncContext, ServletOutputStream}
import de.thomasvolk.easy.core.Logging

class PollingWriteListener(outStream: ServletOutputStream, asyncContext: AsyncContext)
  extends WriteListener with Logging {
  private var output = new LinkedBlockingQueue[AnyRef]()

  def put(item: AnyRef) {
    output.put(item)
  }

  def complete() {
    output.put(PoisonPill)
  }

  override def onError(t: Throwable): Unit = {
    error("error", t)
    put(t.getMessage)
    complete()
    asyncContext.getResponse match {
      case response: HttpServletResponse => response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE)
    }
  }

  override def onWritePossible(): Unit = {
    while (
      output.poll(1, TimeUnit.SECONDS) match {
        case html: String =>
          outStream.print(html)
          true
        case _: PoisonPill =>
          asyncContext.complete()
          false
        case _ =>
          true
      }
    ) {}
  }
}
