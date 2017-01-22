package br.cin.gfads.adalrsjr1.timeseries

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.text.DecimalFormat
import java.text.SimpleDateFormat

import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.chart.labels.StandardXYToolTipGenerator
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYItemRenderer
import org.jfree.data.time.Second
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.TimeSeriesCollection
import org.jfree.data.xy.XYDataset

class ChartPanelModule {
	String chartTitle
	String xAxisLabel
	String yAxisLabel

	XYDataset dataset
	TimeSeries timeSerie
	TimeSeries avgSerie

	private JFreeChart chart
	private static Second TIME = new Second()

	private JLabel avgLabel
	private JLabel varLabel

	private double sumAvg = 0
	private double avg = 0
	private double countAvg = 0
	private double M2 = 0

	ChartPanelModule(ChartInfo info) {
		this(info.name, info.xAxisLabel, info.yAxisLabel, info.timeSerieName, info.serieMaximumCount)
	}
	
	private ChartPanelModule(String chartTitle, String xAxisLabel, String yAxisLabel,
	String timeSerieName, int serieMaximumItemCount) {
		this.chartTitle = chartTitle
		this.xAxisLabel = xAxisLabel
		this.yAxisLabel = xAxisLabel

		timeSerie = new TimeSeries(timeSerieName)
		avgSerie = new TimeSeries("Avg Serie")
		
		timeSerie.setMaximumItemCount(serieMaximumItemCount)
		avgSerie.setMaximumItemCount(serieMaximumItemCount)
		
		dataset = new TimeSeriesCollection()
		dataset.addSeries(avgSerie)
		dataset.addSeries(timeSerie)
	}

	JFreeChart getChart() {
		if(!chart) {
			chart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisLabel, yAxisLabel, dataset, false, false, false)
			chart.getXYPlot().getRangeAxis().setAutoRange(true)
		}
		chart
	}

	JPanel getPanel(JFreeChart chart) {
		JPanel pane = new JPanel(new GridBagLayout())
		pane.setLayout(new GridBagLayout())
		GridBagConstraints c = new GridBagConstraints()

		avgLabel = new JLabel("avg:", SwingConstants.CENTER)
		c.fill = GridBagConstraints.HORIZONTAL
		c.ipady = 5
		c.weightx = 0.5
		c.gridx = 1
		c.gridy = 0
		pane.add(avgLabel, c)

		varLabel = new JLabel("dev:", SwingConstants.CENTER)
		c.fill = GridBagConstraints.HORIZONTAL
		c.ipady = 5
		c.weightx = 0.5
		c.gridx = 2
		c.gridy = 0
		pane.add(varLabel)

		c.fill = GridBagConstraints.HORIZONTAL
		c.ipady = 125      //make this component tall
		c.weightx = 0.0
		c.gridwidth = 3
		c.gridx = 0
		c.gridy = 1
		pane.add(new ChartPanel(chart), c)

		return pane
	}

	private synchronized Second getTime() {
		TIME = TIME.next()
	}

	void addValue(double value) {
		timeSerie.add(getTime(), value)
		calculateAvg(value)
		avgSerie.add(getTime(), avg)
		avgLabel.setText("avg: ${avg}")
		varLabel.setText("var: ${calculateVar(value, avg, countAvg)}")
	}

	private double calculateAvg(double value) {
		countAvg++
		sumAvg += value
		avg = sumAvg / countAvg
		return avg
	}

	private double calculateVar(double value, double mean, double count) {
		double delta = value - mean
		mean += delta / count
		double delta2 = value - mean
		M2 += delta * delta2
		return M2 / (count-1) 
	}

	private void customize() {
		XYPlot plot = chart.getXYPlot()
		
		XYItemRenderer renderer = chart.getXYPlot().getRenderer()
		StandardXYToolTipGenerator toolTip = new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat(), new DecimalFormat("0.00")
			)
		
		renderer.setBaseToolTipGenerator(toolTip)
		
		
		//		NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();

		//		xAxis.setTickUnit(new NumberTickUnit(5));
//		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer()
		// sets paint color for each series
//		renderer.setSeriesPaint(0, Color.BLUE)
//		renderer.setSeriesPaint(1, Color.GREEN);

		// sets thickness for series (using strokes)
		//		renderer.setSeriesStroke(0, new BasicStroke(0.0f,0,1));
		//		renderer.setSeriesStroke(1, new BasicStroke(3.0f));

		// sets paint color for plot outlines
		//		plot.setOutlinePaint(Color.BLACK);
		//		plot.setOutlineStroke(new BasicStroke(1.0f));

		// sets renderer for lines
//		plot.setRenderer(renderer)

		// sets plot background
//		plot.setBackgroundPaint(Color.DARK_GRAY)

		// sets paint color for the grid lines
//		plot.setRangeGridlinesVisible(true)
//		plot.setRangeGridlinePaint(Color.BLACK)

//		plot.setDomainGridlinesVisible(true)
//		plot.setDomainGridlinePaint(Color.BLACK)
	}
}
