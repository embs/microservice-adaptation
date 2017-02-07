package br.cin.gfads.adalrsjr1.docker_monitor

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.DockerClient.ListContainersFilterParam
import com.spotify.docker.client.DockerClient.ListContainersParam
import com.spotify.docker.client.messages.ContainerStats

class DockerStatsMonitor {
	public static void main(String[] args) {
		DockerClient docker = DefaultDockerClient.fromEnv().build()
		
		try {
	//		DefaultDockerClient.builder()
	//				           .uri("localhost:2375")
	//			               .connectionPoolSize(1)
	//				           .build()
			
			String containerId = docker.listContainers(ListContainersParam.allContainers()).get(0).id()
							   		
			ContainerStats stats = docker.stats(containerId);
			
			println stats.memoryStats()
			
		}
		finally {
			docker.close()
		}
	}
}
