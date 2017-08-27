package se306.team7.visual;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class TaskSchedulerGUI extends Application {

	VisualModel _model;
	private List<ITaskSchedulerView> _views;
	private static Digraph _digraph; // this is needed to execute the background task\
	protected static long _startTime;
	private static long _endTime;
	private static CommandLineArgumentConfig _commandLineArgumentConfig;
	private Text _algorithmTypeText;
	protected Text totalSchedulesCostEstimatedText;
	protected static Button _button ;
	protected Text _totalSchedulesCostEstimatedText;

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

		_totalSchedulesCostEstimatedText = new Text("Total schedules estimated: 0");
		_totalSchedulesCostEstimatedText.setFont(new Font(18));

		_algorithmTypeText = new Text("Algorithm used: pending...");
		_algorithmTypeText.setFont(new Font(18));

		View_Histogram hist = View_Histogram.getInstance();
		View_LineGraph lineGraph = View_LineGraph.getInstance();
		View_CurrentBest currentBest = View_CurrentBest.getInstance();

		HBox statusBox = new HBox(status,statusText);

		VBox leftBox = new VBox(statusBox, parallelText, _algorithmTypeText);
		leftBox.setPadding(new Insets(20, 190, 15, 45));
		leftBox.setSpacing(18);
		
		VBox rightBox = new VBox(currentBest._text,_totalSchedulesCostEstimatedText, timerText);
		rightBox.setPadding(new Insets(20, 45, 15, 100));
		rightBox.setSpacing(18);

		Separator separator = new Separator();
		separator.setMaxWidth(1200);

		HBox topHBox = new HBox(leftBox, rightBox); // stats box
		HBox middleHBox = new HBox(hist._barChart, lineGraph._lineChart);
		middleHBox.setPadding(new Insets(25, 12, 25, 12));
		middleHBox.setSpacing(10);

		Separator separatorTwo = new Separator();
		separatorTwo.setMaxWidth(1200);

		final Text outputText = new Text("Your output file path is: \n\n"+Paths.get(".").toAbsolutePath().normalize().toString()+ _commandLineArgumentConfig.outputFileName());
		outputText.setFont(new Font(15));

		HBox bottomLeftBox = new HBox(outputText);
		bottomLeftBox.setPadding(new Insets(20, 20, 25, 25));
		bottomLeftBox.setSpacing(18);
		
		_button  = new Button("Open file in gedit");
		_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Process p;
				try {
					p = Runtime.getRuntime().exec("gedit " + _commandLineArgumentConfig.outputFileName());
					p.waitFor();
					//System.out.println ("exit: " + p.exitValue());
					p.destroy();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		HBox bottomRightBox = new HBox(_button);
		bottomRightBox.setPadding(new Insets(20, 20, 25, 850));
		bottomRightBox.setSpacing(10);

		//HBox bottomBox = new HBox(bottomLeftBox,bottomRightBox);

		VBox root = new VBox(topHBox,separator,middleHBox,separatorTwo, bottomLeftBox,bottomRightBox);

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
								_totalSchedulesCostEstimatedText.setText("Total schedules estimated: " + Metrics.getTotalSchedulesEstimated());
							}
						}
					});
					Thread.sleep(300);
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
				_button.setDisable(false);
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
		Metrics.init(d.getNodes().size(), commandLineArgumentConfig.applicationProcessors()); //bogus code
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
					_button.setDisable(true);
					_startTime = System.currentTimeMillis();
					TaskScheduler.executeAlgorithm(_digraph);

					return null;


				}
			};
		}
	}

}