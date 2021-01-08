import java.awt.*;
import javax.swing.*;

public class FlowLayoutTest {

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("FlowLayoutTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton button = new JButton("1");
        frame.add(button);

        button = new JButton("2");
        frame.add(button);

        frame.add(new JButton("3"));

        frame.add(new JButton("4"));

//        JPanel panel = new JPanel();
//        panel.add(new JButton("4.1"));
//        panel.add(new JButton("4.2"));
//        panel.add(new JButton("4.3"));
//        frame.add(panel, BorderLayout.CENTER);

        button = new JButton("5");
        frame.add(button);

        button = new JButton("6");
        frame.add(button);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }

}