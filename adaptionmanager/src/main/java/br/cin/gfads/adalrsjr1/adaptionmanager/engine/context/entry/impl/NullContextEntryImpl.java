package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullContextEntryImpl extends ContextEntryImpl<Object> {

	private static final Logger log = LoggerFactory.getLogger(NullContextEntryImpl.class);
	
	private static final NullContextEntryImpl INSTANCE = new NullContextEntryImpl(null);
	
	private NullContextEntryImpl(String key) {
		super(key);
	}
	
	private NullContextEntryImpl(String key, Object value) {
		super(key, value);
	}

	public static NullContextEntryImpl getInstance() {
		return INSTANCE;
	}

}
