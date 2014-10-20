package de.thomasvolk.easy.web.actors
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

import de.thomasvolk.easy.web.utils.PollingWriteListener
import akka.actor.{Actor}
import scala.concurrent.duration._

abstract class AbstractStreamActor(writeListener: PollingWriteListener) extends Actor with JsonEncoding {

  context.setReceiveTimeout(20 seconds)

  def put(item: AnyRef): Unit = writeListener.put(item)

  def complete(item: AnyRef): Unit = {
    put(item)
    complete()
  }

  def complete(): Unit = {
    writeListener.complete()
    context.stop(self)
  }

  def onError(t: Throwable): Unit = {
    writeListener.onError(t)
    context.stop(self)
  }

  def sendError(code: Int, message: String): Unit = writeListener.sendError(code, message)

}
