package br.cin.gfads.adalrsjr1.verifier.properties;

import java.util.Random;

import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;

public class TestProperty implements PropertyInstance {
	Random r = new Random(31);
	@Override
	public boolean check(SymptomEvent symptom) {
		return r.nextInt(100) % 7 == 0;
	}

	@Override
	public String getName() {
		return "mock"+r.nextInt(2);
	}
   
}
