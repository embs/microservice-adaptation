package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ContextListener {
	void process(ContextEvent event);
}
