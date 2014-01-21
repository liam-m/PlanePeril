package scn;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import lib.jog.audio;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.window;
import btc.Main;

public class Title extends Scene {
	
	private final static String HELP_URL = "http://imp1.github.io/bear_traffic_controller/ingamehelp";
	
	private audio.Sound beep;
	private lib.ButtonText[] buttons;
	private double angle;
	
	public Title(Main main) {
		super(main);
	}

	@Override
	public void start() {
		beep = audio.newSoundEffect("sfx" + File.separator + "beep.ogg");
		beep.setVolume(0.2f);
		
		buttons = new lib.ButtonText[5];
		// Demo Button
		lib.ButtonText.Action demo = new lib.ButtonText.Action() {
			@Override
			public void action() {
				main.setScene(new DifficultySelect(main, DifficultySelect.CREATE_DEMO));
			}
		};
		buttons[0] = new lib.ButtonText("Play Demo", demo, window.height(), window.height()/2 + 96, window.width() - window.height(), 24, 8, 6);
		// Game Button
		lib.ButtonText.Action play = new lib.ButtonText.Action() {
			@Override
			public void action() {
//				_main.setScene(new Game(main));
			}
		};
		buttons[1] = new lib.ButtonText("Play Full Game", play, window.height(), window.height()/2 + 126, window.width() - window.height(), 24, 8, 6);
		buttons[1].setAvailability(false);
		// Credits Button
		lib.ButtonText.Action credits = new lib.ButtonText.Action() {
			@Override
			public void action() {
				main.setScene(new Credits(main));
				}
			};
		buttons[2] = new lib.ButtonText("Credits", credits, window.height(), window.height()/2 + 156, window.width() - window.height(), 24, 8, 6);
		// Help Button
		lib.ButtonText.Action help = new lib.ButtonText.Action() {
			@Override
			public void action() {
				try {
					Desktop.getDesktop().browse(new URI(HELP_URL));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		};
		buttons[3] = new lib.ButtonText("Help               (Opens in Browser)", help, window.height(), window.height()/2 + 186, window.width() - window.height(), 24, 8, 6);
		// Exit Button
				lib.ButtonText.Action exit = new lib.ButtonText.Action() {
					@Override
					public void action() {
						main.quit();
					}
				};
		buttons[4] = new lib.ButtonText("Exit", exit, window.height(), window.height()/2 + 216, window.width() - window.height(), 24, 8, 6);
		
		angle = 0;
	}

	@Override
	public void update(double dt) {
		angle += dt;
		double beepTimer = (angle * 4) + (Math.PI * 4 / 5);
		beepTimer %= (2 * Math.PI);
		if ( beepTimer <= 0.1 ) {
			playSound(beep);
		}
	}

	@Override
	public void mousePressed(int key, int x, int y) {}

	@Override
	public void mouseReleased(int key, int mx, int my) {
		for (lib.ButtonText b : buttons) {
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
		double radarAngle = (angle * 4) % (2 * Math.PI);
		int w = (int)( Math.cos(radarAngle) * (window.height()/2 - 32) );
		int h = (int)( Math.sin(radarAngle) * (window.height()/2 - 32) );
		graphics.line(window.height()/2, window.height()/2, window.height()/2 + w, window.height()/2 + h);
		graphics.setColour(0, 128, 0, 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, radarAngle, -8 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, radarAngle, -7 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, radarAngle, -6 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, radarAngle, -5 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, radarAngle, -4 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, radarAngle, -3 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, radarAngle, -2 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, radarAngle, -1 * Math.PI / 8);
		// Title
		String title = "Bear Traffic Controller";
		double a = radarAngle + (Math.PI * 4 / 5);
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
		// Draw Extras e.g. Date, Time, Credits
		graphics.setColour(0, 128, 0);
		graphics.line(window.height(), 16, window.height(), window.height() - 16);
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
		java.text.DateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
		java.util.Date date = new java.util.Date();
		graphics.print(dateFormat.format(date), window.height() + 8, 20);
		graphics.print(timeFormat.format(date), window.height() + 8, 36);
		graphics.line(window.height(), 48, window.width() - 16, 48);
		graphics.print("Created by:", window.height() + 8, 56);
		graphics.print("TEAM FLR", window.height() + 8, 72);
		
		// Draw Buttons
		for (lib.ButtonText b : buttons) b.draw();
		graphics.setColour(0, 128, 0);
		graphics.line(window.height(), window.height()/2 + 90, window.width() - 16, window.height()/2 + 90);
		graphics.line(window.height(), window.height()/2 + 120, window.width() - 16, window.height()/2 + 120);
		graphics.line(window.height(), window.height()/2 + 150, window.width() - 16, window.height()/2 + 150);
		graphics.line(window.height(), window.height()/2 + 180, window.width() - 16, window.height()/2 + 180);
		graphics.line(window.height(), window.height()/2 + 210, window.width() - 16, window.height()/2 + 210);
		graphics.line(window.height(), window.height()/2 + 240, window.width() - 16, window.height()/2 + 240);
	}

	@Override
	public void close() {

	}

	@Override
	public void playSound(Sound sound) {
		sound.stop();
		sound.play();
	}

}
