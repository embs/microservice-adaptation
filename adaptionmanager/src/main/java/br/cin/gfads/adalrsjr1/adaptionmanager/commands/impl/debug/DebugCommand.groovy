package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.debug

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.Command
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.AbstractCommandImpl
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.ContextEntry
import groovy.transform.builder.Builder
import metamodel.CommandContext
import metamodel.ContextAttribute
import metamodel.ContextType
import metamodel.ICommand

class DebugCommand extends AbstractCommandImpl {

	private static final Logger log = LoggerFactory.getLogger(DebugCommand)

	@Override
	public Object execute(ICommand metaCommand) {
		//println "${metaCommand.getName()}:${metaCommand.getImplementation()}"
		
		//println "parameters:"
		metaCommand.commandParameters.each { param ->
			//println "\t[${param.getKey()}:${param.getValue()}]"
		}
		Random r = new Random()
		//println "context"
		if(metaCommand instanceof CommandContext) {
			def cmd = metaCommand as CommandContext
			List contextAttributes = cmd.getContextAttributes()
			setCtxAttr(contextAttributes, "context.key1", r.nextInt())
			setCtxAttr(contextAttributes, "context.key2", r.nextDouble())
			setCtxAttr(contextAttributes, "context.key3", "test123")
			setCtxAttr(contextAttributes, "context.tmp.key1", r.nextInt())
			setCtxAttr(contextAttributes, "context.tmp.key2", r.nextDouble())
			
			contextAttributes.each { ContextAttribute ctxAttr ->
				//println "\t[${ctxAttr.isTemporary()}] ${ctxAttr.getKey()}(${ctxAttr.getContextImpl()} : ${ctxAttr.getType()}})"
			}
		}
		//println ""
		
	}
	
}
