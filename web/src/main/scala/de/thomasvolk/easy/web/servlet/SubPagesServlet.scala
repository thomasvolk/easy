package de.thomasvolk.easy.web.servlet

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import akka.actor.Props
import de.thomasvolk.easy.core.message.{FindSubPages, FindPage, PersistPage}
import de.thomasvolk.easy.web.utils.{ServletExtension, PollingWriteListener}
import de.thomasvolk.easy.web.actors.{PageStreamActor}
import ServletExtension._
import de.thomasvolk.easy.core.Logging

class SubPagesServlet extends AbstractServlet with Logging {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (req.invalid) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "invalid page id: " + req.pageId)
    }
    else {
     resp.setContentType("application/json")
      val writeListener: PollingWriteListener = getWriteListener(req, resp)
      val actor = actorSystem.actorOf(Props(new PageStreamActor(pageActor, writeListener)))
      actor ! FindSubPages(req.page.id)
    }
  }

}
