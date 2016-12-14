package br.cin.gfads.adalrsjr1.verifier.processingunits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface EvaluationRule {
	boolean isSatisfied(int count, long time);
}
