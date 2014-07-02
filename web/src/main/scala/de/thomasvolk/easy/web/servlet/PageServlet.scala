package de.thomasvolk.easy.web.servlet

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import akka.actor.{Props}
import de.thomasvolk.easy.core.model.Page
import de.thomasvolk.easy.web.utils.ServletExtension
import ServletExtension._
import de.thomasvolk.easy.web.actors.{LoadPageStreamActor, PersistPageStreamActor, AbstractStreamActor}
import de.thomasvolk.easy.web.utils.{PollingWriteListener}
import de.thomasvolk.easy.core.message.{PersistPage, FindPage}
import de.thomasvolk.easy.core.Logging

class PageServlet extends AbstractServlet with Logging {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (req.invalid) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "invalid page id: " + req.pageId)
    }
    else {
      resp.setContentType("application/json")
      val writeListener: PollingWriteListener = getWriteListener(req, resp)
      val actor = actorSystem.actorOf(Props(new LoadPageStreamActor(pageActor, writeListener)))
      actor ! FindPage(req.pageId)
    }
  }

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (req.invalid) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "invalid page id: " + req.pageId)
    }
    else {
      val writeListener: PollingWriteListener = getWriteListener(req, resp)
      val actor = actorSystem.actorOf(Props(new PersistPageStreamActor(pageActor, writeListener)) )
      actor ! PersistPage(Page(req.pageId, req.pageContent))
    }
  }
}
