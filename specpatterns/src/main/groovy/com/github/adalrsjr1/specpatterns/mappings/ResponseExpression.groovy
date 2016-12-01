package com.github.adalrsjr1.specpatterns.mappings

import com.github.adalrsjr1.specpatterns.ExpressionBuilder
import com.github.adalrsjr1.specpatterns.ExpressionPattern;
import com.github.adalrsjr1.specpatterns.ExpressionVariable;

import groovy.util.logging.Slf4j

@Slf4j
class ResponseExpression {
	ExpressionVariable s
	
	ResponseExpression(ExpressionVariable s) {
		this.s = s
	}
	
	ExpressionBuilder respondsTo(ExpressionVariable p) {
		new ExpressionBuilder(ExpressionPattern.RESPONSE, [s,p])
	}
}
