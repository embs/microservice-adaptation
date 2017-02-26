package br.cin.gfads.adalrsjr1.verifier.processingunits.wrappers;

import static br.cin.gfads.adalrsjr1.endpoint.rabbitmq.ExchangeType.FANOUT;

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
import br.cin.gfads.adalrsjr1.verifier.processingunits.ProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.processingunits.ProcessingUnitConfiguration;
import br.cin.gfads.adalrsjr1.verifier.processingunits.ProcessingUnitListener;

public class RabbitMQProcessingUnitWrapper implements ProcessingUnitListener, Runnable {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQProcessingUnitWrapper.class);
	private static final ProcessingUnitConfiguration CONFIG = ProcessingUnitConfiguration.getInstance();

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private ProcessingUnit processingUnit;

	private boolean stoped = false;

	private BlockingQueue<byte[]> buffer = new LinkedBlockingQueue<>();

	private RabbitMQSubscriber monitorSubscriber;
	private RabbitMQProducer plannerProducer;

	public RabbitMQProcessingUnitWrapper(ProcessingUnit procecessingUnit) {
		this.processingUnit = procecessingUnit;
		this.processingUnit.addListener(this);

		executor.execute(procecessingUnit);

		monitorSubscriber = RabbitMQSubscriber.builder().withBuffer(buffer)
				.withExchangeDurable(CONFIG.monitoQueueDurable).withExchangeName(CONFIG.monitorExchangeName)
				.withExchangeType(FANOUT).withHost(CONFIG.monitorHost).withPort(CONFIG.monitorPort).withRoutingKey("")
				.build();

		plannerProducer = RabbitMQProducer.builder().withDurable(CONFIG.plannerQueueDurable)
				.withHost(CONFIG.plannerHost).withPort(CONFIG.plannerPort).withQueue(CONFIG.plannerQueueName).build();

		this.processingUnit.start();
	}

	public void stop() {
		processingUnit.stop();
		executor.shutdown();
		stoped = true;
	}

	@Override
	public void run() {
		while (!stoped && !Thread.currentThread().isInterrupted()) {
			try {
				byte[] message = buffer.take();

				SymptomEvent symptom = new SymptomEvent(message);
				processingUnit.toEvaluate(symptom);

			}
			catch (InterruptedException e) {
				log.warn(e.getMessage());
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void notify(ChangeRequestEvent changeRequest) {
		plannerProducer.send(changeRequest.serialize());
	}

}
