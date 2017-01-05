package br.gfads.cin.logprocessor

abstract class Processor {
	List values = new LinkedList()
	
	def execute(Map entry) { } 
	def reset() { }
	
	def toTxt(String name) {
		File f = new File(name)
		values.each {
			f.write(it.toString())
		}
	}
}
