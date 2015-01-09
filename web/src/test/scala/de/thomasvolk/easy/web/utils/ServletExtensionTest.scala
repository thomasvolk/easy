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
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.junit.Assert

@RunWith(classOf[JUnitRunner])
class ServletExtensionTest extends FunSuite {

  test("invalid pageId") {
    Assert.assertTrue(ServletExtension.pageIdIsInvalid("*."))
    Assert.assertTrue(ServletExtension.pageIdIsInvalid("te"))
    Assert.assertTrue(ServletExtension.pageIdIsInvalid(".."))
  }

  test("valid pageId") {
    Assert.assertTrue(!ServletExtension.pageIdIsInvalid("/test"))
    Assert.assertTrue(!ServletExtension.pageIdIsInvalid("/te"))
    Assert.assertTrue(!ServletExtension.pageIdIsInvalid("/y"))
    Assert.assertTrue(!ServletExtension.pageIdIsInvalid("/1"))
  }
}
