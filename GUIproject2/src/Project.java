import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;

public class Project {

	private JFrame frame;
	private JPanel containerPane;
	private JPanel topPane;
	private JPanel bottomPane;
	private static JSlider slider;
	private static BouncingBallSimple ball;
	// test
	private static JButton showVIs;

	// images
	private ImageIcon mute;
	private ImageIcon min;
	private ImageIcon med;
	private ImageIcon max;

	public void createAndShowGui() {

		this.frame = new JFrame("Example of 2 panels");
		this.containerPane = new JPanel();
		this.topPane = new JPanel();
		this.bottomPane = new JPanel();

		this.containerPane.setLayout(new GridLayout(2, 1));
		this.topPane.setLayout(new GridLayout(1, 10));
		this.bottomPane.setLayout(new GridLayout());

		// this.topPane.add(new JLabel("Left side"));
		// this.topPane.add(new JLabel("Right side"));
		// The mute botton should be static to interact with slider
		showVIs = new JButton("Mute");

		// this.bottomPane.add(new JLabel("Left side"));
		// this.bottomPane.add(new JLabel("Right side"));

		// Load the images:

		this.mute = new ImageIcon(this.getClass().getResource("images/volMute.png"));
		this.min = new ImageIcon(this.getClass().getResource("images/volMin.png"));
		this.med = new ImageIcon(this.getClass().getResource("images/volMed.png"));
		this.max = new ImageIcon(this.getClass().getResource("images/volMax.png"));

		JLabel labelSlider = new JLabel("Volume Control:");
		this.slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 1);
		this.slider.setPaintTicks(true);
		this.slider.setMajorTickSpacing(5);
		this.slider.setMinorTickSpacing(1);
		this.slider.setPaintLabels(true);
		this.bottomPane.add(this.slider);

		// this.pb.setModel(this.sliderWidth.getModel()); // Share model
		// this.bottomPane.add(label4, BorderLayout.NORTH);

		JLabel volLevel = new JLabel();
		JLabel volLeveli = new JLabel(this.min);

		this.slider.addChangeListener((ChangeEvent event) -> {

			int value = this.slider.getValue();
			volLevel.setText("Volume = " + Integer.toString(value));

			// Try to change really the vol on Bouncing Ball

			ball.setIntialVol(80 + (value * 2));

			// If "unmute" set it to mute

			if (showVIs.getText() == "Unmute") {
				showVIs.setText("Mute");
			}

			// If value = = 0 ..Mute
			if (value == 0) {
				showVIs.doClick();
			}

			// Change the icon :

			if (value == 0) {
				volLeveli.setIcon(this.mute);
			} else if ((value > 0) && (value <= 5)) {
				volLeveli.setIcon(this.min);
			} else if ((value > 5) && (value < 15)) {
				volLeveli.setIcon(this.med);
			} else {
				volLeveli.setIcon(this.max);
			}

		});

		this.topPane.add(volLevel);
		this.topPane.add(volLeveli);

		// add Text box for volume
		JLabel setVol = new JLabel("Set Volume: ");
		JTextField volumeField = new JTextField();
		setVol.setLabelFor(volumeField);
		int valueToDispl = this.slider.getValue();
		volumeField.setText(Integer.toString(valueToDispl));
		// add to bottom
		this.bottomPane.add(setVol);
		this.bottomPane.add(volumeField);

