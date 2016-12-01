package com.github.adalrsjr1.automaton.events

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.github.adalrsjr1.automaton.AutomatonEngine

import groovy.transform.ToString

@ToString(includeNames=true,includeFields=true,includePackage=false)
public class AutomatonEvent {
	private static final Logger log = LoggerFactory.getLogger(AutomatonEvent.class)
	
	final AutomatonEngine source
	final int lastState
	final int currentState
	final long timeElapsed
	final boolean isAcceptanceState
	
	//(this, lastState, currentState, timeInCurrentState)
	AutomatonEvent(AutomatonEngine source, int lastState, int currentState, long timeElapsed, boolean acceptanceState) {
		this.source = source
		this.lastState = lastState
		this.currentState = currentState
		this.timeElapsed = timeElapsed
		this.isAcceptanceState = acceptanceState
	}
	
}