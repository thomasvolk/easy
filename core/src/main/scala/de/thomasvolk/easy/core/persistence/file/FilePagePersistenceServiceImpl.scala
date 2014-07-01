package de.thomasvolk.easy.core.persistence.file

import de.thomasvolk.easy.core.persistence.PagePersistenceService
import java.nio.file._
import de.thomasvolk.easy.core.model.{Reference, Page}
import scala.io.Source
import java.io.{File, FilenameFilter, FileFilter}

class FilePagePersistenceServiceImpl(root: Path)
  extends PagePersistenceService{
  val pageSerializer = new HtmlPageSerializer(
    Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("HtmlPageTemplate.html")).mkString)

  private def getDirPath(id: String): Path = FileSystems.getDefault().getPath(root.toString, id)

  private def getPath(id: String): Path = {
    FileSystems.getDefault().getPath(root.toString, id + ".html")
  }

  def getParentPageReference(id: String): Option[Reference] = getParentPage(id) match {
    case Some(page) => Some(page.reference)
    case None => None
  }

  def loadPage(id: String): Option[Page] = {
    val path = getPath(id)
    if(Files.exists(path)) {
      Some(loadPage(path).copy(
        parentPage = getParentPageReference(id),
        subPages = getSubpages(id).map(_.reference)) )
    }
    else {
      None
    }
  }

  private def loadPage(path: Path): Page = {
    pageSerializer.deserialize(new String(Files.readAllBytes(path), "UTF-8"))
  }

  def persistPage(page: Page): Unit = {
    this.synchronized {
      if(page.id.startsWith("/.") || page.id.startsWith(".") || page.id.endsWith(".")) throw new IllegalStateException("invalid page id: " + page.id)
      val path = getPath(page.id)
      Files.createDirectories(path.getParent)
      Files.write(path, pageSerializer.serialize(page).getBytes("UTF-8"),
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
    }
  }

  def getParentPage(pageId: String): Option[Page] = {
    val pagePath = getPath(pageId)
    val parent = pagePath.getParent
    if(parent != root) {
      val parentPagePath = Paths.get(parent.toString + ".html")
      if(parentPagePath.toFile.isFile) {
        return Some(loadPage(parentPagePath))
      }
    }
    None
}

  def deletePage(page: Page): Unit = {
    this.synchronized {
      Files.delete(getPath(page.id))
    }
  }

  def getSubpages(id: String): Set[Page] = {
    val listFiles = Option(getDirPath(id).toFile.listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = new File(dir, name).isFile && name.endsWith(".html")
    }))
    listFiles match {
      case Some(files) => files.map(f => loadPage(Paths.get(f.toURI))).toSet
      case None => Set.empty
    }
  }
}
