package br.cin.gfads.adalrsjr1.timeseries

import groovy.transform.builder.Builder

@Builder(prefix="with")
class ChartInfo {

	String chartTitle
	String xAxisLabel
	String yAxisLabel
	String timeSerieName
	int serieMaximumCount
	
}
