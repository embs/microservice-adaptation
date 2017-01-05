package br.cin.gfads.adalrsjr1.common.statistics;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mean {

	private static final Logger log = LoggerFactory.getLogger(Mean.class);

	private double acc = 0.0;
	private double count = 0.0;
	
	private double mean = 0.0;
	
	public void sumNewValue(double value) {
		acc += value;
		count += 1.0;
	}
	
	public double result() {
		mean = acc / count;
		return mean;
	}
	
	public double getMean() {
		return mean;
	}
	
	public double getAcc() {
		return acc;
	}
	
	public double getCounter() {
		return count;
	}
	
	public void clear() {
		acc = 0.0;
		count = 0.0;
	}
}
