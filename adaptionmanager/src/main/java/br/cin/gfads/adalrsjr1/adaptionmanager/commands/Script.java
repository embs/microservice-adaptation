package br.cin.gfads.adalrsjr1.adaptionmanager.commands;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.Context;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.ContextImpl;
import metamodel.AdaptationScript;
import metamodel.ICommand;

public class Script {

	private static final Logger log = LoggerFactory.getLogger(Script.class);

	public List<ScriptCommand> commands;
	private Context temporaryContext;
	private AdaptationScript metaScript;

	public Script(AdaptationScript metaScript) {
		temporaryContext = new ContextImpl();
		this.metaScript = metaScript;
		commands = new ArrayList<>(metaScript.getCommands().size());
		init();
	}

	private void init() {
		for (ICommand metaCommand : metaScript.getCommands()) {
			commands.add(new CommandImpl(metaCommand));
		}
	}

	public void execute(Context context) {
		Stopwatch watch = Stopwatch.createStarted();
		for (ScriptCommand command : commands) {
			command.execute(context, temporaryContext);
		}
		temporaryContext.clear();
		log.info("script {} executed in {}", metaScript.getName(),
				watch.stop());
	}

	public void clear() {
		commands.clear();
	}
}