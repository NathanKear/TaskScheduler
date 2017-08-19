package se306.team7.visual;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;


public class VisualModel {
	
	 private View_CurrentBest _view_currentBest;
	 private View_Histogram	_view_histogram;
	 private View_LineGraph _view_lineGraph;
	 private Timeline _updatePerMinute;
	 
	 public VisualModel(){
		 _view_currentBest = View_CurrentBest.getInstance();
		 _view_histogram = View_Histogram.getInstance();
		 _view_lineGraph = View_LineGraph.getInstance(); 
		 
		 setUpTimer();
	 }
	 
	private void setUpTimer(){
		_updatePerMinute = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){

			    @Override
			    public void handle(ActionEvent event) {
			        System.out.println("this is called every minute on UI thread");
			    }
			}));
		_updatePerMinute.setCycleCount(Timeline.INDEFINITE);
	 }
	
	public void startTimer(){

		_updatePerMinute.play();
	}
}
