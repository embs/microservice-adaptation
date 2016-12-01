package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.ContextEntry;

public interface ContextEvent<T> {

	T getOldValue();
	T getNewValue();
	String getKey();
	Object getSource();
	
}
