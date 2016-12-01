package br.cin.gfads.adalrsjr1.adaptionmanager.engine.context;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.ContextEntry;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl.NullContextEntryImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextEventImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextListener;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.events.ContextListenerImpl;
import metamodel.ContextAttribute;

public class ContextImpl extends ContextListenerImpl implements Context {

	private static final Logger log = LoggerFactory.getLogger(ContextImpl.class);

	private Set<ContextEntry> innerContext;
	
	public ContextImpl() {
		super();
		innerContext = new TreeSet<>();
	}

	@Override
	synchronized public boolean addContext(ContextEntry entry) {
		int size = innerContext.size();
		boolean sucess = innerContext.add(entry);
		if(sucess) {
			entry.addListener(this);
			process(new ContextEventImpl(this, "added:"+entry, size, size+1));
		}
		
		return sucess;
	}

	@Override
	synchronized public ContextEntry removeContext(String key) {
		ContextEntry entry = lookup(key);
		int size = innerContext.size();
		boolean sucess = innerContext.remove(entry);
		if(sucess) {
			entry.removeListener(this);
			process(new ContextEventImpl(this, "removed:"+entry, size, size-1));
		}
		
		return entry;
	}

	@Override
	synchronized public ContextEntry lookup(String key) {
		return innerContext.parallelStream()
				.filter(e -> e.getkey().equals(key))
				.findFirst().orElse(NullContextEntryImpl.getInstance());
	}
	
	@Override
	synchronized public ContextEntry lookup(ContextAttribute metaContextEntry) {
		return lookup(metaContextEntry.getKey());
	}

	@Override
	public Set<ContextEntry> fetchAll() {
		return innerContext;
	}
	
	public synchronized void clear() {
		innerContext.parallelStream()
					.forEach(entry -> {
						entry.removeListener(this);
					});
		innerContext.clear();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		for(ContextEntry entry : innerContext) {
			sb.append(entry.toString());
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}
}
