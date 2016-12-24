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
	private static class TimeEntry implements Comparable<TimeEntry> {
		final String id;
		private long time = 0L; 

		private TimeEntry(String id) {
			this.id = id;
		}
		
		static TimeEntry newEntry(String id, long time) {
			TimeEntry t = new TimeEntry(id);
			t.addTime(time);
			return t;
		}
		
		static TimeEntry newEntry(String id) {
			return newEntry(id, 0);
		}
		
		@Override
		public int compareTo(TimeEntry o) {
			return this.id.compareTo(o.id);
		}
		
		public long getTime() {
			return time;
		}
		
		public long addTime(long time) {
			time += time;
			return time;
		}
	
	}
	
	Map<String, Long> map = new WeakHashMap<>();
	double sum = 0.0;
	double count = 0.0;
	double threshold = 0.0;
	
	public ResponseTimeProperty(double threshould) {
		this.threshold = threshould;
	}
	
	private long insertIntoMap(SymptomEvent s) {
		String key = s.tryGet("req-id");
		long time = Long.parseLong(s.tryGet("timeMillis"));
		
		Long value = 0L;
		value = map.putIfAbsent(key, time);
		
		if(value != null) {
			value = time - value;
			map.remove(key);
			return value;
		}
		
		return -1L;
	}
	
	@Override
	public boolean check(SymptomEvent symptom) {
		Stopwatch watch = Stopwatch.createStarted();
		long v = insertIntoMap(symptom);
		boolean result = true;
		if(v > 0) {
			count++;
			sum += v;
//			System.err.println(count + " " + sum + " " + sum/count );
			if(sum/count > this.threshold) {
				result = false;
			}
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
