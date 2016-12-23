package br.cin.gfads.adalrsjr1.verifier.processingunits.instances;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.MicroserviceInfo;
import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.AbstractProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.properties.TestProperty;

public class BlockingProcessingUnit extends AbstractProcessingUnit {

	private SymptomEvent a, b, toCompare;
	private int timeout;
	private boolean unblock = false;
	
	public BlockingProcessingUnit(int bufferSize, PropertyInstance property, SymptomEvent a, SymptomEvent b, int timeout) {
		super(bufferSize, property);
		toCompare = this.a = a;
		this.b = b;
		this.timeout = timeout;
	}
	
	public BlockingProcessingUnit(PropertyInstance property, SymptomEvent a, SymptomEvent b, int timeout) {
		super(property);
		toCompare = this.a = a;
		this.b = b;
		this.timeout = timeout;
	}

	private static final Logger log = LoggerFactory.getLogger(BlockingProcessingUnit.class);

	@Override
	public ChangeRequestEvent evaluate() throws InterruptedException {
		boolean result = false;
		ChangeRequestEvent changeRequest = null;
		SymptomEvent symptom = null;
		if(elapsedTime() > timeout) {
			resetTimer();
			synchronized (symptomBuffer) {
				symptomBuffer.clear();
			}
			changeRequest = result ? null : new ChangeRequestEvent(property.getName(), MicroserviceInfo.timeout() , Collections.emptyMap());
		}
		else if(unblock) {
			unblock = false;
			synchronized (symptomBuffer) {
				while(!symptomBuffer.isEmpty()) {
					symptom = symptomBuffer.take();
					result = result && property.check(symptom);
				}
			}
			
			resetTimer();
			changeRequest = result ? null : new ChangeRequestEvent(property.getName(), symptom.getSource(), Collections.emptyMap());
		}
		return changeRequest;
	}
	
	@Override
	public boolean toEvaluate(SymptomEvent symptom) {
		boolean result = false;
		if(toCompare != null && symptom.equals(toCompare)) {
			synchronized (symptomBuffer) {
				result = super.toEvaluate(symptom);
			}
			toCompare = null;
		}
		else if(toCompare == null) {
			synchronized (symptomBuffer) {
				result = super.toEvaluate(symptom);
			}
			if(symptom.equals(b)) {
				toCompare = a;
				unblock = true;
			}
		}
		log.trace(unblock+"");
		return result;
	}
	
}
