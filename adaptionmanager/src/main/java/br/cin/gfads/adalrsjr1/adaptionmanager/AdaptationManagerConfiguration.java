package br.cin.gfads.adalrsjr1.adaptionmanager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.jeromq.ZmqRunnable.ZmqProtocol;

public class AdaptationManagerConfiguration {

	private static final Logger log = LoggerFactory.getLogger(AdaptationManagerConfiguration.class);

	private static final String PREFIX = "maveric.adaptation.";
	private static AdaptationManagerConfiguration INSTANCE;
	
	private Properties properties;
	
	public final String host;
	public final int port;
	public final String scriptsRepository;
	public final String rabbitmqPriorityQueueName;
	public final Boolean rabbitmqPriorityDurable;
	
	private AdaptationManagerConfiguration(String propertiesPath) {
		Stopwatch watcher = Stopwatch.createStarted();
		
		loadProperty(propertiesPath);
		Random r = new Random();
		
		host = properties.getProperty(PREFIX + "host", "localhost");
		port = Integer.valueOf(properties.getProperty(PREFIX + "port", r.nextInt(10)+5560+""));
		scriptsRepository = properties.getProperty(PREFIX + "scripts-repository", "scripts");
		rabbitmqPriorityQueueName = properties.getProperty(PREFIX + "rabbitmq.priorityqueue-name",PREFIX+System.currentTimeMillis());
		rabbitmqPriorityDurable = Boolean.valueOf(properties.getProperty(PREFIX + "rabbitmq.priorityqueue-durable","false"));
		
		log.info("Properties of AdaptationManager loaded in {}", watcher.stop());
	}
	
	public static AdaptationManagerConfiguration getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new AdaptationManagerConfiguration("adaptation.properties");
		}
		System.err.println(INSTANCE.toString());
		return INSTANCE;
	}
	
	private Properties loadProperty(String propertiesPath) {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("adaptation.properties"));
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
		  .append(scriptsRepository)
		  .append("\npriorityqueue-name:")
		  .append(rabbitmqPriorityQueueName);
		return sb.toString();
	}

}
