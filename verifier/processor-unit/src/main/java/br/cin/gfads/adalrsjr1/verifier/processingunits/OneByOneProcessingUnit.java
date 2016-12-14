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

public class OneByOneProcessingUnit extends AbstractProcessingUnit {

	private static final Logger log = LoggerFactory.getLogger(OneByOneProcessingUnit.class);
	
	public OneByOneProcessingUnit(PropertyInstance property) {
		super(property);
	}

	public OneByOneProcessingUnit(int bufferSize, PropertyInstance property) {
		super(bufferSize, property);
	}

	@Override
	public ChangeRequestEvent evaluate() throws InterruptedException {
		SymptomEvent symptom = symptomBuffer.take();
		count.decrementAndGet();
		
		boolean checkingResult = property.check(symptom);
		
		return checkingResult ? ChangeRequestEvent.getNullChangeRequestEvent() : new ChangeRequestEvent(property.getName(), symptom.getSource(), Collections.emptyMap());
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		PropertyInstance p = new TestProperty();
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(p);

		Thread t = new Thread(pu);
		t.start();
		pu.start();

		Map<String, String> m = new HashMap<>();
		m.put("idx", "0");
		
		pu.toEvaluate(new SymptomEvent(null, m)); //0
		pu.toEvaluate(new SymptomEvent(null, m)); //1
		pu.toEvaluate(new SymptomEvent(null, m)); //2
		m = new HashMap<>();
		m.put("idx", "3");
		pu.toEvaluate(new SymptomEvent(null, m)); //3
		Thread.sleep(5000);
		t.interrupt();
	}
	
}
