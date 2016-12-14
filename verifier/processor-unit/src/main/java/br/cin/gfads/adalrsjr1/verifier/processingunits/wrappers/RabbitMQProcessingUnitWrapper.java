package br.cin.gfads.adalrsjr1.verifier.processingunits.wrappers;

import static br.cin.gfads.adalrsjr1.endpoint.rabbitmq.ExchangeType.FANOUT;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitMQProducer;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitMQSubscriber;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.OneByOneProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.processingunits.ProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.processingunits.ProcessingUnitConfiguration;
import br.cin.gfads.adalrsjr1.verifier.processingunits.ProcessingUnitListener;
import br.cin.gfads.adalrsjr1.verifier.properties.TestProperty;

public class RabbitMQProcessingUnitWrapper implements ProcessingUnitListener, Runnable  {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQProcessingUnitWrapper.class);
	private static final ProcessingUnitConfiguration CONFIG = ProcessingUnitConfiguration.getInstance();
	
	private final ExecutorService executor = Executors.newSingleThreadExecutor(Util.threadFactory("processing-unit-at-"+this));
	
	private ProcessingUnit processingUnit;
	private Thread processingUnitExecutor;
	
	private boolean stoped = false;
	
	private BlockingQueue<byte[]> buffer = new LinkedBlockingQueue<>();
	
	private RabbitMQSubscriber monitorSubscriber;
	private RabbitMQProducer plannerProducer;
	
	public RabbitMQProcessingUnitWrapper(ProcessingUnit procecessingUnit) {
		this.processingUnit = procecessingUnit;
		this.processingUnit.addListener(this);
		
		executor.execute(procecessingUnit);
		
		
		monitorSubscriber = RabbitMQSubscriber.builder()
											  .withBuffer(buffer)
											  .withExchangeDurable(CONFIG.monitoQueueDurable)
											  .withExchangeName(CONFIG.monitorExchangeName)
											  .withExchangeType(FANOUT)
											  .withHost(CONFIG.monitorHost)
											  .withPort(CONFIG.monitorPort)
											  .withRoutingKey("")
											  .build();
		
		plannerProducer = RabbitMQProducer.builder()
										  .withDurable(CONFIG.plannerQueueDurable)
										  .withHost(CONFIG.plannerHost)
										  .withPort(CONFIG.plannerPort)
										  .withQueue(CONFIG.plannerQueueName)
										  .build();
		
		this.processingUnit.start();
	}
	
	public void stop() {
		processingUnit.stop();
		executor.shutdown();
		stoped = false;
	}
	
	@Override
	public void run() {
		while(!stoped && !Thread.currentThread().isInterrupted()) {
			try {
				byte[] message = buffer.take();
				
				SymptomEvent symptom = new SymptomEvent(message);
				processingUnit.toEvaluate(symptom);
				
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void notify(ChangeRequestEvent changeRequest) {
		plannerProducer.send(changeRequest.serialize());
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		PropertyInstance p = new TestProperty();
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(p);

		RabbitMQProcessingUnitWrapper wrapper = new RabbitMQProcessingUnitWrapper(pu);
		
		ExecutorService executor = Executors.newSingleThreadExecutor(Util.threadFactory("rabbitmq-wrapper-processing-unit"));
		executor.execute(wrapper);
		

		Map<String, String> m = new HashMap<>();
		m.put("idx", "0");
		
		pu.toEvaluate(new SymptomEvent(null, m)); //0
		pu.toEvaluate(new SymptomEvent(null, m)); //1
		pu.toEvaluate(new SymptomEvent(null, m)); //2
		m = new HashMap<>();
		m.put("idx", "3");
		pu.toEvaluate(new SymptomEvent(null, m)); //3
		Thread.sleep(5000);
		wrapper.stop();
		
		
		
	}
}
