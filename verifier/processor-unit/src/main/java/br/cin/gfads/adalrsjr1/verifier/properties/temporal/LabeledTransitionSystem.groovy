package br.cin.gfads.adalrsjr1.verifier.properties.temporal

import java.nio.file.Watchable
import java.util.concurrent.TimeUnit

import javax.swing.text.TabStop

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.common.base.Stopwatch

import br.cin.gfads.adalrsjr1.common.Util
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent
import groovy.transform.CompileStatic
import groovy.transform.Memoized
import jhoafparser.ast.BooleanExpression
import jhoafparser.consumer.HOAConsumerPrint
import jhoafparser.consumer.HOAConsumerStore
import jhoafparser.parser.HOAFParser
import jhoafparser.storage.StoredAutomaton
import jhoafparser.storage.StoredState

public class LabeledTransitionSystem {

	private static final Logger log = LoggerFactory.getLogger(LabeledTransitionSystem.class)

	private StoredAutomaton storedAutomaton
	private int currentState = 0
	private Stopwatch timeInCurrentState
	private TimeUnit timeUnit
	
	private List<LabeledTransitionSystemListener> listeners = []
	
	private LabeledTransitionSystem(StoredAutomaton automaton, TimeUnit standardTimeUnit) {
		storedAutomaton = automaton
		timeUnit = standardTimeUnit
		timeInCurrentState = Stopwatch.createStarted()
	}
	
	public void addListener(LabeledTransitionSystemListener listener) {
		listeners << listener
	}
	
	public void removeListener(LabeledTransitionSystemListener listener) {
		listeners.remove(listener)
	}
	
	public void clearListeners() {
		listeners.clear()
	}
	
	private notifyListeners(LabeledTransitionSystemEvent event) {
		log.debug "$event"
		listeners.each { LabeledTransitionSystemListener l ->
			l.notifyTransition(event)
		}
	}
	
	public static LabeledTransitionSystem labeledTransitionSystemFactory(String ltlProperty, TimeUnit timeUnit) {
		Stopwatch watch = Stopwatch.createStarted()
		String LTL_GENERATOR = "ltl2tgba"
		String LTL_GENERATOR_ARGS = "-B -D --lenient"
		
		String spotCommand = "${LTL_GENERATOR} ${LTL_GENERATOR_ARGS} \'${ltlProperty.toString()}\'"
		ProcessBuilder builder 
		
		if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			builder = new ProcessBuilder("bash", "-c", spotCommand)
		}
		else {
			println "toAKI"
			builder = new ProcessBuilder(LTL_GENERATOR, "-B", "-D", "--lenient", ltlProperty.toString())
		}
		
		Process process = builder.start()

		PipedInputStream input = new PipedInputStream()
		PipedOutputStream out = new PipedOutputStream(input)

		process.waitForProcessOutput(out, System.err)
		
		BufferedInputStream bis = new BufferedInputStream(input)
		bis.mark(0)
		
		HOAConsumerStore consumerStore = new HOAConsumerStore()
		HOAFParser.parseHOA(bis, consumerStore)
		StoredAutomaton storedAutomaton = consumerStore.getStoredAutomaton()
	    
