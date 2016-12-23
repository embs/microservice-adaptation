package br.cin.gfads.adalrsjr1.verifier.processingunits.instances;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.AbstractProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.properties.TestProperty;

public class NEventsProcessingUnit extends AbstractProcessingUnit{
	
	private static final Logger log = LoggerFactory.getLogger(NEventsProcessingUnit.class);

	private int nEvents;
	private CountDownLatch eventsLatch;
	
	public NEventsProcessingUnit(int bufferSize, PropertyInstance property, int nEvents) {
		super(bufferSize, property);
	}
	
	public NEventsProcessingUnit(PropertyInstance property, int nEvents) {
		super(property);
		this.nEvents = nEvents;
		eventsLatch = new CountDownLatch(nEvents);
	}

	@Override
	public ChangeRequestEvent evaluate() throws InterruptedException {
		eventsLatch.await();
		eventsLatch = new CountDownLatch(nEvents);
		SymptomEvent symptom = null;
		boolean result = false;
		ChangeRequestEvent changeRequest = null;
		
		for(int i = 0; i < nEvents; i++) {
			symptom = symptomBuffer.take();
			count.decrementAndGet();
			result = result && property.check(symptom);
			changeRequest = result ? null : new ChangeRequestEvent(property.getName(), symptom.getSource() , Collections.emptyMap());
		}
		
		return changeRequest;
	}
	
	@Override
	public synchronized boolean toEvaluate(SymptomEvent symptom) {
		boolean result = super.toEvaluate(symptom);
		eventsLatch.countDown();
		return result;
	}
	

}
