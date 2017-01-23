package br.cin.gfads.adalrsjr1.common.events

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

import groovy.transform.ToString

@ToString(includeNames=true,includeFields=true,includePackage=false,includeSuper=true)
public class SymptomEvent extends CommonEvent {
	private static final Logger log = LoggerFactory.getLogger(SymptomEvent.class)
	
	@JsonProperty("content")
	private Map content
	
	public Long TIME 
	
	public SymptomEvent() { 
		super()
	}
	
	def init(byte[] message) {
		content = deserialize(message)
		TIME = tryGet('timeMillis', Long)
		return tryGet('container_id')
	}
	
	public SymptomEvent(byte[] message) {
		super("")
		super.source = init(message)
		
	}
	
//	public SymptomEvent(Object source, Map content) {
//		super(source)
////		TIME = System.nanoTime()
//		TIME = tryGet('timeMillis', Long)
//		this.content = content
//	}
	
	public Map getContent() {
		return content
	}
	
	public Object getSource() {
		return super.source
	}
	
	public String tryGet(String key) {
		content.getOrDefault(key, content['log'].getOrDefault(key, content['log']['message'][key]))
	}
	
	public def tryGet(String key, Class clazz) {
		def value = tryGet(key)
		
		value ? clazz."parse${clazz.simpleName}"(value) : null
	}
	
	@Override
	Object deserialize(byte[] byteMessage) {
		String message = new String(byteMessage)
		ObjectMapper mapper = new ObjectMapper()
		Map dockerLog = [:]
		dockerLog = mapper.readValue(message, new TypeReference<Map<String, String>>(){})

		ObjectMapper mapper2 = new ObjectMapper()
		Map<String,String> javaLog = [:]
		
		def value = dockerLog["log"]
		if(value)
			javaLog = mapper.readValue(value, new TypeReference<Map<String, String>>(){})
		
		ObjectMapper mapper3 = new ObjectMapper()
		Map<String, String> appLog = [:]
		value = javaLog.get("message")
		if(value)
			appLog = mapper.readValue(javaLog.get("message"), new TypeReference<Map<String, String>>(){})
		
		javaLog['message'] = appLog
		dockerLog['log'] = javaLog
		
		return dockerLog
	}
	
	@Override
	byte[] serialize() {
		ObjectMapper mapper = new ObjectMapper()
		return mapper.writeValueAsBytes(this)
//		throw new RuntimeException("This class cannot be serilized")
	}
}
