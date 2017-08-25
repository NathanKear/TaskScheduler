package se306.team7.visual;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import se306.team7.Metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class View_Histogram implements ITaskSchedulerView {
    public BarChart<String, Number> _barChart;
    public XYChart.Series<String, Number> _series;
    private static View_Histogram _view_histogram;
    private Integer test = new Integer(5);
    private NumberAxis _yAxis;


    private View_Histogram() {
        initialiseHistogram();
    }

    private void initialiseHistogram() {
        //Defining the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Level (schedules with n tasks)");
        _yAxis = new NumberAxis();
        _yAxis.setAnimated(false);
        _yAxis.setAutoRanging(false);
        _yAxis.setLabel("Number of cost-estimated schedules");

        //Creating the Bar Chart
        _barChart = new BarChart<String, Number>(xAxis, _yAxis);
        _barChart.setTitle("Histogram of cost-estimated schedules at each level");
        _barChart.setAnimated(true);
        _barChart.setLegendVisible(false);

        _barChart.setHorizontalGridLinesVisible(false);
        _barChart.setVerticalGridLinesVisible(false);
        
        //Preparing and adding the initial data
        _series = new XYChart.Series<String, Number>();

		System.out.println(Metrics.getLevels());
        for (int i = 1; i <= Metrics.getLevels(); i++) {
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
     * @param currentBestCost
     * @param histogram
     * @param coreCurrentLevel
     */
    public void update( int currentBestCost, ConcurrentHashMap<Integer, Integer> histogram, ConcurrentHashMap<Integer, Integer> coreCurrentLevel) {
    	

        for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
     
            //TODO need to test if histogram automatically resizes itself when its current y-axis upperbound has been exceeded
            // TODO otherwise, recalibrate histogram's y-axis if key's value exceeds current y-axis upperbound

            int newYValue = entry.getValue();
            _series.getData().get(entry.getKey()-1).setYValue(newYValue);

            double currentYAxisUpperBound = _yAxis.getUpperBound();
            if (newYValue > (currentYAxisUpperBound * 0.8)) {
                _yAxis.setUpperBound(Math.ceil(currentYAxisUpperBound * 1.2));
            }

        }
    }

//    /**
//     * Display the Y value as a text label above the bar node.
//     * @param data
//     */
//    private void displayLabelForData(XYChart.Data<String, Number> data) {
//        final Node node = data.getNode();
//        final Text dataText = new Text(data.getYValue() + "");
//        node.parentProperty().addListener(new ChangeListener<Parent>() {
//            public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
//                Group parentGroup = (Group) parent;
//                parentGroup.getChildren().add(dataText);
//            }
//        });
//
//        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
//            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
//                dataText.setLayoutX(
//                        Math.round(
//                                bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
//                        )
//                );
//                dataText.setLayoutY(
//                        Math.round(
//                                bounds.getMinY() - dataText.prefHeight(-1) * 0.5
//                        )
//                );
//            }
//        });
//    }
}
