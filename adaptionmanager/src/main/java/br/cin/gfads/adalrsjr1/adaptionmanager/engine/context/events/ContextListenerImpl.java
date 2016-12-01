package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ContextListenerImpl implements ContextListener, ContextObservable {

	private static final Logger log = LoggerFactory.getLogger(ContextListenerImpl.class);

	protected List<ContextListener> listeners;
	
	public ContextListenerImpl() {
		listeners = new ArrayList<>();
	}
	
	@Override
	public void process(ContextEvent event) {
		for(ContextListener listener : listeners) {
			listener.process(event);
		}
	}
	
	public void addListener(ContextListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ContextListener listener) {
		listeners.remove(listener);
	}
	
	public void clearListeners() {
		listeners.clear();
	}

}
