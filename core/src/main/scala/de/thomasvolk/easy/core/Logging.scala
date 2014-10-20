package de.thomasvolk.easy.core
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

import org.slf4j.LoggerFactory

trait Logging {
  val logger = LoggerFactory.getLogger(this.getClass)
  
  def debug(text: String): Unit = {
    logger.debug(text)
  }
  
  def info(text: String): Unit = {
    logger.info(text)
  }
  
  def error(text: String): Unit = {
    logger.error(text)
  }
  
  def error(text: String, t: Throwable): Unit = {
    logger.error(text, t)
  }
  
  def warn(text: String): Unit = {
    logger.warn(text)
  }

  def warn(text: String, t: Throwable): Unit = {
    logger.warn(text, t)
  }
}