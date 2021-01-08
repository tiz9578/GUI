import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class layouts {

	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add the ubiquitous "Hello World" label.

		Container pane = frame.getContentPane();

		JButton button = new JButton("1");
		pane.add(button, BorderLayout.PAGE_START);

		// Make the center component big, since that's the
		// typical usage of BorderLayout.
		button = new JButton("3");
		// button.setPreferredSize(new Dimension(200, 100));
		JLabel label1 = new JLabel();
		label1.setLayout(new FlowLayout());
		JButton b = new JButton("3.1");
		label1.add(b);
		label1.add(new JButton("3.2"));
		label1.add(new JButton("3.3"));

		pane.add(label1, BorderLayout.CENTER);

		button = new JButton("2");
		pane.add(button, BorderLayout.LINE_START);

		button = new JButton("4");
		pane.add(button, BorderLayout.PAGE_END);

		button = new JButton("5 ");
		pane.add(button, BorderLayout.LINE_END);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
