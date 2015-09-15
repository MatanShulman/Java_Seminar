import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IDhj_JavaFX extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	boolean mousePointFlag = false;
	boolean mouseDraggedFlag = false;
	IdjjjjjjjjjJavaFx mouseDragged = new IdjjjjjjjjjJavaFx();
	IdhhhhhhhhhhJavaFx mousePoint = new IdhhhhhhhhhhJavaFx();

	@Override
	public void start(Stage primaryStage) {

		Button btnMousePoint = new Button("Mouse Point On/Off");
		Button btnMouseDragged = new Button("Mouse Dragged On/Off");

		FlowPane rootPane = new FlowPane();
		rootPane.setAlignment(Pos.BASELINE_CENTER);
		rootPane.setHgap(3);
		rootPane.setVgap(3);
		rootPane.getChildren().add(btnMousePoint);
		rootPane.getChildren().add(btnMouseDragged);

		btnMousePoint.setOnAction(e -> handleMousePointClass());
		btnMouseDragged.setOnAction(e -> handleMouseDraggedClass());

		Scene scene = new Scene(rootPane, 200, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("MousePointDraggedDemo");
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		primaryStage.show();

	}

	private void handleMousePointClass() {
		mousePointFlag = (mousePointFlag) ? false : true;
		mousePoint.setAlwaysOnTop(true);
		mousePoint.setOnCloseRequest(e -> e.consume());

		if (mousePointFlag)
			mousePoint.show();
		else
			mousePoint.hide();
	}

	private void handleMouseDraggedClass() {
		mouseDraggedFlag = (mouseDraggedFlag) ? false : true;
		mouseDragged.setAlwaysOnTop(true);
		mouseDragged.setOnCloseRequest(e -> e.consume());

		if (mouseDraggedFlag)
			mouseDragged.show();
		else
			mouseDragged.hide();
	}
}

class IdjjjjjjjjjJavaFx extends Stage {
	private double x = 20;
	private double y = 20;
	public static int RADIUS = 10;

	public IdjjjjjjjjjJavaFx() {
		Pane pane = new Pane();
		Circle cir = new Circle(x, y, RADIUS, Color.TRANSPARENT);
		cir.setStroke(Color.BLACK);
		cir.setOnMouseDragged(e -> cirSetXY(cir, e));
		
		pane.getChildren().addAll(cir);
		Scene scene = new Scene(pane, 500, 500);
		this.setScene(scene); 
		this.setAlwaysOnTop(true);
	}

	private void cirSetXY(Circle circle, MouseEvent e) {
		x=e.getX();
		y=e.getY();
		
		if (insideCircle(x, y)) {
			circle.setCenterX(x);
			circle.setCenterY(y);
			circle.setRadius(RADIUS);
		}
	}

	private boolean insideCircle(double d, double e) {
		return distance(d, e, x, y) < RADIUS;
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
}

class IdhhhhhhhhhhJavaFx extends Stage {

	private BallPane ballPane;
	private Text mouseText;
	private String[] circleScolors = { "Red", "Cyan", "Green", "Blue",
			"Yellow", "Empty" };
	private Color[] circleOcolors = { Color.RED, Color.CYAN, Color.GREEN,
			Color.BLUE, Color.YELLOW };
	private String[] circleFrameScolors = { "Red", "Cyan", "Green", "Blue",
			"Yellow" };
	private Color[] circleFrameOcolors = { Color.RED, Color.CYAN, Color.GREEN,
			Color.BLUE, Color.YELLOW };

	private ComboBox<String> jCircleCbo;
	private ComboBox<String> jCircleFrameCbo;

	FlowPane paneForComboBox;
	BorderPane pane;

