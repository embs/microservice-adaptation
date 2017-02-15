package br.cin.gfads.adalrsjr1.eventloop;

import br.cin.gfads.adalrsjr1.eventloop.events.EventLoopEvent;

public interface EventLoopWorkerListener {
	void handle(EventLoopEvent event);
}
