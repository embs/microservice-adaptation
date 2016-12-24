package br.cin.gfads.adalrsjr1.adaptionmanager.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.CommandImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.Script;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.Context;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.ContextImpl;
import br.cin.gfads.adalrsjr1.common.Util;
import metamodel.AdaptationScript;

public class ScriptEngine {

	private static final Logger log = LoggerFactory.getLogger(ScriptEngine.class);

	private Context context;
	
	public ScriptEngine(Context context) {
		this.context = context;
	}
	
	public void execute(Script script) {
		Stopwatch watch = Stopwatch.createStarted();
		script.execute(context);
		Util.mavericLog(log, this.getClass(), "execute", watch.stop());
	}
	
}