	public IdhhhhhhhhhhJavaFx() {

		mouseText = new Text();
		paneForComboBox = new FlowPane();
		paneForComboBox.setAlignment(Pos.BASELINE_CENTER);
		
		pane = new BorderPane();
		ballPane = new BallPane();
		
		jCircleCbo = new ComboBox<String>();
		jCircleFrameCbo = new ComboBox<String>();
		
		ObservableList<String> itemsCirColor = FXCollections
				.observableArrayList(circleScolors);	
		jCircleCbo.getItems().addAll(itemsCirColor);
		jCircleCbo.getSelectionModel().select(0);
		
		ObservableList<String> itemsCirFrame = FXCollections
				.observableArrayList(circleFrameScolors);
		jCircleFrameCbo.getItems().addAll(itemsCirFrame);
		jCircleFrameCbo.getSelectionModel().select(0);
		
		BorderedTitledPane btpCirColor = new BorderedTitledPane("Circle Color",
				jCircleCbo);
		BorderedTitledPane btpCirFrame = new BorderedTitledPane("Circle Frame",
				jCircleFrameCbo);

		paneForComboBox.getChildren().add(btpCirColor);
		paneForComboBox.getChildren().add(btpCirFrame);
		
		
		jCircleFrameCbo.setOnAction(e -> ballPane
				.setCircleFrameColor(circleFrameOcolors[itemsCirFrame
						.indexOf(jCircleFrameCbo.getValue())]));
		
		jCircleCbo.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				if (itemsCirColor.indexOf(jCircleCbo.getValue()) == circleScolors.length-1 )
					ballPane.setCircleColor(Color.TRANSPARENT);
				else
					ballPane.setCircleColor(circleOcolors[itemsCirColor.indexOf(jCircleCbo.getValue())]);
			}
		});
			
		pane.setTop(ballPane);
		pane.setBottom(paneForComboBox);
		
		Scene scene = new Scene(pane, 500, 500);
		this.setScene(scene); 
		ballPane.requestFocus();
		ballPane.getChildren().add(mouseText);
		
		ballPane.setOnMouseMoved(e -> setText(e.getSceneX(), e.getSceneY()));
		ballPane.setOnMouseDragged(e -> setText(e.getSceneX(), e.getSceneY()));
	}

	private void setText(double x, double y) {

		mouseText.setX(x);
		mouseText.setY(y);

		if (ballPane.insideCircle(x, y))
			mouseText.setText("Mouse point is in the circle");
		else
			mouseText.setText("Mouse point is not in the circle");
	}

	public class BorderedTitledPane extends StackPane {

		BorderedTitledPane(String titleString, Node content) {
			Label title = new Label(" " + titleString + " ");

			title.setStyle("-fx-font-weight: bold; -fx-translate-y: -11; -fx-padding: 0 0 0 0; -fx-background-color: -fx-background");
			StackPane.setAlignment(title, Pos.TOP_CENTER);

			StackPane contentPane = new StackPane();
			contentPane.setStyle("-fx-padding: 12 7 7 7");			 
			contentPane.getChildren().add(content);

			this.setStyle("-fx-content-display: top;  -fx-border-insets: 20 10 10 10; -fx-border-color: -fx-text-box-border;  -fx-border-width: 1");
			getChildren().addAll(title, contentPane);
		}
	}
}
class BallPaneJavaFx extends Pane {
	private final static double RADIUS = 50;
	private double x = RADIUS, y = RADIUS;
	private Circle circle = new Circle(x, y, RADIUS);
	public int delay = 5000;
	private int xCircle = 100;
	private int yCircleUp = 100;
	private int yCircleDown = 300;
	boolean leftUp = true;
	private Timeline animation;

	public BallPaneJavaFx() {
		circle.setFill(Color.RED);
		circle.setStroke(Color.RED);
		circle.setCenterX(xCircle);
		circle.setCenterY(yCircleUp);
		getChildren().add(circle); 
		
		animation = new Timeline(new KeyFrame(Duration.millis(delay),
				e -> setBallNewLocation()));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play(); 
	}

	public void play() {
		animation.play();
	}

	public void pause() {
		animation.pause();
	}

	protected void setBallNewLocation() {
		leftUp = (leftUp) ? false : true;
		if (leftUp) {
			x = xCircle;
			y = yCircleUp;
		} else {
			x = xCircle;
			y = yCircleDown;
		}

		circle.setCenterX(x);
		circle.setCenterY(y);
	}

	public boolean insideCircle(double d, double e) {
		if(circle.contains(d, e))
			return true;
		else
			return false;
	}

	public void setCircleColor(Color circleColor) {
		circle.setFill(circleColor);
	}

	public void setCircleFrameColor(Color circleFrameColor) {
		circle.setStroke(circleFrameColor);
	}
}