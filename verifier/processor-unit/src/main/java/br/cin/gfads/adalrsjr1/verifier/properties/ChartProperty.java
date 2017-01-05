package br.cin.gfads.adalrsjr1.verifier.properties;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.gfads.adalrsjr1.common.Util;
import br.cin.gfads.adalrsjr1.common.events.SymptomEvent;
import br.cin.gfads.adalrsjr1.common.statistics.Mean;
import br.cin.gfads.adalrsjr1.common.statistics.Variance;
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
	private static final Logger log = LoggerFactory
			.getLogger(ChartProperty.class);
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

	static Mean mean = new Mean();
	static Variance variance = new Variance(mean);
	static Second n = new Second();
	
	public void addValues(double value) {
		mean.sumNewValue(value);
		
		if(mean.getCounter() % 100 == 0) {
			double avg = mean.result();
			double dev = variance.calculate(value);
			series1.add(n, avg);
		}
		n = (Second) n.next();
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
			Util.mavericLog(log, getClass(), "serviceTime", serviceTime);
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