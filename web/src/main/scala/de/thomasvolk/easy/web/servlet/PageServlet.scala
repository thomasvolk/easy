package de.thomasvolk.easy.web.servlet
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

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import akka.actor.{Props}
import de.thomasvolk.easy.core.model.Page
import de.thomasvolk.easy.web.utils.ServletExtension
import ServletExtension._
import de.thomasvolk.easy.web.actors.{LoadPageStreamActor, PersistPageStreamActor, AbstractStreamActor}
import de.thomasvolk.easy.web.utils.{PollingWriteListener}
import de.thomasvolk.easy.core.message.{PersistPage, FindPage}
import de.thomasvolk.easy.core.Logging

class PageServlet extends AbstractServlet with Logging {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (req.invalid) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "invalid page id: " + req.pageId)
    }
    else {
      resp.setContentType("application/json")
      val writeListener: PollingWriteListener = getWriteListener(req, resp)
      val actor = actorSystem.actorOf(Props(new LoadPageStreamActor(pageActor, writeListener)))
      actor ! FindPage(req.pageId)
    }
  }

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (req.invalid) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, "invalid page id: " + req.pageId)
    }
    else {
      val writeListener: PollingWriteListener = getWriteListener(req, resp)
      val actor = actorSystem.actorOf(Props(new PersistPageStreamActor(pageActor, writeListener)) )
      actor ! PersistPage(Page(req.pageId, req.pageContent))
    }
  }
}
