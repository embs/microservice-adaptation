package br.cin.gfads.adalrsjr1.verifier.processingunits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;

public interface ProcessingUnitListener {
	void notify(ChangeRequestEvent changeRequest);
}
