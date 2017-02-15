package br.cin.gfads.adalrsjr1.jeromq.parsers;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.ArrayValue;
import org.msgpack.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.json.JsonSlurper;

/**
 * Parse messages from Fluentd when using ZeroMQ publisher
 * @see <a href="https://github.com/ogibayashi/fluent-plugin-zmq-pub">Fluentd ZeroMQ PubSub</a>
 * @author adalrsjr
 */
public class FluentdParser implements Parser<Map> {

	private static final Logger log = LoggerFactory.getLogger(FluentdParser.class);

	/**
	 * remove the topic name in message
	 * @param raw
	 * @return
	 */
	private static byte[] fluentdMessage(byte[] raw) {
		int i = 0;
		while(raw[i++] != 32) {}
		return Arrays.copyOfRange(raw, i, raw.length);
	}

	/**
	 * Receive a byte array from fluentd marshalled by MessagePack and
	 * return a Map representing this message
	 * @param raw byte array with a marshalled message from Fluentd publisher plugin
	 * @return a map with the unmarshalled message 
	 * @see <a href="https://github.com/msgpack/msgpack-java/blob/develop/msgpack-core/src/test/java/org/msgpack/core/example/MessagePackExample.java">MessagePack Java</a>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map unmarshaller(byte[] message) {
		MessageUnpacker unpacker = MessagePack.newDefaultUnpacker((fluentdMessage(message)));
		Map parsedMessage = new HashMap<String, String>();
		try {
			Value v = unpacker.unpackValue();
			ArrayValue arrayValue = v.asArrayValue();
			parsedMessage.put("TOPIC", arrayValue.get(0));
			parsedMessage.put("TIMESTAMP", Long.parseLong(arrayValue.get(1).toString()));
			parsedMessage.put("CONTENT", arrayValue.get(2));
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return parsedMessage;
	}

	@SuppressWarnings({"rawtypes"})
	public byte[] marshaller(Map message) {
		MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
		try {
			packer.packArrayHeader(3);
			packer.packString( (String) message.get("TOPIC") );
			packer.packLong( (Long) message.get("TIMESTAMP") );
			packer.packString( (String) message.get("CONTENT") );
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return packer.toByteArray();
	}

}
