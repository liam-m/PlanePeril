package screen;

import java.io.File;

import lib.jog.audio;
import lib.jog.graphics;
import lib.jog.window;
import bearTrafficControl.Main;

public class Title extends Scene {
	
	private audio.Music _beep;
	private libraryClasses.ButtonText[] _buttons;
	private double _angle;

	public Title(Main main) {
		super(main);
	}

	@Override
	public void start() {
		_beep = audio.newMusic("sfx" + File.separator + "beep.ogg");
		_beep.setVolume(0.2f);
		_buttons = new libraryClasses.ButtonText[3];
		// Demo Button
		libraryClasses.ButtonText.Action demo = new libraryClasses.ButtonText.Action() {
			@Override
			public void action() {
				_main.setScene(new DifficultySelect(_main));
			}
		};
		_buttons[0] = new libraryClasses.ButtonText("Play Demo", demo, window.height(), window.height()/2 + 96, window.width() - window.height(), 24, 8, 6);
		// Game Button
		libraryClasses.ButtonText.Action play = new libraryClasses.ButtonText.Action() {
			@Override
			public void action() {
//				_main.setScene(new Game(_main));
			}
		};
		_buttons[1] = new libraryClasses.ButtonText("Play Full Game", play, window.height(), window.height()/2 + 126, window.width() - window.height(), 24, 8, 6);
		_buttons[1].setAvailability(false);
		// Exit Button
		libraryClasses.ButtonText.Action exit = new libraryClasses.ButtonText.Action() {
			@Override
			public void action() {
				_main.quit();
			}
		};
		_buttons[2] = new libraryClasses.ButtonText("Exit", exit, window.height(), window.height()/2 + 156, window.width() - window.height(), 24, 8, 6);
		_angle = 0;
	}

	@Override
	public void update(double dt) {
		_angle += dt;
		double beepTimer = (_angle * 4) + (Math.PI * 4 / 5);
		beepTimer %= (2 * Math.PI);
		if ( beepTimer <= 0.1 ) {
			_beep.stop();
			_beep.play();
		}
	}

	@Override
	public void mousePressed(int key, int x, int y) {}

	@Override
	public void mouseReleased(int key, int mx, int my) {
		for (libraryClasses.ButtonText b : _buttons) {
			if (b.isMouseOver(mx, my)) {
				b.act();
			}
		}
		
	}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void draw() {
		drawRadar();
		drawMenu();
	}
	
	private void drawRadar() {
		// Radar
		graphics.setColour(0, 128, 0);
		graphics.circle(false, window.height()/2, window.height()/2, window.height()/2 - 32, 100);
		graphics.setColour(0, 128, 0, 32);
		graphics.circle(false, window.height()/2, window.height()/2, window.height()/3, 100);
		graphics.circle(false, window.height()/2, window.height()/2, window.height()/4 - 16, 100);
		graphics.circle(false, window.height()/2, window.height()/2, window.height()/9, 100);
		graphics.circle(false, window.height()/2, window.height()/2, 2, 100);
		graphics.setColour(0, 128, 0);
		double angle = (_angle * 4) % (2 * Math.PI);
		int w = (int)( Math.cos(angle) * (window.height()/2 - 32) );
		int h = (int)( Math.sin(angle) * (window.height()/2 - 32) );
		graphics.line(window.height()/2, window.height()/2, window.height()/2 + w, window.height()/2 + h);
		graphics.setColour(0, 128, 0, 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -8 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -7 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -6 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -5 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -4 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -3 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -2 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -1 * Math.PI / 8);
		// Title
		String title = "Bear Traffic Controller";
		double a = angle + (Math.PI * 4 / 5);
		for (int i = 0; i < title.length(); i++) {
			a -= Math.PI / 32;
			double opacity = a %= (2 * Math.PI);
			opacity *= 256 / (2 * Math.PI);
			opacity = 256 - opacity;
			opacity %= 256;
			graphics.setColour(0, 128, 0, opacity);
			graphics.print(title.substring(i, i+1), 74*4.5 + i * 14, 344, 1.8);
		}
	}
	
	private void drawMenu() {
		// Draw Random Extras
		graphics.setColour(0, 128, 0);
		graphics.line(window.height(), 16, window.height(), window.height() - 16);
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
		java.text.DateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
		java.util.Date date = new java.util.Date();
		graphics.print(dateFormat.format(date), window.height() + 8, 20);
		graphics.print(timeFormat.format(date), window.height() + 8, 36);
		graphics.line(window.height(), 48, window.width() - 16, 48);
		// Draw Random Statistics
		graphics.line(window.height(), 108, window.width() - 16, 108);
		graphics.print("Statistics:", window.height() + 8, 116);
		graphics.line(window.height(), 130, window.width() - 16, 130);
		graphics.print("Time Played:", window.height() + 8, 138);
		int hours = (int)(_main.score().timePlayed() / (60 * 60));
		int minutes = (int)(_main.score().timePlayed() / 60);
		minutes %= 60;
		double seconds = _main.score().timePlayed() % 60;
		java.text.DecimalFormat df = new java.text.DecimalFormat("00.00");
		String timePlayed = String.format("%d:%02d:", hours, minutes) + df.format(seconds); 
		graphics.print(timePlayed, window.height() + 8, 154);
		graphics.print("Current Score:", window.height() + 8, 170);
		graphics.print(String.valueOf(_main.score().calculate()), window.height() + 8, 186);
		// Draw Buttons
		for (libraryClasses.ButtonText b : _buttons) {
			b.draw();
		}
		graphics.setColour(0, 128, 0);
		graphics.line(window.height(), window.height()/2 + 90, window.width() - 16, window.height()/2 + 90);
		graphics.line(window.height(), window.height()/2 + 120, window.width() - 16, window.height()/2 + 120);
		graphics.line(window.height(), window.height()/2 + 150, window.width() - 16, window.height()/2 + 150);
		graphics.line(window.height(), window.height()/2 + 180, window.width() - 16, window.height()/2 + 180);
	}

	@Override
	public void close() {
		
	}

}
