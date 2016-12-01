package com.github.adalrsjr1.specpatterns.mappings

import groovy.util.logging.Slf4j

import com.github.adalrsjr1.specpatterns.ExpressionBuilder
import com.github.adalrsjr1.specpatterns.ExpressionPattern
import com.github.adalrsjr1.specpatterns.ExpressionVariable

@Slf4j
class ExistenceExpression {
	ExpressionVariable p
	
	ExistenceExpression(ExpressionVariable p) {
		this.p = p
	}
	
	ExpressionBuilder becomesTrue() {
		new ExpressionBuilder(ExpressionPattern.EXISTENCE, p)
	}
}
