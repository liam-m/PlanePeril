package scn;

import pp.Main;
import lib.ButtonText;
import lib.TextBox;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

public class DifficultySelect extends Scene {

	// Position of things to draw in the window
	private final int EASY_BUTTON_X = window.width() / 4;
	private final int EASY_BUTTON_Y = 2 * window.height() / 3;
	private final int EASY_BUTTON_W = 128;
	private final int EASY_BUTTON_H = 16;

	private final int MEDIUM_BUTTON_X = window.width() / 2;
	private final int MEDIUM_BUTTON_Y = EASY_BUTTON_Y;
	private final int MEDIUM_BUTTON_W = EASY_BUTTON_W;
	private final int MEDIUM_BUTTON_H = EASY_BUTTON_H;

	private final int HARD_BUTTON_X = 3 * window.width() / 4;
	private final int HARD_BUTTON_Y = EASY_BUTTON_Y;
	private final int HARD_BUTTON_W = EASY_BUTTON_W;
	private final int HARD_BUTTON_H = EASY_BUTTON_H;

	private ButtonText[] buttons; // List of buttons	
	private TextBox text_box; // Text box to write flavour text about the game setting into
	private static final String place_name = "Moscow";

	private final int scene_to_create; // To allow the difficulty selection to work with multiple potential game scenes, e.g. separate Demo and a Full Game
	
	// Static ints for clarity of reading. Implement more to allow more game scenes
	public final static int CREATE_DEMO = 0;

	/**
	 * Constructor
	 * 
	 * @param main The main containing the scene
	 * @param scene_to_create The scene to create after a difficulty has been selected, e.g. Demo
	 */
	protected DifficultySelect(Main main, int scene_to_create) {
		super(main);
		this.scene_to_create = scene_to_create;
	}

	/**
	 * Initialises scene variables, buttons, text box.
	 */
	@Override
	public void start() {
		buttons = new ButtonText[3];

		ButtonText.Action easy = new ButtonText.Action() {
			@Override
			public void action() {
				switch (scene_to_create) {
				case DifficultySelect.CREATE_DEMO:
					main.setScene(new Demo(main, Demo.DIFFICULTY_EASY));
					break;
				}
			}
		};

		buttons[0] = new ButtonText("Easy", easy, EASY_BUTTON_X, EASY_BUTTON_Y,
				EASY_BUTTON_W, EASY_BUTTON_H);

		ButtonText.Action medium = new ButtonText.Action() {
			@Override
			public void action() {
				switch (scene_to_create) {
				case DifficultySelect.CREATE_DEMO:
					main.setScene(new Demo(main, Demo.DIFFICULTY_MEDIUM));
					break;
				}
			}
		};

		buttons[1] = new lib.ButtonText("Medium", medium, MEDIUM_BUTTON_X,
				MEDIUM_BUTTON_Y, MEDIUM_BUTTON_W, MEDIUM_BUTTON_H);

		ButtonText.Action hard = new ButtonText.Action() {
			@Override
			public void action() {
				switch (scene_to_create) {
				case DifficultySelect.CREATE_DEMO:
					main.setScene(new Demo(main, Demo.DIFFICULTY_HARD));
					break;
				}
			}
		};

		buttons[2] = new ButtonText("Hard", hard, HARD_BUTTON_X, HARD_BUTTON_Y,
				HARD_BUTTON_W, HARD_BUTTON_H);

		text_box = new TextBox(128, 96, window.width() - 256, window.height() - 96, 32);
		text_box.addText("You are a 500 kilogram ferocious Grizzly Bear." + TextBox.DELAY_START + "0.5" + TextBox.DELAY_END
				+ " The Humans are not aware of your hidden identity.");
		text_box.delay(0.5);
		text_box.addText("You have become an air traffic controller at "	+ DifficultySelect.place_name
				+ " international in order to provide for your family during the harsh winters ahead.");
		text_box.delay(0.5);
		text_box.newline();
		text_box.addText("Somehow, miraculously, your true nature has not yet been discovered.");
		text_box.newlines(3);
		text_box.delay(1);
		text_box.addText("Guide planes to their destination successfully and you will be rewarded." + TextBox.DELAY_START
				+ "0.5"	+ TextBox.DELAY_END + " Fail," + TextBox.DELAY_START + "0.5" + TextBox.DELAY_END + " and the humans may " +
				"discover your secret identity and put you in a zoo."	+ TextBox.DELAY_START + "1" + TextBox.DELAY_END + " Or worse.");
	}

	/**
	 * Updates text box
	 */
	@Override
	public void update(double time_difference) {
		text_box.update(time_difference);
	}

	/**
	 * Draws text box, buttons, and prints strings
	 */
	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printCentred("Select the difficulty:", window.width() / 2,	window.height() / 2 + 50, 1, 100);
		graphics.rectangle(false, EASY_BUTTON_X, EASY_BUTTON_Y, EASY_BUTTON_W, EASY_BUTTON_H);
		graphics.rectangle(false, MEDIUM_BUTTON_X, MEDIUM_BUTTON_Y, MEDIUM_BUTTON_W, MEDIUM_BUTTON_H);
		graphics.rectangle(false, HARD_BUTTON_X, HARD_BUTTON_Y, HARD_BUTTON_W, HARD_BUTTON_H);

		for (ButtonText button : buttons) {
			button.draw();
		}

		text_box.draw();
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
	

	@Override
	public void mousePressed(int key, int x, int y) {
	}

	/**
	 * Causes a button to act if mouse released over it
	 */
	@Override
	public void mouseReleased(int key, int x, int y) {
		for (ButtonText button : buttons) {
			if (button.isMouseOver(x, y)) {
				button.act();
			}
		}
	}

	@Override
	public void keyPressed(int key) {
	}

	/**
	 * Quits back to title scene on escape button
	 */
	@Override
	public void keyReleased(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
	}
}