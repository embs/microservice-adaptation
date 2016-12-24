package br.cin.gfads.adalrsjr1.verifier.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.common.SoftHashMap;
import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.instances.OneByOneProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.processingunits.wrappers.RabbitMQProcessingUnitWrapper;
import br.cin.gfads.adalrsjr1.verifier.properties.temporal.LabeledTransitionSystem;

public class ReqRespProperty implements PropertyInstance {

	private static final Logger log = LoggerFactory
			.getLogger(ReqRespProperty.class);

	private Map<String, LabeledTransitionSystem> map = new SoftHashMap<>();
	private String property;

	ReqRespProperty(String property) {
		this.property = property;
	}

	private LabeledTransitionSystem getLts(String key) {
		LabeledTransitionSystem lts = map.get(key);
		if (lts == null) {
			lts = LabeledTransitionSystem.labeledTransitionSystemFactory(
					property, TimeUnit.MICROSECONDS);
			map.put(key, lts);
		}

		return lts;
	}

	@Override
	public boolean check(SymptomEvent symptom) {
		Stopwatch watch = Stopwatch.createStarted();
 		String key1 = symptom.tryGet("container_id");
 		String key2 = symptom.tryGet("client");
		LabeledTransitionSystem lts = getLts(key1+"-"+key2);
		boolean result = lts.next(symptom);
		Util.mavericLog(log, this.getClass(), "check", watch.stop());
		return result;
	}

	@Override
	public String getName() {
		return "Request-Response";
	}

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<byte[]> buffer = new ArrayBlockingQueue<>(100);

		// RabbitMQSubscriber sub = RabbitMQSubscriber.builder()
		// .withBuffer(buffer)
		// .withExchangeDurable(true)
		// .withExchangeName("fluentd.fanout")
		// .withExchangeType(FANOUT)
		// //.withHost("10.0.75.1")
		// .withHost("10.66.66.22")
		// .withRoutingKey("")
		// .build();

		PropertyInstance p = new ReqRespProperty(
				"G(((client:(.)*) && (container_name:.client(.)*)) ->F((client:(.)*) && (container_name:.server(.)*)))");
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(p);
		RabbitMQProcessingUnitWrapper wrapper = new RabbitMQProcessingUnitWrapper(
				pu);

		ExecutorService executor = Executors.newSingleThreadExecutor(
				Util.threadFactory("rabbitmq-wrapper-processing-unit-reqresp"));
		executor.execute(wrapper);

		while (true)
			pu.toEvaluate(new SymptomEvent(buffer.take()));

		
	}

}
