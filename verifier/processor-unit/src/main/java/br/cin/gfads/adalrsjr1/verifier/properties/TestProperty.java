package br.cin.gfads.adalrsjr1.verifier.properties;

import static br.cin.gfads.adalrsjr1.endpoint.rabbitmq.ExchangeType.FANOUT;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.endpoint.rabbitmq.RabbitMQSubscriber;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.OneByOneProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.processingunits.wrappers.RabbitMQProcessingUnitWrapper;

public class TestProperty implements PropertyInstance {
	Random r = new Random(31);
	@Override
	public boolean check(SymptomEvent symptom) {
		return r.nextInt(100) % 7 == 0;
	}

	@Override
	public String getName() {
		return "mock"+r.nextInt(2);
	}
	
	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<byte[]> buffer = new ArrayBlockingQueue<>(100);

		RabbitMQSubscriber sub = RabbitMQSubscriber.builder()
				.withBuffer(buffer)
				.withExchangeDurable(true)
				.withExchangeName("fluentd.fanout")
				.withExchangeType(FANOUT)
				//.withHost("10.0.75.1")
				.withHost("10.66.66.22")
				.withRoutingKey("")
				.build();
		
		
		PropertyInstance p = new TestProperty();
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(p);

		RabbitMQProcessingUnitWrapper wrapper = new RabbitMQProcessingUnitWrapper(pu);
		
		ExecutorService executor = Executors.newSingleThreadExecutor(Util.threadFactory("rabbitmq-wrapper-processing-unit"));
		executor.execute(wrapper);

		while(true)
			pu.toEvaluate(new SymptomEvent(buffer.take()));
		
	}
   
}
