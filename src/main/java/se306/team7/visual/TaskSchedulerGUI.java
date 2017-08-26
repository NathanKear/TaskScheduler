package se306.team7.visual;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import se306.team7.CommandLineArgumentConfig;
import se306.team7.Metrics;
import se306.team7.TaskScheduler;
import se306.team7.Digraph.Digraph;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.control.Separator;

public class TaskSchedulerGUI extends Application {

	VisualModel _model;
	private List<ITaskSchedulerView> _views;
	private static Digraph _digraph; // this is needed to execute the background task\
	protected static long _startTime;
	private static long _endTime;
	private static CommandLineArgumentConfig _commandLineArgumentConfig;

	/**
	 * Constructs the GUI.
	 * @param primaryStage
	 */
	@SuppressWarnings("restriction")
	@Override
	public void start(Stage primaryStage) {
		
		_views = new ArrayList<ITaskSchedulerView>();
		_views.add(View_CurrentBest.getInstance());
		_views.add(View_Histogram.getInstance());
		_views.add(View_LineGraph.getInstance()); 

		primaryStage.setTitle("Task Scheduler");

		final Text statusText = new Text(" In progress...");
		statusText.setFill(Color.ORANGE);
		statusText.setFont(new Font(18));

		final Text status = new Text("Current status: ");
		status.setFont(new Font(18));
		
		final Text timerText = new Text("Time taken:	");
		timerText.setFont(new Font(18));
		timerText.setOpacity(0);//set transparent text
		
		final Text parallelText = new Text("Parallelisation:" + (_commandLineArgumentConfig.applicationProcessors() > 1 ? " On" : " Off"));
		parallelText.setFont(new Font(18));
		
		
		View_Histogram hist = View_Histogram.getInstance();
		View_LineGraph lineGraph = View_LineGraph.getInstance();
		View_CurrentBest currentBest = View_CurrentBest.getInstance();

		/*
        Visual look option 2
		 */
		
		HBox statusBox = new HBox(status,statusText);
		
		VBox leftBox = new VBox(statusBox, parallelText);
		leftBox.setPadding(new Insets(20, 190, 15, 45));
		leftBox.setSpacing(18);
		
		VBox rightBox = new VBox(timerText,currentBest._text);
		rightBox.setPadding(new Insets(20, 45, 15, 100));
		rightBox.setSpacing(18);
		
		Separator separator = new Separator();
		separator.setMaxWidth(1200);
		/*separator.setStyle("-fx-color: orange ;"
				+ "-fx-border-style: dotted;"+
				"-fx-border-width: 1px;"+
				"-fx-border-height: 1px;");*/
		
		/*
		HBox topHBox = new HBox(status,statusText,timerText);
		topHBox.setPadding(new Insets(15, 12, 15, 12));
		topHBox.setSpacing(10);

		HBox midHBox = new HBox(currentBest._text);
		midHBox.setPadding(new Insets(5, 12, 15, 12));
		midHBox.setSpacing(10);
		
		topHBox.setAlignment(Pos.CENTER);
		midHBox.setAlignment(Pos.CENTER);
		*/

		HBox topHBox = new HBox(leftBox, rightBox); // stats box
		HBox bottomHBox = new HBox(hist._barChart, lineGraph._lineChart);
		bottomHBox.setPadding(new Insets(25, 12, 25, 12));
		bottomHBox.setSpacing(10);


		VBox root = new VBox(topHBox,separator,bottomHBox);

		primaryStage.setResizable(false);

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);

		primaryStage.show();
	
		/*
		 * Set up timer task
		 */
		final Task task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				while (true) {
					Platform.runLater(new Runnable() {
						public void run() {
							for (ITaskSchedulerView view : _views){
								view.update(Metrics.getCurrentBestCost(), Metrics.getHistogram(), Metrics.getCoreCurrentLevel());
							}
						}
					});
					Thread.sleep(100);
				}
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(true);

		/*
		 * Set up algorithm service on background thread
		 */
		AlgorithmService service = new AlgorithmService();
		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			public void handle(WorkerStateEvent t) {
				//System.out.println("done:" + t.getSource().getValue());
				_endTime = System.currentTimeMillis();
				timerText.setText("Time taken: " + (_endTime - _startTime)/1000.00 + " seconds");
				timerText.setOpacity(1);//set transparent text
				//update GUI text
				statusText.setText("Finished");
				statusText.setFill(Color.GREEN);
				
				task.cancel(true);
				//final update
				for (ITaskSchedulerView view : _views){
					System.out.println("update cost is " + Metrics.getCurrentBestCost());
					view.update(Metrics.getCurrentBestCost(), Metrics.getHistogram(), Metrics.getCoreCurrentLevel());
				}
			}
		});

		service.start();
		th.start();
	}

	public static void LaunchGUI(String[] args, Digraph d, CommandLineArgumentConfig commandLineArgumentConfig) {
		_digraph = d;
		_commandLineArgumentConfig = commandLineArgumentConfig;
		launch(args);
	}

	/**
	 * Inner class for Javafx Service to run the search algorithm in background thread
	 *
	 */
	private static class AlgorithmService extends Service<Void> {
		// change Void to return type of call
		protected Task<Void> createTask() {

			return new Task<Void>() {

				protected Void call() {
					_startTime = System.currentTimeMillis();
					TaskScheduler.executeAlgorithm(_digraph);

					return null;


				}
			};
		}
	}

}