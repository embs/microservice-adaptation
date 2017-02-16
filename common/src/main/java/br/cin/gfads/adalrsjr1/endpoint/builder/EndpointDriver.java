package br.cin.gfads.adalrsjr1.endpoint.builder;

import br.cin.gfads.adalrsjr1.endpoint.Endpoint;

public interface EndpointDriver<T extends Endpoint> {
	T create(EndpointBuilder<T> builder);
	void shutdown();
}
