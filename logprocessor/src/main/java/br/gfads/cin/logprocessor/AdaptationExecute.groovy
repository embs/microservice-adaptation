package br.gfads.cin.logprocessor

import java.math.RoundingMode
import java.util.Map
import java.util.concurrent.TimeUnit

class AdaptationExecute extends Processor {
	def key = ""
	
	@Override
	public Object execute(Map entry) {
		if(entry.level == "TRACE" && entry.message instanceof Map && entry.message.method == key) {
			values << entry.message.time.toLong()
		}
	}

	@Override
	public Object reset() {
		key = ""
		values.clear()
	}
	
	public def avg() {
		for(int i = 0; i < 10; i++) {
			values.remove(i)
		}
		for(int i = 0; i < 10; i++) {
			values.remove(values.size()-1)
		}
		
		def sum = values.inject { s , e ->
			s + e
		}
		
		TimeUnit.NANOSECONDS.toMillis(sum)/ values.size()
		
	}
}
