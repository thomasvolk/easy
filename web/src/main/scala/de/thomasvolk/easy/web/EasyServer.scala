package de.thomasvolk.easy.web

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
