package com.github.adalrsjr1.specpatterns.mappings

import com.github.adalrsjr1.specpatterns.ExpressionBuilder;
import com.github.adalrsjr1.specpatterns.ExpressionPattern;
import com.github.adalrsjr1.specpatterns.ExpressionVariable;

import groovy.util.logging.Slf4j

@Slf4j
class AbsenceExpression {
	ExpressionVariable p
	
	AbsenceExpression(ExpressionVariable p) {
		this.p = p
	}
	
	ExpressionBuilder isFalse() {
		new ExpressionBuilder(ExpressionPattern.ABSENCE, p)
	}
}
