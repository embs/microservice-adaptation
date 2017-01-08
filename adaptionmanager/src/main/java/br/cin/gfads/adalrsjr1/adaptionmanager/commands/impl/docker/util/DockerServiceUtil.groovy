package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.docker.util

import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.exceptions.DockerCertificateException
import com.spotify.docker.client.messages.swarm.ContainerSpec
import com.spotify.docker.client.messages.swarm.Driver
import com.spotify.docker.client.messages.swarm.EndpointSpec
import com.spotify.docker.client.messages.swarm.GlobalService
import com.spotify.docker.client.messages.swarm.PortConfig
import com.spotify.docker.client.messages.swarm.ReplicatedService
import com.spotify.docker.client.messages.swarm.Service
import com.spotify.docker.client.messages.swarm.ServiceMode
import com.spotify.docker.client.messages.swarm.ServiceSpec
import com.spotify.docker.client.messages.swarm.TaskSpec
import com.spotify.docker.client.messages.swarm.UpdateConfig

import groovy.sql.Sql.CreateStatementCommand
import metamodel.CommandParameter
import metamodel.CompositeCommandParameter
import metamodel.ICommandParameter

public class DockerServiceUtil {

	private static final Logger log = LoggerFactory.getLogger(DockerServiceUtil.class)

	private static DockerClient dockerClient
	
	public static DockerClient getDockerClient() {
		if(dockerClient == null) {
			try {
//				return DefaultDockerClient.builder()
//						.uri(URI.create("http://10.66.66.22:2376"))
//						.build()
				dockerClient= DefaultDockerClient.fromEnv().build()
			} catch (DockerCertificateException e) {
				e.printStackTrace()
			}
		}
		return dockerClient
	}
	
	public static ServiceSpec.Builder serviceSpecBuilder(ServiceSpec spec) {
		return ServiceSpec.builder()
					      .withEndpointSpec(spec.endpointSpec())
					      .withLabels(spec.labels())
					      .withName(spec.name())
					      .withServiceMode(spec.mode())
					      .withTaskTemplate(spec.taskTemplate())
					      .withUpdateConfig(spec.updateConfig())
					      .withNetworks(spec.networks())
	}
	
	public static Driver.Builder logDriverBuilder(Driver driver) {
		Driver.Builder builder =  Driver.builder()
				     					.withName(driver.name())
		
		driver.options.each { k, v ->
			builder.withOption(k,v)
		}							 
										 
		return builder
	}
	
	public static EndpointSpec.Builder endpointSpecBuilder(EndpointSpec spec) {
		return EndpointSpec.builder()
				           .withPorts(spec.ports())
	}
	
	public static ServiceMode.Builder serviceModeBuilder(ServiceMode mode) {
		return ServiceMode.builder()
						  .withGlobalService(mode.global())
						  .withReplicatedService(mode.replicated())
	}
	
	public static TaskSpec.Builder taskSpecBuilder(TaskSpec task) {
		return TaskSpec.builder()
				       .withContainerSpec(task.containerSpec())
				       .withLogDriver(task.logDriver())
				       .withPlacement(task.placement())
				       .withResources(task.resources())
				       .withRestartPolicy(task.restartPolicy())
				       .withNetworks(task.networks())
	}
	
	public static Map compositeCommandToMap(ICommandParameter parameter) {
		if(parameter instanceof CommandParameter) return [(parameter.getKey()):parameter.getValue()]
		
		return parameter.getValue()
		                .collectEntries { 
							[(it.getKey()) : it] 
						}
	}
	
	public static PortConfig.Builder createPortConfigBuilder(ICommandParameter parameters) {
		PortConfig.Builder builder = PortConfig.builder()
		if(!(parameters instanceof CompositeCommandParameter)) return builder
		
		Map<String, String> map = compositeCommandToMap(parameters)
				
		builder.withProtocol(map["protocol"].getValue())
		
		String sPort = map["publishedPort"].getValue()
		if(sPort != "") {
			builder.withPublishedPort(Integer.parseInt(sPort))
		}
		sPort = map["targetPort"].getValue()
		if(sPort != "") {
			builder.withPublishedPort(Integer.parseInt(sPort))
		}
		
		return builder
	}
	
	public static PortConfig createPortConfig(ICommandParameter parameters) {
		createPortConfigBuilder(parameters).build()
	}
	
