package br.cin.gfads.adalrsjr1.timeseries

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart

import groovy.swing.SwingBuilder

class TimeSeriesVisualizer {
	private static final SwingBuilder swing = new SwingBuilder()
	
	private JFrame jFrame
	private int height = 200
	
	Map<String, ChartPanelModule> modules
	
	TimeSeriesVisualizer(List<ChartInfo> infos) {
		modules = infos.collectEntries {
			[(it.name) : new ChartPanelModule(it)]
		}
		
		height = modules.size() * 200
		
		jFrame = swing.frame(title: '', size: [800,height], show: true, defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
			vbox {
				modules.each { k,v ->
					JFreeChart chart = v.getChart()
					widget(v.getPanel(chart))
				}
			}
		}
		
		SwingUtilities.invokeLater({
			jFrame.setVisible(true)
		})
	}
	
	static void main(def args) {

		def infoList = []
		
		infoList << ChartInfo.builder()
		                     .withName("test1")
				             .withXAxisLabel("")
				             .withYAxisLabel("")
				             .withTimeSerieName("")
				             .withSerieMaximumCount(100)
				             .build()
				 
		infoList << ChartInfo.builder()
            		         .withName("test2")
							 .withXAxisLabel("")
							 .withYAxisLabel("")
							 .withTimeSerieName("")
							 .withSerieMaximumCount(100)
		                     .build()
		
		
							 
		TimeSeriesVisualizer app = new TimeSeriesVisualizer(infoList)
		
		Random r = new Random()
		
		
		Thread.start {
			
			while(true) {
				double i = r.nextGaussian()
				
				app.modules['test1'].addValue(i)
				app.modules['test2'].addValue((i*i + 2*i - 3))
				
				Thread.sleep(100)
			} 
		}
	}
}

