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

import javax.servlet.http.HttpServletRequest
import de.thomasvolk.easy.core.model.{Page}
import org.apache.commons.io.FilenameUtils

object ServletExtension {

  implicit class RequestToPageId(req: HttpServletRequest) {

    def pageURI: String = {
      req.getPathInfo
    }

    def extension: String = {
      FilenameUtils.getExtension(pageURI)
    }

    def pageId: String = {
      if (extension == "") {
        pageURI
      }
      else {
        pageURI.substring(0, pageURI.size - extension.size - 1)
      }
    }

    def pageContent: String = req.getParameter("content")

    def invalid: Boolean = {
      "\\/[\\/A-Za-z0-9]+".r.findAllIn(pageId).size == 0
    }
  }
}