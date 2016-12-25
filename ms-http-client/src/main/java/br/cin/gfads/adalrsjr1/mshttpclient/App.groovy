package br.cin.gfads.adalrsjr1.mshttpclient

import groovy.util.logging.Slf4j

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.common.base.Stopwatch
import com.google.common.base.Stopwatch
import com.google.common.util.concurrent.RateLimiter

public class App {
	private static final Logger log = LoggerFactory.getLogger(App)
	
	public static void main( String[] args ) {
		
		Map env = System.getenv()
		
		String destination = env.getOrDefault("SERVER", "localhost")
		int rate = Integer.parseInt(env.getOrDefault("THROTTLE", "1")) % 1000
		long MAX_TIME = Long.parseLong(env.getOrDefault("MAX_TIME", "10"))
		int cores = Runtime.getRuntime().availableProcessors()
		
		ExecutorService tPool = Executors.newWorkStealingPool()//Executors.newFixedThreadPool((int)Math.round(rate/2)+1 * cores);
		RateLimiter throttle = RateLimiter.create(rate)
		
		
		Stopwatch watch = Stopwatch.createStarted()
		while(watch.elapsed(TimeUnit.SECONDS) <= MAX_TIME) {
			throttle.acquire()
			tPool.execute( {
				try {
				UUID uuid = UUID.randomUUID()
				
				URL server = new URL("http://${destination}:8080/${System.identityHashCode(this)}-${Thread.currentThread().getId()}/${uuid.toString()}")
				URLConnection connection = server.openConnection()
				
				BufferedReader buffReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))
				
				String input
				while((input = buffReader.readLine()) != null) {
					log.info input
				}
				buffReader.close()
				}
				catch(Exception e) {
					log.error e.getMessage()
				}
			})
		}
		watch.stop()
	}
}
