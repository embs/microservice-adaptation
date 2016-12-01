package br.cin.gfads.adalrsjr1.common.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

import br.cin.gfads.adalrsjr1.common.MicroserviceInfo
import groovy.transform.ToString

@ToString(includeNames=true,includeFields=true,includePackage=false,includeSuper=true)
public class SymptomEvent extends CommonEvent {
	private static final Logger log = LoggerFactory.getLogger(SymptomEvent.class)
	
	@JsonProperty("content")
	private Map<String, String> content
	
	public SymptomEvent() { super() }
	
	public SymptomEvent(@JsonProperty("source") MicroserviceInfo source, 
						@JsonProperty("content") Map<String, String> content) {
		super(source)
		this.content = Collections.unmodifiableMap(content)
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SymptomEvent other = (SymptomEvent) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		return true;
	}

	public Map getContent() {
		return content
	}
	
	public MicroserviceInfo getSource() {
		return super.source
	}
	
}
