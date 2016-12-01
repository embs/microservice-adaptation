package com.github.adalrsjr1.specpatterns

import groovy.util.logging.Slf4j

@Slf4j
enum TemporalOccurrence {
	GLOBALLY,
	BEFORE_R,
	AFTER_Q,
	BETWEEN_Q_AND_R,
	AFTER_Q_UNTIL_R
}

