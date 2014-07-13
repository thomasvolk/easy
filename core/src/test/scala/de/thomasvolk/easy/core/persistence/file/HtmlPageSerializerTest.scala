package de.thomasvolk.easy.core.persistence.file

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{MustMatchers, WordSpecLike}
import de.thomasvolk.easy.core.model.Page

@RunWith(classOf[JUnitRunner])
class HtmlPageSerializerTest extends WordSpecLike with MustMatchers {
  "an HtmlPageSerializer" must {
    "create a html page" in {
      val serializer = HtmlPageSerializer()
      val htmlPage = serializer.serialize(Page("/1/2/3/test-page", "Test Page", "content", Some(("/1/2/3", "3 Parent")),
        Seq(("/1/2/3/test-page/child1", "Subpage 1"))))
      htmlPage must include ("""<title data-easy-page-id="/1/2/3/test-page">Test Page</title>""")
      htmlPage must include ("<h1>Test Page</h1>")
      htmlPage must include ("content")
      htmlPage must include ("""<li class="parent"><a data-easy-page-id="/1/2/3" href="../3.html">3 Parent</a></li>""")
      htmlPage must include ("""<li><a data-easy-page-id="/1/2/3/test-page/child1" href="test-page/child1.html">Subpage 1</a></li>""")
    }
  }
  "an HtmlPageSerializer" must {
    "create a page" in {
      val template =
        """<!DOCTYPE HTML>
          |<html data-easy-model-version="1.0" lang="en">
          |<head>
          |    <title data-easy-page-id="ID">TITLE</title>
          |</head>
          |<body>
          |    <header><h1>TITLE</h1></header>
          |    <section>
          |        <article>ARTICLE</article>
          |    </section>
          |</body>
          |</html>""".stripMargin
      val serializer = HtmlPageSerializer()
      val page = serializer.deserialize(template)
      page.id must equal("ID")
      page.title must equal("TITLE")
      page.content must equal("ARTICLE")
    }
  }
}
