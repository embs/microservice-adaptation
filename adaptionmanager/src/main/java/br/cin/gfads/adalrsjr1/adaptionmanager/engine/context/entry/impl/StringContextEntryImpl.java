package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringContextEntryImpl extends ContextEntryImpl<String> {
	private static final Logger log = LoggerFactory.getLogger(StringContextEntryImpl.class);

	public StringContextEntryImpl(String key) {
		super(key);
	}

	public StringContextEntryImpl(String key, String value) {
		super(key, value);
	}

}
