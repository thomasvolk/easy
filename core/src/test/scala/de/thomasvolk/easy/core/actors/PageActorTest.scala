package de.thomasvolk.easy.core.actors

import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.{Props, ActorSystem}
import org.scalatest.{WordSpecLike, Matchers, BeforeAndAfterAll}
import de.thomasvolk.easy.core.persistence.PagePersistenceService
import de.thomasvolk.easy.core.model.Page
import de.thomasvolk.easy.core.message._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import de.thomasvolk.easy.core.persistence.file.FilePagePersistenceServiceImpl
import java.nio.file.{FileSystems, Files}

@RunWith(classOf[JUnitRunner])
class PageActorTest extends TestKit(ActorSystem("PageActor"))
  with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {
  var persistenceService: PagePersistenceService = _

  override def beforeAll {
      persistenceService = new FilePagePersistenceServiceImpl(
      Files.createTempDirectory(FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir")), this.getClass.getName)
    )
  }

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "an PageActor" must {

    "send back ReceivedPage" in {
      val pageActor = system.actorOf(Props(classOf[PageActor], persistenceService), "page1")

      pageActor ! FindPage("/my/new/page")
      expectMsg(PageNotFound("/my/new/page"))

      pageActor ! PersistPage(Page("/my/page", "Hello World"))
      expectMsg(PageSaved(Page("/my/page", "Hello World")))

      pageActor ! FindPage("/my/page")
      expectMsg(PageFound(Page("/my/page", "Hello World")))
    }
  }
}
