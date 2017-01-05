package br.cin.gfads.adalrsjr1.verifier.properties;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.instances.OneByOneProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.processingunits.wrappers.RabbitMQProcessingUnitWrapper;

public class ResponseTimeProperty implements PropertyInstance {
	private static final Logger log = LoggerFactory
			.getLogger(ResponseTimeProperty.class);
	
	double threshold = 0.0;
	
	public ResponseTimeProperty(double threshould) {
		this.threshold = threshould;
	}
	
	@Override
	public boolean check(SymptomEvent symptom) {
		Stopwatch watch = Stopwatch.createStarted();
		boolean result = false;
		Long serviceTime = (Long)symptom.tryGet("serviceTime", Long.class);
		if(serviceTime != null && serviceTime > this.threshold) {
			result = true;
		}
		result = false;
		if(serviceTime != null) {
			Util.mavericLog(log, this.getClass(), "check-serviceTime", serviceTime);
		}
		Util.mavericLog(log, this.getClass(), "check", watch.stop());
		return result;
	}

	@Override
	public String getName() {
		return "Response-Time";
	}

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<byte[]> buffer = new ArrayBlockingQueue<>(100);

//		RabbitMQSubscriber sub = RabbitMQSubscriber.builder()
//				.withBuffer(buffer)
//				.withExchangeDurable(true)
//				.withExchangeName("fluentd.fanout")
//				.withExchangeType(FANOUT)
//				//.withHost("10.0.75.1")
//				.withHost("10.66.66.22")
//				.withRoutingKey("")
//				.build();
		
		
		PropertyInstance p = new ResponseTimeProperty(10);
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(p);

		RabbitMQProcessingUnitWrapper wrapper = new RabbitMQProcessingUnitWrapper(pu);
		
		ExecutorService executor = Executors.newSingleThreadExecutor(Util.threadFactory("rabbitmq-wrapper-processing-unit-processingtime"));
		executor.execute(wrapper);

		while(true)
			pu.toEvaluate(new SymptomEvent(buffer.take()));
		
	}
	
}
