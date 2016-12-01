package br.cin.gfads.adalrsjr1.app;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;

import br.cin.gfads.adalrsjr1.jeromq.ZmqPublisherRunnable;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqCommunicationPattern;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqProtocol;

public class PubApp {
	private static final Logger log = LoggerFactory.getLogger(PubApp.class);

	static final ExecutorService TPOOL = Executors.newFixedThreadPool(1);
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ZContext zmqContext = new ZContext(1);

		BlockingQueue<byte[]> input = new LinkedBlockingQueue<>();

		ZmqPublisherRunnable pub = (ZmqPublisherRunnable) ZmqRunnable.builder(zmqContext, input)
											  .setHost("10.0.75.1")
											  .setPort(Integer.parseInt(args[1]))
											  .setTopic("java")
											  .setProtocol(ZmqProtocol.TCP)
											  .setCommunicationPattern(ZmqCommunicationPattern.PUB)
											  .build();
		
		
		TPOOL.execute(pub);
		
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextLine()) {
			String message = scanner.nextLine();
			input.offer(message.getBytes());
		}
	}
}
