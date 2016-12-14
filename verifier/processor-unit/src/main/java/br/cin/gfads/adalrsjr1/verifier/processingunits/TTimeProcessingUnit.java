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
	
	public static void main(String[] args) throws InterruptedException {
		PropertyInstance p = new TestProperty();
		TTimeProcessingUnit pu = new TTimeProcessingUnit(p, 1000);

		Thread t = new Thread(pu);
		t.start();
		pu.start();

		Map<String, String> m = new HashMap<>();
		m.put("idx", "3");
		Thread.sleep(700);
		pu.toEvaluate(new SymptomEvent(null, m)); //0
		pu.toEvaluate(new SymptomEvent(null, m)); //1
		Thread.sleep(500);
		pu.toEvaluate(new SymptomEvent(null, m)); //2
		m = new HashMap<>();
		m.put("idx", "3");
		pu.toEvaluate(new SymptomEvent(null, m)); //3
		pu.toEvaluate(new SymptomEvent(null, m)); //0
		pu.toEvaluate(new SymptomEvent(null, m)); //1
		Thread.sleep(600);
		pu.toEvaluate(new SymptomEvent(null, m)); //2
		Thread.sleep(10000);
		t.interrupt();
	}

}
