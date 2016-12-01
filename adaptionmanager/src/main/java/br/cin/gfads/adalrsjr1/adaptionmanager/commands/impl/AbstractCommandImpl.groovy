package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.Command;
import br.cin.gfads.adalrsjr1.adaptionmanager.engine.context.entry.ContextEntry;
import groovy.transform.CompileStatic
import metamodel.CommandParameter
import metamodel.CompositeCommandParameter
import metamodel.ContextAttribute;
import metamodel.ContextType;
import metamodel.ICommandParameter;

@CompileStatic
public abstract class AbstractCommandImpl implements Command {

	private static final Logger log = LoggerFactory.getLogger(AbstractCommandImpl.class);

	protected Map<String, ICommandParameter> parameterToMap(ICommandParameter parameter) {
		if(parameter instanceof CommandParameter) return [(parameter.getKey()):parameter.getValue() as ICommandParameter]
		
		return ((CompositeCommandParameter) parameter).getValue()
													  .collectEntries {
														  [(it.getKey()) : it]
													  }
	}
	
	protected ICommandParameter lookup(String parameter, ICommandParameter root) {
		if(root.getKey() == parameter) {
			return root
		}
		TreeIterator<EObject> iterator = EcoreUtil.getAllContents((EObject) root, true);
		while(iterator.hasNext()) {
			ICommandParameter param = (ICommandParameter) iterator.next();
			if(param.getKey() == parameter) {
				return param;
			}
		}
	}
	
	protected void setCtxAttr(List<ContextAttribute> context, String key, def value) {
		setCtxAttr(getCtxAttr(context, key), value)
	}
	
	protected ContextAttribute getCtxAttr(List<ContextAttribute> context, String key) {
		context.find {
			it.getKey() == key
		}
	}
	
	protected void setCtxAttr(ContextAttribute ctx, def value) {
		if(ctx == null) {
			throw new RuntimeException("Impossible to set $value at a null context")
		}
		else if(value instanceof Integer) {
			setCtxAttr(ctx, value);
		}
		else if(value instanceof Double) {
			setCtxAttr(ctx, value);
		}
		else if(value instanceof String) {
			setCtxAttr(ctx, value);
		}
		else {
			throw new RuntimeException("Expected a ${ctx.type} value but $value is ${value.class}")
		}
	}
	
	protected void setCtxAttr(ContextAttribute ctx, Integer value) {
		if(ctx == null) return;
		if(ctx.type == ContextType.INTEGER) {
			((ContextEntry<Integer>)ctx.getContextImpl()).setValue(value);
		}
		else {
			throw new RuntimeException("Expected a ${ctx.type} value but $value is ${value.class}")
		}
	}
	
	protected void setCtxAttr(ContextAttribute ctx, Double value) {
		if(ctx == null) return;
		if(ctx.type == ContextType.DOUBLE) {
			((ContextEntry<Double>)ctx.getContextImpl()).setValue(value);
		}
		else {
			throw new RuntimeException("Expected a ${ctx.type} value but $value is ${value.class}")
		}
	}
	
	protected void setCtxAttr(ContextAttribute ctx, String value) {
		if(ctx == null) return;
		if(ctx.type == ContextType.STRING) {
			((ContextEntry<String>)ctx.getContextImpl()).setValue(value);
		}
		else {
			throw new RuntimeException("Expected a ${ctx.type} value but $value is ${value.class}")
		}
	}
}
