package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.ContextEntry;

public class ContextEventImpl implements ContextEvent<Integer> {

	private static final Logger log = LoggerFactory.getLogger(ContextEventImpl.class);

	private Object source;
	private String key;
	private int oldValue;
	private int newValue;
	
	public ContextEventImpl(Object source, String key, int oldValue, int newValue) {
		super();
		this.source = source;
		this.key = key;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public Integer getOldValue() {
		return oldValue;
	}

	@Override
	public Integer getNewValue() {
		return newValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Object getSource() {
		return source;
	}
	
	@Override
	public String toString() {
		return "Event("+key+"):[old: "+ oldValue +", new:"+ newValue + "] from " + source;
	}
}
