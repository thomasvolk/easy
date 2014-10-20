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

import akka.actor.{Props, Actor}
import de.thomasvolk.easy.core.actors.page._

import de.thomasvolk.easy.core.persistence.PagePersistenceService
import de.thomasvolk.easy.core.message._
import de.thomasvolk.easy.core.model.Page
import de.thomasvolk.easy.core.Logging

class PageActor(pagePersistenceService: PagePersistenceService) extends Actor with Logging {
  val loadPageActor = context.actorOf(Props(new LoadPageActor(pagePersistenceService)))
  val savePageActor = context.actorOf(Props(new SavePageActor(pagePersistenceService)))
  val deletePageActor = context.actorOf(Props(new DeletePageActor(pagePersistenceService)))

  context watch(loadPageActor)
  context watch(savePageActor)
  context watch(deletePageActor)

  def receive = {
    case lp: FindPage =>
      loadPageActor.!(lp)(sender)
    case sp: PersistPage =>
     savePageActor.!(sp)(sender)
    case dp: DeletePage =>
      deletePageActor.!(dp)(sender)
  }
}
