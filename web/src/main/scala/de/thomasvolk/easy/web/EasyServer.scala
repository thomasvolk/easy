package de.thomasvolk.easy.web

import org.apache.catalina.startup.Tomcat
import java.io.File

object EasyServer {
  def main(args: Array[String]) {
    val webappDirLocation = "web/src/main/webapp"
    val port = sys.env.getOrElse("EASY_PORT", "8080").toInt
    val tomcat = new Tomcat
    tomcat.setPort(port)
    tomcat.addWebapp("/easy", new File(webappDirLocation).getAbsolutePath())
    tomcat.start()
    tomcat.getServer().await()

  }
}
