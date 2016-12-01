package com.github.adalrsjr1.specpatterns.mappings

import com.github.adalrsjr1.specpatterns.ExpressionBuilder
import com.github.adalrsjr1.specpatterns.ExpressionPattern;
import com.github.adalrsjr1.specpatterns.ExpressionVariable;

import groovy.util.logging.Slf4j

@Slf4j
class ResponseChainExpression {
	ExpressionVariable s
	ExpressionVariable t
	ExpressionVariable p
	
	ResponseChainExpression(ExpressionVariable p) {
		this.p = p
	}
	
	ResponseChainExpression(ExpressionVariable s, ExpressionVariable t) {
		this.s = s
		this.t = t
	}
	
	// s, t preceds p
	ExpressionBuilder respondsTo(ExpressionVariable p) {
		new ExpressionBuilder(ExpressionPattern.RESPONSE_CHAIN_ONE, [s,t,p])
	}
	
	// s preceds p, t
	ExpressionBuilder respondsTo(ExpressionVariable s, ExpressionVariable t) {
		new ExpressionBuilder(ExpressionPattern.RESPONSE_CHAIN_TWO, [p,s,t])
	}
}
