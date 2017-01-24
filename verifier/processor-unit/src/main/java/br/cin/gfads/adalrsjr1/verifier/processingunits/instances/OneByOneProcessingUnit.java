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

public class OneByOneProcessingUnit extends AbstractProcessingUnit {

	private static final Logger log = LoggerFactory
			.getLogger(OneByOneProcessingUnit.class);

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
		ChangeRequestEvent event = checkingResult
				? ChangeRequestEvent.getNullChangeRequestEvent()
				: new ChangeRequestEvent(property.getName(),
						symptom.getSource(), Collections.emptyMap());
		event.setTime(symptom.getTime());
		return event;
	}

}
