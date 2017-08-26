package se306.team7.visual;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import se306.team7.Metrics;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.geometry.Bounds;
import javafx.scene.Parent;

public class View_Histogram implements ITaskSchedulerView {
	public BarChart<Number, String> _barChart;
	public XYChart.Series<Number, String> _series;
	private static View_Histogram _view_histogram;
	private Integer test = new Integer(5);
	private NumberAxis _xAxis;


	private View_Histogram() {
		initialiseHistogram();
	}

	private void initialiseHistogram() {
		//Defining the axes
		CategoryAxis yAxis = new CategoryAxis();
		yAxis.setLabel("Level (schedules with n tasks)");
		yAxis.setAnimated(false);
		_xAxis = new NumberAxis();
		_xAxis.setAnimated(false);
		_xAxis.setAutoRanging(false);
		_xAxis.setLabel("Number of cost-estimated schedules");

		//Creating the Bar Chart
		_barChart = new BarChart<Number, String>(_xAxis, yAxis);
		_barChart.setTitle("Total cost-estimated schedules at each level");
		_barChart.setAnimated(true);
		_barChart.setLegendVisible(false);

		_barChart.setHorizontalGridLinesVisible(false);
		_barChart.setVerticalGridLinesVisible(false);

		//Preparing and adding the initial data
		_series = new XYChart.Series<Number, String>();

		System.out.println(Metrics.getLevels());
		for (int i = 1; i <= Metrics.getLevels(); i++) {

			final XYChart.Data<Number,String> data = new XYChart.Data( i, Integer.toString(i));

			_series.getData().add(data);
		}

		_barChart.getData().add(_series);

	}

	public static View_Histogram getInstance(){
		if (_view_histogram == null){
			_view_histogram = new View_Histogram();
		}
		return _view_histogram;
	}

	/**
	 * Updates the histogram view.
	 * For each level, get the XYChart.Data object in the series for the corresponding key (i.e. level) and set its Y-value to the key's value.
	 * Keys and values come from the input histogram.
	 *
	 * If the new Y value is closely approaching the Y axis' upper bound, then recalibrate the Y axis to approx 1.2 times its current size.
	 * @param currentBestCost
	 * @param histogram
	 * @param coreCurrentLevel
	 */
	public void update( int currentBestCost, ConcurrentHashMap<Integer, Integer> histogram, ConcurrentHashMap<Integer, Integer> coreCurrentLevel) {

		for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
			int newXValue = entry.getValue();
			_series.getData().get(entry.getKey()-1).setXValue(newXValue);
			double currentXAxisUpperBound = _xAxis.getUpperBound();
			if (newXValue > (currentXAxisUpperBound * 0.8)) {
				double newXAxisUpperBound = Math.ceil((currentXAxisUpperBound * 1.5) / 10) * 10;
				_xAxis.setUpperBound(newXAxisUpperBound);
				_xAxis.setTickUnit(Math.round(newXAxisUpperBound/10));

			}
		}
	}
}
