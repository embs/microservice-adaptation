package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context;

import java.util.Set;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.ContextEntry;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl.DoubleContextEntryImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl.IntegerContextEntryImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl.StringContextEntryImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextObservable;
import metamodel.ContextAttribute;

public interface Context extends ContextObservable {
	boolean addContext(ContextEntry entry);
	ContextEntry removeContext(String key);
	ContextEntry lookup(String key);
	ContextEntry lookup(ContextAttribute metaContextEntry);
	Set<ContextEntry> fetchAll();
	void clear();
	
	public static ContextEntry createContextEntry(ContextAttribute metaContextEntry) {
		ContextEntry entry = null;
		String key = metaContextEntry.getKey();
		switch(metaContextEntry.getType()) {
		case INTEGER: entry = new IntegerContextEntryImpl(key);
			break;
		case DOUBLE: entry = new DoubleContextEntryImpl(key);
			break;
		case STRING: entry = new StringContextEntryImpl(key);
			break;
		}
		return entry;
	}
}
