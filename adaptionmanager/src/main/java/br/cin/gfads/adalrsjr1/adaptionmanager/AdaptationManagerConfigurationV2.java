package br.cin.gfads.adalrsjr1.adaptionmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdaptationManagerConfigurationV2 {

	private static final Logger log = LoggerFactory.getLogger(AdaptationManagerConfigurationV2.class);

	public final String rabbitmqHost = System.getProperty("maverick.adaptation.rabbitmq.host", "localhost");
	public final int rabbitmqPort = Integer.parseInt(System.getProperty("maverick.adaptation.rabbitmq.port", "localhost"));
	public final String scriptsRepository =  System.getProperty("maverick.adaptation.script.repository", "scripts");
	public final String rabbitmqPriorityQueueName = System.getProperty("maverick.adaptation.queue", "adaptationmanager.priorityqueue");
	public final Boolean rabbitmqPriorityDurable = Boolean.parseBoolean(System.getProperty("maverick.adaptation.queue.isDurable", "false"));
	public final int bufferCapacity = Integer.parseInt(System.getProperty("maverick.adaptation.buffer", Integer.MAX_VALUE+""));
	public final int adaptationWorkers = Integer.parseInt(System.getProperty("maverick.adaptation.workers", "1"));

	private AdaptationManagerConfigurationV2() { }

	private static final AdaptationManagerConfigurationV2 INSTANCE = new AdaptationManagerConfigurationV2();

	public static AdaptationManagerConfigurationV2 getInstance() {
		return INSTANCE;
	}

}
