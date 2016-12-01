package br.cin.gfads.adalrsjr1.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper

import groovy.transform.Immutable
import groovy.transform.ToString
import groovy.transform.builder.Builder

@ToString(includeNames=true,includeFields=true,includePackage=false)
public class MicroserviceInfo {

	private static final Logger log = LoggerFactory.getLogger(MicroserviceInfo.class);

	@JsonProperty("microserviceName")
	private String microserviceName
	@JsonProperty("host")
	private String host
	
	MicroserviceInfo(@JsonProperty("microserviceName") microserviceName,
					 @JsonProperty("host") host) {
		this.microserviceName = microserviceName
		this.host = host
	}

	byte[] serialize() {
		ObjectMapper mapper = new ObjectMapper()
		mapper.writeValueAsBytes(this)
	}
	
	static MicroserviceInfo deserialize(byte[] content) {
		ObjectMapper mapper = new ObjectMapper()
		mapper.readValue(content, MicroserviceInfo.class)
	}
					 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((microserviceName == null) ? 0 : microserviceName.hashCode());
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
		MicroserviceInfo other = (MicroserviceInfo) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (microserviceName == null) {
			if (other.microserviceName != null)
				return false;
		} else if (!microserviceName.equals(other.microserviceName))
			return false;
		return true;
	}

	public String getMicroserviceName() {
		return microserviceName;
	}

	public String getHost() {
		return host;
	}
	
}