		// Action Listener for volume
		volumeField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// System.out.println("The entered text is: " + volumeField.getText());
				slider.setValue(Integer.parseInt(volumeField.getText()));

			}
		});

		// Create jToggleButtons to Change color
		// They act as a single button (buttonGroup Option.
		ButtonGroup buttonGroup = new ButtonGroup();

		JToggleButton redBtn = new JToggleButton("red");
		JToggleButton greenBtn = new JToggleButton("green");
		JToggleButton blueBtn = new JToggleButton("blue");
		JPanel ToglePanel = new JPanel();
		buttonGroup.add(redBtn);
		buttonGroup.add(greenBtn);
		buttonGroup.add(blueBtn);
		ToglePanel.add(redBtn);
		ToglePanel.add(blueBtn);
		ToglePanel.add(greenBtn);

		this.bottomPane.add(ToglePanel);

		// Create Radio Button for speed:
		JLabel speedLab = new JLabel("Speed:");
		ButtonGroup radioGroup = new ButtonGroup();
		JRadioButton jrf = new JRadioButton("Fast");
		JRadioButton jrm = new JRadioButton("Medium");
		JRadioButton jrs = new JRadioButton("Slow", true);
		radioGroup.add(jrf);
		radioGroup.add(jrm);
		radioGroup.add(jrs);
		JPanel RadioPanel = new JPanel();
		RadioPanel.setLayout(new BoxLayout(RadioPanel, BoxLayout.Y_AXIS));
		RadioPanel.add(speedLab);
		RadioPanel.add(jrs);
		RadioPanel.add(jrm);
		RadioPanel.add(jrf);

		jrs.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ball.setBallSpeedY(5);
				}

			}
		});

		jrm.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ball.setBallSpeedY(10);
				}

			}
		});

		jrf.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					ball.setBallSpeedY(15);
				}

			}
		});

		this.bottomPane.add(RadioPanel);

		// Create button to mute Metronome.
		JLabel labelShowVis = new JLabel("Mute sound");
		// JButton showVIs = new JButton("Mute");

		// Mute the sound on click
		showVIs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// System.out.println("The entered text is: " + volumeField.getText());
				int vol = ball.getIntialVol();
				if (showVIs.getText() == "Mute") {
					ball.setIntialVol(0);
					showVIs.setText("Unmute");
				} else {
					if (showVIs.getText() == "Unmute") {
						// If you mute with slider be sure to go to default value
						if (Project.slider.getValue() == 0) {
							Project.slider.setValue(1);
						} else {

							ball.setIntialVol(Project.slider.getValue() + 80);
							showVIs.setText("Mute");
						}
					}
				}

			}
		});

		JPanel visualMetro = new JPanel();
		visualMetro.add(labelShowVis);
		visualMetro.add(showVIs);
		this.bottomPane.add(visualMetro);

		// create checkbox
		JCheckBox cb = new JCheckBox("Show Help", false);
		this.bottomPane.add(cb);

		// Display Help if checkbox is selected
		cb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == 1) {
					JDialog helpDial = new JDialog();
					JPanel hpanel = new JPanel(new FlowLayout());
					helpDial.getContentPane().add(hpanel);

					JLabel helpText = new JLabel();
					String helpContent = "<html>" + "<head>" + "<meta charset='UTF-8'>"
							+ "<title>Insert title here</title>" + "</head>" + "<body>"
							+ "<div align='center' style='font-size:1.2em'><b>Help for the Application</b></div>"
							+ "<ul>" + "    <li>This show a dialog to help the user.</li><br/>"
							+ "    <li>You can find, i.e, explanations about buttons<br/>" + "    </li>"
							+ "    <ul style='margin-top: -0.5em'>" + "</ul>" + "</ul>" + "</body>" + "</html>";
					helpText.setText(helpContent);
					hpanel.add(helpText);

					helpDial.setBounds(300, 200, 640, 620);

					hpanel.setLocation(20, 50);

					helpDial.setModal(true);
					helpDial.setVisible(true);

					// Set again to false
					cb.setSelected(false);

				}

			}
		});

		// The progress bar

		JProgressBar pb = new JProgressBar(JProgressBar.VERTICAL);
		// share the model data of the slider
		pb.setModel(this.slider.getModel());

		this.topPane.add(pb);

		// create Metronome image
		ball = new BouncingBallSimple();
		this.topPane.add(ball);

		// Set borders, add panes etc. No modif after this

		this.topPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE)));
		this.bottomPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE)));

		this.containerPane.add(this.topPane);
		this.containerPane.add(this.bottomPane);

		this.frame.add(this.containerPane);

//      frame.pack();
		this.frame.setSize(500, 400);
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Project().createAndShowGui();
			}
		});

	}
}
