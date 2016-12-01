package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerContextEntryImpl extends ContextEntryImpl<Integer> {
	private static final Logger log = LoggerFactory.getLogger(IntegerContextEntryImpl.class);

	public IntegerContextEntryImpl(String key, Integer value) {
		super(key, value);
	}

	public IntegerContextEntryImpl(String key) {
		super(key);
	}
	
	
}
