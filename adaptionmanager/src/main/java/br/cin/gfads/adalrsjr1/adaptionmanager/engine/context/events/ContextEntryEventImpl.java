package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.ContextEntry;

public class ContextEntryEventImpl<T> implements ContextEvent<T> {

	private static final Logger log = LoggerFactory.getLogger(ContextEntryEventImpl.class);

	private Object source;
	private String key;
	private T oldValue;
	private T newValue;
	
	public ContextEntryEventImpl(Object source, String key, T oldValue, T newValue) {
		super();
		this.source = source;
		this.key = key;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public T getOldValue() {
		return this.oldValue;
	}

	@Override
	public T getNewValue() {
		return this.newValue;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public Object getSource() {
		return this.source;
	}
	
	@Override
	public String toString() {
		return "Event("+key+"):[old: "+ oldValue +", new:"+ newValue + "] from " + source;
	}
}
