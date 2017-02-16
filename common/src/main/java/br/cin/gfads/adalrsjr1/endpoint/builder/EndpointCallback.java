package br.cin.gfads.adalrsjr1.endpoint.builder;

import br.cin.gfads.adalrsjr1.endpoint.Endpoint;

public abstract class EndpointCallback<S> {
	protected EndpointBuilder<Endpoint> builder;

	protected boolean stoped = false;

	abstract public S getImpl();

	public void setBuilder(EndpointBuilder<Endpoint> builder) {
		this.builder = builder;
	}

	public void stop() {
		stoped = true;
	}
}
