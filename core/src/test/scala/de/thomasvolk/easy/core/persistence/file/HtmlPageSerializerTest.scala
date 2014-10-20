package de.thomasvolk.easy.core.persistence.file
/*
 * Copyright 2014 Thomas Volk
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

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
