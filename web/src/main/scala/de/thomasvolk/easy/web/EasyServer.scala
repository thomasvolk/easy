package de.thomasvolk.easy.web

import org.apache.catalina.startup.Tomcat
import java.io.File
import org.apache.catalina.connector.Connector

object EasyServer {
  def main(args: Array[String]) {
    val webappDirLocation = "web/src/main/webapp"
    val port = sys.env.getOrElse("EASY_PORT", "8080").toInt
    val tomcat = new Tomcat
    val ajpConnector = new Connector("org.apache.coyote.ajp.AjpProtocol")
    ajpConnector.setPort(8009)
    ajpConnector.setProtocol("AJP/1.3")
    ajpConnector.setRedirectPort(8443)
    ajpConnector.setEnableLookups(false)
    ajpConnector.setProperty("redirectPort", "8443")
    ajpConnector.setProperty("protocol", "AJP/1.3")
    ajpConnector.setProperty("enableLookups", "false")
    tomcat.getService.addConnector(ajpConnector)
    tomcat.setPort(port)
    tomcat.addWebapp("/easy", new File(webappDirLocation).getAbsolutePath())
    tomcat.start()
    tomcat.getServer().await()

  }
}
