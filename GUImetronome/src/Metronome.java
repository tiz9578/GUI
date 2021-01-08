import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Metronome extends Thread {

	public static int defaultMsPbpm = 5000;
	// by testing
	/*
	 * (emperical) 5000ms = 1bpm Therefore, timerDelay(ms) = 5000/bpm
	 */
	public int wy = 342; // initial position of weight
	public static int bpmInt;
	public static int msPbpm = defaultMsPbpm; // number ms at 1bpm
	public int timerDelay;

	public static int sliderKnob_width = 42;
	public static int sliderKnob_height = 19;
	public static int weight_width = 84;
	public static int weight_height = 68;
	final static int MAX_WY = 550;

	// public Point cursorPressedPosition;
	public int X = 0;
	public int Y = 0;
	boolean volUprWasHit = false;
	boolean volLwrWasHit = false;
	boolean subBeatWasHit = false;
	public int deltaX = 0;
	boolean weightWasHit;
	public int deltaY = 0;

	public Timer timer;
	public boolean hasStarted = false;
	public boolean stopAnimation = false;

	// Stuff for Midi
	public int volUprX = 110 + (80 * (140 / 127));
	public int volLwrX = 110 + (80 * (140 / 127));
	public int subBeatX = 395;
	public int numSubBeats;
	private MidiChannel channel = null;
	private static int majorVelocity = 80; // initial volume
	private static int minorVelocity = 80; // initial volume

	final static int HAND_CLAP = 39;
	final static int RIM_SHOT = 37;
	final static int COWBELL = 56;
	final static int HIGH_BONGO = 60;
	final static int LOW_BONGO = 61;
	final static int HIGH_TIMBALE = 65;
	final static int LOW_TIMBALE = 66;
	final static int HIGH_AGOGO = 67;
	final static int LOW_AGOGO = 68;
	final static int CLAVES = 75;
	final static int HIGH_WOOD_BLOCK = 76;
	final static int LOW_WOOD_BLOCK = 77;
	final static int CUICA = 78;
	final static int MUTE_TRIANGLE = 81;

	static int noteForMainBeat = CLAVES;
	static int noteForSubBeat = HIGH_WOOD_BLOCK;
	static int NUM_SUB_BEAT = 1;

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem settingsMenuItem;
	private JLabel soundLabel1;
	private JLabel soundLabel2;
	private JLabel timerDelayLabel = new JLabel();
	private JMenuItem helpMenuItem;

	public static void main(String[] args) {
		(new Metronome()).start();
	}

	public Metronome() {
		/**
		 * Get properties that were save when the app was last closed.
		 **/
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("metronome.properties");

			// load a properties file
			prop.load(input);

			// get the property values that were save when application was closed
			noteForMainBeat = Integer.parseInt(prop.getProperty("noteForMainBeat"));
			noteForSubBeat = Integer.parseInt(prop.getProperty("noteForSubBeat"));
			majorVelocity = Integer.parseInt(prop.getProperty("majorVelocity"));
			// System.out.println("majorVelocity: "+majorVelocity);
			this.volUprX = 124 + (majorVelocity * (140 / 127));
			minorVelocity = Integer.parseInt(prop.getProperty("minorVelocity"));
			// System.out.println("majorVelocity: "+majorVelocity);
			this.volLwrX = 124 + (minorVelocity * (140 / 127));
			this.subBeatX = Integer.parseInt(prop.getProperty("subBeatX"));
			this.numSubBeats = Integer.parseInt(prop.getProperty("numSubBeats"));
			msPbpm = Integer.parseInt(prop.getProperty("msPerBPM"));

			bpmInt = Integer.parseInt(prop.getProperty("tempo"));
			this.wy = ((bpmInt - 24) * 2) + 150;

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * Get Synthesizer
		 **/
		try {
			final Synthesizer synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			this.channel = synthesizer.getChannels()[9];

		} catch (MidiUnavailableException ex) {
			// log.error(ex);
		}

		/**
		 * Java Frame set-up
		 */
		EventQueue.invokeLater(new Runnable() {
			// @Override
			@Override
			public void run() {

				JFrame frame = new JFrame("DaveS Metronome");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// Add icon for the frame
				URL iconURL = this.getClass().getResource("images/DavesMetronome.png");
				// iconURL is null when not found
				ImageIcon icon = new ImageIcon(iconURL);
				frame.setIconImage(icon.getImage());

				// Add the menu
				Metronome.this.settingsMenuItem = new JMenuItem("Settings");
				Metronome.this.settingsMenuItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// Execute when UP button is pressed
						SettingsDialog dialog = new SettingsDialog();
						dialog.setModal(true);
						dialog.setVisible(true);
					}
				});
				Metronome.this.helpMenuItem = new JMenuItem("Help");
				Metronome.this.helpMenuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				Metronome.this.helpMenuItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// Execute when UP button is pressed
						HelpDialog dialog = new HelpDialog();
						dialog.setModal(true);
						dialog.setVisible(true);
					}
				});

				// define the KeyListener
				JTextField timerTrigger = new JTextField();
				timerTrigger.addKeyListener(new MyKeyListener());
				frame.add(timerTrigger);

				// add graphics pane
				frame.add(new AnimationPane());

				Metronome.this.menuBar = new JMenuBar();

				// add menus to menubar
				Metronome.this.menuBar.add(Metronome.this.settingsMenuItem);
				Metronome.this.menuBar.add(Metronome.this.helpMenuItem);

				// put the menubar on the frame
				frame.setJMenuBar(Metronome.this.menuBar);

				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent ev) {
						// Save variables to File
						Properties prop = new Properties();
						OutputStream output = null;

						try {

							output = new FileOutputStream("metronome.properties");

							// set the properties value
							prop.setProperty("noteForMainBeat", Integer.toString(noteForMainBeat));
							prop.setProperty("noteForSubBeat", Integer.toString(noteForSubBeat));
							prop.setProperty("majorVelocity", Integer.toString(majorVelocity));
							prop.setProperty("minorVelocity", Integer.toString(minorVelocity));
							prop.setProperty("tempo", Integer.toString(bpmInt));
							prop.setProperty("subBeatX", Integer.toString(Metronome.this.subBeatX));
							prop.setProperty("numSubBeats", Integer.toString(Metronome.this.numSubBeats));
							prop.setProperty("msPerBPM", Integer.toString(msPbpm));

							// save properties to project root folder
							prop.store(output, null);

						} catch (IOException io) {
							io.printStackTrace();
						} finally {
							if (output != null) {
								try {
									output.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

						}
					}
				});
				frame.setMinimumSize(new Dimension(600, 880));
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Help menu item was clicked
	 */
	public void actionPerformed(ActionEvent ev) {
		HelpDialog dialog = new HelpDialog();
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	/**
	 * Java JPanel set-up
	 */
	public class AnimationPane extends JPanel {

		private static final long serialVersionUID = 8113062312141634760L;
		// Load Basic images
		BufferedImage baseBack = Metronome.this.loadImage("base-back");
		BufferedImage baseLower = Metronome.this.loadImage("base-lower");
		BufferedImage baseUpper = Metronome.this.loadImage("base-upper");
		BufferedImage wand = Metronome.this.loadImage("wand");
		// Load Button images
		ImageIcon upButtonIcon = createImageIcon("images/up.gif");
		ImageIcon downButtonIcon = createImageIcon("images/down.gif");
		protected JButton up = new JButton();
		protected JButton down = new JButton();
		// Load Slider images
		BufferedImage volumePlate = Metronome.this.loadImage("volumePlate");
		BufferedImage volumeKnobUpper = Metronome.this.loadImage("volumeKnobUpper");
		BufferedImage volumeKnobLower = Metronome.this.loadImage("volumeKnobLower");
		// Sub-beat switch
		BufferedImage subBeatPlate = Metronome.this.loadImage("subBeatPlate");
		BufferedImage subBeatKnob = Metronome.this.loadImage("subBeatKnob");

		// Here we go
		private BufferedImage weight = Metronome.this.loadImage("weight");
		private int xPos = 258;
		private int direction = 1;

		int angle = 360;
		int MAX_ANGLE = 378;
		int MIN_ANGLE = 342;
		int angleIncrement = 3;

		// Here is the ActionListener and Animation definition
		public AnimationPane() {

			InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
			ActionMap am = this.getActionMap();

			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "pressed.up");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "pressed.down");

			am.put("pressed.up", new MoveAction("up"));
			am.put("pressed.down", new MoveAction("down"));

			Metronome.this.timer = new Timer(Metronome.this.timerDelay, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					// Animation
					switch (Metronome.this.numSubBeats) {
					case 0:
						AnimationPane.this.angle = AnimationPane.this.angle
								+ (AnimationPane.this.angleIncrement * AnimationPane.this.direction);
						if (AnimationPane.this.angle >= AnimationPane.this.MAX_ANGLE) {
							AnimationPane.this.direction = -1;

							// Midi
							if (AnimationPane.this.angle == 378) {
								Metronome.this.channel.noteOn(noteForMainBeat, majorVelocity);
							}

						} else if (AnimationPane.this.angle <= AnimationPane.this.MIN_ANGLE) {
							AnimationPane.this.direction = 1;

							if (AnimationPane.this.angle == 342) {
								Metronome.this.channel.noteOn(noteForMainBeat, majorVelocity);
							}
						}

						AnimationPane.this.repaint();
						break;
					case 1:
						AnimationPane.this.angle = AnimationPane.this.angle
								+ (AnimationPane.this.angleIncrement * AnimationPane.this.direction);
						if (AnimationPane.this.angle >= AnimationPane.this.MAX_ANGLE) {
							AnimationPane.this.direction = -1;

							// Midi
							if (AnimationPane.this.angle == 378) {
								Metronome.this.channel.noteOn(noteForMainBeat, majorVelocity);
							}

						} else if (AnimationPane.this.angle <= AnimationPane.this.MIN_ANGLE) {
							AnimationPane.this.direction = 1;

							if (AnimationPane.this.angle == 342) {
								Metronome.this.channel.noteOn(noteForMainBeat, majorVelocity);
							}
						} else if (AnimationPane.this.angle == 360) {
							Metronome.this.channel.noteOn(noteForSubBeat, minorVelocity);
						}

						AnimationPane.this.repaint();
						break;
					case 2:
						AnimationPane.this.angle = AnimationPane.this.angle
								+ (AnimationPane.this.angleIncrement * AnimationPane.this.direction);
						if (AnimationPane.this.angle >= AnimationPane.this.MAX_ANGLE) {
							AnimationPane.this.direction = -1;

							// Midi
							if (AnimationPane.this.angle == 378) {
								Metronome.this.channel.noteOn(noteForMainBeat, majorVelocity);
							}

						} else if (AnimationPane.this.angle <= AnimationPane.this.MIN_ANGLE) {
							AnimationPane.this.direction = 1;

							if (AnimationPane.this.angle == 342) {
								Metronome.this.channel.noteOn(noteForMainBeat, majorVelocity);
							}
						} else if (AnimationPane.this.angle == 354) {
							Metronome.this.channel.noteOn(noteForSubBeat, minorVelocity);
						} else if (AnimationPane.this.angle == 366) {
							Metronome.this.channel.noteOn(noteForSubBeat, minorVelocity);
						}

						AnimationPane.this.repaint();
						break;
					}
				}

			});
			Metronome.this.timer.setRepeats(true);
			Metronome.this.timer.setCoalesce(true);

			// add buttons to fine tune tempo
			this.up = new JButton(null, this.upButtonIcon);
			this.up.setMnemonic(KeyEvent.VK_UP);
			this.up.setPreferredSize(new Dimension(20, 26));

			this.down = new JButton(null, this.downButtonIcon);
			this.down.setMnemonic(KeyEvent.VK_DOWN);
			this.down.setPreferredSize(new Dimension(20, 26));

			this.up.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Execute when UP button is pressed
					if (!Metronome.this.hasStarted) {
						bpmInt = bpmInt + 1;
						if (bpmInt > 224) {
							bpmInt = 224;
						}
						Metronome.this.wy = ((bpmInt - 24) * 2) + 150;
						Metronome.this.timerDelay = msPbpm / bpmInt;
						AnimationPane.this.down.requestFocus(false);
						AnimationPane.this.repaint();
					}
				}
			});

			this.down.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Execute when DOWN button is pressed
					if (!Metronome.this.hasStarted) {
						bpmInt = bpmInt - 1;
						if (bpmInt < 24) {
							bpmInt = 24;
						}
						Metronome.this.wy = ((bpmInt - 24) * 2) + 150;
						Metronome.this.timerDelay = msPbpm / bpmInt;
						AnimationPane.this.repaint();
					}
				}
			});
		}

		// Graphics definition
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			this.addMouseMotionListener(new ChangeTempo());
			this.addMouseListener(new GetCurPos());

			Graphics2D g2d = (Graphics2D) g;

			AffineTransform original = g2d.getTransform(); // save for original animation state

			// Static images
			g2d.drawImage(this.baseBack, 0, 0, null);
			g2d.drawImage(this.baseUpper, 0, 0, null);

			// The BPM box
			int thickness = 4;
			Color myColor = Color.decode("#905838"); // ("#C1E0FF");
			g2d.setColor(myColor);
			g2d.fill3DRect(270, 70, 60, 26, false); // (X, Y, length, width)
			for (int i = 1; i <= thickness; i++) {
				g2d.draw3DRect(270 - i, 70 - i, (60 + (2 * i)) - 1, (26 + (2 * i)) - 1, true);
			}
			// Display BPM
			Font courierBold10 = new Font("Droid", Font.BOLD, 20);
			Color textColor = Color.decode("#ECEFFF");// ("#62F35B");
			g2d.setColor(textColor);
			g2d.setFont(courierBold10);
			String textBpm = Integer.toString(((Metronome.this.wy - 150) / 2) + 24);
			g2d.drawString(textBpm, 280, 90);

			if (!Metronome.this.hasStarted) {
				g2d.setTransform(original);
				g2d.drawImage(this.wand, 290, 116, this);
				g2d.drawImage(this.weight, this.xPos, Metronome.this.wy, this);
				if (Metronome.this.stopAnimation) { // spacebar was pressed to stop the animation
					Metronome.this.timer.stop();
				}
			} else {

				// Animated images
				g2d.rotate(Math.toRadians(Math.abs(this.angle)), 298, 700); // 298, 700 is X, Y coordinate for center of
																			// rotation
				g2d.drawImage(this.wand, 290, 116, null);
				g2d.drawImage(this.weight, this.xPos, Metronome.this.wy, this);
			}
			// Now draw the base with volume slider etc...
			g2d.setTransform(original);
			g2d.drawImage(this.baseLower, 0, 0, null);
			g2d.drawImage(this.volumePlate, 100, 670, null); // was 200
			g2d.drawImage(this.volumeKnobUpper, Metronome.this.volUprX, 680, null);
			g2d.drawImage(this.volumeKnobLower, Metronome.this.volLwrX, 702, null);
			g2d.drawImage(this.subBeatPlate, 360, 685, null);
			g2d.drawImage(this.subBeatKnob, Metronome.this.subBeatX, 700, null);

			// Add the buttons
			this.up.setForeground(Color.decode("#643D27"));
			this.up.setBackground(Color.decode("#CD7D50"));
			this.up.setLocation(246, 70);
			this.add(this.up);

			this.down.setForeground(Color.decode("#643D27"));
			this.down.setBackground(Color.decode("#CD7D50"));
			this.down.setLocation(334, 70);
			this.add(this.down);
		}

		public class GetCurPos extends MouseAdapter {
			@Override
			public void mousePressed(MouseEvent e) {
				Metronome.this.X = e.getX();
				Metronome.this.Y = e.getY();

				// What object was hit?
				Rectangle weightRect = new Rectangle(290, Metronome.this.wy, weight_width, weight_height);
				Rectangle volUprRect = new Rectangle(Metronome.this.volUprX, 680, sliderKnob_width, sliderKnob_height);
				Rectangle volLwrRect = new Rectangle(Metronome.this.volLwrX, 702, sliderKnob_width, sliderKnob_height);
				Rectangle subBeatRect = new Rectangle(Metronome.this.subBeatX, 700, sliderKnob_width,
						sliderKnob_height);

				// Weight
				if (weightRect.contains(Metronome.this.X, Metronome.this.Y)) {
					Metronome.this.weightWasHit = true;
					Metronome.this.deltaY = Metronome.this.Y - Metronome.this.wy;
				}
				// Main beat volume
				else if (volUprRect.contains(Metronome.this.X, Metronome.this.Y)) {
					Metronome.this.volUprWasHit = true;
					Metronome.this.deltaX = Metronome.this.X - Metronome.this.volUprX;
				}
				// Sub-Beat Volume
				else if (volLwrRect.contains(Metronome.this.X, Metronome.this.Y)) {
					Metronome.this.volLwrWasHit = true;
					Metronome.this.deltaX = Metronome.this.X - Metronome.this.volLwrX;
				}
				// Sub-Beat switch
				else if (subBeatRect.contains(Metronome.this.X, Metronome.this.Y)) {
					Metronome.this.subBeatWasHit = true;
					Metronome.this.deltaX = Metronome.this.X - Metronome.this.subBeatX;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Metronome.this.volUprWasHit = false;
				Metronome.this.volLwrWasHit = false;
				Metronome.this.subBeatWasHit = false;
				Metronome.this.weightWasHit = false;
			}
		}

		public class ChangeTempo extends MouseAdapter {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (Metronome.this.weightWasHit) {
					if (!Metronome.this.hasStarted) {
						// if (e.getY() < 570) {
						Metronome.this.wy = e.getY() - Metronome.this.deltaY;
						if (Metronome.this.wy > MAX_WY) {
							Metronome.this.wy = MAX_WY;
						}
						if (Metronome.this.wy < 150) {
							Metronome.this.wy = 150;
						}

						bpmInt = ((Metronome.this.wy - 150) / 2) + 24;
						Metronome.this.timerDelay = msPbpm / bpmInt;
						AnimationPane.this.repaint();
					}
				} else if (Metronome.this.volUprWasHit) {
					Metronome.this.volUprX = e.getX() - Metronome.this.deltaX;
					if (Metronome.this.volUprX > 250) {
						Metronome.this.volUprX = 250;
					}
					if (Metronome.this.volUprX <= 110) {
						Metronome.this.volUprX = 110;
					}

					majorVelocity = ((Metronome.this.volUprX - 110) * 127) / 140;
					AnimationPane.this.repaint();
					// }
				} else if (Metronome.this.volLwrWasHit) {
					Metronome.this.volLwrX = e.getX() - Metronome.this.deltaX;
					if (Metronome.this.volLwrX > 250) {
						Metronome.this.volLwrX = 250;
					}
					if (Metronome.this.volLwrX <= 110) {
						Metronome.this.volLwrX = 110;
					}
					minorVelocity = ((Metronome.this.volLwrX - 110) * 127) / 140;
					AnimationPane.this.repaint();
				} else if (Metronome.this.subBeatWasHit) {
					Metronome.this.subBeatX = e.getX();
					if (Metronome.this.subBeatX < 420) {
						Metronome.this.subBeatX = 390;
						Metronome.this.numSubBeats = 0;
					}
					// number of sub-beats = 1
					if ((Metronome.this.subBeatX > 420) && (Metronome.this.subBeatX < 445)) {
						Metronome.this.subBeatX = 412;
						Metronome.this.numSubBeats = 1;
					}
					// number of sub-beats = 2
					if (Metronome.this.subBeatX > 445) {
						Metronome.this.subBeatX = 432;
						Metronome.this.numSubBeats = 2;
					}
					AnimationPane.this.repaint();
				}

			}

		}

		public class MoveAction extends AbstractAction {

			private static final long serialVersionUID = 3308815138155077750L;
			private int direction;

			public MoveAction(String whichWay) {

				if (whichWay == "up") {
					this.direction = 1;
				} else if (whichWay == "down") {
					this.direction = -1;
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Metronome.this.hasStarted) {
					bpmInt = bpmInt + this.direction;
					if (bpmInt > 224) {
						bpmInt = 224;
					} else if (bpmInt < 24) {
						bpmInt = 24;
					}
					Metronome.this.wy = ((bpmInt - 24) * 2) + 150;
					Metronome.this.timerDelay = msPbpm / bpmInt;
					AnimationPane.this.repaint();
				}
			}
		}
	}

	private BufferedImage loadImage(String name) {
		// BufferedImage bi;
		String imgFileName = "images/Metronome-" + name + ".png";
		URL url = Metronome.class.getResource(imgFileName);
		BufferedImage img = null;
		try {
			img = ImageIO.read(url);
		} catch (Exception e) {
		}

		return img;
	}

	/**
	 * Returns an ImageIcon, or null if the path was invalid.
	 */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Metronome.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Start/Stop animation
	 **/
	class MyKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent evt) {
			if (evt.getKeyChar() == KeyEvent.VK_SPACE) {

				if (!Metronome.this.hasStarted) {
					bpmInt = ((Metronome.this.wy - 150) / 2) + 24;
					Metronome.this.timerDelay = msPbpm / bpmInt;
					Metronome.this.timer.setDelay(Metronome.this.timerDelay);
					Metronome.this.timer.start();
				} else {
					Metronome.this.stopAnimation = true;
				}
				Metronome.this.hasStarted = !Metronome.this.hasStarted;
			}
			// Sub-Beat Switch
			else if ((evt.getKeyChar() == KeyEvent.VK_0) && Metronome.this.hasStarted) {
				Metronome.this.subBeatX = 390;
				Metronome.this.numSubBeats = 0;
			} else if ((evt.getKeyChar() == KeyEvent.VK_1) && Metronome.this.hasStarted) {
				Metronome.this.subBeatX = 412;
				Metronome.this.numSubBeats = 1;
			} else if ((evt.getKeyChar() == KeyEvent.VK_2) && Metronome.this.hasStarted) {
				Metronome.this.subBeatX = 432;
				Metronome.this.numSubBeats = 2;
			}
		}
	}

	// Help dialog box
	private class HelpDialog extends JDialog implements ActionListener {

		private static final long serialVersionUID = -273240361506739164L;

		private HelpDialog() {

			super(Metronome.this.frame, "Help", true);
			// Stop the Metronome
			if (Metronome.this.hasStarted) {
				Metronome.this.timer.stop();
				(Metronome.this.hasStarted) = false;
			}

			JPanel panel = new JPanel(new FlowLayout());
			this.getContentPane().add(panel);
			JLabel helpText = new JLabel();
			String helpContent = "<html>" + "<head>" + "<meta charset='UTF-8'>" + "<title>Insert title here</title>"
					+ "</head>" + "<body>"
					+ "<div align='center' style='font-size:1.2em'><b>How to use Metronome</b></div>" + "<ul>"
					+ "    <li>Drag the weight to set tempo.</li><br/>"
					+ "    <li>Use up/down arrows on either side of the BPM display<br/>"
					+ "    or use up/down arrow keys on your keyboard to fine adjust tempo.</li>"
					+ "    <ul style='margin-top: -0.5em'>" + "        <li>Up increases tempo - weight moves down</li>"
					+ "        <li>Down decreases tempo - weight moves up</li>" + "    </ul>"
					+ "    <li>Press spacebar to Start/Stop the metronome.</li><br/>" + "    <li>Volume Sliders:</li>"
					+ "    <ul style='margin-top: -0.5em'>"
					+ "        <li>Top slider controls the major beat volume.</li>"
					+ "        <li>Bottom slider controls the sub-beat volume.</li>" + "    </ul>"
					+ "    <li>Select 0, 1 or 2 sub-beats with the Sub-Beat switch.<br/>"
					+ "        Or while the metronome is running,<br/>"
					+ "        use keypad number 0, 1, 2 to change number of sub-beats.</li><br/>"
					+ "    <li>Major beat and sub-beat sounds can be changed in the 'Settings' menu item.</li>"
					+ "</ul>" + "<ul>" + "<li>Timer Delay (Optional adjustment in Settings panel.)</li></br>"
					+ "Tempo accuracy may vary by 1/4 beat per minute at 60BPM<br/>depending on your computer configuration.<br/>"
					+ "To calibrate:<br/>" + "<ul style='margin-top: -0.5em'>"
					+ "<li>Set tempo to 60BPM and use a stop watch<br/>to count the number of beats that you hear in one minute.</li>"
					+ "<li>If you count more than 60 beats,<br/>then the metronome is running to fast.<br/>"
					+ "Increase the delay by moving the slider to the right<br/>or use right arrow key on your keyboard.</li>"
					+ "<li>If you count less than 60 beats,<br/>the metronome is running slow.<br/>"
					+ "Decrease the delay by moving the slider to the left<br/>or use left arrow key on your keyboard.</li>"
					+ "</ul>" + "</ul>" + "</body>" + "</html>";
			helpText.setText(helpContent);
			panel.add(helpText);

			this.setBounds(300, 200, 640, 620);

			panel.setLocation(20, 50);
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			this.setVisible(false);
		}
	}

	/**
	 * The Settings dialog box
	 **/
	private class SettingsDialog extends JDialog implements ActionListener, ChangeListener {

		private static final long serialVersionUID = 147052588095926828L;

		private SettingsDialog() {
			// Stop Metronome
			if (Metronome.this.hasStarted) {
				Metronome.this.timer.stop();
				Metronome.this.hasStarted = false;
			}

			// Add main panel for Settings
			JPanel contentPane = new JPanel();
			contentPane.setLayout(new FlowLayout());
			contentPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

			// panel for sound
			JPanel soundsContainer = new JPanel();
			soundsContainer.setLayout(new GridLayout(1, 2, 10, 10));

			// LEFT side of panel
			JPanel buttonPanel1 = new JPanel();
			buttonPanel1.setLayout(new GridLayout(16, 1, 10, 10));
			JLabel Left = new JLabel("Major Beat", JLabel.CENTER);
			Left.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			buttonPanel1.add(Left);

			// SELECT SOUND PANEL 1
			Metronome.this.soundLabel1 = new JLabel(this.getSoundName(noteForMainBeat), JLabel.CENTER);
			buttonPanel1.add(Metronome.this.soundLabel1);

			// Sound buttons
			JButton hc1 = new JButton("Hand Clap");
			hc1.addActionListener(new soundButtonPanel1("Hand Clap", HAND_CLAP));
			buttonPanel1.add(hc1);

			JButton rs1 = new JButton("Side Stick/Rimshot");
			rs1.addActionListener(new soundButtonPanel1("Side Stick/Rimshot", RIM_SHOT));
			buttonPanel1.add(rs1);

			JButton cb1 = new JButton("Cowbell");
			cb1.addActionListener(new soundButtonPanel1("CowBell", COWBELL));
			buttonPanel1.add(cb1);

			JButton hb1 = new JButton("High Bongo");
			hb1.addActionListener(new soundButtonPanel1("High Bongo", HIGH_BONGO));
			buttonPanel1.add(hb1);

			JButton lb1 = new JButton("Low Bongo");
			lb1.addActionListener(new soundButtonPanel1("Low Bongo", LOW_BONGO));
			buttonPanel1.add(lb1);

			JButton ht1 = new JButton("High Timbale");
			ht1.addActionListener(new soundButtonPanel1("High Timbale", HIGH_TIMBALE));
			buttonPanel1.add(ht1);

			JButton lt1 = new JButton("Low Timbale");
			lt1.addActionListener(new soundButtonPanel1("Low Timbale", LOW_TIMBALE));
			buttonPanel1.add(lt1);

			JButton ha1 = new JButton("High Agogo");
			ha1.addActionListener(new soundButtonPanel1("High Agogo", HIGH_AGOGO));
			buttonPanel1.add(ha1);

			JButton la1 = new JButton("Low Agogo");
			la1.addActionListener(new soundButtonPanel1("Low Agogo", LOW_AGOGO));
			buttonPanel1.add(la1);

			JButton c1 = new JButton("Claves");
			c1.addActionListener(new soundButtonPanel1("Claves", CLAVES));
			buttonPanel1.add(c1);

			JButton hwb1 = new JButton("High Wood Block");
			hwb1.addActionListener(new soundButtonPanel1("High Wood Block", HIGH_WOOD_BLOCK));
			buttonPanel1.add(hwb1);

			JButton lwb1 = new JButton("Low Wood Block");
			lwb1.addActionListener(new soundButtonPanel1("Low Wood Block", LOW_WOOD_BLOCK));
			buttonPanel1.add(lwb1);

			JButton mc1 = new JButton("Mute Cuica");
			mc1.addActionListener(new soundButtonPanel1("Mute Cuica", CUICA));
			buttonPanel1.add(mc1);

			JButton mt1 = new JButton("Mute Triangle");
			mt1.addActionListener(new soundButtonPanel1("Mute Triangle", MUTE_TRIANGLE));
			buttonPanel1.add(mt1);

			soundsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			soundsContainer.add(buttonPanel1, BorderLayout.NORTH);

			// RIGHT side of panel
			JPanel buttonPanel2 = new JPanel();
			buttonPanel2.setLayout(new GridLayout(16, 1, 10, 10));
			JLabel Right = new JLabel("Sub-Beat", JLabel.CENTER);
			Right.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			buttonPanel2.add(Right);

			// Selected sound
			Metronome.this.soundLabel2 = new JLabel(this.getSoundName(noteForSubBeat), JLabel.CENTER);
			buttonPanel2.add(Metronome.this.soundLabel2);

			// Sound buttons
			JButton hc2 = new JButton("Hand Clap");
			hc2.addActionListener(new soundButtonPanel2("Hand Clap", HAND_CLAP));
			buttonPanel2.add(hc2);

			JButton rs2 = new JButton("Side Stick/Rimshot");
			rs2.addActionListener(new soundButtonPanel2("Side Stick/Rimshot", RIM_SHOT));
			buttonPanel2.add(rs2);

			JButton cb2 = new JButton("Cowbell");
			cb2.addActionListener(new soundButtonPanel2("Cowbell", COWBELL));
			buttonPanel2.add(cb2);

			JButton hb2 = new JButton("High Bongo");
			hb2.addActionListener(new soundButtonPanel2("High Bongo", HIGH_BONGO));
			buttonPanel2.add(hb2);

			JButton lb2 = new JButton("Low Bongo");
			lb2.addActionListener(new soundButtonPanel2("Low Bongo", LOW_BONGO));
			buttonPanel2.add(lb2);

			JButton ht2 = new JButton("High Timbale");
			ht2.addActionListener(new soundButtonPanel2("High Timbale", HIGH_TIMBALE));
			buttonPanel2.add(ht2);

			JButton lt2 = new JButton("Low Timbale");
			lt2.addActionListener(new soundButtonPanel2("Low Timbale", LOW_TIMBALE));
			buttonPanel2.add(lt2);

			JButton ha2 = new JButton("High Agogo");
			ha2.addActionListener(new soundButtonPanel2("High Agogo", HIGH_AGOGO));
			buttonPanel2.add(ha2);

			JButton la2 = new JButton("Low Agogo");
			la2.addActionListener(new soundButtonPanel2("Low Agogo", LOW_AGOGO));
			buttonPanel2.add(la2);

			JButton c2 = new JButton("Claves");
			c2.addActionListener(new soundButtonPanel2("Claves", CLAVES));
			buttonPanel2.add(c2);

			JButton hwb2 = new JButton("High Wood Block");
			hwb2.addActionListener(new soundButtonPanel2("High Wood Block", HIGH_WOOD_BLOCK));
			buttonPanel2.add(hwb2);

			JButton lwb2 = new JButton("Low Wood Block");
			lwb2.addActionListener(new soundButtonPanel2("Low Wood Block", LOW_WOOD_BLOCK));
			buttonPanel2.add(lwb2);

			JButton mc2 = new JButton("Mute Cuica");
			mc2.addActionListener(new soundButtonPanel2("Mute Cuica", CUICA));
			buttonPanel2.add(mc2);

			JButton mt2 = new JButton("Mute Triangle");
			mt2.addActionListener(new soundButtonPanel2("Mute Triangle", MUTE_TRIANGLE));
			buttonPanel2.add(mt2);

			soundsContainer.add(buttonPanel2, BorderLayout.NORTH);

			contentPane.add(soundsContainer);

			// Bottom Panel for Fine Timing Adjustment
			JPanel timingPanel = new JPanel(new BorderLayout());
			timingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			timingPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			timingPanel.setLayout(new GridLayout(3, 1));

			final JSlider timingSlider = new JSlider(JSlider.HORIZONTAL, 4500, 5500, msPbpm);
			timingSlider.addChangeListener(this);

			timingSlider.setMinorTickSpacing(50);
			timingSlider.setMajorTickSpacing(500);
			timingSlider.setPaintTicks(true);
			timingSlider.setPaintLabels(true);

			JPanel timerDelayPanel = new JPanel();
			Metronome.this.timerDelayLabel.setText("Timer delay is " + Integer.toString(msPbpm) + "ms");
			timerDelayPanel.add(Metronome.this.timerDelayLabel);
			timingPanel.add(timerDelayPanel);

			JButton reset = new JButton("Reset to Default");
			reset.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					msPbpm = defaultMsPbpm;
					Metronome.this.timerDelayLabel.setText("Timer delay is " + Integer.toString(msPbpm) + "ms");
					timingSlider.setValue(msPbpm);
				}
			});

			timingPanel.add(timingSlider);
			timingPanel.add(reset);

			contentPane.add(timingPanel);

			this.setContentPane(contentPane);

			this.setSize(480, 800);
			this.setLocationByPlatform(true);
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider timingJSlider = (JSlider) e.getSource();
			if (!timingJSlider.getValueIsAdjusting()) {
				msPbpm = timingJSlider.getValue();
				Metronome.this.timerDelayLabel.setText("Timer delay is " + Integer.toString(msPbpm) + "ms");
			}
		}

		private String getSoundName(int noteNumber) {
			String retString = "not found";
			switch (noteNumber) {
			case 39:
				retString = "Hand Clap";
				break;
			case 37:
				retString = "Sidestick/Rimshot";
				break;
			case 56:
				retString = "Cowbell";
				break;
			case 60:
				retString = "High Bongo";
				break;
			case 61:
				retString = "Low Bongo";
				break;
			case 65:
				retString = "High Timbale";
				break;
			case 66:
				retString = "Low Timbale";
				break;
			case 67:
				retString = "High Agogo";
				break;
			case 68:
				retString = "Low Agogo";
				break;
			case 75:
				retString = "Claves";
				break;
			case 76:
				retString = "High Wood Block";
				break;
			case 77:
				retString = "Low Wood Block";
				break;
			case 78:
				retString = "Mute Cuica";
				break;
			case 81:
				retString = "Mute Triangle";
				break;
			}
			return retString;
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			this.setVisible(false);
		}
	}

	class soundButtonPanel1 implements ActionListener {

		private String noteDescription = null;
		private int noteNumber = 0;

		public soundButtonPanel1(String desc, int note) {
			this.noteDescription = desc;
			this.noteNumber = note;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Metronome.this.soundLabel1.setText(this.noteDescription);
			Metronome.this.channel.noteOn(this.noteNumber, majorVelocity);
			noteForMainBeat = this.noteNumber;
		}
	}

	class soundButtonPanel2 implements ActionListener {

		private String noteDescription = null;
		private int noteNumber = 0;

		public soundButtonPanel2(String desc, int note) {
			this.noteDescription = desc;
			this.noteNumber = note;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Metronome.this.soundLabel2.setText(this.noteDescription);
			Metronome.this.channel.noteOn(this.noteNumber, minorVelocity);
			noteForSubBeat = this.noteNumber;
		}
	}
}
