import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Created by yoni on 19/05/2015.
 */
public class BallViewMainPanel extends Application {

	// public static void main(String[] args) {
	// launch(args);
	// }

	boolean mousePointFlag = false;
	boolean mouseDraggedFlag = false;
	boolean MPfirstFlag = true;
	boolean MDfirstFlag = true;
	private PointViewController MPviewController;
	private DraggedViewController MDviewController;
	private BallMVCModel model;
	private int modelNum;

	public void start(Stage primaryStage, int modelNum)
			throws UnknownHostException, IOException {

		this.modelNum = modelNum;
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

		model = new BallMVCModel(modelNum);

		Scene scene = new Scene(rootPane, 200, 200);
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setTitle("View #" + modelNum);
		primaryStage.setOnCloseRequest(e -> closeAllWindows());

	}

	private void closeAllWindows() {

		if (!MPfirstFlag)
			MPviewController.close();

		if (!MDfirstFlag)
			MDviewController.close();

		model.closeClient();

	}

	private void handleMousePointClass() {

		if (MPfirstFlag) {
			MPviewController = new PointViewController(modelNum);
			MPviewController.setModel(model);
			MPviewController.setAlwaysOnTop(true);
			MPviewController.setOnCloseRequest(e -> e.consume());
			MPfirstFlag = false;
		}

		mousePointFlag = (mousePointFlag) ? false : true;

		if (mousePointFlag) {
			MPviewController.show();
		} else {
			MPviewController.hide();
		}
	}

	private void handleMouseDraggedClass() {

		if (MDfirstFlag) {
			MDviewController = new DraggedViewController(modelNum);
			try {
				MDviewController.setModel(model);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			MDviewController.setAlwaysOnTop(true);
			MDviewController.setOnCloseRequest(e -> e.consume());
			MDfirstFlag = false;
		}

		mouseDraggedFlag = (mouseDraggedFlag) ? false : true;

		if (mouseDraggedFlag)
			MDviewController.show();
		else
			MDviewController.hide();
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub

	}
}
