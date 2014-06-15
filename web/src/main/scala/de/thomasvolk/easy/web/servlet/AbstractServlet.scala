package de.thomasvolk.easy.web.servlet

import akka.actor.{ActorRef, ActorSystem}
import javax.servlet.ServletConfig
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import de.thomasvolk.easy.web.utils.PollingWriteListener

class AbstractServlet extends HttpServlet {
  var actorSystem: ActorSystem =_
  var pageActor: ActorRef = _

  override def init(servletConfig: ServletConfig): Unit = {
    super.init(servletConfig)
    actorSystem = getServletContext.getAttribute("actorSystem").asInstanceOf[ActorSystem]
    pageActor = getServletContext.getAttribute("pageActor").asInstanceOf[ActorRef]
  }

  protected def getWriteListener(req: HttpServletRequest, resp: HttpServletResponse): PollingWriteListener = {
    val asyncContext = req.startAsync()
    val writeListener = new PollingWriteListener(resp.getOutputStream, asyncContext)
    resp.getOutputStream.setWriteListener(writeListener)
    writeListener
  }
}
