package de.thomasvolk.easy.web.utils

import javax.servlet.http.HttpServletRequest
import de.thomasvolk.easy.core.model.Page
import org.apache.commons.io.FilenameUtils

object ServletExtension {

  implicit class RequestToPageId(req: HttpServletRequest) {

    def pageURI: String = {
      req.getRequestURI.substring(req.getServletPath.size)
    }

    def extension: String = {
      FilenameUtils.getExtension(pageURI)
    }

    def pageId: String = {
      if (extension == "") {
        pageURI
      }
      else {
        pageURI.substring(0, pageURI.size - extension.size - 1)
      }
    }

    def page: Page = Page(pageId, req.getParameter("content"))

    def invalid: Boolean = {
      "\\/[\\/A-Za-z0-9]+".r.findAllIn(pageId).size == 0
    }
  }
}