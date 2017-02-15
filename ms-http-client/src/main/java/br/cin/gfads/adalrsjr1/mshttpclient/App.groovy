package br.cin.gfads.adalrsjr1.mshttpclient

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.common.base.Stopwatch
import com.google.common.util.concurrent.RateLimiter

public class App {
	private static final Logger log = LoggerFactory.getLogger(App)

	public static void main( String[] args ) {

		Map env = System.getenv()

		String destination = env.getOrDefault("SERVER", "localhost")
		int rate = Integer.parseInt(env.getOrDefault("THROTTLE", "1")) 
		long MAX_TIME = Long.parseLong(env.getOrDefault("MAX_TIME", "${Long.MAX_VALUE}"))
		MAX_TIME = MAX_TIME < 0 ? Long.MAX_VALUE : MAX_TIME
		int cores = Runtime.getRuntime().availableProcessors()

		ExecutorService tPool
//		tPool = Executors.newFixedThreadPool((int)Math.round(rate/2)+1 * cores);//Executors.newFixedThreadPool((int)Math.round(rate/2)+1 * cores);
		tPool = Executors.newCachedThreadPool()
		RateLimiter throttle = RateLimiter.create(rate)

		Stopwatch watch = Stopwatch.createStarted()
		while(watch.elapsed(TimeUnit.SECONDS) <= MAX_TIME) {
			throttle.acquire()
			
			tPool.execute({
				try {
					UUID uuid = UUID.randomUUID()

					URL server = new URL("http://${destination}:8080/${System.identityHashCode(this)}-${Thread.currentThread().getId()}/${uuid.toString()}")
					Stopwatch timer = Stopwatch.createStarted()
					URLConnection connection = server.openConnection()
					BufferedReader buffReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))

					String input
					while((input = buffReader.readLine()) != null) {
						log.info input
					}
					buffReader.close()
					log.info "{\"serviceTime\":${timer.elapsed(TimeUnit.MILLISECONDS)}}"
					timer.stop()
				}
				catch(Exception e) {
					log.error(e.getMessage());
					throw new RuntimeException(e);
				}
			})
		}
		
		watch.stop()
		
		tPool.shutdown()
	}
}
