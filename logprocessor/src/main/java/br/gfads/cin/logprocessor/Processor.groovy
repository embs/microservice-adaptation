package br.gfads.cin.logprocessor

interface Processor {
	def execute(Map entry) 
	def reset() 
}
