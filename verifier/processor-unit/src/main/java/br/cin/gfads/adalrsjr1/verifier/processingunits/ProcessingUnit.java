package br.cin.gfads.adalrsjr1.verifier.processingunits;

import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;

public interface ProcessingUnit extends Runnable {

	boolean toEvaluate(SymptomEvent symptom);
	void stop();
	void start();
	int getCounter();
	void setCounter(int n);
	void resetCounter();
	long elapsedTime();
	void resetTimer();
	void addListener(ProcessingUnitListener listener);
	void removeListener(ProcessingUnitListener listener);
	ChangeRequestEvent evaluate() throws InterruptedException;
}
