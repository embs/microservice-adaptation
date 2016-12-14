package br.cin.gfads.adalrsjr1.verifier.processingunits;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.properties.TestProperty;

public class NEventsProcessingUnit extends AbstractProcessingUnit{
	
	private static final Logger log = LoggerFactory.getLogger(NEventsProcessingUnit.class);

	private int nEvents;
	
	public NEventsProcessingUnit(int bufferSize, PropertyInstance property, int nEvents) {
		super(bufferSize, property);
	}
	
	public NEventsProcessingUnit(PropertyInstance property, int nEvents) {
		super(property);
		this.nEvents = nEvents;
	}

	@Override
	public ChangeRequestEvent evaluate() throws InterruptedException {
		boolean result = false;
		SymptomEvent symptom = null;
		ChangeRequestEvent changeRequest = null;
		int n = getCounter();
		if(n >= nEvents) {
			result = true;
			for(int i = 0; i < nEvents; i++) {
				symptom = symptomBuffer.take();
				count.decrementAndGet();
				result = result && property.check(symptom);
				changeRequest = result ? null : new ChangeRequestEvent(property.getName(), symptom.getSource() , Collections.emptyMap());
			}
		} else {
			Thread.sleep(1000);
		}
		
		return changeRequest;
	}
	

}
