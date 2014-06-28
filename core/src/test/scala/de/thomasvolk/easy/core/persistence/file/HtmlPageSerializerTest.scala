package de.thomasvolk.easy.core.persistence.file

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{MustMatchers, WordSpecLike}
import de.thomasvolk.easy.core.model.Page

@RunWith(classOf[JUnitRunner])
class HtmlPageSerializerTest extends WordSpecLike with MustMatchers {
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
  "an HtmlPageSerializer" must {
    "create a html page" in {
      val serializer = new HtmlPageSerializer(template)
      val htmlPage = serializer.serialize(Page("1/2/3/test-page", "content"))
      htmlPage must include ("""<title data-easy-page-id="1/2/3/test-page">test-page</title>""")
      htmlPage must include ("<h1>test-page</h1>")
      htmlPage must include ("content")

    }
  }
  "an HtmlPageSerializer" must {
    "create a page" in {
      val serializer = new HtmlPageSerializer(template)
      val page = serializer.deserialize(template)
      page.id must equal("ID")
      page.title must equal("TITLE")
      page.content must equal("ARTICLE")
    }
  }
}
