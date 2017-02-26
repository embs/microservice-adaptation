package br.cin.gfads.adalrsjr1.planner;

public class PlannerConfigurationV2 {

	public final String rabbitmqHost = System.getProperty("maverick.planner.rabbitmq.host", "localhost");
	public final int rabbitmqPort = Integer.parseInt(System.getProperty("maverick.planner.rabbitmq.port", "5672"));
	public final String policiesRepository = System.getProperty("maverick.planner.policies", "repository/policies.json");
	public final String plannerQueue = System.getProperty("maverick.planner.queue", "planner.queue");
	public final boolean plannerQueueDurable = Boolean.parseBoolean(System.getProperty("maverick.planner.queue.isdurable", "false"));
	public final String adaptationQueue = System.getProperty("maverick.adaptation.queue", "adaptationmanager.priorityqueue");
	public final int bufferCapacity = Integer.parseInt(System.getProperty("maverick.planner.buffer", Integer.MAX_VALUE+""));
	public final int plannerWorkers = Integer.parseInt(System.getProperty("maverick.planner.workers", Runtime.getRuntime().availableProcessors()+""));
	
	private PlannerConfigurationV2() { }
	
	private static final PlannerConfigurationV2 INSTANCE = new PlannerConfigurationV2();
	
	public static PlannerConfigurationV2 getInstance() {
		return INSTANCE;
	}
}
	