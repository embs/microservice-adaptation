package br.cin.gfads.adalrsjr1.mshttpserver

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer

import groovy.util.logging.Slf4j

@Slf4j
public class App {
	private static final String PACKAGE_HANDLERS = "br.cin.gfads.adalrsjr1.mshttpserver.handlers"
	private static final int SERVER_PORT = 8080
	
	public static void main( String[] args ) {
		ResourceConfig config = new ResourceConfig()
		config.packages(true, PACKAGE_HANDLERS)
		ServletHolder servlet = new ServletHolder(new ServletContainer(config))
		
		Server server = new Server(SERVER_PORT)
		ServletContextHandler context = new ServletContextHandler(server, "/*")
		context.addServlet(servlet, "/*")
		
		try {
			server.start()
			server.join()
		}
		catch(Exception e) {
			log.error e.message
			server.stop()
			server.destroy()
		}
	}
}