package br.cin.gfads.adalrsjr1.timeseries

class ChartInfo {

	static class ChartInfoBuilder {

		String name
		String xAxisLabel
		String yAxisLabel
		String timeSerieName
		int serieMaximumCount

		ChartInfoBuilder withName(String name) {
			this.name = name
			return this
		}

		ChartInfoBuilder withXAxisLabel(String xAxisLabel) {
			this.xAxisLabel = xAxisLabel
			return this
		}

		ChartInfoBuilder withYAxisLabel(String yAxisLabel) {
			this.yAxisLabel = yAxisLabel
			return this
		}

		ChartInfoBuilder withTimeSerieName(String timeSerieName) {
			this.timeSerieName = timeSerieName
			return this
		}

		ChartInfoBuilder withSerieMaximumCount(int serieMaximumCount) {
			this.serieMaximumCount = serieMaximumCount
			return this
		}
		
		ChartInfo build() {
			return new ChartInfo(this)
		}
	}

	final String name
	final String xAxisLabel
	final String yAxisLabel
	final String timeSerieName
	final int serieMaximumCount

	private ChartInfo(ChartInfoBuilder builder) {
		name = builder.name
		xAxisLabel = builder.xAxisLabel
		yAxisLabel = builder.yAxisLabel
		timeSerieName = builder.timeSerieName
		serieMaximumCount = builder.serieMaximumCount
	}

	public static ChartInfoBuilder builder() {
		return new ChartInfoBuilder()
	}
}