	public static EndpointSpec.Builder createEndpointSpecBuilder(ICommandParameter parameters) {
		EndpointSpec.Builder builder = EndpointSpec.builder()
		if(!(parameters instanceof CompositeCommandParameter)) return builder
		
		List<PortConfig> ports = []
		parameters.getValue().each { port ->
			ports << createPortConfig(port)
		}
		
		builder.withPorts(ports)
		return builder
	}

	public static EndpointSpec createEndpointSpec(ICommandParameter parameters) {
		createEndpointSpecBuilder(parameters).build()
	}
	
	public static Driver.Builder createLogDriverBuilder(ICommandParameter parameters) {
		Driver.Builder builder = Driver.builder()
		
		if(!(parameters instanceof CompositeCommandParameter)) return builder
		
		parameters.getValue().each { attr -> 
			if(attr.getKey() == "name") {
				builder.withName(attr.getValue())
			}
			else {
				builder.withOption(attr.getKey(), attr.getValue())
			}
		}
		
		return builder
	}
	
	public static Driver createLogDriver(ICommandParameter parameters) {
		createLogDriverBuilder(parameters).build()
	}
	
	public static TaskSpec.Builder createTaskSpecBuilder(ICommandParameter parameters) {
		TaskSpec.Builder builder = TaskSpec.builder()
		
		if(!(parameters instanceof CompositeCommandParameter)) return builder
		
		Map map = compositeCommandToMap(parameters)
		
		builder.withContainerSpec(createContainerSpec(map["containerSpec"]))
		       .withLogDriver(createLogDriver(map["logDriver"]))
			//TODO: withNetworks
			//TODO: withPlacement
			//TODO: withRestartPolicy
			//TODO: withResources
		return builder
	}
	
	public static TaskSpec createTaskSpec(ICommandParameter parameters) {
		createTaskSpecBuilder(parameters).build()
	}

	public static ServiceMode.Builder createServiceModeBuilder(ICommandParameter parameters) {
		ServiceMode.Builder builder = ServiceMode.builder()
		
		if(!(parameters instanceof CompositeCommandParameter)) return builder
		
		Map map = compositeCommandToMap(parameters)
		
		if( map.containsKey("global") ) {
			builder.withGlobalService(new GlobalService())
		}
		else if(map["replicated"]) {
			builder.withReplicatedService(
					ReplicatedService.builder()
									 .withReplicas(Integer.parseInt(map["replicated"].getValue()))
									 .build()
				)
		}
		return builder
	}
	
	public static ServiceMode createServiceMode(ICommandParameter parameters) {
		createServiceModeBuilder(parameters).build()
	}
		
	public static ServiceSpec.Builder serviceSpecBuilder(ICommandParameter parameters) {
		ServiceSpec.Builder builder = ServiceSpec.builder()
		
		if(!(parameters instanceof CompositeCommandParameter)) return builder
		
		Map map = compositeCommandToMap(parameters)
		
		return builder.withEndpointSpec(createEndpointSpec(map["endpointSpec"]))
		              .withName(map["name"].getValue())
			          .withTaskTemplate(createTaskSpec(map["taskTemplate"]))
					  .withServiceMode(createServiceMode(map["mode"]))
	    //TODO: withUpdateConfig
		//TODO: withLabel
	    //TODO: withNetworks
	}
	
	public static ServiceSpec createServiceSpec(ICommandParameter parameters) {
		serviceSpecBuilder(parameters).build()
	}
	
	public static ContainerSpec.Builder createContainerSpecBuilder(ICommandParameter parameters) {
		ContainerSpec.Builder builder = ContainerSpec.builder()
		
		if(!(parameters instanceof CompositeCommandParameter)) return builder
		
		Map map = compositeCommandToMap(parameters)
		
		List<String> command = map["command"].getValue().split(" ").collect() 

		builder.withCommands(command)
//			   .withArgs(null,null)
//			   .withDir("")
//			   .withEnv(null,null)
//			   .withGroups(null,null)
			   .withImage(map["image"].getValue())
//			   .withLabel("", "")
//			   .withMounts(null,null)
//			   .withStopGracePeriod(0L)
//			   .withTty(false)
//			   .withUser("")
	}

	public static ContainerSpec createContainerSpec(ICommandParameter parameters) {
		createContainerSpecBuilder(parameters).build()
	}
}
