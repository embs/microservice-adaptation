package br.cin.gfads.adalrsjr1.common.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.cin.gfads.adalrsjr1.common.MicroserviceInfo
import com.fasterxml.jackson.annotation.JsonProperty

import groovy.transform.ToString

@ToString(includeNames=true,includeFields=true,includePackage=false,includeSuper=true)
public class ChangePlanEvent extends CommonEvent implements Comparable {

	private static final Logger log = LoggerFactory.getLogger(ChangePlanEvent.class);

	@JsonProperty("priority")
	private int priority
	@JsonProperty("adaptationScript")
	private String adaptationScript
	
	ChangePlanEvent() { super() }
	
	/**
	 * 
	 * @param source
	 * @param priority
	 * @param adaptationScript Class name of adaptation script xxx.yyy.AdaptationScriptClassName
	 */
	ChangePlanEvent(@JsonProperty("source") Object source,
				    @JsonProperty("priority") int priority,
				    @JsonProperty("adaptationScript") String adaptationScript) {
		super(source)
		this.priority = priority
		this.adaptationScript = adaptationScript
	}
					
	int getPriority() {
		priority
	}
	
	String getAdaptationScript() {
		adaptationScript
	}

	public int compareTo(Object o) {
		ChangePlanEvent changeEvent = o as ChangePlanEvent
		if(changeEvent.getPriority() == priority) {
			return adaptationScript.compareTo(changeEvent.getAdaptationScript())
		}
		return Integer.compare(changeEvent.priority, priority)
	}

}
