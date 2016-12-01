package br.cin.gfads.adalrsjr1.common.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CdiContext {

	private static final Logger log = LoggerFactory.getLogger(CdiContext.class);

	public static final CdiContext INSTANCE = new CdiContext();
	
	private final Weld weld;
	private final WeldContainer container;
	
	private CdiContext() {
		this.weld = new Weld();
		this.container = weld.initialize();
//		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//			weld.shutdown();
//		}));
		
	}
	
	public <T> T getBean(Class<T> type) {
		return container.instance().select(type).get();
	}
	
}
