package br.cin.gfads.adalrsjr1.mshttpserver.handlers


import groovy.util.logging.Slf4j

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Path("/")
class HelloWorld {
	private static final Logger log = LoggerFactory.getLogger(HelloWorld)
	
	private static final Random r = new Random(31);
	
	private static final int deviation = 100;
	private static final int average = 1000;
	
	@GET
	@Path("/{client}/{reqId}")
	@Produces(MediaType.APPLICATION_JSON)
	String helloWorld(@PathParam("client") String client, @PathParam("reqId") String reqId) {
		long n = (long) Math.round(r.nextGaussian() * deviation + average)
		Thread.sleep(n)
		log.info "{\"client\":\"$client\", \"processing\":$n, \"req-id\":\"$reqId\"}"
		return "{\"client\":\"$client\", \"processing\":$n, \"req-id\":\"$reqId\"}"
	}
	
	@GET
	@Path("hello")
	@Produces(MediaType.APPLICATION_JSON)
	Response anotherHelloWorld(@Context HttpHeaders headers) {
		String userAgent = headers.getRequestHeader("user-agent").get(0)
		
		return Response.status(200)
					   .encoding("UTF-8")
					   .entity("{" + userAgent + "}")
					   .build()
	}
}
