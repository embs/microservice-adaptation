package br.cin.gfads.adalrsjr1.common;

import java.util.Collections;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.codehaus.groovy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class Util {

	private static final Logger log = LoggerFactory.getLogger(Util.class);

	public static ThreadFactory threadFactory(String nameFormat) {
		return new ThreadFactoryBuilder().setNameFormat(nameFormat).build();
	}
	
	public static void mavericLog(Logger log, Class clazz, String methodName, Stopwatch watch) {
		mavericLog(log, clazz, methodName, watch.elapsed(TimeUnit.NANOSECONDS));
	}
	
	public static void mavericLog(Logger log, Class clazz, String methodName, long time) {
		log.trace("{\"clazz\":\"{}\", \"method\":\"{}\", \"time\":{}}", clazz.getName(), methodName, time);
	}
	
}
