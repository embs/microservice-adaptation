package br.cin.gfads.adalrsjr1.planner.policy;

import java.util.List;

import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;

public interface PolicyRepository {
	void storePolicy(Policy policy);
	List<Policy> fetchAdaptationPlans(ChangeRequestEvent changeRequest);
	List<Policy> fetchAll();
	
}
