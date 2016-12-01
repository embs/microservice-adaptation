package br.cin.gfads.adalrsjr1.adaptionmanager.commands;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.Context;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.ContextEntry;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.impl.NullContextEntryImpl;
import metamodel.CommandContext;
import metamodel.ContextAttribute;
import metamodel.ICommand;


public class CommandImpl implements ScriptCommand {

	private static final Logger log = LoggerFactory.getLogger(CommandImpl.class);

	private ICommand metaCommand;
	
	public CommandImpl(ICommand metaCommand) {
		this.metaCommand = metaCommand;
	}
	
	private List<ContextAttribute> fetchTemporary(CommandContext metaContext) {
		return metaContext.getContextAttributes().parallelStream()
										  .filter(c -> c.isTemporary())
										  .collect(Collectors.toList());
	}
	
	private List<ContextAttribute> fetchNonTemporary(CommandContext metaContext) {
		return metaContext.getContextAttributes().parallelStream()
										  .filter(c -> !c.isTemporary())
										  .collect(Collectors.toList());
	}
	
	private void bindContext(List<ContextAttribute> metaContext, Context context) {
		Stopwatch watch = Stopwatch.createStarted();
		// if context entry does not exist create it in the context
		for(ContextAttribute metaEntry : metaContext) {
			ContextEntry entry = context.lookup(metaEntry);
			if(entry == NullContextEntryImpl.getInstance()) {
				entry = Context.createContextEntry(metaEntry);
				context.addContext(entry);
			}
			metaEntry.setContextImpl(entry);
		}
		log.trace("Context of {} binded in {}", metaCommand.getName(), watch.stop());
	}
	
	private void unbindContext(List<ContextAttribute> metaContext) {
		metaContext.parallelStream().forEach(ctx -> {
			ctx.setContextImpl(null);
		});
	}
	
	@Override
	public Object execute(Context context, Context temporaryContext) {
		CommandContext metaContext = null;
		if(metaCommand instanceof CommandContext) {
			metaContext = (CommandContext) metaCommand;
			bindContext(fetchTemporary(metaContext), temporaryContext);
			bindContext(fetchNonTemporary(metaContext), context);
		}
		
		Object result = null;
		try {
			Class<?> clazz = Class.forName(metaCommand.getImplementation());
			Command instance = (Command) clazz.newInstance();
			result = instance.execute(metaCommand);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if(metaContext != null) {
				unbindContext(metaContext.getContextAttributes());
			}
		}
		return result;
	}

}