		bis.reset()
		HOAFParser.parseHOA(bis, new HOAConsumerPrint(System.out))
		LabeledTransitionSystem lts = new LabeledTransitionSystem(storedAutomaton, TimeUnit.MICROSECONDS)
		Util.mavericLog(log, this.class, "labeledTrasitionSystemFactory", watch.stop())
		return new LabeledTransitionSystem(storedAutomaton, TimeUnit.MICROSECONDS)
	}
	
	public int getCurrentState() {
		return currentState
	}
	
	public long getElapsedTimeInCurrentState() {
		return timeInCurrentState.elapsed(TimeUnit.MILLISECONDS)
	}
	
	public long getElapsedTimeInCurrentState(TimeUnit timeUnit) {
		return timeInCurrentState.elapsed(timeUnit)
	}
	
	public void resetLts() {
		setCurrentState(0, new SymptomEvent())
	}
	
	private void setCurrentState(int state, SymptomEvent event) {
		int oldState = currentState
		long time = getElapsedTimeInCurrentState(timeUnit)
		currentState = state
		timeInCurrentState.reset()
		timeInCurrentState.start()
		notifyListeners(
			LabeledTransitionSystemEvent.newEvent(
				oldState, 
				currentState, 
				time, 
				timeUnit,
				isInAcceptanceState(),
				event))
	}
	
	public boolean isInAcceptanceState() {
		return storedAutomaton.getStoredState(currentState).getAccSignature() != null
	}
	
	private Map stringToMap(String token) {
		def splited = token.split(":")
		return [(splited[0]):splited[1]]
	}
	
	private boolean check(Map token, SymptomEvent symptom) {
		boolean result = true
		token.each { key, value ->
			String symptomResult = symptom.tryGet(key)
			result &= (symptomResult ==~ /$value/)
			log.debug "SymptomResult: ${symptomResult} :: value:${value} :: result:${result}"
		}
		return result
	}

	boolean evaluate(BooleanExpression expression, SymptomEvent toEvaluate) {
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

				return check(mapToken, toEvaluate)

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
	
	boolean transition(int currentState, SymptomEvent event) {
		def transitions = storedAutomaton.getEdgesWithLabel(currentState)

		for(transition in transitions) {
			boolean result = evaluate(transition.labelExpr, event)
			if(result) {
				setCurrentState(transition.conjSuccessors.first(), event)
				return isInAcceptanceState()
			}
		}
		return isInAcceptanceState()
	}
	
	boolean next(SymptomEvent event) {
		Stopwatch watch = Stopwatch.createStarted()
		boolean result = transition(currentState, event)
		Util.mavericLog(log, this.class, "next", watch.stop())
		return transition(currentState, event)
	}
	
	public static void main(String[] args) {
//		String LTL_GENERATOR = "ltl2tgba"
//		String LTL_GENERATOR_ARGS = "-B -D -G --lenient"
//		String property = "G((a:10)->F(b:11))"
//		
//		String spotCommand = "${LTL_GENERATOR} ${LTL_GENERATOR_ARGS} \'${property.toString()}\'"
//		ProcessBuilder builder = new ProcessBuilder("bash", "-c", spotCommand)
//		Process process = builder.start()
//
//		PipedInputStream input = new PipedInputStream()
//		PipedOutputStream out = new PipedOutputStream(input)
//
//		process.waitForProcessOutput(out, System.err)
//		
//		BufferedInputStream bis = new BufferedInputStream(input)
//		bis.mark(0)
//		
//		HOAConsumerStore consumerStore = new HOAConsumerStore()
//		HOAFParser.parseHOA(bis, consumerStore)
//		StoredAutomaton storedAutomaton = consumerStore.getStoredAutomaton()
//	    
//		bis.reset()
//		HOAFParser.parseHOA(bis, new HOAConsumerPrint(System.out))
//		println ""
//		LabeledTransitionSystem lts = new LabeledTransitionSystem(storedAutomaton, TimeUnit.MICROSECONDS)
//		
//		lts.addListener([notifyTransition:{println it}] as LabeledTransitionSystemListener)
//		
//		for(int i = 0; i < 1000; i++) 
//			{
//		SymptomEvent event1 = new SymptomEvent(null, [log:[message:[b:"11"]]])
//		lts.next(event1)
//		SymptomEvent event2 = new SymptomEvent(null, [log:[message:[a:"10", b:"10"]]])
//		lts.next(event2)
//		SymptomEvent event3 = new SymptomEvent(null, [log:[message:[a:"10", b:"10"]]])
//		lts.next(event3)
//		SymptomEvent event4 = new SymptomEvent(null, [log:[message:[a:"10", b:"11"]]])
//		lts.next(event4)
//			}
		
		println "/client.1.cqzx9brzfgyv96xiii66g9n5m" ==~ /.client(.)*/
	}
	
}
