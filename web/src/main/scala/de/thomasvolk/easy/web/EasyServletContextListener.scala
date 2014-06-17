package de.thomasvolk.easy.web

import javax.servlet.{ServletContextEvent, ServletContextListener}
import akka.actor.{Props, ActorSystem}
import de.thomasvolk.easy.core.actors.PageActor
import de.thomasvolk.easy.core.persistence.PagePersistenceService
import de.thomasvolk.easy.core.persistence.file.FilePagePersistenceServiceImpl
import java.nio.file.FileSystems
import de.thomasvolk.easy.core.Logging

class EasyServletContextListener extends ServletContextListener with Logging {
  var actorSystem: ActorSystem = _
  var pagePersistenceService: PagePersistenceService = _

  override def contextInitialized(e: ServletContextEvent): Unit = {
    info("start")
    val pageRoot = System.getProperty("pageRoot", "_marbles.db")
    info("pageRoot=" + pageRoot)
    actorSystem = ActorSystem("MarblesActorSystem")
    e.getServletContext.setAttribute("actorSystem", actorSystem)
    pagePersistenceService = new FilePagePersistenceServiceImpl(FileSystems.getDefault.getPath(pageRoot))
    val pageActor = actorSystem.actorOf(Props(classOf[PageActor], pagePersistenceService), "page")
    e.getServletContext.setAttribute("pageActor", pageActor)
  }

  override def contextDestroyed(e: ServletContextEvent): Unit = {
    pagePersistenceService = null
    actorSystem.shutdown()
    info("stopped")
  }
}