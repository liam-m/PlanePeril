package scn;

import lib.TextBox;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import btc.Main;

public class DifficultySelect extends Scene {
	
	private final int EASY_BUTTON_X = window.width()/4;
	private final int EASY_BUTTON_Y = 2*window.height()/3;
	private final int EASY_BUTTON_W = 128;
	private final int EASY_BUTTON_H = 16;
	
	private final int MEDIUM_BUTTON_X = window.width()/2;
	private final int MEDIUM_BUTTON_Y = EASY_BUTTON_Y;
	private final int MEDIUM_BUTTON_W = EASY_BUTTON_W;
	private final int MEDIUM_BUTTON_H = EASY_BUTTON_H;
	
	private final int HARD_BUTTON_X = 3*window.width()/4;
	private final int HARD_BUTTON_Y = EASY_BUTTON_Y;
	private final int HARD_BUTTON_W = EASY_BUTTON_W;
	private final int HARD_BUTTON_H = EASY_BUTTON_H;
	
	private lib.ButtonText[] buttons;
	private lib.TextBox textBox;
	private static final String placeName = "Moscow";

	protected DifficultySelect(Main main) {
		super(main);
	}

	@Override
	public void mousePressed(int key, int x, int y) {}

	@Override
	public void mouseReleased(int key, int x, int y) {
		for (lib.ButtonText b : buttons) {
			if (b.isMouseOver(x, y)) {
				b.act();
			}
		}
	}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
	}

	@Override
	public void start() {
		buttons = new lib.ButtonText[3];
		lib.ButtonText.Action easy = new lib.ButtonText.Action() {
			@Override
			public void action() {
				main.setScene(new Demo(main, Demo.DIFFICULTY_EASY));
			}
		};
		buttons[0] = new lib.ButtonText("Easy", easy, EASY_BUTTON_X, EASY_BUTTON_Y, EASY_BUTTON_W, EASY_BUTTON_H);
		
		lib.ButtonText.Action medium = new lib.ButtonText.Action() {
			@Override
			public void action() {
				main.setScene(new Demo(main, Demo.DIFFICULTY_MEDIUM));
			}
		};
		buttons[1] = new lib.ButtonText("Medium", medium, MEDIUM_BUTTON_X, MEDIUM_BUTTON_Y, MEDIUM_BUTTON_W, MEDIUM_BUTTON_H);
		
		lib.ButtonText.Action hard = new lib.ButtonText.Action() {
			@Override
			public void action() {
				main.setScene(new Demo(main, Demo.DIFFICULTY_HARD));
			}
		};
		buttons[2] = new lib.ButtonText("Hard", hard, HARD_BUTTON_X, HARD_BUTTON_Y, HARD_BUTTON_W, HARD_BUTTON_H);
		
		textBox = new lib.TextBox(128, 96, window.width() - 256, window.height() - 96, 32);
		textBox.addText("You are a 500 kilogram ferocious Grizzly Bear." + TextBox.DELAY_START + "0.5" + TextBox.DELAY_END + " The Humans are not aware of your hidden identity.");
		textBox.delay(0.5);
		textBox.addText("You have become an air traffic controller at " + DifficultySelect.placeName + " international in order to provide for your family during the harsh winters ahead.");
		textBox.delay(0.5);
		textBox.newline();
		textBox.addText("Somehow, miraculously, your true nature has not yet been discovered.");
		textBox.newline();
		textBox.newline();
		textBox.newline();
		textBox.delay(1);
		textBox.addText("Guide planes to their destination successfully and you will be rewarded." + TextBox.DELAY_START + "0.5" + TextBox.DELAY_END + 
						" Fail," + TextBox.DELAY_START + "0.5" + TextBox.DELAY_END + " and the humans may discover your secret identity and put you in a zoo." + 
						TextBox.DELAY_START + "1" + TextBox.DELAY_END + " Or worse.");
	}

	@Override
	public void update(double dt) {
		textBox.update(dt);
	}

	@Override
	public void draw() {
		graphics.setColour(0,128,0);
		graphics.printCentred("Select the difficulty:", window.width()/2, window.height()/2 + 50, 1, 100);
		graphics.rectangle(false, EASY_BUTTON_X, EASY_BUTTON_Y, EASY_BUTTON_W, EASY_BUTTON_H);
		graphics.rectangle(false, MEDIUM_BUTTON_X, MEDIUM_BUTTON_Y, MEDIUM_BUTTON_W, MEDIUM_BUTTON_H);
		graphics.rectangle(false, HARD_BUTTON_X, HARD_BUTTON_Y, HARD_BUTTON_W, HARD_BUTTON_H);
		for (lib.ButtonText b : buttons) {
			b.draw();
		}
		textBox.draw();
	}

	@Override
	public void close() {}

	@Override
	public void playSound(Sound sound) {
		// TODO Auto-generated method stub
		
	}

}
