package br.cin.gfads.adalrsjr1.mshttpserver.handlers


import groovy.util.logging.Slf4j

import java.lang.Class.Atomic
import java.util.Currency.CurrencyNameGetter
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import javax.jws.Oneway
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
import com.google.common.util.concurrent.AtomicDouble

@Path("/")
class HelloWorld {
	private static final Logger log = LoggerFactory.getLogger(HelloWorld)
	
	private static final Random r = new Random(31);
	static Map env = System.getenv()
	
	private static int deviation = Integer.parseInt(env.getOrDefault("DEV", "0"))
	private static int average = Integer.parseInt(env.getOrDefault("AVG", "1000"))
	private static Stopwatch globalWatch = Stopwatch.createUnstarted()
	private static AtomicInteger count = new AtomicInteger(0)
	private static AtomicInteger sum = new AtomicInteger(0)
	private static AtomicDouble throughput = new AtomicDouble(0.0)
	
	private long i = 1;
	
	@GET
	@Path("/{client}/{reqId}/{timestamp}")
	@Produces(MediaType.APPLICATION_JSON)
	String helloWorld(@PathParam("client") String client, 
		              @PathParam("reqId") String reqId,
					  @PathParam("timestamp") long timestamp) {
		Stopwatch timer = Stopwatch.createStarted()
		long latency = System.currentTimeMillis()-timestamp
		
		if(!globalWatch.isRunning()) {
			globalWatch.start()
			sum.addAndGet(0)
			throughput.addAndGet(0)
			count.addAndGet(0)
		}
		
		long n = (long) Math.round(r.nextGaussian() * deviation + average)
		
		Stopwatch watch = Stopwatch.createStarted()

		while(watch.elapsed(TimeUnit.MILLISECONDS) <= n) {
			Thread.sleep(100)
		}
		
		count.addAndGet(1)
		i = globalWatch.elapsed(TimeUnit.MILLISECONDS) == 0 ? 1 : globalWatch.elapsed(TimeUnit.MILLISECONDS)
		double avg = 0
		if(i >= 1000 ) {
			throughput.set(count.getAndSet(0)/i)
			globalWatch.reset()
			globalWatch.start()
		}
		
		timer.stop()
		String str = "{\"client\":\"$client\", \"throughput\":\"${throughput.get()*1000}\", \"latency\":\"$latency\", \"processing\":${timer.elapsed(TimeUnit.MILLISECONDS)}, \"req-id\":\"$reqId\"}"
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
