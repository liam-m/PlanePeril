package scn;

import lib.TextBox;
import lib.jog.graphics;
import lib.jog.window;
import btc.Main;

public class DifficultySelect extends Scene {
	
	private lib.ButtonText[] _buttons;
	private lib.TextBox textBox;
	private static final String placeName = "Moscow";

	protected DifficultySelect(Main main) {
		super(main);
		_buttons = new lib.ButtonText[3];
	}

	@Override
	public void mousePressed(int key, int x, int y) {}

	@Override
	public void mouseReleased(int key, int x, int y) {
		for (lib.ButtonText b : _buttons) {
			if (b.isMouseOver(x, y)) {
				b.act();
			}
		}
	}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void start() {
		lib.ButtonText.Action easy = new lib.ButtonText.Action() {
			@Override
			public void action() {
				_main.setScene(new Demo(_main, Demo.difficultyEasy));
			}
		};
		_buttons[0] = new lib.ButtonText("Easy", easy, window.width()/4, 2*window.height()/3, window.width() - window.height(), 16, 8, 6);
		
		lib.ButtonText.Action medium = new lib.ButtonText.Action() {
			@Override
			public void action() {
				_main.setScene(new Demo(_main, Demo.difficultyMedium));
			}
		};
		_buttons[1] = new lib.ButtonText("Medium", medium, window.width()/2, 2*window.height()/3, window.width() - window.height(), 16, 8, 6);
		
		lib.ButtonText.Action hard = new lib.ButtonText.Action() {
			@Override
			public void action() {
				_main.setScene(new Demo(_main, Demo.difficultyHard));
			}
		};
		_buttons[2] = new lib.ButtonText("Hard", hard, 3*window.width()/4, 2*window.height()/3,	window.width() - window.height(), 8, 8, 6);
		
		textBox = new lib.TextBox(128, 96, window.width() - 256, window.height() - 96, 32);
		textBox.addText("You are a 500 kilogram ferocious Grizzly Bear." + TextBox.DELAY_START + "0.5" + TextBox.DELAY_END + " The Humans are not aware of your hidden identity.");
		textBox.delay(0.5);
		textBox.addText("You have become an air traffic controller at " + DifficultySelect.placeName + " international in order to provide for your family during the harsh winters ahead.");
		textBox.delay(0.5);
		textBox.addText("Somehow, miraculously, your true nature have not yet been discovered.");
		textBox.addText(" ");
		textBox.addText(" ");
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
		graphics.rectangle(false, window.width()/4, 2*window.height()/3, 64, 16);
		graphics.rectangle(false, window.width()/2, 2*window.height()/3, 64, 16);
		graphics.rectangle(false, 3*window.width()/4, 2*window.height()/3, 64, 16);
		for (lib.ButtonText b : _buttons) {
			b.draw();
		}
		textBox.draw();
	}

	@Override
	public void close() {}

}
