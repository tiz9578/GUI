import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TreePanel extends JFrame implements ActionListener {
	// Buttons
	private JButton lightButton;
	private JButton presentButton;
	private JButton ornamentButton;
	private JButton addAllButton;
	private JButton exitButton;

	// Panels
	private JPanel titlePanel, imagePanel, buttonPanel, infoPanel;

	// Labels
	private JLabel title, imageLabel, buttonLabel;

	// Image
	private ImageIcon image;

	// Boolean
	private boolean presents = true;
	private boolean ornaments = true;
	private boolean lights = true;

	public TreePanel() {
		// Set title
		this.setTitle("Christmas Page");

		this.setLayout(new BorderLayout());

		// Setting the title of the JLabel
		this.title = new JLabel("Decorate the Christmas tree!");

		// Setting the font
		this.title.setFont(new Font("CENTURY GOTHIC", Font.ITALIC, 24));

		// Setting the text color to red and positioning it to the center
		this.title.setForeground(Color.red);
		this.title.setHorizontalAlignment(SwingConstants.CENTER);

		// Creating a new JPanel and adding the title label to it
		this.titlePanel = new JPanel();
		this.titlePanel.add(this.title);

		// Setting colour of title panel
		this.titlePanel.setBackground(Color.white);

		// Creating a new JPanel for the image to go
		this.imagePanel = new JPanel();

		// Retrieving image from the file
		this.image = new ImageIcon(this.getClass().getResource("tree.png"));

		// Adding the image to a label
		this.imageLabel = new JLabel(this.image);

		// Adding image label to the image panel
		this.imagePanel.add(this.imageLabel);

		// Setting colour of image panel
		this.imagePanel.setBackground(Color.white);

		// Creating a new JPanel for the buttons to go
		this.buttonPanel = new JPanel();

		// Setting colour of button panel
		this.buttonPanel.setBackground(Color.blue);

		// Button Label
		this.buttonLabel = new JLabel("Click on the button to add the item to the tree.");
		this.buttonLabel.setFont(new Font("CENTURY GOTHIC", Font.ITALIC, 16));
		this.buttonLabel.setForeground(Color.red);
		this.buttonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.buttonLabel.setBackground(Color.white);

		// Info panel
		this.infoPanel = new JPanel();
		this.infoPanel.add(this.buttonLabel);
		this.infoPanel.setBackground(Color.white);

		// Naming buttons
		this.lightButton = new JButton("Lights");
		this.presentButton = new JButton("Presents");
		this.ornamentButton = new JButton("Ornaments");
		this.addAllButton = new JButton("Add All");
		this.exitButton = new JButton("Exit");

		// Setting colour of buttons
		this.lightButton.setBackground(Color.red);
		this.ornamentButton.setBackground(Color.red);
		this.presentButton.setBackground(Color.red);
		this.addAllButton.setBackground(Color.red);
		this.exitButton.setBackground(Color.red);

		// Setting font on buttons
//		this.lightButton.setFont(new Font("CENTURY GOTHIC", Font.ITALIC, 16));
//		this.ornamentButton.setFont(new Font("CENTURY GOTHIC", Font.ITALIC, 16));
//		this.presentButton.setFont(new Font("CENTURY GOTHIC", Font.ITALIC, 16));
//		this.addAllButton.setFont(new Font("CENTURY GOTHIC", Font.ITALIC, 16));
//		this.exitButton.setFont(new Font("CENTURY GOTHIC", Font.ITALIC, 16));

		// Setting font colour on buttons
		this.lightButton.setForeground(Color.DARK_GRAY);
		this.ornamentButton.setForeground(Color.DARK_GRAY);
		this.presentButton.setForeground(Color.DARK_GRAY);
		this.addAllButton.setForeground(Color.DARK_GRAY);
		this.exitButton.setForeground(Color.DARK_GRAY);

		// Add the buttons to the buttonPanel
		this.buttonPanel.add(this.lightButton);
		this.buttonPanel.add(this.ornamentButton);
		this.buttonPanel.add(this.presentButton);
		this.buttonPanel.add(this.addAllButton);
		this.buttonPanel.add(this.exitButton);

		// Enable buttons to listen for a mouse-click
		this.lightButton.addActionListener(this);
		this.ornamentButton.addActionListener(this);
		this.presentButton.addActionListener(this);
		this.addAllButton.addActionListener(this);
		this.exitButton.addActionListener(this);

		// Positioning Panels
		this.add(this.titlePanel, BorderLayout.NORTH);
		this.add(this.imagePanel, BorderLayout.CENTER);
		this.imagePanel.add(this.infoPanel, BorderLayout.NORTH);
		this.add(this.buttonPanel, BorderLayout.SOUTH);

		// Configure the frame
		this.getContentPane().setBackground(Color.white);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 650);
		this.setLocation(300, 40);
		this.setVisible(true);

	}// Constructor

	@Override
	public void paint(Graphics g) {
		// Call the paint method of the superclass
		super.paint(g);

		if (this.lights) {
			// draw lights
			g.setColor(Color.white);

			// middle lights
			g.fillOval(250, 190, 10, 10);
			g.fillOval(250, 260, 10, 10);
			g.fillOval(250, 350, 10, 10);
			g.fillOval(250, 430, 10, 10);

			// left lights
			g.fillOval(200, 260, 10, 10);
			g.fillOval(180, 350, 10, 10);
			g.fillOval(160, 430, 10, 10);

			// right lights
			g.fillOval(300, 260, 10, 10);
			g.fillOval(330, 350, 10, 10);
			g.fillOval(340, 430, 10, 10);

		} // if lights

		else if (this.ornaments) {
			// draw ornaments
			g.setColor(Color.red);
			g.fillOval(220, 220, 15, 15);
			g.fillOval(280, 220, 15, 15);
			g.setColor(Color.blue);
			g.fillOval(320, 380, 15, 15);
			g.fillOval(180, 380, 15, 15);
			g.setColor(Color.cyan);
			g.fillOval(200, 300, 15, 15);
			g.fillOval(300, 300, 15, 15);

		} // if ornaments

		else {
			// draw presents
			g.setColor(Color.red);
			g.fillRect(320, 500, 60, 60);
			g.fillRect(140, 480, 30, 30);

			g.setColor(Color.orange);
			g.fillRect(180, 530, 40, 40);

			g.setColor(Color.blue);
			g.fillRect(120, 500, 60, 60);
			g.fillRect(360, 540, 30, 30);

		} // if presents

	} // paint

	// Coding the event-handling routine
	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == this.lightButton) {
			this.ornaments = false;
			this.presents = false;
			this.lights = true;
			this.repaint();

		} // if light

		else if (event.getSource() == this.ornamentButton) {
			this.lights = false;
			this.presents = false;
			this.ornaments = true;
			this.repaint();

		} // if ornament

		else if (event.getSource() == this.presentButton) {
			this.lights = false;
			this.ornaments = false;
			this.presents = true;
			this.repaint();

		} // if present

		else if (event.getSource() == this.addAllButton) {
			this.lights = true;
			this.ornaments = true;
			this.presents = true;
			this.repaint();
		} // if add all

		else {
			System.exit(0);

		} // else exit

	} // actionPerformed

	public static void main(String[] args) {
		TreePanel gui = new TreePanel();

	}// main

}// class