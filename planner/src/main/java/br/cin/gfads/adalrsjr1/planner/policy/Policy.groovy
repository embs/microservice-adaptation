package br.cin.gfads.adalrsjr1.planner.policy;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper

import br.cin.gfads.adalrsjr1.common.events.ChangePlanEvent
import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent
import groovy.transform.ToString
import groovy.transform.builder.Builder

public class PolicyComparator implements Comparator<Policy> {
	@Override
	public int compare(Policy o1, Policy o2) {
		return o1.priority - o2.priority
	}
}

@ToString(includeNames=true,includeFields=true,includePackage=false)
public class Policy implements Comparable<Policy> {
	private static final Logger log = LoggerFactory.getLogger(Policy.class);

	@JsonProperty("changeRequest")
	private String changeRequest;
	@JsonProperty("action")
	private String action;
	@JsonProperty("priority")
	private int priority

	/**
	 * Builder with class names as arguments
	 * @param source
	 * @param action
	 */
	private Policy(@JsonProperty("changeRequest") String changeRequest,
	               @JsonProperty("action") String action,
	               @JsonProperty("priority") int priority) {
		this.changeRequest = changeRequest
		this.action = action
		this.priority = priority
	}

	public String getChangeRequest() {
		return changeRequest;
	}

	public String getAction() {
		return action;
	}

	public int getPriority() {
		return priority;
	}

	String serializeToString() {
		ObjectMapper mapper = new ObjectMapper()
		mapper.writeValueAsString(this)
	}

	String serializeToBytes() {
		ObjectMapper mapper = new ObjectMapper()
		mapper.writeValueAsBytes(this)
	}

	static Policy deserialize(String content) {
		ObjectMapper mapper = new ObjectMapper()
		mapper.readValue(content, Policy.class)
	}

	static Policy deserialize(byte[] content) {
		ObjectMapper mapper = new ObjectMapper()
		mapper.readValue(content, Policy.class)
	}

	@Override
	public int compareTo(Policy o) {
		if(o.priority == priority) {
			return action.compareTo(o.action)
		}
		return Integer.compare(o.priority, priority)
	}
}
