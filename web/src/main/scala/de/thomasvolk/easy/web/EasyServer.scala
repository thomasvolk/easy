package de.thomasvolk.easy.web
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

import org.apache.catalina.startup.Tomcat
import java.io.File
import org.apache.catalina.connector.Connector

object EasyServer {
  def main(args: Array[String]) {
    val port = sys.env.getOrElse("EASY_HTTP_PORT", "10080")
    val ajpPort = sys.env.getOrElse("EASY_AJP_PORT", "10009")
    val ajpRedirectPort = sys.env.getOrElse("EASY_AJP_REDIRECT_PORT", "10443")
    val baseDir = sys.env.getOrElse("EASY_SERVER_DIR", ".")
    val webAppDirLocation = sys.props.getOrElse("app.home", "web/src/main") + "/webapp"

    val tomcat = new Tomcat
    tomcat.setBaseDir(baseDir)
    val ajpConnector = new Connector("org.apache.coyote.ajp.AjpProtocol")
    ajpConnector.setPort(ajpPort.toInt)
    ajpConnector.setProtocol("AJP/1.3")
    ajpConnector.setRedirectPort(ajpRedirectPort.toInt)
    ajpConnector.setEnableLookups(false)
    ajpConnector.setProperty("redirectPort", ajpRedirectPort)
    ajpConnector.setProperty("protocol", "AJP/1.3")
    ajpConnector.setProperty("enableLookups", "false")
    tomcat.getService.addConnector(ajpConnector)
    tomcat.setPort(port.toInt)
    tomcat.addWebapp("/easy", new File(webAppDirLocation).getAbsolutePath())
    tomcat.start()
    tomcat.getServer().await()

  }
}
