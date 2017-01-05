package br.gfads.cin.logprocessor

import java.util.List
import java.util.Map
import java.util.concurrent.TimeUnit

class Planner extends Processor {
	@Override
	public Object execute(Map entry) {
		if(entry.level == "TRACE" && entry.message instanceof Map && entry.message.method == "handle") {
			values << entry.message.time.toLong()
		}
	}

	@Override
	public Object reset() {
		values.clear()
	}
	
	public def avg() {
		for(int i = 0; i < 10; i++) {
			values.remove(i)
		}
		for(int i = 0; i < 10; i++) {
			values.remove(values.size()-1)
		}
		
		def sum = values.inject { s, e ->
			s + e
		}
		TimeUnit.NANOSECONDS.toMillis(sum)/ values.size()
	}
	
}
