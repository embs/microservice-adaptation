package com.adalrsjr.example

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

import com.adalrsjr.processor_unit.Parser
import com.adalrsjr.processor_unit.ProcessorFactory
import com.adalrsjr.processor_unit.processor.ConditionOperator
import com.adalrsjr.processor_unit.processor.Processor
import com.adalrsjr.util.Configuration
import com.google.common.util.concurrent.ThreadFactoryBuilder


class App
{
	public static void main( String[] args )
	{
		Configuration c = Configuration.getInstance()
		String subHost = c.properties["app.trace.subscriber.host"]
		String pubHost = c.properties["app.trace.publisher.host"]
		int subPort = c.properties["app.trace.subscriber.port"].toInteger()
		int pubPort = c.properties["app.trace.publisher.port"].toInteger()
		ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("processor-%d").build()
		Executor tPool = Executors.newCachedThreadPool(threadFactory)
		
//		TraceSubscriber subscriber1 = TraceSubscriber.create("localhost", 5556)
//		Processor processor1 = new Processor(subscriber1) 
//		HoafAutomaton automaton1 = new HoafAutomaton("G(\"req_host_src:172.017.000.001\" && \"req_method:GET\"->F\"req_host_dst:172.017.000.006\" && \"response:200\")")
//		processor1.registerProcessorUnit(automaton1)
		
		
		Processor processor1 = ProcessorFactory.newHoafAutomatonProcessor(subHost, subPort, pubHost,pubPort, Parser.JSON_PARSER, "G(\"req_host_src:172.017.000.001\" && \"req_method:GET\"->F\"req_host_dst:172.017.000.006\" && \"response:200\")")
		Processor processor2 = ProcessorFactory.newHoafAutomatonProcessor(subHost, subPort, pubHost, pubPort, Parser.JSON_PARSER, "G(\"req_method:GET\"->F\"response:200\")")
		Processor processor3 = ProcessorFactory.newTimeResponseProcessor(subHost, subPort, pubHost, pubPort, Parser.JSON_PARSER, 0, ConditionOperator.EQ)
		
		processor3.processor.registerListener({e->
			println ">>> ${e.toString()}"
		})
		
//		TraceSubscriber subscriber2 = TraceSubscriber.create("localhost", 5556)
//		Processor processor2 = new Processor(subscriber2)
//		HoafAutomaton automaton2 = new HoafAutomaton("G(\"req_method:GET\"->F\"response:200\")")
//		processor2.registerProcessorUnit(automaton2)
		
//		HoafAutomatonVisualizer visualizer = new HoafAutomatonVisualizer(automaton1)
//		automaton1.registerListener(visualizer)
//		visualizer.createGraph()
//		visualizer.show()
		
		tPool.execute(processor1)
		tPool.execute(processor2)
		tPool.execute(processor3)
		
				
	}
}

