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

import akka.actor.{ActorRef, ActorSystem}
import javax.servlet.ServletConfig
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import de.thomasvolk.easy.web.utils.PollingWriteListener

class AbstractServlet extends HttpServlet {
  var actorSystem: ActorSystem =_
  var pageActor: ActorRef = _

  override def init(servletConfig: ServletConfig): Unit = {
    super.init(servletConfig)
    actorSystem = getServletContext.getAttribute("actorSystem").asInstanceOf[ActorSystem]
    pageActor = getServletContext.getAttribute("pageActor").asInstanceOf[ActorRef]
  }

  protected def getWriteListener(req: HttpServletRequest, resp: HttpServletResponse): PollingWriteListener = {
    val asyncContext = req.startAsync()
    val writeListener = new PollingWriteListener(resp.getOutputStream, asyncContext)
    resp.getOutputStream.setWriteListener(writeListener)
    writeListener
  }
}
