package de.thomasvolk.easy.web.utils
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

import java.util.concurrent.{TimeUnit, LinkedBlockingQueue}
import javax.servlet.http.HttpServletResponse
import akka.actor.PoisonPill
import javax.servlet.{WriteListener, AsyncContext, ServletOutputStream}
import de.thomasvolk.easy.core.Logging

class PollingWriteListener(outStream: ServletOutputStream, asyncContext: AsyncContext)
  extends WriteListener with Logging {
  private var output = new LinkedBlockingQueue[AnyRef]()

  def put(item: AnyRef) {
    output.put(item)
  }

  def complete() {
    output.put(PoisonPill)
  }

  override def onError(t: Throwable): Unit = {
    error("error", t)
    put(t.getMessage)
    sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, t.getMessage)
  }

  def sendError(code: Int, message: String): Unit = {
    try {
      asyncContext.getResponse match {
        case response: HttpServletResponse => response.sendError(code, message)
      }
    }
    catch {
      case e: Exception => warn("error sending http response error", e)
    }
    complete()

  }

  override def onWritePossible(): Unit = {
    while (
      output.poll(1, TimeUnit.SECONDS) match {
        case html: String =>
          outStream.print(html)
          true
        case _: PoisonPill =>
          asyncContext.complete()
          false
        case _ =>
          true
      }
    ) {}
  }
}
