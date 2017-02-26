package br.cin.gfads.adalrsjr1.app;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;

import br.cin.gfads.adalrsjr1.jeromq.ZmqResponserRunnable;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqCommunicationPattern;
import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqProtocol;
import br.cin.gfads.adalrsjr1.jeromq.ZmqSubscriberRunnable;
import br.cin.gfads.adalrsjr1.jeromq.worker.AbstractWorker;

public class RepApp extends AbstractWorker<byte[], byte[]> {
	private static final Logger log = LoggerFactory.getLogger(RepApp.class);

	static final ExecutorService TPOOL = Executors.newFixedThreadPool(2);
	
	public RepApp(BlockingQueue<byte[]> inputBuffer, BlockingQueue<byte[]> outputBuffer) {
		super(inputBuffer, outputBuffer);
	}
	
	@Override
	protected byte[] process(byte[] toProcess) {
		String str = new String(toProcess);
		try {
			log.info("processing {} at {}", str, Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return toProcess;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ZContext zmqContext = new ZContext(1);

		BlockingQueue<byte[]> input = new LinkedBlockingQueue<>();
		BlockingQueue<byte[]> output = new LinkedBlockingQueue<>();

		ZmqResponserRunnable rep = (ZmqResponserRunnable) ZmqRunnable.builder(zmqContext, input)
											  .setHost(args[0])
											  .setPort(Integer.parseInt(args[1]))
											  .setProtocol(ZmqProtocol.TCP)
											  .setCommunicationPattern(ZmqCommunicationPattern.AREP)
											  .build();
		RepApp app = new RepApp(input, output);
		TPOOL.execute(rep);
		TPOOL.execute(app);
	}

	
}
