package de.thomasvolk.easy.web

import org.eclipse.jetty.server.{ServerConnector, Server}
import org.eclipse.jetty.webapp.WebAppContext


object Jetty {
  def main(args: Array[String]) {
    val server: Server = new Server
    val connector: ServerConnector = new ServerConnector(server)
    connector.setSoLingerTime(-1)
    connector.setPort(8080)
    server.addConnector(connector)

    val bb: WebAppContext = new WebAppContext
    bb.setAttribute("useFileMappedBuffer", false)
    bb.setDefaultsDescriptor("web/src/test/resources/webdefault.xml")
    bb.setServer(server)
    bb.setContextPath("/easy")
    bb.setWar("web/src/main/webapp")

    server.setHandler(bb)

    try {
      System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP")
      server.start
      System.in.read
      System.out.println(">>> STOPPING EMBEDDED JETTY SERVER")
      server.stop
      server.join
    }
    catch {
      case e: Exception => {
        e.printStackTrace
        System.exit(1)
      }
    }
  }
}
