package se306.team7.visual;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import se306.team7.Metrics;

public class View_LineGraph implements ITaskSchedulerView {

    public LineChart<Number,Number> _lineChart;
    private static View_LineGraph _view_lineGraph;
    private static int currentTimeUnit = 0;

    private View_LineGraph() {
        //defining the axes
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");
//        NumberAxis yAxis = new NumberAxis(1, Metrics.getLevels(), 1); //TODO use this line after Metrics class has been instantiated
        NumberAxis yAxis = new NumberAxis(1, 10, 1);
        yAxis.setLabel("Current level");

        //creating the chart
        _lineChart = new LineChart<Number,Number>(xAxis, yAxis);
        _lineChart.setTitle("Current level explored for each core");
        _lineChart.setAnimated(true);
        _lineChart.setLegendVisible(false);

        //Preparing and adding the initial data
       /* for (int i = 1; i <= Metrics.getNumOfCores(); i++) {
            XYChart.Series<Number, Number> newCoreSeries = new XYChart.Series<Number, Number>();
            newCoreSeries.setName("Core " + i);
            newCoreSeries.getData().add(new XYChart.Data<Number, Number>(0, 0));
            _lineChart.getData().add(newCoreSeries);
        }*/
    }
    
    public static View_LineGraph getInstance(){
    	if (_view_lineGraph == null){
    		_view_lineGraph = new View_LineGraph();
    	}
    	
    	return _view_lineGraph;
    }

    /**
     * For each core, get its corresponding current level, add a new XYChart.Data to corresponding series
     * @param numOfLevels
     * @param numOfCores
     * @param currentBestCost
     * @param histogram
     * @param coreCurrentLevel
     */
    public void update( int currentBestCost, HashMap<Integer, Integer> histogram, HashMap<Integer, Integer> coreCurrentLevel) {
    	
    	if (_lineChart.getData().size() == 0){
    		 //Preparing and adding the initial data
            for (int i = 1; i <= Metrics.getNumOfCores(); i++) {
                XYChart.Series<Number, Number> newCoreSeries = new XYChart.Series<Number, Number>();
                newCoreSeries.setName("Core " + i);
                newCoreSeries.getData().add(new XYChart.Data<Number, Number>(0, 0));
                _lineChart.getData().add(newCoreSeries);
            }
    	}
    	
        currentTimeUnit++;
        for (Map.Entry<Integer, Integer> entry : coreCurrentLevel.entrySet()) {
            _lineChart.getData().get(entry.getKey()-1) //get the series for the corresponding core
                    .getData().add(new XYChart.Data<Number, Number>(currentTimeUnit, entry.getValue())); //add new Y value for the current time unit
        }

    }
}
