import java.awt.*;
import javax.swing.*;

public class BorderLayoutTest {

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("BorderLayoutTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Use the frame's default BorderLayout. Explicit setting via
        //frame.setLayout(new BorderLayout());

        JButton button = new JButton("1");
        frame.add(button, BorderLayout.PAGE_START); //Also: NORTH

        button = new JButton("2");
        frame.add(button, BorderLayout.LINE_START); //Also: WEST

        frame.add(new JButton("3"), BorderLayout.CENTER);

//        JPanel panel = new JPanel();
//        panel.add(new JButton("3.1"));
//        panel.add(new JButton("3.2"));
//        panel.add(new JButton("3.3"));
//        frame.add(panel, BorderLayout.CENTER);

        button = new JButton("4");
        frame.add(button, BorderLayout.LINE_END);   //Also: EAST

        button = new JButton("5");
        frame.add(button, BorderLayout.PAGE_END);   //Also: SOUTH

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
