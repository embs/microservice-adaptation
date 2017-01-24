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

	@JsonProperty("time")
	public Long time

	public SymptomEvent() {
		super()
		time = System.currentTimeMillis()
	}

	public SymptomEvent(byte[] message) {
		content = deserialize(message)
		time = tryGet('timeMillis',Long)
		
		if(!time) {
			time = System.currentTimeMillis()
		}
		
		source = tryGet('container_id')
	}

	public Long getTime() {
		return time
	}

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

		if(value != null) {
			javaLog = mapper.readValue(value, new TypeReference<Map<String, String>>(){})
		}

		ObjectMapper mapper3 = new ObjectMapper()
		Map<String, String> appLog = [:]
		value = javaLog.get("message")

		if(value) {
			appLog = mapper.readValue(javaLog.get("message"), new TypeReference<Map<String, String>>(){})
		}

		if(!appLog.empty)
			javaLog['message'] = appLog

		if(!javaLog.empty)
			dockerLog['log'] = javaLog

		return dockerLog
	}
	
	@Override
	byte[] serialize() {
		throw new UnsupportedOperationException("Not Implemented yet")		
	}
}
