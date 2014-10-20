package de.thomasvolk.easy.web
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

import javax.servlet.{ServletContextEvent, ServletContextListener}
import akka.actor.{Props, ActorSystem}
import de.thomasvolk.easy.core.actors.PageActor
import de.thomasvolk.easy.core.persistence.PagePersistenceService
import de.thomasvolk.easy.core.persistence.file.FilePagePersistenceServiceImpl
import java.nio.file.FileSystems
import de.thomasvolk.easy.core.Logging

class EasyServletContextListener extends ServletContextListener with Logging {
  var actorSystem: ActorSystem = _
  var pagePersistenceService: PagePersistenceService = _
  val DEFAULT_PAGE_ROOT = "_easy.db"

  override def contextInitialized(e: ServletContextEvent): Unit = {
    info("start")
    val envPageRoot = Option(System.getenv("EASY_PAGE_ROOT")).getOrElse(DEFAULT_PAGE_ROOT)
    val pageRoot = System.getProperty("easyPageRoot", envPageRoot)
    info("pageRoot=" + pageRoot)
    actorSystem = ActorSystem("EasyActorSystem")
    e.getServletContext.setAttribute("actorSystem", actorSystem)
    pagePersistenceService = new FilePagePersistenceServiceImpl(FileSystems.getDefault.getPath(pageRoot))
    val pageActor = actorSystem.actorOf(Props(classOf[PageActor], pagePersistenceService), "page")
    e.getServletContext.setAttribute("pageActor", pageActor)
  }

  override def contextDestroyed(e: ServletContextEvent): Unit = {
    pagePersistenceService = null
    actorSystem.shutdown()
    info("stopped")
  }
}
