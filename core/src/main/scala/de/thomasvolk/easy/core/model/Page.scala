package de.thomasvolk.easy.core.model
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

case class Page(id: String, title: String, content: String, parentPage: Option[(String, String)], subPages: Seq[(String, String)]) {
  def ref = (id, title)
  def name = id.drop(id.lastIndexOf("/") + 1)
}

object Page {
  def apply(id: String, content: String): Page = {
    val title = Option(id) match {
      case Some(str) if str.contains("/") => str.split("/").last
      case Some(str) => str
      case None => null
    }
    new Page(id = id, title = title, content = content, parentPage = None, subPages = Seq.empty)
  }
}