import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class BallPaneView extends BorderPane {
	private final static double RADIUS = 50;
	private double x = RADIUS, y = RADIUS;
	private Circle circle = new Circle(x, y, RADIUS);

	private int xCircle = 100;
	private int yCircleUp = 100;
	private int yCircleDown = 300;
	boolean leftUp = true;

	public BallPaneView() {
		circle.setFill(Color.RED);
		circle.setStroke(Color.RED);
		circle.setCenterX(xCircle);
		circle.setCenterY(yCircleUp);
		getChildren().add(circle);

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
		if (circle.contains(d, e))
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