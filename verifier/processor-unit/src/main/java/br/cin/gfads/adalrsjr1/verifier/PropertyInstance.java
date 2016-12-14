package br.cin.gfads.adalrsjr1.verifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;

public interface PropertyInstance {
	boolean check(SymptomEvent symptom);
	String getName();
}
