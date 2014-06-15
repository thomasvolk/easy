package de.thomasvolk.easy.web.servlet

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import akka.actor.{Props}
import de.thomasvolk.easy.web.utils.ServletExtension
import ServletExtension._
import de.thomasvolk.easy.web.actors.{PageStreamActor}
import de.thomasvolk.easy.web.utils.{PollingWriteListener}
import de.thomasvolk.easy.core.message.{FindParentPage, PersistPage, FindPage}
import de.thomasvolk.easy.core.Logging

class ParentPageServlet extends AbstractServlet with Logging {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    resp.setContentType("application/json")
    val writeListener: PollingWriteListener = getWriteListener(req, resp)
    val actor =  actorSystem.actorOf(Props(new PageStreamActor(pageActor, writeListener)) )
    actor ! FindParentPage(req.page.id)
  }

}
