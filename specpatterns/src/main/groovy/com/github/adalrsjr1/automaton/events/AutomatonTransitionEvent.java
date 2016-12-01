package com.github.adalrsjr1.automaton.events;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomatonTransitionEvent {

	private static final Logger log = LoggerFactory.getLogger(AutomatonTransitionEvent.class);
	
	private Map content;
	
	public AutomatonTransitionEvent(Map content) {
		this.content = content;
	}
	
	public Map getContent() {
		return content;
	}

}
