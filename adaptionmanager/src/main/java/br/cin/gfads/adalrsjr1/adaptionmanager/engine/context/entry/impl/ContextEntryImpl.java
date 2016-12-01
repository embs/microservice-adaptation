package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.ContextEntry;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextEntryEventImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextListener;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextListenerImpl;

public abstract class ContextEntryImpl<T> extends ContextListenerImpl implements ContextEntry<T>  {

	private static final Logger log = LoggerFactory.getLogger(ContextEntryImpl.class);
	
	private final String key;
	private T value;
	
	public ContextEntryImpl(String key, T value) {
		this(key);
		this.value = value;
	}
	
	public ContextEntryImpl(String key) {
		super();
		this.key = key;
	}
	
	public String getkey() { 
		return key;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		ContextEntryEventImpl event = new ContextEntryEventImpl(this, key, this.value, value);
		this.value = value;
		
		for(ContextListener listener : listeners) {
			listener.process(event);
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContextEntryImpl other = (ContextEntryImpl) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int compareTo(ContextEntry<T> o) {
		return key.compareTo(o.getkey());
	}
	
	@Override
	public String toString() {
		return "[" + key + ":" + value + "]";
	}
	
}
