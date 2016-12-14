package com.adalrsjr.processor_unit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance
import br.cin.gfads.adalrsjr1.verifier.processingunits.OneByOneProcessingUnit
import br.cin.gfads.adalrsjr1.verifier.properties.TestProperty

public class TestOneByOneProcessingUnit extends GroovyTestCase {

	private static final Logger log = LoggerFactory.getLogger(TestOneByOneProcessingUnit.class);

	public void test() {
		PropertyInstance p = new TestProperty()
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(p)
		
		Thread.start {
			pu
		}
		pu.start()
		
		Thread.sleep(1000);
		
		pu.toEvaluate(new SymptomEvent(null, [idx:"3"])); //0
		pu.toEvaluate(new SymptomEvent(null, [idx:"3"])); //1
		pu.toEvaluate(new SymptomEvent(null, [idx:"3"])); //2
		pu.toEvaluate(new SymptomEvent(null, [idx:"3"])); //3
	}
	
}
