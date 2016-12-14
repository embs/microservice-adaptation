package br.cin.gfads.adalrsjr1.common;

import java.util.Collections;
import java.util.concurrent.ThreadFactory;

import org.codehaus.groovy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class Util {

	private static final Logger log = LoggerFactory.getLogger(Util.class);

	public static ThreadFactory threadFactory(String nameFormat) {
		return new ThreadFactoryBuilder().setNameFormat(nameFormat).build();
	}
	
	public static void instrumentation(String methodName, long time, String message) {
		instumentation(log, "methodName", methodName, "executionTime", time+"", "message", message);
	}
	
	public static void instumentation(Logger log, String ... args) {
		int n = args.length;
		if(n == 0) return;
		StringBuilder sb = new StringBuilder("{");
		for(int i = 0; i < n-1; i+=2) {
			sb.append("\"");
			sb.append(args[i]);
			sb.append("\"");
			sb.append(":");
			sb.append("\"");
			sb.append(args[i+1]);
			sb.append("\"");
			sb.append(",");
		}
		sb.setLength(sb.length()-1);
		sb.append("}");
		log.trace(sb.toString());
	}
	
}
