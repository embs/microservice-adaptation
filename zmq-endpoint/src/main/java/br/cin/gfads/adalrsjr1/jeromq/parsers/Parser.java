package br.cin.gfads.adalrsjr1.jeromq.parsers;

public interface Parser<E> {
	E unmarshaller(byte[] message);
	byte[] marshaller(E message);
}
