package se306.team7.visual;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import se306.team7.Metrics;

/**
 * A class for the line graph representation in the Task Scheduler GUI
 */
public class View_LineGraph implements ITaskSchedulerView {

	public LineChart<Number,Number> _lineChart;
	private static View_LineGraph _view_lineGraph;
	private static int currentTimeUnit = 0;
	private NumberAxis _xAxis;
	private static double currentTime;

	private View_LineGraph() {
		//defining the axes
		_xAxis = new NumberAxis(0, 2, 0.5);
		_xAxis.setLabel("Time (seconds)");
		_xAxis.setAutoRanging(false);
		_xAxis.setAnimated(false);
		NumberAxis yAxis = new NumberAxis(1, Metrics.getLevels(), 1);
		yAxis.setLabel("Current level");
		yAxis.setAnimated(false);

		//creating the chart
		_lineChart = new LineChart<Number,Number>(_xAxis, yAxis);
		_lineChart.setTitle("Current level explored for each core");
		_lineChart.setAnimated(true);
		_lineChart.setLegendVisible(true);
		_lineChart.setLegendSide(Side.RIGHT);
		_lineChart.setCreateSymbols(false);

		//Preparing and adding the initial data
		for (int i = 1; i <= Metrics.getNumOfCores(); i++) {
			XYChart.Series<Number, Number> newCoreSeries = new XYChart.Series<Number, Number>();
			newCoreSeries.setName("Core " + i);
			newCoreSeries.getData().add(new XYChart.Data<Number, Number>(0, 0));
			_lineChart.getData().add(newCoreSeries);
		}

		for (XYChart.Series<Number, Number> s : _lineChart.getData()) {
			s.getNode().setStyle("-fx-stroke-width: 0.75px;");
		}
	}

	public static View_LineGraph getInstance(){
		if (_view_lineGraph == null){
			_view_lineGraph = new View_LineGraph();
		}

		return _view_lineGraph;
	}

	/**
	 * Updates the line graph by adding a new data point for each series.
	 * Each series corresponds to a core.
	 * Each data point in the series represents the level of the solution tree at which the core was operating on at that moment in time
	 * The level of the solution tree corresponds to the number of tasks that have been scheduled in the (partial) solution.
	 *
	 * If the new X value is closely approaching the X axis' upper bound, then recalibrate the X axis to twice its current size.
	 * @param currentBestCost
	 * @param histogram
	 * @param coreCurrentLevel
	 */
	@SuppressWarnings("restriction")
	public void update( int currentBestCost, ConcurrentHashMap<Integer, Integer> histogram, ConcurrentHashMap<Integer, Integer> coreCurrentLevel) {

		/* if algorithm has not finished, plot current time on line graph;
		 * else if is final GUI update, plot end time on line graph
		*/
		if (TaskSchedulerGUI._isDone){
			currentTime = (TaskSchedulerGUI._endTime - TaskSchedulerGUI._startTime)/1000.00;
		}else {

			currentTime = (System.currentTimeMillis() - TaskSchedulerGUI._startTime)/1000.00;
		}
		
		double currentXAxisUpperBound = _xAxis.getUpperBound();
		if (currentTime > (currentXAxisUpperBound * 0.8)) {
			double newXAxisUpperBound = Math.ceil((currentXAxisUpperBound * 1.5) / 10) * 10;
			_xAxis.setUpperBound(newXAxisUpperBound);
			_xAxis.setTickUnit(Math.round(newXAxisUpperBound/10));

		}

		for (Map.Entry<Integer, Integer> entry : coreCurrentLevel.entrySet()) {
			_lineChart.getData().get(entry.getKey()-1) //get the series for the corresponding core
			.getData().add(new XYChart.Data<Number, Number>(currentTime, entry.getValue())); //add new Y value for the current time unit
		}


	}
}
