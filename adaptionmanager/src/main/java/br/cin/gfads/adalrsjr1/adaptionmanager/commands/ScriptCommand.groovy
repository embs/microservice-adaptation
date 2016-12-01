package br.cin.gfads.adalrsjr1.adaptionmanager.commands;

import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.Context

interface ScriptCommand {
	def execute(Context context, Context temporaryContext)
}
