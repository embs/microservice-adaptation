package br.cin.gfads.adalrsjr1.common.events;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.transform.ToString

@ToString(includeNames=false,includeFields=true,includePackage=false)
public abstract class CommonEvent {

	@JsonProperty("source")
	protected def source 
	
	private static final Logger log = LoggerFactory.getLogger(CommonEvent.class);
	
	public CommonEvent() { }
	
	public CommonEvent(@JsonProperty("source") Object source) {
		this.source = source
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommonEvent other = (CommonEvent) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	public def getSource() {
		source
	}

	byte[] serialize() {
		ObjectMapper mapper = new ObjectMapper()
		mapper.writeValueAsBytes(this)
	}
	
	Object deserialize(byte[] content) {
		ObjectMapper mapper = new ObjectMapper()
		mapper.readValue(content, this.class)
	}
	
}
