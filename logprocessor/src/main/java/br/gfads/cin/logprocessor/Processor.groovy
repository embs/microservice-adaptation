package br.gfads.cin.logprocessor

abstract class Processor {
	List values = new LinkedList()

	def execute(Map entry) { }
	def reset() { }

	def toTxt(String name) {
		new File(name).withWriter('utf-8') { writer ->
			values.each { entry ->
				writer.writeLine(entry.toString())
			}
		}
	}
}
