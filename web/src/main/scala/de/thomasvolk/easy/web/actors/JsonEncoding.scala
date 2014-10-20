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

import de.thomasvolk.easy.core.model.Page
import argonaut._,Argonaut._

trait JsonEncoding {
  implicit def PageToJson: EncodeJson[Page] =
    EncodeJson((p: Page) =>
      ("id" := p.id) ->: ("title" := p.title) ->: ("content" := p.content) ->:
      ("parentPage" := p.parentPage.getOrElse(("",""))) ->: ("subPages" := p.subPages.toList) ->: jEmptyObject)

  def toJson(page: Page) = page.jencode

  def toJson(pages: Set[Page]) = pages.toList.jencode

  def toJson(map: Map[String, String]) = map.jencode

  def toJson(set: List[Map[String, String]]) = set.jencode

}
