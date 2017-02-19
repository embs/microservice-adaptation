package br.cin.gfads.adalrsjr1.verifier.processingunits;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqProtocol;

public class ProcessingUnitConfiguration {

	private static final Logger log = LoggerFactory.getLogger(ProcessingUnitConfiguration.class);
	private static final String PREFIX_PLANNER = "maveric.planner.";
	private static final String PREFIX_MONITOR = "maveric.monitor.";
	private static ProcessingUnitConfiguration INSTANCE;
	
	private Properties properties;
	
	public final String plannerHost;
	public final String monitorHost;
	public final int plannerPort;
	public final int monitorPort;

	public final String plannerQueueName;
	public boolean plannerQueueDurable;
	public final String monitorExchangeName;
	public final boolean monitoQueueDurable;
	
	private ProcessingUnitConfiguration(String propertiesPath) {
		Stopwatch watcher = Stopwatch.createStarted();
		loadProperty(propertiesPath);
		Random r = new Random();
		
		
		
		monitorHost = System.getProperty("maverick.rabbitmq", properties.getProperty(PREFIX_MONITOR + "host", "localhost"));
		monitorPort = Integer.valueOf(properties.getProperty(PREFIX_MONITOR + "port"));
		
		plannerHost = System.getProperty("maverick.rabbitmq", properties.getProperty(PREFIX_PLANNER + "host", "localhost"));
		plannerPort = Integer.valueOf(properties.getProperty(PREFIX_PLANNER + "port"));
		
		monitorExchangeName = properties.getProperty(PREFIX_MONITOR + "rabbitmq.exchange-name");
		monitoQueueDurable = Boolean.parseBoolean(properties.getProperty(PREFIX_MONITOR + "rabbitmq.queue-durable"));
		plannerQueueName = properties.getProperty(PREFIX_PLANNER + "rabbitmq.queue-name");
		plannerQueueDurable = Boolean.parseBoolean(properties.getProperty(PREFIX_PLANNER + "rabbitmq.queue-durable"));
		
		log.info("Properties of Property loaded in {}", watcher.stop());
	}
	
	public static ProcessingUnitConfiguration getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ProcessingUnitConfiguration("processing-unit.properties");
		}
		System.err.println(INSTANCE.toString());
		return INSTANCE;
	}
	
	private Properties loadProperty(String propertiesPath) {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("processing-unit.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("planner-host:")
		  .append(plannerHost)
		  .append("\nplanner-port:")
		  .append(plannerPort)
		  .append("\nmonitor-host:")
		  .append(monitorHost)
		  .append("\nmonitor-port:")
		  .append(monitorPort)
		  .append("\nplanner-queue:")
		  .append(plannerQueueName)
		  .append(" durable: ")
		  .append(plannerQueueDurable)
		  .append("\nmonitor-exchange:")
		  .append(monitorExchangeName)
		  .append(" durable: ")
		  .append(monitoQueueDurable);
		return sb.toString();
	}
}
