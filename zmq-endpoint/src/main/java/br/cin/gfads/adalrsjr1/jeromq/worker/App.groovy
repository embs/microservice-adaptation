package br.cin.gfads.adalrsjr1.jeromq.worker

import groovy.util.logging.Slf4j

import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

import org.zeromq.ZContext

import br.cin.gfads.adalrsjr1.jeromq.ZmqPublisherRunnable
import br.cin.gfads.adalrsjr1.jeromq.ZmqRequestorRunnable
import br.cin.gfads.adalrsjr1.jeromq.ZmqResponserRunnable
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable
import br.cin.gfads.adalrsjr1.jeromq.ZmqSubscriberRunnable
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqCommunicationPattern
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqProtocol
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqRunnableBuilder
import br.cin.gfads.adalrsjr1.jeromq.parsers.FluentdParser

/**
 * Hello world!
 *
 */
@Slf4j
class App extends AbstractWorker
{
	
	public App(BlockingQueue input, BlockingQueue output) {
		super(input, output)
	}
	
	protected process(Object fromInputBuffer) {
		FluentdParser parser = new FluentdParser()
		
//		println ">>> App " + new String(fromInputBuffer)
		def result = parser.unmarshaller(fromInputBuffer)
		println ">>> App " + result 
		return result
	}
	
	static void pubsub() {
		ZContext zmqContext = new ZContext(1)
		ZContext zmqContext2 = new ZContext(1)
		ExecutorService tPool = Executors.newCachedThreadPool()
		
		BlockingQueue channel1 = new LinkedBlockingQueue()
		BlockingQueue channel2 = new LinkedBlockingQueue()
		BlockingQueue channel3 = new LinkedBlockingQueue()
		
		FluentdParser parser = new FluentdParser()

//		ZmqPublisherRunnable pub = ZmqRunnable.builder(zmqContext, channel2)
//				.host("localhost")
//				.port(5679)
//				.topic("java")
//				.protocol(ZmqProtocol.IPC)
//				.communicationPattern(ZmqCommunicationPattern.PUB)
//				.build()

		ZmqSubscriberRunnable sub1 = ZmqRunnable.builder(zmqContext, channel3)
				.setHost("localhost")
				.setPort(5558)
				.setTopic("")
				.setProtocol(ZmqProtocol.TCP)
				.setCommunicationPattern(ZmqCommunicationPattern.SUB)
				.build()

//		ZmqSubscriberRunnable sub2 = ZmqRunnable.builder(zmqContext, channel3)
//				.host("localhost")
//				.port(5679)
//				.topic("java")
//				.protocol(ZmqProtocol.IPC)
//				.communicationPattern(ZmqCommunicationPattern.SUB)
//				.build()
		App processorUnit = new App(channel3, channel1)

		//		tPool.execute(fluentd)
//		tPool.execute(pub)
		tPool.execute(sub1)
//		tPool.execute(sub2)
		tPool.execute(processorUnit)

		int i = 0;
		while(true) {
			String s = i++ +""
			channel2.offer(s.getBytes())
			Thread.sleep(100)
		}
	}
	
	static void reqres() {
		ZContext zmqContext = new ZContext(1)
		ZContext zmqContext2 = new ZContext(1)
		ExecutorService tPool = Executors.newCachedThreadPool()
		
		BlockingQueue channel1 = new LinkedBlockingQueue()
		BlockingQueue channel2 = new LinkedBlockingQueue()
		BlockingQueue channel3 = new LinkedBlockingQueue()
		
		// client-server
		ZmqRequestorRunnable req1 = ZmqRunnable.builder(zmqContext, channel1)
				.host("localhost")
				.port(5679)
				.protocol(ZmqProtocol.TCP)
				.communicationPattern(ZmqCommunicationPattern.AREQ)
				.build()

		//new ZmqRequestorRunnable(zmqContext, "localhost", 5679, channel1)
		ZmqRequestorRunnable req2 = ZmqRunnable.builder(zmqContext, channel1)
				.host("localhost")
				.port(5679)
				.protocol(ZmqProtocol.TCP)
				.communicationPattern(ZmqCommunicationPattern.AREQ)
				.build()
		ZmqRequestorRunnable req3 = ZmqRunnable.builder(zmqContext, channel1)
				.host("localhost")
				.port(5679)
				.protocol(ZmqProtocol.TCP)
				.communicationPattern(ZmqCommunicationPattern.AREQ)
				.build()

		ZmqResponserRunnable res = ZmqRunnable.builder(zmqContext, channel2)
				.host("localhost")
				.port(5679)
				.protocol(ZmqProtocol.TCP)
				.communicationPattern(ZmqCommunicationPattern.AREP)
				.build()

		App processorUnit = new App(channel2, channel3)

		tPool.execute(res)
		Thread.sleep(100)
		tPool.execute(req1)
		tPool.execute(req2)
		tPool.execute(req3)
		Thread.sleep(300)
		tPool.execute(processorUnit)

		int i = 0;
		boolean run = true;
		while(run) {
			String s = i++ +""
			channel1.offer(s.getBytes())
			Thread.sleep(100)
			if(i % 17 == 0) {


				req1.shutdown()
				req2.shutdown()
				req3.shutdown()
				res.shutdown()

				processorUnit.stop()

				tPool.shutdownNow()
				zmqContext.close()
				zmqContext2.close()
				run = false
			}

		}
	}
	
    static void main( String[] args )
    {
		App.pubsub()
		

		

    }
}
