package br.cin.gfads.adalrsjr1.adaptationqueue;

import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent;

public interface AdaptationPriorityQueue extends AdaptationPriorityQueueClient {
	public ChangePlanEvent remove();
	public void clear();
	public ChangePlanEvent take() throws InterruptedException;
}
