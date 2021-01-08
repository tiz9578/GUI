import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoxLayoutTest {

	private static void createAndShowGUI() {

		// 2 Boxes one near to the other in a default FlowLayout

		JFrame frame = new JFrame("BoxLayoutTest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());

		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		frame.add(p1);

		JPanel p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		frame.add(p2);

		JButton button = new JButton("1");
		p1.add(button);
		button = new JButton("2");
		p1.add(button);

		p2.add(new JButton("3"));
		p2.add(new JButton("4"));

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
