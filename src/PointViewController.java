import java.awt.event.ActionListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PointViewController extends Stage implements BallEvent {

	private BallMVCModel model;
	private BallPaneView ballPane;

	private Timeline animation;
	private int delay = 5000;

	private double mouseX;
	private double mouseY;
	private Text mouseText;
	private String[] circleScolors = { "Red", "Cyan", "Green", "Blue",
			"Yellow", "Transparent" };
	private Color[] circleOcolors = { Color.RED, Color.CYAN, Color.GREEN,
			Color.BLUE, Color.YELLOW, Color.TRANSPARENT };
	private String[] circleFrameScolors = { "Red", "Cyan", "Green", "Blue",
			"Yellow" };
	private Color[] circleFrameOcolors = { Color.RED, Color.CYAN, Color.GREEN,
			Color.BLUE, Color.YELLOW };

	private ObservableList<String> itemsCirColor;
	private ObservableList<String> itemsCirFrame;

	private String eventTitle;
	private int modelNumber;

	private ComboBox<String> jCircleCbo;
	private ComboBox<String> jCircleFrameCbo;

	private FlowPane paneForComboBox;
	private BorderPane pane;

	public PointViewController(int modelNumber) {
		this.modelNumber = modelNumber;
		this.setTitle("MP View #" + modelNumber);

	}

	public void setModel(BallMVCModel newModel) {
		model = newModel;
		model.addActionListener(new mouseInsideEvent(), eventType.ISINSIDE);
		model.addActionListener(new mouseOutsideEvent(), eventType.ISOUTSIDE);
		model.addActionListener(new fillColorEvent(), eventType.FILL);
		model.addActionListener(new frameColorEvent(), eventType.FRAME);

		mouseText = new Text();
		paneForComboBox = new FlowPane();
		paneForComboBox.setAlignment(Pos.BASELINE_CENTER);

		pane = new BorderPane();
		ballPane = new BallPaneView();

		jCircleCbo = new ComboBox<String>();
		jCircleFrameCbo = new ComboBox<String>();

		itemsCirColor = FXCollections.observableArrayList(circleScolors);
		jCircleCbo.getItems().addAll(itemsCirColor);
		jCircleCbo.getSelectionModel().select(0);

		itemsCirFrame = FXCollections.observableArrayList(circleFrameScolors);
		jCircleFrameCbo.getItems().addAll(itemsCirFrame);
		jCircleFrameCbo.getSelectionModel().select(0);

		BorderedTitledPane btpCirColor = new BorderedTitledPane("Circle Color",
				jCircleCbo);
		BorderedTitledPane btpCirFrame = new BorderedTitledPane("Circle Frame",
				jCircleFrameCbo);

		paneForComboBox.getChildren().add(btpCirColor);
		paneForComboBox.getChildren().add(btpCirFrame);

		jCircleFrameCbo.setOnAction(e -> changeFrameColor());

		jCircleCbo.setOnAction(e -> changeFillColor());

		pane.setTop(ballPane);
		pane.setBottom(paneForComboBox);

		animation = new Timeline(new KeyFrame(Duration.millis(delay),
				e -> setBallNewLocation()));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();

		Scene scene = new Scene(pane, 500, 500);
		this.setScene(scene);
		ballPane.requestFocus();
		ballPane.getChildren().add(mouseText);

		pane.setOnMouseDragged(e -> setText(e.getSceneX(), e.getSceneY()));
		pane.setOnMouseMoved(e -> setText(e.getSceneX(), e.getSceneY()));

		model.setFillColor(jCircleCbo.getValue());
		model.setFramecolor(jCircleFrameCbo.getValue());

	}

	class mouseInsideEvent implements ActionListener {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (model == null)
				return;
			eventTitle = eventType.ISINSIDE.toString();
			mouseText.setText("Mouse point is in the circle");

		}
	}

	class mouseOutsideEvent implements ActionListener {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (model == null)
				return;
			eventTitle = eventType.ISOUTSIDE.toString();
			mouseText.setText("Mouse point is not in the circle");

		}

	}

	class fillColorEvent implements ActionListener {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (model == null)
				return;
			eventTitle = eventType.FILL.toString();

			ballPane.setCircleColor(circleOcolors[itemsCirColor
					.indexOf(jCircleCbo.getValue())]);

		}
	}

	class frameColorEvent implements ActionListener {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (model == null)
				return;
			eventTitle = eventType.FRAME.toString();

			ballPane.setCircleFrameColor(circleFrameOcolors[itemsCirFrame
					.indexOf(jCircleFrameCbo.getValue())]);

		}
	}

	private void changeFrameColor() {

		model.setFramecolor(jCircleFrameCbo.getValue());
	}

	private void changeFillColor() {

		model.setFillColor(jCircleCbo.getValue());

	}

	private void setBallNewLocation() {
		ballPane.setBallNewLocation();
		setText(mouseX, mouseY);

	}

	private void setText(double x, double y) {

		mouseX = x;
		mouseY = y;

		mouseText.setX(x);
		mouseText.setY(y);

		if (ballPane.insideCircle(x, y)) {

			model.isInside();

		} else {
			model.isOutside();

		}

	}

}

class BorderedTitledPane extends StackPane {

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
