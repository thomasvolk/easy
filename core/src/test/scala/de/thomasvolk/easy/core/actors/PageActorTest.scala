package de.thomasvolk.easy.core.actors
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
      expectMsg(PageFound(Page("/my/page", "Hello World")))

      pageActor ! FindPage("/my/page")
      expectMsg(PageFound(Page("/my/page", "Hello World")))
    }
  }
}
