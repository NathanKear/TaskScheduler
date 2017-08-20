package se306.team7.visual;

import java.util.HashMap;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
public class View_LineGraph implements ITaskSchedulerView{

    public LineChart<Number,Number> _lineChart;
    public XYChart.Series<Number, Number> _series;
    private static View_LineGraph _view_lineGraph;

    private View_LineGraph() {
        //defining the axes
        NumberAxis xAxis = new NumberAxis(0, 10, 1);
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis(0, 50, 5);
        yAxis.setLabel("Current Level");

        //creating the chart
        _lineChart = new LineChart<Number,Number>(xAxis, yAxis);

        _lineChart.setTitle("Current level explored for each core");

        //defining a series
        _series = new XYChart.Series<Number,Number>();
        _series.setName("Core 1");
        //populating the series with data
        _series.getData().add(new XYChart.Data<Number,Number>(1, 23));
        _series.getData().add(new XYChart.Data<Number,Number>(2, 14));
        _series.getData().add(new XYChart.Data<Number,Number>(3, 15));
        _series.getData().add(new XYChart.Data<Number,Number>(4, 24));
        _lineChart.getData().add(_series);
    }
    
    public static View_LineGraph getInstance(){
    	if (_view_lineGraph == null){
    		_view_lineGraph = new View_LineGraph();
    	}
    	
    	return _view_lineGraph;
    }

	@Override
	public void update(int currentBestCost, HashMap<Integer, Integer> histogram,
			HashMap<Integer, Integer> coreCurrentLevel) {
		// TODO Auto-generated method stub
		
		//System.out.println("update line");
		
	}
}
