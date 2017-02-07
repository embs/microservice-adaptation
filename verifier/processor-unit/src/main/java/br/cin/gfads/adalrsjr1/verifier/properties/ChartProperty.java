package br.cin.gfads.adalrsjr1.verifier.properties;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.timeseries.ChartInfo;
import br.cin.gfads.adalrsjr1.timeseries.TimeSeriesVisualizer;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.instances.OneByOneProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.processingunits.wrappers.RabbitMQProcessingUnitWrapper;

public class ChartProperty implements PropertyInstance {
	private static final Logger log = LoggerFactory.getLogger(ChartProperty.class);

	TimeSeriesVisualizer visualizer;

	public ChartProperty(List<ChartInfo> infos) {
		visualizer = new TimeSeriesVisualizer(infos);
	}

	@Override
	public boolean check(SymptomEvent symptom) {
		Long serviceTime = (Long) symptom.tryGet("serviceTime", Long.class);

		if (serviceTime != null) {
			Util.mavericLog(log, getClass(), "serviceTime", serviceTime);
			visualizer.getModules().get("serviceTime").addValue(serviceTime);
		}
		return false;
	}

	@Override
	public String getName() {
		return null;
	}

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<byte[]> buffer = new ArrayBlockingQueue<>(100);

//		 RabbitMQSubscriber sub = RabbitMQSubscriber.builder()
//		 .withBuffer(buffer)
//		 .withExchangeDurable(true)
//		 .withExchangeName("fluentd.fanout")
//		 .withExchangeType(FANOUT)
//		 //.withHost("10.0.75.1")
//		 .withHost("10.66.66.22")
//		 .withRoutingKey("")
//		 .build();


		ChartInfo[] infos = {ChartInfo.builder()
				.withName("serviceTime")
				.withXAxisLabel("")
				.withYAxisLabel("")
				.withTimeSerieName("")
				.withSerieMaximumCount(100)
				.build()};

		ChartProperty chart = new ChartProperty(Arrays.asList(infos));
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(chart);
		RabbitMQProcessingUnitWrapper wrapper = new RabbitMQProcessingUnitWrapper(pu);

		ExecutorService executor = Executors
				.newSingleThreadExecutor(Util.threadFactory("rabbitmq-wrapper-processing-unit-reqresp"));
		executor.execute(wrapper);

		while (true)
			pu.toEvaluate(new SymptomEvent(buffer.take()));

	}


}