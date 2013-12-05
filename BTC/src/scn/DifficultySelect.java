package scn;

import lib.jog.graphics;
import lib.jog.window;
import btc.Main;

public class DifficultySelect extends Scene {
	
	private lib.ButtonText[] _buttons;
	private lib.TextBox intro;
	private static final String placeName = "Moscow";

	protected DifficultySelect(Main main) {
		super(main);
		_buttons = new lib.ButtonText[3];
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		for (lib.ButtonText b : _buttons) {
			if (b.isMouseOver(x, y)) {
				b.act();
			}
		}
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int key) {

	}

	@Override
	public void keyReleased(int key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		lib.ButtonText.Action easy = new lib.ButtonText.Action() {
			
			@Override
			public void action() {
				_main.setScene(new Demo(_main, Demo.difficultyEasy));
			}
		};
		_buttons[0] = new lib.ButtonText("Easy", easy, window.width()/4, 2*window.height()/3, 
				window.width() - window.height(), 16, 8, 6);
		
		lib.ButtonText.Action medium = new lib.ButtonText.Action() {
			
			@Override
			public void action() {
				_main.setScene(new Demo(_main, Demo.difficultyMedium));
			}
		};
		_buttons[1] = new lib.ButtonText("Medium", medium, window.width()/2, 2*window.height()/3, 
				window.width() - window.height(), 16, 8, 6);
		
			lib.ButtonText.Action hard = new lib.ButtonText.Action() {
			
			@Override
			public void action() {
				_main.setScene(new Demo(_main, Demo.difficultyHard));
			}
		};
		_buttons[2] = new lib.ButtonText("Hard", hard, 3*window.width()/4, 2*window.height()/3, 
				window.width() - window.height(), 8, 8, 6);
		intro = new lib.TextBox(64, 96, window.width() - 128, window.height() - 96, 32);
		intro.addText("You are a 500kg ferocious Grizzly Bear.");
		intro.addText("The Humans are not aware of your hidden identity.");
		intro.delay(2);
		intro.addText("You have become an air traffic controller at " + DifficultySelect.placeName + " international.");
		intro.addText("in order to provide for your family during the harsh winters ahead");
		intro.addText("Guide planes to their destination successfully and you will be rewarded");
		intro.addText("Fail and the humans may discover your secret identity and put you in a zoo");
		intro.addText("Or worse, they may shoot you and your family.");
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		intro.update(dt);
		
	}

	@Override
	public void draw() {
		graphics.setColour(0,128,0);
		graphics.rectangle(false, window.width()/4, 2*window.height()/3, 64, 16);
		graphics.rectangle(false, window.width()/2, 2*window.height()/3, 64, 16);
		graphics.rectangle(false, 3*window.width()/4, 2*window.height()/3, 64, 16);
		// TODO Auto-generated method stub
		for (lib.ButtonText b : _buttons) {
			b.draw();
		}
		intro.draw();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
