package br.cin.gfads.adalrsjr1.verifier.properties;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.verifier.PropertyInstance;
import br.cin.gfads.adalrsjr1.verifier.processingunits.instances.OneByOneProcessingUnit;
import br.cin.gfads.adalrsjr1.verifier.processingunits.wrappers.RabbitMQProcessingUnitWrapper;

/**
 * This program demonstrates how to draw XY line chart with XYDataset
 * using JFreechart library.
 * @author www.codejava.net
 *
 */
public class ChartProperty extends JFrame implements PropertyInstance {

	public TimeSeries series1;
	static public Thread t = null;
	public ChartProperty() {
		super("XY Line Chart Example with JFreechart");

		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);

		setSize(2400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private JPanel createChartPanel() {
		String chartTitle = "Objects Movement Chart";
		String xAxisLabel = "X";
		String yAxisLabel = "Y";

		XYDataset dataset = createDataset();
		JFreeChart chart = ChartFactory.createTimeSeriesChart("", "X", "Y", dataset, false, false, false);

//		chart.getXYPlot().getRangeAxis().setFixedAutoRange(1000.0);
		chart.getXYPlot().getRangeAxis().setAutoRange(false);
		chart.getXYPlot().getRangeAxis().setRangeWithMargins(1000, 2000);
//		chart.getXYPlot().getRangeAxis().setAutoRangeMinimumSize(1000);
		
		
		return new ChartPanel(chart);
	}

	private XYDataset createDataset() {

		series1 = new TimeSeries( "Data" );     
		series1.setMaximumItemCount(5000);
		return new TimeSeriesCollection(series1);
	}

	static Second n = new Second();
	static int c = 0;
	static double total = 0.0;
	double dev = 0.0;
	public void addValues(double value) {
		c++;
		total += value;
		if(c % 100 == 0) {
			double avg = total/c;
			
			dev = stddev((double)c, dev, avg, value);
			series1.add(n, avg);
			System.err.println(avg + " " + dev);
			
		}
		n = (Second) n.next();
	}
	
	/*
	 * def online_variance(data):
	    n = 0
	    mean = 0.0
	    M2 = 0.0
	     
	    for x in data:
	        n += 1
	        delta = x - mean
	        mean += delta/n
	        delta2 = x - mean
	        M2 += delta*delta2

	    if n < 2:
	        return float('nan')
	    else:
	        return M2 / (n - 1)
	 */
	double M2 = 0.0;
	double stddev(double n, double olddev, double mean, double data) {
		double delta = data - mean;
		mean += delta / n;
		double delta2 = data - mean;
		M2 += delta*delta2;
		return M2 / (n-1);
	}

	private void customizeChart(JFreeChart chart) {
		XYPlot plot = chart.getXYPlot();

		//		NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();

		//		xAxis.setTickUnit(new NumberTickUnit(5));
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		// sets paint color for each series
		renderer.setSeriesPaint(0, Color.RED);
		//		renderer.setSeriesPaint(1, Color.GREEN);

		// sets thickness for series (using strokes)
		//		renderer.setSeriesStroke(0, new BasicStroke(0.0f,0,1));
		//		renderer.setSeriesStroke(1, new BasicStroke(3.0f));

		// sets paint color for plot outlines
		//		plot.setOutlinePaint(Color.BLACK);
		//		plot.setOutlineStroke(new BasicStroke(1.0f));

		// sets renderer for lines
		plot.setRenderer(renderer);

		// sets plot background
		plot.setBackgroundPaint(Color.DARK_GRAY);

		// sets paint color for the grid lines
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);

	}

	public static void start(ChartProperty chart) {
		if(t == null) {
			t = new Thread(() -> {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						chart.setVisible(true);
					}
				});
			});
			t.start();
		}
		
		
	}

	@Override
	public boolean check(SymptomEvent symptom) {
		Long serviceTime = (Long)symptom.tryGet("serviceTime", Long.class);
		if(serviceTime != null) {
			addValues((double)serviceTime);
			//			SwingUtilities.updateComponentTreeUI(this);
		}
		return false;
	}

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<byte[]> buffer = new ArrayBlockingQueue<>(100);

		// RabbitMQSubscriber sub = RabbitMQSubscriber.builder()
		// .withBuffer(buffer)
		// .withExchangeDurable(true)
		// .withExchangeName("fluentd.fanout")
		// .withExchangeType(FANOUT)
		// //.withHost("10.0.75.1")
		// .withHost("10.66.66.22")
		// .withRoutingKey("")
		// .build();

		ChartProperty chart = new ChartProperty();
		start(chart);
		OneByOneProcessingUnit pu = new OneByOneProcessingUnit(chart);
		RabbitMQProcessingUnitWrapper wrapper = new RabbitMQProcessingUnitWrapper(
				pu);

		ExecutorService executor = Executors.newSingleThreadExecutor(
				Util.threadFactory("rabbitmq-wrapper-processing-unit-reqresp"));
		executor.execute(wrapper);

		//		Random r = new Random();
		//		new Thread(() -> {
		//			double count = 6.0;
		//			while(true) {
		//				chart.addValues(count, r.nextDouble());
		//				count += 1.0;
		//				SwingUtilities.updateComponentTreeUI(chart);
		//				try {
		//					Thread.sleep(10);
		//				}
		//				catch (InterruptedException e) {
		//					e.printStackTrace();
		//				}
		//			}
		//		}).start();

		while (true)
			pu.toEvaluate(new SymptomEvent(buffer.take()));


	}
}