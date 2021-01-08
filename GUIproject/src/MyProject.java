
// Using AWT's Graphics and Color
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
// Using AWT's event classes and listener interface
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Using Swing's components and containers
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Custom Graphics Example: Using key/button to move a line left or right.
 */
@SuppressWarnings("serial")
public class MyProject extends JFrame {
	// Define constants for the various dimensions
	public static final int CANVAS_WIDTH = 400;
	public static final int CANVAS_HEIGHT = 140;
	public static final Color LINE_COLOR = Color.BLACK;
	// public static final Color CANVAS_BACKGROUND = Color.CYAN;
	private JPanel display;

	Color bgColor = Color.LIGHT_GRAY;

	// The moving line from (x1, y1) to (x2, y2), initially position at the center
	private int x1 = CANVAS_WIDTH / 2;
	private int y1 = CANVAS_HEIGHT / 8;
	private int x2 = this.x1;
	private int y2 = (CANVAS_HEIGHT / 8) * 7;
	private JProgressBar pb = new JProgressBar();

	private DrawCanvas canvas; // The custom drawing canvas
	private JSlider sliderWidth;

	// Constructor to set up the GUI components and event handlers
	public MyProject() {
		// Set up a panel for the buttons
		JPanel btnPanel = new JPanel(new BorderLayout());
		JButton btnLeft = new JButton("Move Left ");

		btnPanel.add(btnLeft, BorderLayout.NORTH);

		btnLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				MyProject.this.x1 -= 10;
				MyProject.this.x2 -= 10;
				MyProject.this.canvas.repaint();
				MyProject.this.requestFocus(); // change the focus to JFrame to receive KeyEvent
			}
		});
		JButton btnRight = new JButton("Move Right");
		btnPanel.add(btnRight, BorderLayout.SOUTH);
		btnRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				MyProject.this.x1 += 10;
				MyProject.this.x2 += 10;
				MyProject.this.canvas.repaint();
				MyProject.this.requestFocus(); // change the focus to JFrame to receive KeyEvent
			}
		});

		JButton btn1 = new JButton("Move here ");
		btnPanel.add(btn1, BorderLayout.EAST);

		JButton fill = new JButton("Set Background"); // The first button.

		fill.setBackground(Color.lightGray);
		btnPanel.add(fill);

		fill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Color color = JColorChooser.showDialog(MyProject.this, "Choose a color", MyProject.this.bgColor);
				if (color != null) { // new color selected
					MyProject.this.bgColor = color;
				}
				// this.canvas.setBackground(MyProject.this.bgColor); // change panel's
				// background color
			}
		});

		JLabel label4 = new JLabel("Width Control:", JLabel.RIGHT);
		this.sliderWidth = new JSlider(JSlider.HORIZONTAL, 0, 20, 1);
		this.sliderWidth.setPaintTicks(true);
		this.sliderWidth.setMajorTickSpacing(5);
		this.sliderWidth.setMinorTickSpacing(1);
		this.sliderWidth.setPaintLabels(true);
		this.pb.setModel(this.sliderWidth.getModel()); // Share model
		this.sliderWidth.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				MyProject.this.canvas.repaint();
			}
		});
		JPanel widthPanel = new JPanel();
		widthPanel.setLayout(new GridLayout(1, 2));
		widthPanel.add(label4);
		widthPanel.add(this.sliderWidth);
		btnPanel.add(widthPanel, BorderLayout.WEST);

		// Set up a custom drawing JPanel
		this.display = new JPanel();
		this.display.setPreferredSize(new Dimension(120, 120));
		this.display.setBorder(LineBorder.createGrayLineBorder());
		this.display.setBackground(Color.red);

		this.canvas = new DrawCanvas();
		this.canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		this.display.add(this.canvas);

		// Add both panels to this JFrame's content-pane
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(this.display, BorderLayout.CENTER);
		// cp.add(this.canvas, BorderLayout.CENTER);
		cp.add(this.pb, BorderLayout.NORTH);
		cp.add(btnPanel, BorderLayout.SOUTH);

		// "super" JFrame fires KeyEvent
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				switch (evt.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					MyProject.this.x1 -= 10;
					MyProject.this.x2 -= 10;
					MyProject.this.repaint();
					break;
				case KeyEvent.VK_RIGHT:
					MyProject.this.x1 += 10;
					MyProject.this.x2 += 10;
					MyProject.this.repaint();
					break;
				}
			}
		});

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle the CLOSE button
		this.setTitle("Move a Line");
		this.pack(); // pack all the components in the JFrame
		this.setVisible(true); // show it
		this.requestFocus(); // set the focus to JFrame to receive KeyEvent
	}

	/**
	 * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
	 */
	class DrawCanvas extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			// this.setBackground(CANVAS_BACKGROUND);
			g.setColor(LINE_COLOR);
			g.drawLine(MyProject.this.x1, MyProject.this.y1, MyProject.this.x2, MyProject.this.y2); // Draw the line

			//// test
//			Rectangle bounds = MyProject.this.sliderWidth.getBounds();
//			g.fillRoundRect(bounds.x - 5, bounds.y - 5, bounds.width / 3, bounds.height + 10, 15, 15);
//			g.fillOval(bounds.x - 15, (bounds.y + bounds.height) - 10, 35, 30);

		}
	}

	// The entry main() method
	public static void main(String[] args) {
		// Run GUI codes on the Event-Dispatcher Thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MyProject(); // Let the constructor do the job
			}
		});
	}
}
