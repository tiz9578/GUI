import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridLayoutTest {

	private static void createAndShowGUI() {

		JFrame frame = new JFrame("GridLayoutTest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// GridLayout(int row, int col, int hgap, int vgap)
		// : construct a grid layout with specified rows, columns and gaps between
		// components.
		frame.setLayout(new GridLayout(3, 2, 10, 10));

		JButton button = new JButton("1");
		frame.add(button);

		button = new JButton("2");
		frame.add(button);

		frame.add(new JButton("3"));

		frame.add(new JButton("4"));

		JPanel panel = new JPanel();
		panel.add(new JButton("4.1"));
		panel.add(new JButton("4.2"));
		panel.add(new JButton("4.3"));
		// roba mia
//		JPanel panel1 = new JPanel();
//		panel1.add(new JButton("4.1"));
//		panel1.add(new JButton("4.2"));
//		panel1.add(new JButton("4.3"));
		frame.add(panel, BorderLayout.CENTER);
		// frame.add(panel1, BorderLayout.NORTH);

		button = new JButton("5");
		frame.add(button);

		button = new JButton("6");
		frame.add(button);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});

	}

}
