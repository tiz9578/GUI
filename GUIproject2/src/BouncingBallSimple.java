
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JPanel;

// Extends JPanel, so as to override the paintComponent() for custom rendering codes.
public class BouncingBallSimple extends JPanel {
	// Container box's width and height
	private static final int BOX_WIDTH = 20;
	private static final int BOX_HEIGHT = 200;

	// Prepare for the sound
	private MidiChannel channel = null;
	private Synthesizer synthesizer;
	final static int HAND_CLAP = 39;
	private int intialVol = 80; // initial volume

	// Ball's properties
	private float ballRadius = 10; // Ball's radius
	private float ballX = this.ballRadius + 5; // Ball's center (x, y)
	private float ballY = this.ballRadius + 2;
	private float ballSpeedX = 0; // Ball's speed for x is 0
	private float ballSpeedY = 5;

	private static final int UPDATE_RATE = 30; // Number of refresh per second

	// Constructor
	public BouncingBallSimple() {
		this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));

		try {
			final Synthesizer synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			this.channel = synthesizer.getChannels()[9];

		} catch (MidiUnavailableException ex) {

		}

		// Start the ball bouncing (
		Thread gameThread = new Thread() {
			@Override
			public void run() {
				while (true) { // Execute one update step
					// Calculate the ball's new position
					BouncingBallSimple.this.ballX += BouncingBallSimple.this.ballSpeedX;
					BouncingBallSimple.this.ballY += BouncingBallSimple.this.ballSpeedY;
					// Check if the ball moves over the bounds
					// If so, adjust the position and speed.
					if ((BouncingBallSimple.this.ballX - BouncingBallSimple.this.ballRadius) < 0) {
						BouncingBallSimple.this.ballSpeedX = -BouncingBallSimple.this.ballSpeedX; // Reflect along
																									// normal
						BouncingBallSimple.this.ballX = BouncingBallSimple.this.ballRadius;
						// Toolkit.getDefaultToolkit().beep();// Re-position the ball at
						// the edge
					} else if ((BouncingBallSimple.this.ballX + BouncingBallSimple.this.ballRadius) > BOX_WIDTH) {
						BouncingBallSimple.this.ballSpeedX = -BouncingBallSimple.this.ballSpeedX;
						BouncingBallSimple.this.ballX = BOX_WIDTH - BouncingBallSimple.this.ballRadius;
						// Toolkit.getDefaultToolkit().beep();
					}
					// May cross both x and y bounds
					if ((BouncingBallSimple.this.ballY - BouncingBallSimple.this.ballRadius) < 0) {
						BouncingBallSimple.this.ballSpeedY = -BouncingBallSimple.this.ballSpeedY;
						BouncingBallSimple.this.ballY = BouncingBallSimple.this.ballRadius;
						// Toolkit.getDefaultToolkit().beep();
						BouncingBallSimple.this.channel.noteOn(BouncingBallSimple.this.HAND_CLAP,
								BouncingBallSimple.this.intialVol);

					} else if ((BouncingBallSimple.this.ballY + BouncingBallSimple.this.ballRadius) > BOX_HEIGHT) {
						BouncingBallSimple.this.ballSpeedY = -BouncingBallSimple.this.ballSpeedY;
						BouncingBallSimple.this.ballY = BOX_HEIGHT - BouncingBallSimple.this.ballRadius;
						// Toolkit.getDefaultToolkit().beep();
						BouncingBallSimple.this.channel.noteOn(BouncingBallSimple.this.HAND_CLAP,
								BouncingBallSimple.this.intialVol);
					}
					// Refresh the display
					BouncingBallSimple.this.repaint(); // Callback paintComponent()
					// Delay for timing control
					try {
						Thread.sleep(1000 / UPDATE_RATE);
					} catch (InterruptedException ex) {
					}
				}
			}
		};
		gameThread.start(); // Callback run()
	}

	public int getIntialVol() {
		return this.intialVol;
	}

	public void setIntialVol(int intialVol) {
		this.intialVol = intialVol;
	}

	/** Custom rendering codes for drawing the JPanel */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Paint background

		// Draw the box
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, BOX_WIDTH, BOX_HEIGHT);

		// Draw the ball
		g.setColor(Color.BLUE);
		g.fillOval((int) (this.ballX - this.ballRadius), (int) (this.ballY - this.ballRadius),
				(int) (2 * this.ballRadius), (int) (2 * this.ballRadius));

		// Display the ball's information
//		g.setColor(Color.WHITE);
//		g.setFont(new Font("Courier New", Font.PLAIN, 12));
//		StringBuilder sb = new StringBuilder();
//		Formatter formatter = new Formatter(sb);
//		formatter.format("Ball @(%3.0f,%3.0f) Speed=(%2.0f,%2.0f)", this.ballX, this.ballY, this.ballSpeedX,
//				this.ballSpeedY);
//		g.drawString(sb.toString(), 20, 30);
	}

	public void setBallSpeedY(float ballSpeedY) {
		this.ballSpeedY = ballSpeedY;
	}

	/** main program (entry point) */
//	public static void main(String[] args) {
//		// Run GUI in the Event Dispatcher Thread (EDT) instead of main thread.
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				// Set up main window (using Swing's Jframe)
//				JFrame frame = new JFrame();
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				frame.setContentPane(new BouncingBallSimple());
//				frame.pack();
//				frame.setVisible(true);
//			}
//		});
//	}
}