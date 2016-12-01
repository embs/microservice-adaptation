package br.cin.gfads.adalrsjr1.endpoint;

public interface SenderEndpoint extends Endpoint {

	Object send(byte[] message);
	
}
