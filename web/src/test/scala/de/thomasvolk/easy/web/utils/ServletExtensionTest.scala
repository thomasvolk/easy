package de.thomasvolk.easy.web.utils

import javax.servlet.http.HttpServletRequest
import org.scalatest.FunSuite
import org.mockito.Mockito._
import ServletExtension._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ServletExtensionTest extends FunSuite {

  test("pageId with extension") {
    val r = mock(classOf[HttpServletRequest])
    when(r.getServletPath).thenReturn("http://localhost:8080/page")
    when(r.getRequestURI).thenReturn("http://localhost:8080/page/path/to/my/page.html")
    assert("/path/to/my/page.html" == r.pageURI)
    assert("html" == r.extension)
    assert("/path/to/my/page" == r.pageId)
    assert(!r.invalid)
  }

  test("pageId with no extension") {
    val r = mock(classOf[HttpServletRequest])
    when(r.getServletPath).thenReturn("http://localhost:8080/page")
    when(r.getRequestURI).thenReturn("http://localhost:8080/page/path/to/my/page")
    assert("/path/to/my/page" == r.pageURI)
    assert("" == r.extension)
    assert("/path/to/my/page" == r.pageId)
    assert(!r.invalid)
  }

  test("invalid pageId") {
    val r = mock(classOf[HttpServletRequest])
    when(r.getServletPath).thenReturn("http://localhost:8080/page")
    when(r.getRequestURI).thenReturn("http://localhost:8080/page/.*")
    assert("/.*" == r.pageURI)
    assert("*" == r.extension)
    assert("/" == r.pageId)
    assert(r.invalid)
  }
}
