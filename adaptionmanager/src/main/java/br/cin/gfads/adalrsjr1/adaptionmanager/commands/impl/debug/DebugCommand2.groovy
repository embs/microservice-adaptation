package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.debug

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.Command
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.AbstractCommandImpl
import groovy.transform.builder.Builder
import metamodel.CommandContext
import metamodel.ContextAttribute
import metamodel.ContextType
import metamodel.ICommand

class DebugCommand2 extends AbstractCommandImpl {

	private static final Logger log = LoggerFactory.getLogger(DebugCommand2)

	@Override
	public Object execute(ICommand metaCommand) {
		
		//println "${metaCommand.getName()}:${metaCommand.getImplementation()}"
		
		//println "parameters:"
		metaCommand.commandParameters.each { param ->
			//println "\t[${param.getKey()}:${param.getValue()}]"
		}
		
		//println "context"
		if(metaCommand instanceof CommandContext) {
			def cmd = metaCommand as CommandContext
			
			List contextAttributes = cmd.getContextAttributes()
			
			contextAttributes.each { ContextAttribute ctxAttr ->
				//println "\t[${ctxAttr.isTemporary()}] ${ctxAttr.getKey()}(${ctxAttr.getContextImpl()} : ${ctxAttr.getType()}})"
			}
		}
		//println ""
		
	}
	
}
