package br.cin.gfads.adalrsjr1.adaptationqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;

public interface AdaptationPriorityQueueClient {
	public void enqueue(ChangePlanEvent changePlan);
	public void start();
	public void stop();
}
