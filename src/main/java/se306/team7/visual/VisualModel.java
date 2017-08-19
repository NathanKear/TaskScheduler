package se306.team7.visual;

public class VisualModel {
	
	 private View_CurrentBest _view_currentBest;
	 private View_Histogram	_view_histogram;
	 private View_LineGraph _view_lineGraph;
	 
	 public VisualModel(){
		 _view_currentBest = View_CurrentBest.getInstance();
		 _view_histogram = View_Histogram.getInstance();
		 _view_lineGraph = View_LineGraph.getInstance(); 
	 }
	 
	 
}
