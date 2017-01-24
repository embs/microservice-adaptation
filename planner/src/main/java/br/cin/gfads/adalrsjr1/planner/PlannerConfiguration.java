package br.cin.gfads.adalrsjr1.planner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

public class PlannerConfiguration {

	private static final Logger log = LoggerFactory.getLogger(PlannerConfiguration.class);
	private static final String PREFIX = "maveric.planner.";
	private static PlannerConfiguration INSTANCE;
	
	private Properties properties;
	
	public final String host;
	public final int port;
	public final String policiesRepository;
	public final String adaptationHost;
	public final int adaptationPort;
	public final String rabbitmqQueueName;
	public final boolean rabbitmqQueueDurable;
	public final String adaptationRabbitmqPriorityQueueName;
	
	private PlannerConfiguration(String propertiesPath) {
		Stopwatch watcher = Stopwatch.createStarted();
		loadProperty(propertiesPath);
		Random r = new Random();
		
		host = System.getProperty("maverick.rabbitmq", properties.getProperty(PREFIX + "host", "localhost"));
		port = Integer.valueOf(properties.getProperty(PREFIX + "port", r.nextInt(10)+5560+""));
		policiesRepository = properties.getProperty(PREFIX + "policy-repository", "repository/policies.json");
		adaptationHost = properties.getProperty(PREFIX + "adaptation.host", "localhost");
		adaptationPort = Integer.valueOf(properties.getProperty(PREFIX + "adaptation.port", r.nextInt(10)+5560+""));
		rabbitmqQueueName = properties.getProperty(PREFIX + "rabbitmq.queue-name",PREFIX+System.currentTimeMillis());
		rabbitmqQueueDurable = Boolean.valueOf(properties.getProperty(PREFIX + "rabbitmq.queue-durable","false"));
		adaptationRabbitmqPriorityQueueName = properties.getProperty(PREFIX + "adaptation.rabbitmq.priority-queue");
		log.info("Properties of PLANNER loaded in {}", watcher.stop());
	}
	
	public static PlannerConfiguration getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new PlannerConfiguration("planner.properties");
		}
		System.err.println(INSTANCE.toString());
		return INSTANCE;
	}
	
	private Properties loadProperty(String propertiesPath) {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("planner.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("host:")
		  .append(host)
		  .append("\nport:")
		  .append(port)
		  .append("\nrepository:")
		  .append(policiesRepository)
		  .append("\nadaptation.host:")
		  .append(adaptationHost)
		  .append("\nadaptation.port:")
		  .append(adaptationPort)
		  .append("\nrabbitmq.queue:")
		  .append(rabbitmqQueueName)
		  .append("\nrabbitmq.queue-durable:")
		  .append(rabbitmqQueueDurable);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(PlannerConfiguration.getInstance().host);
	}
}
