package br.cin.gfads.adalrsjr1.verifier.properties.temporal;

public interface LabeledTransitionSystemListener {
	void notifyTransition(LabeledTransitionSystemEvent transitionEvent);
}
