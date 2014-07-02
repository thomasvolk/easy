package de.thomasvolk.easy.core

import org.junit.{Assert, Test, After, Before}
import de.thomasvolk.easy.core.persistence.PagePersistenceService
import de.thomasvolk.easy.core.persistence.file.{FilePagePersistenceServiceImpl}
import de.thomasvolk.easy.core.model.{Page}
import java.nio.file.{FileSystems, Files}

class PagePersistenceServiceTest {
  var persistenceService: PagePersistenceService = _

  @Before
  def prepareTestDatabase() {
    persistenceService = new FilePagePersistenceServiceImpl(
      Files.createTempDirectory(FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir")),
        this.getClass.getName))
  }

  @After
  def destroyTestDatabase() {
  }

  @Test
  def persistPage() {
    val id = "/1/2/test1"
    Assert.assertEquals("<p>Hello</p>", persistenceService.persist(Page(id, "<p>Hello</p>")).content)
    Assert.assertEquals("<p>Hello</p>", persistenceService.loadPage(id).get.content)
    Assert.assertEquals("<p>Hello 123</p>", persistenceService.persist(Page(id, "<p>Hello 123</p>")).content)
    Assert.assertEquals("<p>Hello 123</p>", persistenceService.loadPage(id).get.content)
    Assert.assertEquals(0, persistenceService.loadPage(id).get.subPages.size)
    Assert.assertFalse(persistenceService.loadPage(id).get.parentPage.isDefined)

    val subPageId01 = "/1/2/test1/sub1"
    persistenceService.persist(Page(subPageId01, "<p>Sub01</p>"))
    Assert.assertEquals(1, persistenceService.loadPage(id).get.subPages.size)
    Assert.assertEquals("sub1", persistenceService.loadPage(id).get.subPages(0)._2)

    val subPageId02 = "/1/2/test1/sub2"
    persistenceService.persist(Page(subPageId02, "<p>Sub02</p>"))
    Assert.assertEquals(2, persistenceService.loadPage(id).get.subPages.size)
    Assert.assertEquals("sub1", persistenceService.loadPage(id).get.subPages(0)._2)
    Assert.assertEquals("sub2", persistenceService.loadPage(id).get.subPages(1)._2)

    val parentId = "/1/2"
    persistenceService.persist(Page(parentId, "<p>Parent</p>"))
    Assert.assertEquals(2, persistenceService.loadPage(id).get.subPages.size)
    Assert.assertTrue(persistenceService.loadPage(id).get.parentPage.isDefined)
    Assert.assertEquals("2", persistenceService.loadPage(id).get.parentPage.get._2)

    persistenceService.deletePage(subPageId01)
    persistenceService.deletePage(subPageId02)
    persistenceService.deletePage(parentId)
    Assert.assertEquals(0, persistenceService.loadPage(id).get.subPages.size)
    Assert.assertFalse(persistenceService.loadPage(id).get.parentPage.isDefined)

    persistenceService.deletePage(id)
    Assert.assertEquals(None, persistenceService.loadPage(id))
  }

  @Test
  def parentPage() {
    val rootPageId = "/1"
    persistenceService.persist(Page(rootPageId, "<p>Hello</p>"))
    assert(None == persistenceService.getParentPage(rootPageId))

    val rootPage = persistenceService.loadPage(rootPageId)
    assert(None != rootPage)

    val childPageId = s"${rootPageId}/2"
    persistenceService.persist(Page(childPageId, "<p>Hello Child</p>"))
    val parentPage = persistenceService.getParentPage(childPageId)
    assert(None != parentPage)
    assert(rootPage.get == parentPage.get)
  }

  @Test
  def subpages() {
    val parentId = "/dir1/main"
    val subPages0 = persistenceService.getSubpages(parentId)
    persistenceService.persist(Page(parentId, "<p>Main</p>"))
    val subPages1 = persistenceService.getSubpages(parentId)

    List("A", "XXX", "06", "test123").foreach { childName =>
      val childId = s"${parentId}/${childName}"
      persistenceService.persist(Page(childId, s"<p>Child-${childName}</p>"))
    }
    val subPages4 = persistenceService.getSubpages(parentId)
    Assert.assertEquals(4, subPages4.size)
    val subPages4Iter = subPages4.iterator
    Assert.assertEquals("/dir1/main/06", subPages4Iter.next().id)
    Assert.assertEquals("/dir1/main/A", subPages4Iter.next().id)
    Assert.assertEquals("/dir1/main/test123", subPages4Iter.next().id)
    Assert.assertEquals("/dir1/main/XXX", subPages4Iter.next().id)

  }
}
