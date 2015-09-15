import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DraggedViewController extends Stage implements BallEvent {

	private int modelNumber;
	private String eventTitle;
	private Circle cir;
	private Button sizeUp;
	private Button sizeDown;
	private BorderPane mainPane;
	private Pane ballPane;
	private FlowPane btnPane;
	private BallMVCModel model;

	public DraggedViewController(int modelNumber) {
		this.modelNumber = modelNumber;

		this.setTitle("MD View #" + modelNumber);

	}

	public void setModel(BallMVCModel model) throws IOException {

		this.model = model;

		model.addActionListener(new moveMouseEvent(), eventType.DRAGGED);
		model.addActionListener(new increaseSizeEvent(), eventType.INCREASE);
		model.addActionListener(new decreaseSizeEvent(), eventType.DECREASE);

		sizeUp = new Button("Increase Ball Size");
		sizeDown = new Button("Decrease Ball Size");

		mainPane = new BorderPane();

		btnPane = new FlowPane();
		btnPane.setAlignment(Pos.BASELINE_CENTER);
		btnPane.getChildren().add(sizeUp);
		btnPane.getChildren().add(sizeDown);

		sizeUp.setOnAction(e -> increaseRadius());
		sizeDown.setOnAction(e -> decreaseRadius());

		ballPane = new Pane();
		cir = new Circle(model.getX(), model.getY(), model.getRadius(),
				Color.TRANSPARENT);

		cir.setStroke(Color.BLACK);
		cir.setOnMouseDragged(e -> cirSetXY(e, cir));
		ballPane.getChildren().addAll(cir);

		mainPane.setTop(ballPane);
		mainPane.setBottom(btnPane);
		Scene scene = new Scene(mainPane, 500, 500);
		this.setScene(scene);
		this.setAlwaysOnTop(true);
	}

	class moveMouseEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (modelNumber == 0)
				return;

			eventTitle = eventType.DRAGGED.toString();
			cir.setCenterX(model.getX());
			cir.setCenterY(model.getY());

		}
	}

	class increaseSizeEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (modelNumber == 0)
				return;

			eventTitle = eventType.INCREASE.toString();
			cir.setRadius(model.getRadius());
		}
	}

	class decreaseSizeEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (modelNumber == 0)
				return;
			eventTitle = eventType.DECREASE.toString();
			cir.setRadius(model.getRadius());
		}
	}

	public void cirSetXY(MouseEvent e, Circle c) {

		cir.setCenterX(e.getX());
		cir.setCenterY(e.getY());

		if (insideCircle(e.getX(), e.getY())) {

			model.moveBall(e.getX(), e.getY());

		}
	}

	private boolean insideCircle(double d, double e) {
		return distance(d, e, cir.getCenterX(), cir.getCenterY()) < cir
				.getRadius();
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	

	public void decreaseRadius() {

		model.decreaseRadius();

	}

	public void increaseRadius() {
		model.increaseRadius();

	}
	
}
