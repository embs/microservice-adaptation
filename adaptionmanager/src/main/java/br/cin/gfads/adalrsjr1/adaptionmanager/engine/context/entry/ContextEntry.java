package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextObservable;

public interface ContextEntry<T> extends Comparable<ContextEntry<T>>, ContextObservable {
	String getkey();
	void setValue(T value);
	T getValue();
}
