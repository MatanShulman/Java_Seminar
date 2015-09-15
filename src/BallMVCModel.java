import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yoni on 19/05/2015.
 */

/** Connect to DB */

public class BallMVCModel implements BallEvent {

	// Finals //
	private final int MAX_RADIUS = 70;
	private final int MIN_RADIUS = 10;

	// General Model variables //
	private int modelNumber;
	private Map<eventType, ArrayList<ActionListener>> BallModelHashMap = new HashMap<eventType, ArrayList<ActionListener>>();

	// Mouse Dragged variables //
	private double mdx = 20;
	private double mdy = 20;
	private double mdRadius = MIN_RADIUS;

	// Mouse Point variables //
	private boolean mouseInCircle = false;
	private String ballColor;
	private String frameColor;

	// server variables //
	private Socket socket;
	private DataOutputStream toServer;
	private DataInputStream FromServer;

	public BallMVCModel(int modelNumber) throws UnknownHostException,
			IOException {
		connectToServer();

		this.modelNumber = modelNumber;

		toServer.writeUTF("View #" + modelNumber + " Was Created \n");
		for (eventType et : eventType.values()) {
			BallModelHashMap.put(et, new ArrayList<ActionListener>());
		}

	}

	private void connectToServer() throws UnknownHostException, IOException {

		socket = new Socket("localhost", 8000);
		toServer = new DataOutputStream(socket.getOutputStream());
		FromServer = new DataInputStream(socket.getInputStream());
	}

	public int getModelNumber() {
		return modelNumber;
	}

	// Mouse Dragged Methods //

	public double getX() {
		return mdx;
	}

	public double getY() {
		return mdy;
	}

	public double getRadius() {
		return mdRadius;
	}

	public void decreaseRadius() {
		if (mdRadius > MIN_RADIUS) {
			mdRadius--;
		}

		processEvent(eventType.DECREASE, new ActionEvent(this,
				ActionEvent.ACTION_PERFORMED, eventType.DECREASE.toString()));

		try {
			toServer.writeUTF("MPview #" + getModelNumber()
					+ " Ball Radius Decreased to " + mdRadius + "  \n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void increaseRadius() {
		if (mdRadius < MAX_RADIUS) {
			mdRadius++;
		}

		processEvent(eventType.INCREASE, new ActionEvent(this,
				ActionEvent.ACTION_PERFORMED, eventType.INCREASE.toString()));

		try {
			toServer.writeUTF("MPview #" + getModelNumber()
					+ " Ball Radius Increased to " + mdRadius + "  \n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void moveBall(double x, double y) {
		mdx = x;
		mdy = y;
		processEvent(eventType.DRAGGED, new ActionEvent(this,
				ActionEvent.ACTION_PERFORMED, eventType.DRAGGED.toString()));

		try {
			toServer.writeUTF("MPview #" + getModelNumber()
					+ " Ball Moved To (" + x + "," + y + ") \n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setMdRadius(int mdRadius) {
		this.mdRadius = mdRadius;
	}

	public void setMdx(int mdx) {
		this.mdx = mdx;
	}

	public void setMdy(int mdy) {
		this.mdy = mdy;
	}

	// Mouse Point Methods //

	public void isInside() {

		try {
			if (!mouseInCircle)
				toServer.writeUTF("MPview #" + getModelNumber()
						+ " mouse moved inside circle \n");
		} catch (IOException e) {

			e.printStackTrace();
		}
		mouseInCircle = true;

		processEvent(eventType.ISINSIDE, new ActionEvent(this,
				ActionEvent.ACTION_PERFORMED, eventType.ISINSIDE.toString()));

	}

	public void isOutside() {

		try {
			if (mouseInCircle)
				toServer.writeUTF("MPview #" + getModelNumber()
						+ " mouse moved outside circle \n");
		} catch (IOException e) {

			e.printStackTrace();
		}
		mouseInCircle = false;

		processEvent(eventType.ISOUTSIDE, new ActionEvent(this,
				ActionEvent.ACTION_PERFORMED, eventType.ISOUTSIDE.toString()));
	}

	public void setFillColor(String fillColor) {
		ballColor = fillColor;

		processEvent(eventType.FILL, new ActionEvent(this,
				ActionEvent.ACTION_PERFORMED, eventType.FILL.toString()));

		try {

			toServer.writeUTF("MPview #" + getModelNumber()
					+ " Ball Fill Color Changed To " + ballColor + " \n");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void setFramecolor(String framColor) {
		this.frameColor = framColor;

		processEvent(eventType.FRAME, new ActionEvent(this,
				ActionEvent.ACTION_PERFORMED, eventType.FRAME.toString()));

		try {

			toServer.writeUTF("MPview #" + getModelNumber()
					+ " Ball Frame Color Changed To " + framColor + " \n");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	// general model events section //

	public synchronized void addActionListener(ActionListener l, eventType et) {
		ArrayList<ActionListener> al;
		al = BallModelHashMap.get(et);

		if (al == null)
			al = new ArrayList<ActionListener>();

		al.add(l);
		BallModelHashMap.put(et, al);
	}

	public synchronized void removeActionListener(ActionListener l, eventType et) {
		ArrayList<ActionListener> al;
		al = BallModelHashMap.get(et);
		if (al != null && al.contains(l))
			al.remove(l);
		BallModelHashMap.put(et, al);
	}

	private void processEvent(eventType et, ActionEvent e) {
		ArrayList<ActionListener> al;
		synchronized (this) {
			al = BallModelHashMap.get(et);
			if (al == null)
				return;
		}

		for (int i = 0; i < al.size(); i++) {
			ActionListener listener = (ActionListener) al.get(i);
			listener.actionPerformed(e);
		}
	}

	public void closeClient() {
		try {
			toServer.writeUTF("View #" + modelNumber + " Closed \n");
			toServer.writeUTF("close");
			socket.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
