package br.cin.gfads.adalrsjr1.common.events;

import br.cin.gfads.adalrsjr1.common.MicroserviceInfo
import groovy.transform.ToString

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory;

@ToString(includeNames=true,includeFields=true,includePackage=false,includeSuper=true)
public class ChangeRequestEvent extends CommonEvent {

	private static final Logger log = LoggerFactory.getLogger(ChangeRequestEvent.class)

	@JsonProperty("name")
	private String name
	@JsonProperty("metadata")
	private Map<String, String> metadata
	
	private static ChangeRequestEvent nullChangeRequestEvent = new ChangeRequestEvent("NULL", new Object(), Collections.emptyMap());
	
	public static ChangeRequestEvent getNullChangeRequestEvent() {
		return nullChangeRequestEvent;
	}
	
	ChangeRequestEvent() { super() }
	
	ChangeRequestEvent(@JsonProperty("name") name,
					   @JsonProperty("source") Object source, 
					   @JsonProperty("metadata") Map<String, String> metadata) {
		super(source)
		this.name = name
		this.metadata = Collections.unmodifiableMap(metadata)
	}
	
	String getName() {
		name
	}
					   
	Map getMetadata() {
		metadata
	}
	
	@Override
	MicroserviceInfo getSource() {
		super.source
	}
					   
}
