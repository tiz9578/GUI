
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * One ball bouncing inside a rectangular box. All codes in one file. Poor
 * design!
 */
// Extends JPanel, so as to override the paintComponent() for custom rendering codes.
public class BouncingBallSimple extends JPanel {
	// Container box's width and height
	private static final int BOX_WIDTH = 20;
	private static final int BOX_HEIGHT = 200;

	// Ball's properties
	private float ballRadius = 10; // Ball's radius
	private float ballX = this.ballRadius + 5; // Ball's center (x, y)
	private float ballY = this.ballRadius + 2;
	private float ballSpeedX = 0; // Ball's speed for x and y
	private float ballSpeedY = 2;

	private static final int UPDATE_RATE = 30; // Number of refresh per second

	/** Constructor to create the UI components and init game objects. */
	public BouncingBallSimple() {
		this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));

		// Start the ball bouncing (in its own thread)
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
						BouncingBallSimple.this.ballX = BouncingBallSimple.this.ballRadius; // Re-position the ball at
																							// the edge
					} else if ((BouncingBallSimple.this.ballX + BouncingBallSimple.this.ballRadius) > BOX_WIDTH) {
						BouncingBallSimple.this.ballSpeedX = -BouncingBallSimple.this.ballSpeedX;
						BouncingBallSimple.this.ballX = BOX_WIDTH - BouncingBallSimple.this.ballRadius;
					}
					// May cross both x and y bounds
					if ((BouncingBallSimple.this.ballY - BouncingBallSimple.this.ballRadius) < 0) {
						BouncingBallSimple.this.ballSpeedY = -BouncingBallSimple.this.ballSpeedY;
						BouncingBallSimple.this.ballY = BouncingBallSimple.this.ballRadius;
					} else if ((BouncingBallSimple.this.ballY + BouncingBallSimple.this.ballRadius) > BOX_HEIGHT) {
						BouncingBallSimple.this.ballSpeedY = -BouncingBallSimple.this.ballSpeedY;
						BouncingBallSimple.this.ballY = BOX_HEIGHT - BouncingBallSimple.this.ballRadius;
					}
					// Refresh the display
					BouncingBallSimple.this.repaint(); // Callback paintComponent()
					// Delay for timing control and give other threads a chance
					try {
						Thread.sleep(1000 / UPDATE_RATE); // milliseconds
					} catch (InterruptedException ex) {
					}
				}
			}
		};
		gameThread.start(); // Callback run()
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

	/** main program (entry point) */
	public static void main(String[] args) {
		// Run GUI in the Event Dispatcher Thread (EDT) instead of main thread.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Set up main window (using Swing's Jframe)
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(new BouncingBallSimple());
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}