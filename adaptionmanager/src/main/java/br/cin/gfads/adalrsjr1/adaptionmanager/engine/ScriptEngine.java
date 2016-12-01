package br.cin.gfads.adalrsjr1.adaptionmanager.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.CommandImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.Script;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.Context;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.ContextImpl;
import metamodel.AdaptationScript;

public class ScriptEngine {

	private static final Logger log = LoggerFactory.getLogger(ScriptEngine.class);

	private Context context;
	
	public ScriptEngine(Context context) {
		this.context = context;
	}
	
	public void execute(Script script) {
		script.execute(context);
	}
	
}
