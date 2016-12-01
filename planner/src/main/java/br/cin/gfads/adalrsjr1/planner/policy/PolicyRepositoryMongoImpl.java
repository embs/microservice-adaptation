package br.cin.gfads.adalrsjr1.planner.policy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.events.ChangeRequestEvent;

public class PolicyRepositoryMongoImpl implements PolicyRepository {

	private static final Logger log = LoggerFactory.getLogger(PolicyRepositoryMongoImpl.class);

	@Override
	public void storePolicy(Policy policy) {
	}

	@Override
	public List<Policy> fetchAdaptationPlans(ChangeRequestEvent changeRequest) {
		return null;
	}

	@Override
	public List<Policy> fetchAll() {
		return null;
	}

}
