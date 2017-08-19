package se306.team7.visual;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class View_Histogram {
    public BarChart<String, Number> _barChart;
    public XYChart.Series<String, Number> _series;
    private static View_Histogram _view_histogram;

    private  View_Histogram() {
        initialiseHistogram();
    }

    private void initialiseHistogram() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Schedules with n tasks");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of cost-estimated schedules");

        _barChart = new BarChart<String, Number>(xAxis, yAxis);
        _series = new XYChart.Series<String, Number>();

        _series.getData().add(new XYChart.Data<String, Number>("level 1", 25601));
        _series.getData().add(new XYChart.Data<String, Number>("level 2", 5000));
        _barChart.getData().add(_series);
    }
    
    public static View_Histogram getInstance(){
    	if (_view_histogram == null){
    		_view_histogram = new View_Histogram();
    	}
    		return _view_histogram;
    }
}
