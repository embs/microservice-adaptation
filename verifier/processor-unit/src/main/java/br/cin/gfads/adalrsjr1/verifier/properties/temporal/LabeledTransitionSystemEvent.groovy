package br.cin.gfads.adalrsjr1.verifier.properties.temporal;

import java.util.concurrent.TimeUnit

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import jhoafparser.storage.StoredState;

public class LabeledTransitionSystemEvent {

	public final int source;
	public final int destination;
	public final long elapsedTime;
	public final boolean acceptanceState;
	public final TimeUnit standardTime;
	public final SymptomEvent triggerEvent;
	
	private LabeledTransitionSystemEvent(int source, int destination, 
			long elapsedTime, TimeUnit standardTime, boolean acceptanceState, SymptomEvent triggerEvent) {
		this.source = source;
		this.destination = destination;
		this.elapsedTime = elapsedTime;
		this.standardTime = standardTime;
		this.acceptanceState = acceptanceState;
		this.triggerEvent = triggerEvent;
	}
	
	public static LabeledTransitionSystemEvent newEvent(int source, int destination, 
			long elapsedTime, TimeUnit standardTime, boolean acceptanceState, SymptomEvent triggerEvent) {
		return new LabeledTransitionSystemEvent(source, destination, elapsedTime, standardTime, 
				acceptanceState, triggerEvent);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder('[')
		sb.append(source)
		  .append(']->[')
		  .append(destination)
		  .append('] ')
		  .append('time:')
		  .append(elapsedTime)
		  .append(' ')
		  .append(standardTime)
		  .append(' trigger:')
		  .append(triggerEvent)
		return sb.toString()
	}
	
}
