package br.cin.gfads.adalrsjr1.verifier.processingunits.instances;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.AbstractProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.properties.TestProperty;

public class TTimeProcessingUnit extends AbstractProcessingUnit {

	private int time; 
	
	public TTimeProcessingUnit(int bufferSize, PropertyInstance property, int tMilliseconds) {
		super(bufferSize, property);
		time = tMilliseconds;
	}
	
	public TTimeProcessingUnit(PropertyInstance property, int tMilliseconds) {
		super(property);
		time = tMilliseconds;
	}

	private static final Logger log = LoggerFactory.getLogger(TTimeProcessingUnit.class);

	@Override
	public ChangeRequestEvent evaluate() throws InterruptedException {
		boolean result = false;
		SymptomEvent symptom = null;
		ChangeRequestEvent changeRequest = null;
		if(elapsedTime() >= time) {
			result = true;
			int nSymptoms = getCounter();
			resetCounter();
			resetTimer();
			for(int i = 0; i < nSymptoms; i++) {
				symptom = symptomBuffer.take();
				result = result && property.check(symptom);
			}
			changeRequest = result ? null : new ChangeRequestEvent(property.getName(), symptom.getSource() , Collections.emptyMap());
		}
		else {
			synchronized (this) {
				wait(time-elapsedTime());	
			}
		}
		return changeRequest;
	}
	
}
