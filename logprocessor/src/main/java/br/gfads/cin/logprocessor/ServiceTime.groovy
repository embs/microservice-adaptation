package br.gfads.cin.logprocessor

import java.util.concurrent.TimeUnit

public class ServiceTime extends Processor {
	List values = new LinkedList()
	
	@Override
	public Object execute(Map entry) {
		if(entry.level == "TRACE" && entry.message instanceof Map &&
		entry.message.clazz == "br.cin.gfads.adalrsjr1.verifier.properties.ResponseTimeProperty" && entry.message.method == "serviceTime") {
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
