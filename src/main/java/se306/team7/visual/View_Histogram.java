package se306.team7.visual;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.HashMap;
import java.util.Map;

public class View_Histogram implements ITaskSchedulerView {
    public BarChart<String, Number> _barChart;
    public XYChart.Series<String, Number> _series;
    private static View_Histogram _view_histogram;
    private NumberAxis _yAxis;

    private View_Histogram() {
        initialiseHistogram();
    }

    private void initialiseHistogram() {
        //Defining the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Level (schedules with n tasks)");
        _yAxis = new NumberAxis();
        _yAxis.setAutoRanging(false);
        _yAxis.setLabel("Number of cost-estimated schedules");

        //Creating the Bar Chart
        _barChart = new BarChart<String, Number>(xAxis, _yAxis);
        _barChart.setTitle("Histogram of cost-estimated schedules at each level");
        _barChart.setAnimated(true);
        _barChart.setLegendVisible(false);

        //Preparing and adding the initial data
        _series = new XYChart.Series<String, Number>();
//        for (int i = 1; i <= Metrics.getLevels(); i++) {
        for (int i = 1; i <= 10; i++) { //TODO delete this line after Metrics has been instantiated in TaskScheduler
            _series.getData().add(new XYChart.Data<String, Number>(String.valueOf(i), 0));
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
     * If the new Y value is closely approaching the Y axis' upper bound, then recalibrate the Y axis to twice its current size.
     * @param numOfLevels
     * @param numOfCores
     * @param currentBestCost
     * @param histogram
     * @param coreCurrentLevel
     */
    public void update(int numOfLevels, int numOfCores, int currentBestCost, HashMap<Integer, Integer> histogram, HashMap<Integer, Integer> coreCurrentLevel) {
        for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
            int newYValue = entry.getValue();
            _series.getData().get(entry.getKey()-1).setYValue(newYValue);

            double currentYAxisUpperBound = _yAxis.getUpperBound();
            if (newYValue > (currentYAxisUpperBound * 0.8)) {
                _yAxis.setUpperBound(currentYAxisUpperBound * 2);
            }

        }
    }
}
