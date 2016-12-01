package com.github.adalrsjr1.automaton;

import java.util.concurrent.TimeUnit

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.adalrsjr1.automaton.events.AutomatonEvent
import com.github.adalrsjr1.automaton.events.AutomatonListener
import com.github.adalrsjr1.automaton.events.AutomatonTransitionEvent
import com.github.adalrsjr1.automaton.visualizer.AutomatonVisuzalizer
import com.github.adalrsjr1.specpatterns.ExpressionVariable
import com.github.adalrsjr1.specpatterns.Property
import com.github.adalrsjr1.specpatterns.PropertyInstance
import com.github.adalrsjr1.specpatterns.PropertyPattern
import jhoafparser.storage.StoredAutomaton

public class App implements AutomatonListener {

	private static final Logger log = LoggerFactory.getLogger(App.class);

	void notify(AutomatonEvent event) {
		log.trace event.toString()
	}
	
	public static void main(String[] args) {
		App app = new App()
		ExpressionVariable a = new ExpressionVariable("(req_host_dst:172.017.000.006) && (response:200)")
		ExpressionVariable b = new ExpressionVariable("(req_host_src:172.017.000.001) && (req_method:GET)")
		
		PropertyPattern pp = Property.create()
		PropertyInstance property = pp.response(a)
									  .respondsTo(b)
									  .globally()
									  .build()
		
		StoredAutomaton storedAutomaton = AutomatonFactory.createAutomaton(property)
		AutomatonEngine engine = new AutomatonEngine(storedAutomaton, TimeUnit.NANOSECONDS)
		
		AutomatonVisuzalizer visualizer = new AutomatonVisuzalizer(engine)
		visualizer.createGraph()
		
		engine.addListener(app)
		engine.addListener(visualizer)
		
		while(1) {
		engine.transition(new AutomatonTransitionEvent([req_host_src:"172.017.000.001", req_host_dst:"172.017.000.005", req_method:"GET", response:"300"]))
//		Thread.sleep(10)
		engine.transition(new AutomatonTransitionEvent([req_host_src:"172.017.000.001", req_host_dst:"172.017.000.005", req_method:"GET", response:"200"]))
//		Thread.sleep(10)
		engine.transition(new AutomatonTransitionEvent([req_host_src:"172.017.000.001", req_host_dst:"172.017.000.006", req_method:"GET", response:"200"]))
//		Thread.sleep(10)
		engine.transition(new AutomatonTransitionEvent([req_host_src:"172.017.000.002", req_host_dst:"172.017.000.006", req_method:"POST", response:"200"]))
//		Thread.sleep(10)
		}
	}
	
}
