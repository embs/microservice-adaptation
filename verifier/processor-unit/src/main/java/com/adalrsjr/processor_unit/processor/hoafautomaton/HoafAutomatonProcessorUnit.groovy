package com.adalrsjr.processor_unit.processor.hoafautomaton

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import groovy.transform.Memoized
import groovy.util.logging.Slf4j

import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.Map
import java.util.Set

import jhoafparser.ast.BooleanExpression
import jhoafparser.consumer.HOAConsumerStore
import jhoafparser.parser.HOAFParser
import jhoafparser.storage.StoredAutomaton

import com.adalrsjr.graphview.HoafAutomatonVisualizer
import com.adalrsjr.processor_unit.fluentd.pubsub.IPublisher
import com.adalrsjr.processor_unit.processor.IProcessorUnit
import com.adalrsjr.processor_unit.processor.IProcessorUnitEvent
import com.adalrsjr.processor_unit.processor.IProcessorUnitListener


//interface IHoafAutomatonListener {
//	void reachNewStateEvent(int prevState, int nextState, boolean isAcceptanceState, long timeAtPrevState, Map event)
//	void stop()
//}

@Immutable
class HoafEvent implements IProcessorUnitEvent {
	int prevState
	int nextState
	boolean isAcceptanceState
	long timeAtPrevState
	Map event
}

@Slf4j
class HoafAutomatonProcessorUnit implements IProcessorUnit {
	private static final String LTL_GENERATOR = "ltl2tgba"
	private static final String LTL_GENERATOR_ARGS = "-B -D -G --lenient"

	StoredAutomaton storedAutomaton

	protected int currentState = 0
	protected long timeInCurrentState = 0

	private Set<IProcessorUnitListener> listeners = [] as Set
	
	private IPublisher publisher
	
	HoafAutomatonProcessorUnit(String property) {
		String spotCommand = "${LTL_GENERATOR} ${LTL_GENERATOR_ARGS} \'${property.toString()}\'"
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", spotCommand)
		Process process = builder.start()

		PipedInputStream input = new PipedInputStream()
		PipedOutputStream out = new PipedOutputStream(input)

		process.waitForProcessOutput(out, System.err)

		HOAConsumerStore consumerStore = new HOAConsumerStore()
//				HOAFParser.parseHOA(input, new HOAConsumerPrint(System.out))
		HOAFParser.parseHOA(input, consumerStore)
		storedAutomaton = consumerStore.getStoredAutomaton()
	}

	@Override
	void setPublisher(IPublisher publisher) {
		this.publisher = publisher
	}
	
	@Override
	IPublisher getPublisher() {
		publisher
	}
	
	@Override
	void publish(Map message) {
		if(publisher) {
			publisher.publish(message)
		} else {
			log.warn "need to set a publisher at ${this}"
		}
	}
	
	@Override
	void registerListener(IProcessorUnitListener listener) {
		listeners << listener
	}

	@Override
	void unregisterListener(IProcessorUnitListener  listener) {
		listeners.remove(listener)
	}

	@Override
	void notifyListeners(IProcessorUnitEvent event) {
		log.debug "prevState:${event.prevState} nextState:${event.nextState} accState:${event.isAcceptanceState} time:${event.timeAtPrevState} ev:${event.event}"
		for(listener in listeners) {
			listener.beNotified(event)
		}
	}

	int getCurrentState() {
		currentState
	}

	private void setCurrentState(int state, Map event) {
		int prevState = currentState
		long newTime = System.currentTimeMillis()
		currentState = state
		notifyListeners(new HoafEvent(prevState, state, isInAcceptanceState(), System.currentTimeMillis()-timeInCurrentState, event))
		timeInCurrentState = newTime
	}

	boolean isInAcceptanceState() {
		boolean result = storedAutomaton.getStoredState(currentState).accSignature != null
		return result
	}

