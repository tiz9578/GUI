import java.awt.*;
import javax.swing.*;

public class GridBagLayoutTest {

    private static void createAndShowGUI() {

        JFrame frame = new JFrame("GridBagLayoutTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;

        TextField textfield = new TextField();
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=4;
        frame.add(textfield,c);

        c.gridwidth=1;

        JButton button = new JButton("7");
        c.gridx=0;
        c.gridy=1;
        frame.add(button,c);

        button = new JButton("8");
        c.gridx=1;
        c.gridy=1;
        frame.add(button,c);

        button = new JButton("9");
        c.gridx=2;
        c.gridy=1;
        frame.add(button,c);

        button = new JButton("/");
        c.gridx=3;
        c.gridy=1;
        frame.add(button,c);


        button = new JButton("4");
        c.gridx=0;
        c.gridy=2;
        frame.add(button,c);

        button = new JButton("5");
        c.gridx=1;
        c.gridy=2;
        frame.add(button,c);

        button = new JButton("6");
        c.gridx=2;
        c.gridy=2;
        frame.add(button,c);

        button = new JButton("*");
        c.gridx=3;
        c.gridy=2;
        frame.add(button,c);


        button = new JButton("1");
        c.gridx=0;
        c.gridy=3;
        frame.add(button,c);

        button = new JButton("2");
        c.gridx=1;
        c.gridy=3;
        frame.add(button,c);

        button = new JButton("3");
        c.gridx=2;
        c.gridy=3;
        frame.add(button,c);

        button = new JButton("-");
        c.gridx=3;
        c.gridy=3;
        frame.add(button,c);


        button = new JButton("0");
        c.gridx=0;
        c.gridy=4;
        frame.add(button,c);

        button = new JButton(".");
        c.gridx=1;
        c.gridy=4;
        frame.add(button,c);

        button = new JButton("=");
        c.gridx=2;
        c.gridy=4;
        frame.add(button,c);

        button = new JButton("+");
        c.gridx=3;
        c.gridy=4;
        frame.add(button,c);


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
