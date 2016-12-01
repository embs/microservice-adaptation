package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ContextObservable {
	public void addListener(ContextListener listener);
	
	public void removeListener(ContextListener listener);
	
	public void clearListeners();
}
