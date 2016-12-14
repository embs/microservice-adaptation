package br.cin.gfads.adalrsjr1.verifier.properties;

import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;

public class TestProperty implements PropertyInstance {

	@Override
	public boolean check(SymptomEvent symptom) {
		return symptom.getContent().get("idx").equals("0");
	}

	@Override
	public String getName() {
		return "mock";
	}
   
}
