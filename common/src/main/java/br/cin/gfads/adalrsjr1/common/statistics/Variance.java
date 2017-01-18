package br.cin.gfads.adalrsjr1.common.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
public class Variance {

	private static final Logger log = LoggerFactory
			.getLogger(Variance.class);
	
	private double M2 = 0.0;
	private double lastVariance = 0.0;
	
	private Mean mean;
	
	public Variance(Mean mean) {
		this.mean = mean;
	}

	public double calculate(double data) {
			double innerMean = mean.getMean();
			double n = mean.getCounter();
			double delta = data - innerMean;
			innerMean += delta / n;
			double delta2 = data - innerMean;
			M2 += delta*delta2;
			lastVariance = M2 / (n-1);
			return lastVariance;
	}
	
	public double getVariance() {
		return lastVariance;
	}
	
	public double stdDeviationFromLastVariance() {
		return Math.sqrt(lastVariance);
	}
	
	public double stdDeviation(double data) {
		return Math.sqrt(calculate(data));
	}
	
	

}
