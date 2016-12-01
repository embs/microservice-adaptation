package com.github.adalrsjr1.automaton;

import com.github.adalrsjr1.specpatterns.PropertyInstance
import com.google.common.base.Stopwatch;

import jhoafparser.consumer.HOAConsumerPrint
import jhoafparser.consumer.HOAConsumerStore
import jhoafparser.parser.HOAFParser;
import jhoafparser.storage.StoredAutomaton;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class AutomatonFactory {

	private static final Logger log = LoggerFactory.getLogger(AutomatonFactory.class)

	private static final String LTL_GENERATOR = "ltl2tgba"
	private static final String LTL_GENERATOR_ARGS = "-D -G -B --lenient"

	/**
	 * only construct automaton on windows with Spot ModelChecker installed at bash
	 * @param property
	 * @return
	 */
	static StoredAutomaton createAutomaton(PropertyInstance property) {
		final Stopwatch stopwatch = Stopwatch.createStarted()
		String spotCommand = "\"${LTL_GENERATOR} ${LTL_GENERATOR_ARGS} \'${property.toString()}\'\""
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", spotCommand)
		Process process = builder.start()
		
		PipedInputStream input = new PipedInputStream()
		PipedOutputStream output = new PipedOutputStream(input)
		
		process.waitForProcessOutput(output, System.err);

		HOAConsumerStore consumerStore = new HOAConsumerStore()
		// for debug purpose
//		 HOAFParser.parseHOA(input, new HOAConsumerPrint(System.out))
		HOAFParser.parseHOA(input, consumerStore)
		stopwatch.stop()
		log.info "Automaton for property '${property}' created in ${stopwatch}"
		return consumerStore.getStoredAutomaton()
	}
	
	static StoredAutomaton createAutomaton(String property) {
		final Stopwatch stopwatch = Stopwatch.createStarted()
		String spotCommand = "\"${LTL_GENERATOR} ${LTL_GENERATOR_ARGS} \'${property}\'\""
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", spotCommand)
		Process process = builder.start()
		
		PipedInputStream input = new PipedInputStream()
		PipedOutputStream output = new PipedOutputStream(input)
		
		process.waitForProcessOutput(output, System.err);

		HOAConsumerStore consumerStore = new HOAConsumerStore()
		// for debug purpose
//		 HOAFParser.parseHOA(input, new HOAConsumerPrint(System.out))
		HOAFParser.parseHOA(input, consumerStore)
		stopwatch.stop()
		log.info "Automaton for property '${property}' created in ${stopwatch}"
		return consumerStore.getStoredAutomaton()
	}
}
