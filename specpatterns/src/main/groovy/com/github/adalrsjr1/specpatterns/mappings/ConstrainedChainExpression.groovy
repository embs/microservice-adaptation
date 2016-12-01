package com.github.adalrsjr1.specpatterns.mappings

import com.github.adalrsjr1.specpatterns.ExpressionBuilder
import com.github.adalrsjr1.specpatterns.ExpressionPattern;
import com.github.adalrsjr1.specpatterns.ExpressionVariable;

import groovy.util.logging.Slf4j

@Slf4j
class ConstrainedChainExpression {
	ExpressionVariable s
	ExpressionVariable t
	ExpressionVariable z
	
	ConstrainedChainExpression(ExpressionVariable s, ExpressionVariable t) {
		this.s = s
		this.t = t
	}
	
	ConstrainedChainExpression without(ExpressionVariable z) {
		this.z = z
		return this
	}
	
	ExpressionBuilder respondsTo(ExpressionVariable p) {
		new ExpressionBuilder(ExpressionPattern.CONSTRAINED_CHAIN , [s,t,z,p])
	}
	
}
