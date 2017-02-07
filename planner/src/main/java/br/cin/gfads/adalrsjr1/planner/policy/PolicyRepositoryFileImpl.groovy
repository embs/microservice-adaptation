package br.cin.gfads.adalrsjr1.planner.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;
import br.cin.gfads.adalrsjr1.planner.Planner
import groovy.transform.Memoized

public class PolicyRepositoryFileImpl implements PolicyRepository {

	private static final Logger log = LoggerFactory.getLogger(PolicyRepositoryFileImpl.class);

	private File dataStore
	private List<Policy> policies 

	PolicyRepositoryFileImpl() {
		this(Planner.CONFIG.policiesRepository)
	}
		
	PolicyRepositoryFileImpl(String path) {
		dataStore = new File(path)
		policies = new LinkedList<Policy>(
		dataStore.readLines("UTF-8").collect {policySerialized ->
			Policy.deserialize(policySerialized)
		})
	}
	
	public synchronized void storePolicy(Policy policy) {
		String toAppend = policy.serializeToString() + "\n"
		dataStore.append(toAppend, "UTF-8")
		policies << policy
	}

	@Memoized
	public List<Policy> fetchAdaptationPlans(ChangeRequestEvent changeRequest) {
		Collection<Policy> policies = policies.findAll { policy ->
			policy.getChangeRequest() == changeRequest.getName()
		}
		return 
	}
	
	public List<Policy> fetchAll() {
		return policies
	}
	
}
