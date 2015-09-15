import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
	private TextArea ta = new TextArea();
	private ServerSocket serverSocket;
	private Socket socket;
	private ArrayList<HandleAClient> clientArray = new ArrayList<>();
	static int modelNum = 0;
	boolean isRunning = true;

	@Override
	// Override the start method in the Application class
	public void start(Stage primaryStage) {
		Scene scene = new Scene(new ScrollPane(ta), 480, 185);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();

		new Thread(() -> {
			try {
				serverSocket = new ServerSocket(8000);
				Platform.runLater(() -> {

				});

				// Client communications management
				while (isRunning) {
					socket = serverSocket.accept();
					Platform.runLater(() -> {
					});
					// Create and start a new thread for the connection
					HandleAClient temp = new HandleAClient(socket);
					clientArray.add(temp);

					new Thread(temp).start();
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}).start();
	}

	public void closeServer() {

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isRunning = false;

	}

	// Class for handling new connection
	class HandleAClient implements Runnable {
		private Socket socket;
		private boolean clientIsRunning = true;

		public HandleAClient(Socket socket) {
			this.socket = socket;

		}

		public void closeClient() {
			clientIsRunning = false;
			for (int i = 0; i < clientArray.size(); i++) {

				if (clientArray.get(i).socket == this.socket) {
					clientArray.remove(i);
				}
			}


		}

		/** Run a thread */
		public void run() {
			try {
				DataInputStream fromClient = new DataInputStream(
						socket.getInputStream());
				DataOutputStream toClient = new DataOutputStream(
						socket.getOutputStream());

				// Continuously serve the client

				while (clientIsRunning) {
					String s = fromClient.readUTF();

					switch (s) {
					case "close":
						closeClient();
						break;
					default:
						ta.appendText(s);
						break;
					}

				}

			} catch (IOException e) {

			}
		}
	}

}