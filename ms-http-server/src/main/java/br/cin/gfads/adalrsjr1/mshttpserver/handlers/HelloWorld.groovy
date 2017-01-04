package br.cin.gfads.adalrsjr1.mshttpserver.handlers


import groovy.util.logging.Slf4j

import java.util.concurrent.TimeUnit

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

import com.google.common.base.Stopwatch

@Path("/")
class HelloWorld {
	private static final Logger log = LoggerFactory.getLogger(HelloWorld)
	
	private static final Random r = new Random(31);
	static Map env = System.getenv()
	
	private static int deviation = Integer.parseInt(env.getOrDefault("DEV", "100"))
	private static int average = Integer.parseInt(env.getOrDefault("AVG", "1000"))
	
	@GET
	@Path("/{client}/{reqId}")
	@Produces(MediaType.APPLICATION_JSON)
	String helloWorld(@PathParam("client") String client, @PathParam("reqId") String reqId) {
		Stopwatch timer = Stopwatch.createStarted()
		long n = (long) Math.round(r.nextGaussian() * deviation + average)
		
		Stopwatch watch = Stopwatch.createStarted()

		while(watch.elapsed(TimeUnit.MILLISECONDS) <= n) {
			Thread.sleep(100)
		}
		timer.stop()
		String str = "{\"client\":\"$client\", \"processing\":${timer.elapsed(TimeUnit.MILLISECONDS)}, \"req-id\":\"$reqId\"}"
		log.info str
		return str
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