	@Memoized
	private Map stringToMap(String token) {
		def splited = token.split(":")
		def map = [:]
		map[splited[0]] = splited[1]
		return map
	}

	@Memoized
	boolean evaluate(BooleanExpression expression, Map toEvaluate) {
		BooleanExpression root = expression
		boolean leftResult = null, rightResult = null, result = true
		if(expression.left != null)
			leftResult = evaluate(expression.left, toEvaluate)

		if(expression.right != null)
			rightResult = evaluate(expression.right, toEvaluate)

		if(expression.left == null && expression.right == null && root != null) {
			if(root.type == BooleanExpression.Type.EXP_ATOM) {
				int nRoot = root.toString().toInteger()

				String token = storedAutomaton.storedHeader.APs[nRoot]
				Map mapToken = stringToMap(token)

				// if intersection is empty then there aren't correspondence
				return toEvaluate.intersect(mapToken).size() != 0

			}
			if(BooleanExpression.Type.EXP_TRUE == expression.type) {
				return true
			}
			if(BooleanExpression.Type.EXP_FALSE == expression.type) {
				return false
			}
		}

		if(BooleanExpression.Type.EXP_NOT == expression.type) {
			result = leftResult == null ? !rightResult : !leftResult

		}
		else if(BooleanExpression.Type.EXP_OR == expression.type) {
			result = leftResult || rightResult
		}
		else if(BooleanExpression.Type.EXP_AND == expression.type) {
			result = leftResult && rightResult
		}
		return result
	}

	boolean transition(int currentState, Map event) {
		def transitions = storedAutomaton.getEdgesWithLabel(currentState)

		for(transition in transitions) {
			boolean result = evaluate(transition.labelExpr, event)
			if(result) {
				setCurrentState(transition.conjSuccessors.first(), event)
				return true
			}
		}
		return false
	}

	boolean next(Map event) {
		def result = transition(currentState, event)
		return result
	}
 
	@Override
	public Object process(Map object) {
		def init = System.nanoTime()
		next(object)
		log.debug "evaluated in ${(System.nanoTime() - init)/1000000} ms"
	}

	void cleanup() {
		listeners.each {
			it.stop()
		}
		listeners.clear()
	}
	
	public static void main(String[] args) {
		//		AutomatonProcessorUnit automaton = ProcessorUnitFactory.createLtlProcessorUnit("G(\"context:REQUEST\"->F\"statusCode:404\")", true)
		//		HoafAutomaton automaton = new HoafAutomaton("G(\"req_host_src:172.017.000.001\" && \"res_host_dst:172.017.000.006\" -> F \"req_host_dst:172.017.000.001\" && \"res_host_src:172.017.000.006\")")
		HoafAutomatonProcessorUnit automaton = new HoafAutomatonProcessorUnit("G((req_host_src:172.017.000.001) && (req_method:GET)->F(req_host_dst:172.017.000.006) && (response:200))")
//G("req_host_src:172.017.000.001"->F"req_host_dst:172.017.000.006")
		HoafAutomatonVisualizer visualizer = new HoafAutomatonVisualizer(automaton)
		automaton.registerListener(visualizer)
		visualizer.createGraph()
		visualizer.show()
		
		while(true) {
			automaton.next([req_host_src:"172.017.000.001", req_host_dst:"172.017.000.005", req_method:"GET", response:"300"])
			Thread.sleep(500)
			automaton.next([req_host_src:"172.017.000.001", req_host_dst:"172.017.000.005", req_method:"GET", response:"200"])
			Thread.sleep(500)
			automaton.next([req_host_src:"172.017.000.001", req_host_dst:"172.017.000.006", req_method:"GET", response:"200"])
			Thread.sleep(500)
			automaton.next([req_host_src:"172.017.000.002", req_host_dst:"172.017.000.006", req_method:"POST", response:"200"])
			Thread.sleep(500)
			
		}


		//		automaton.handle([context:"REQUEST"]);
	}

	
}
