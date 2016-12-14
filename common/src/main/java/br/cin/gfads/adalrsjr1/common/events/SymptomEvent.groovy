package br.cin.gfads.adalrsjr1.common.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

import br.cin.gfads.adalrsjr1.common.MicroserviceInfo
import groovy.transform.ToString

@ToString(includeNames=true,includeFields=true,includePackage=false,includeSuper=true)
public class SymptomEvent extends CommonEvent {
	private static final Logger log = LoggerFactory.getLogger(SymptomEvent.class)
	
	@JsonProperty("content")
	private Map content
	
	public final long TIME; 
	
	public SymptomEvent() { 
		super()
		
	}
	
	public SymptomEvent(byte[] message) {
		this(null, message);
	}
	
	public SymptomEvent(Object source, byte[] message) {
		TIME = System.nanoTime();
		content = deserialize(message)
		super.source = tryGet('container_id')
	}
	
	public SymptomEvent(Object source, Map content) {
		super(source)
		TIME = System.nanoTime();
		this.content = content
	}
	
	public Map getContent() {
		return content
	}
	
	public Object getSource() {
		return super.source
	}
	
	public String tryGet(String key) {
		content.getOrDefault(key, content['log'].getOrDefault(key, content['log']['message']['key']))
	}
	
	@Override
	Object deserialize(byte[] byteMessage) {
		String message = new String(byteMessage)
		ObjectMapper mapper = new ObjectMapper()
		Map dockerLog = [:]
		dockerLog = mapper.readValue(message, new TypeReference<Map<String, String>>(){})

		ObjectMapper mapper2 = new ObjectMapper()
		Map javaLog = [:]
		javaLog = mapper.readValue(dockerLog["log"], new TypeReference<Map<String, String>>(){})
		
		ObjectMapper mapper3 = new ObjectMapper()
		Map appLog = [:]
		appLog = mapper.readValue(javaLog.get("message"), new TypeReference<Map<String, String>>(){})
		
		javaLog['message'] = appLog
		dockerLog['log'] = javaLog
		
		return dockerLog
	}
	
	@Override
	byte[] serialize() {
		throw new RuntimeException("This class cannot be serilized")
	}
}
