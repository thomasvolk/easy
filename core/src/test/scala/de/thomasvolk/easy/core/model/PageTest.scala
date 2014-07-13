package de.thomasvolk.easy.core.model

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{MustMatchers, WordSpecLike}

@RunWith(classOf[JUnitRunner])
class PageTest extends WordSpecLike with MustMatchers {
  "a Page" must {
    "have ref and name" in {
      val page = Page("/1/2/3/test-page", "Test Page", "content", Some(("/1/2/3", "3 Parent")),
        Seq(("/1/2/3/test-page/child1", "Subpage 1")))
      page.name must equal ("test-page")
      page.ref must equal ( ("/1/2/3/test-page", "Test Page") )
    }
  }
}
