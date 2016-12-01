package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleContextEntryImpl extends ContextEntryImpl<Double> {

	private static final Logger log = LoggerFactory.getLogger(DoubleContextEntryImpl.class);
	
	public DoubleContextEntryImpl(String key, Double value) {
		super(key, value);
	}

	public DoubleContextEntryImpl(String key) {
		super(key);
	}

}
