import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class BallJFXClientServerMVC extends Application {

	private final String CLIENT = "client.png";
	private final String SERVER = "server.png";
	static int modelNum = 0;

	private Button serverBtn;
	private Button clientBtn;

	private Image imgClient = new Image(new File("img/client.jpg").toURI()
			.toString());
	private Image imgServer = new Image(new File("img/server.jpg").toURI()
			.toString());

	private int posX = 400;
	private int posY = 400;
	private final int INTERVAL = 15;

	private static int nextID = 1;

	private Server server;
	private BallViewMainPanel mainPanel;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Stage stage = new Stage();
		FlowPane mainPane = new FlowPane();

		serverBtn = new Button("Server - on", new ImageView(imgServer));
		serverBtn.setContentDisplay(ContentDisplay.TOP);
		clientBtn = new Button("Client", new ImageView(imgClient));
		clientBtn.setContentDisplay(ContentDisplay.TOP);
		serverBtn.setPrefWidth(350);
		serverBtn.setPrefHeight(300);
		clientBtn.setPrefWidth(350);
		clientBtn.setPrefHeight(300);

		mainPane.getChildren().addAll(serverBtn, clientBtn);
		Scene scence = new Scene(mainPane);

		clientBtn.setDisable(true);
		clientBtn.setOnAction(e -> clientClick());
		serverBtn.setOnAction(e -> serverClick());

		// scence.wi
		primaryStage.setScene(scence); // Place the scene in the stage
		primaryStage.setWidth(350);
		primaryStage.setHeight(600);
		primaryStage.show(); // Display the stage
		primaryStage.setTitle("Seminar");
		primaryStage.setResizable(false);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setOnCloseRequest(e -> exitAll());
	}

	private void exitAll() {
		Platform.exit();
		System.exit(0);
	}

	private void serverClick() {

		Stage stage = new Stage();
		server = new Server();
		server.start(stage);
		clientBtn.setDisable(false);

		serverBtn.setDisable(true);

	}

	private void clientClick() {
		// TODO Auto-generated method stub
		Stage stage = new Stage();
		mainPanel = new BallViewMainPanel();

		try {
			mainPanel.start(stage, ++modelNum);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}