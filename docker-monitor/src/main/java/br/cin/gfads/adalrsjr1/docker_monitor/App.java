package br.cin.gfads.adalrsjr1.docker_monitor;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.EventStream;
import com.spotify.docker.client.ObjectMapperProvider;
import com.spotify.docker.client.messages.Event;


/**
 * Hello world!
 *
 */

public class App 
{
	/**
	 * workaround to be in conformance with com.spotify.docker.client.jackson.UnixTimestampDeserializer
	 * @author adalr
	 *
	 */
	private static class MyDateSerialize extends StdSerializer<Date> {

		protected MyDateSerialize(Class<Date> t) {
			super(t);
		}

		private static final long serialVersionUID = -8233777676438370391L;

		@Override
		public void serialize(Date value, JsonGenerator gen,
				SerializerProvider provider) throws IOException {
			gen.writeNumber(value.getTime()/1000); 
		}

	}

	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main( String[] args ) throws Exception
	{
		DockerClient docker = DefaultDockerClient.fromEnv().build();
		DefaultDockerClient.builder()
		.uri("localhost:2376")
		.connectionPoolSize(1)
		.build();

		EventStream eventStream = docker.events();
		while(eventStream.hasNext()) {
			Event ev = eventStream.next();

			ObjectMapperProvider omp = new ObjectMapperProvider();

			SimpleModule timestampDeserializer = new SimpleModule("CustomTimestampDeserializerModule")
					.addSerializer(Date.class, new MyDateSerialize(Date.class));

			ObjectMapper mapper = omp.getContext(Event.class);
			mapper.registerModule(timestampDeserializer);

			String json = mapper.writerWithView(Event.class).writeValueAsString(ev);

			Event ev2 = mapper.getFactory().createParser(json).readValueAs(Event.class);
			System.err.println(ev2.actor().id());
			System.err.println(ev2.actor().attributes().get("name"));
			ev2.action();
			ev2.type();
			ev2.timeNano();
			ev2.time();
			
			log.info(ev.toString());
			log.debug(ev2.toString());
			
		}

		docker.close();

	}
}
