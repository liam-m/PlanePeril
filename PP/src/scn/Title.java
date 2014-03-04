package scn;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import pp.Main;

import lib.ButtonText;
import lib.jog.audio;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.window;

public class Title extends Scene {

	/**
	 * A URL to the Bear Traffic Controller Website Webpage contains explanation of game's controls and goal
	 */
	private final static String HELP_URL = "http://imp1.github.io/bear_traffic_controller/ingamehelp";

	/**
	 * The 'beep' played as the radar makes a sweep
	 */
	private Sound beep_sound;

	/**
	 * Array of buttons, to hold declared buttons in the scene
	 */
	private ButtonText[] buttons;

	/**
	 * Holds the angle to draw the radar sweep at. Also used to play the beep
	 * sound as the radar sweeps the BTC title string Updated regularly during
	 * Title's update()
	 */
	private double radar_sweep_angle;

	/**
	 * Constructor for the Title Scene
	 * 
	 * @param main The main holding the scene
	 */
	public Title(Main main) {
		super(main);
	}

	/**
	 * Initialises anything which needs to be initialised, such as buttons and
	 * sound effects Runs only at start of scene
	 */
	@Override
	public void start() {
		beep_sound = audio.newSoundEffect("sfx" + File.separator + "beep.ogg");
		beep_sound.setVolume(0.2f);

		int i = 0;
		int offset = 96;
		int buttonHeight = 30;

		buttons = new ButtonText[6];
		// Single player Button
		ButtonText.Action single_player = new ButtonText.Action() {
			@Override
			public void action() {
				main.setScene(new DifficultySelect(main,
						DifficultySelect.CREATE_DEMO));
			}
		};

		buttons[i] = new ButtonText("Single Player", single_player, window.height(),
				window.height() / 2 + (offset + buttonHeight * i), window.width() - window.height(), 24, 8, 6);
		i++;
		
		// Multiplayer Button
		ButtonText.Action multiplayer = new ButtonText.Action() {
			@Override
			public void action() {
				// Launch multiplayer
			}
		};

		buttons[i] = new ButtonText("Multiplayer", multiplayer, window.height(),
				window.height() / 2 + (offset + buttonHeight * i), window.width() - window.height(), 24, 8, 6);
		i++;

		// High Scores Button
		ButtonText.Action highScores = new ButtonText.Action() {
			@Override
			public void action() {
				main.setScene(new HighScores(main));
			}
		};

		buttons[i] = new ButtonText("High Scores", highScores,
				window.height(), window.height() / 2 + (offset + buttonHeight * i), window.width()
				- window.height(), 24, 8, 6);
		i++;

		// Credits Button
		ButtonText.Action credits = new ButtonText.Action() {
			@Override
			public void action() {
				main.setScene(new Credits(main));
			}
		};

		buttons[i] = new ButtonText("Credits", credits, window.height(),
				window.height() / 2 + (offset + buttonHeight * i),
				window.width() - window.height(), 24, 8, 6);
		i++;

		// Help Button
		ButtonText.Action help = new ButtonText.Action() {
			@Override
			public void action() {
				try {
					Desktop.getDesktop().browse(new URI(HELP_URL));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		};

		buttons[i] = new ButtonText("Help               (Opens in Browser)", help, window.height(), window.height() / 2
						+ (offset + buttonHeight * i), window.width() - window.height(), 24, 8, 6);
		i++;

		// Exit Button
		ButtonText.Action exit = new ButtonText.Action() {
			@Override
			public void action() {
				main.quit();
			}
		};

		buttons[i] = new ButtonText("Exit", exit, window.height(),
				window.height() / 2 + (offset + buttonHeight * i),
				window.width() - window.height(), 24, 8, 6);

		i++;

		radar_sweep_angle = 0;
	}

	/**
	 * Updates all objects in the title scene Called by Main class
	 * 
	 * @param time_difference The delta time since the last update
	 */
	@Override
	public void update(double time_difference) {
		radar_sweep_angle += time_difference; // Increase the angle of the radar sweep

		// Check the angle of the radar sweep. If approaching the title, play the beep
		double beepTimer = (radar_sweep_angle * 4) + (Math.PI * 4 / 5);
		beepTimer %= (2 * Math.PI);
		if (beepTimer <= 0.1) {
			playSound(beep_sound);
		}
	}
	@Override
	public void mousePressed(int key, int x, int y) {
	}

	/**
	 * Causes a button to act if clicked by any mouse key
	 */
	@Override
	public void mouseReleased(int key, int mx, int my) {
		for (ButtonText b : buttons) {
			if (b.isMouseOver(mx, my)) {
				b.act();
			}
		}
	}
	@Override
	public void keyPressed(int key) {
	}

	@Override
	public void keyReleased(int key) {
	}

	/**
	 * Handles drawing of the scene Calls drawRadar() and drawMenu() to draw
	 * elements of the scene called regularly by Main
	 */
	@Override
	public void draw() {
		drawRadar();
		drawMenu();
	}

	/**
	 * Draws the radar arc and title string
	 */
	private void drawRadar() {
		// Draw circles for radar
		graphics.setColour(Main.GREEN);
		graphics.circle(false, window.height() / 2, window.height() / 2,
				window.height() / 2 - 32, 100);
		graphics.setColour(0, 128, 0, 32);
		graphics.circle(false, window.height() / 2, window.height() / 2,
				window.height() / 3, 100);
		graphics.circle(false, window.height() / 2, window.height() / 2,
				window.height() / 4 - 16, 100);
		graphics.circle(false, window.height() / 2, window.height() / 2,
				window.height() / 9, 100);
		graphics.circle(false, window.height() / 2, window.height() / 2, 2, 100);

		// Draw radar sweep
		double radar_angle = (radar_sweep_angle * 4) % (2 * Math.PI);
		int width = (int) (Math.cos(radar_angle) * (window.height() / 2 - 32));
		int height = (int) (Math.sin(radar_angle) * (window.height() / 2 - 32));
		graphics.line(window.height() / 2, window.height() / 2, window.height() / 2 + width, window.height() / 2 + height);
		
		graphics.setColour(0, 128, 0, 8);
		for (int i = -8; i <= -1; i++) {
			graphics.arc(true, window.height() / 2, window.height() / 2, window.height() / 2 - 32, radar_angle, i * Math.PI / 8);
		}

		// Title
		String title = Main.TITLE;

		// Fades title string's characters over time, characters brighten when the sweep passes over them
		double a = radar_angle + (Math.PI * 4 / 5);

		for (int i = 0; i < title.length(); i++) {
			a -= Math.PI / 32;
			double opacity = a %= (2 * Math.PI);
			opacity *= 256 / (2 * Math.PI);
			opacity = 256 - opacity;
			opacity %= 256;
			graphics.setColour(0, 128, 0, opacity);
			graphics.print(title.substring(i, i + 1), 35 * 7.8 + i * 17, 325, 2.5);
		}
	}

	/**
	 * Draws menu boxes, boxes around buttons, and so on
	 */
	private void drawMenu() {
		// Draw Extras e.g. Date, Time, Credits
		graphics.setColour(Main.GREEN);
		graphics.line(window.height(), 16, window.height(), window.height() - 16);
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
		java.text.DateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
		java.util.Date date = new java.util.Date();
		graphics.print(dateFormat.format(date), window.height() + 8, 20);
		graphics.print(timeFormat.format(date), window.height() + 8, 36);
		graphics.line(window.height(), 48, window.width() - 16, 48);
		graphics.print("Created by:", window.height() + 8, 56);
		graphics.print("TEAM FLR", window.height() + 8, 72);

		graphics.print("Fixed and Extended by:", window.height() + 8, 88);
		graphics.print("TEAM INI", window.height() + 8, 104);

		// Draw Buttons
		for (ButtonText b : buttons)
			b.draw();
		
		// Draw lines between buttons
		graphics.setColour(Main.GREEN);
		for (int i = 90; i <= 240; i += 30) {
			graphics.line(window.height(), window.height() / 2 + i, window.width() - 16, window.height() / 2 + i);			
		}
	}

	/**
	 * cleanly exits the title scene
	 */
	@Override
	public void close() {
	}

	/**
	 * Plays a requested sound
	 */
	@Override
	public void playSound(Sound sound) {
		sound.stop();
		sound.play();
	}
}