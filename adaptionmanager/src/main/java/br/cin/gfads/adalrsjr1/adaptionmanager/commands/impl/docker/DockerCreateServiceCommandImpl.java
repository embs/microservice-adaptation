package br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.docker;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ServiceCreateOptions;
import com.spotify.docker.client.messages.ServiceCreateResponse;
import com.spotify.docker.client.messages.swarm.ServiceSpec;

import br.cin.gfads.adalrsjr1.adaptionmanager.commands.Command;
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.AbstractCommandImpl;
import br.cin.gfads.adalrsjr1.adaptionmanager.commands.impl.docker.util.DockerServiceUtil;
import br.gfads.cin.adalrsjr1.emf.util.EmfLoader;
import metamodel.AdaptationScript;
import metamodel.ICommand;
import metamodel.ICommandParameter;

public class DockerCreateServiceCommandImpl extends AbstractCommandImpl {

	private static final Logger log = LoggerFactory.getLogger(DockerCreateServiceCommandImpl.class);

	@Override
	public Object execute(ICommand metaCommand) throws Exception {
		Stopwatch watch = Stopwatch.createStarted();
		DockerClient docker = DockerServiceUtil.getDockerClient();
		ICommandParameter parameters = metaCommand.getCommandParameter();
		Stopwatch watch2 = Stopwatch.createStarted();
		ServiceSpec serviceSpec = DockerServiceUtil.createServiceSpec(lookup("serviceSpec", parameters));
		ServiceCreateOptions serviceCreateOptions = new ServiceCreateOptions();
		log.info("command {} created in {}", metaCommand.getName(), watch2.stop());
//		ServiceCreateResponse response = docker.createService(serviceSpec, serviceCreateOptions);
//		docker.listTasks();
		log.info("commands {} executed in {}", this, watch.stop());
////		System.out.println(response.id());
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		
		DockerCreateServiceCommandImpl run = new DockerCreateServiceCommandImpl();
		
		AdaptationScript script = EmfLoader.getRootFromXmi("scripts/test.xmi");
		
		System.out.println(script.getCommands().get(0).getCommandParameter());
		
		run.execute(script.getCommands().get(0));
		
	}

}
