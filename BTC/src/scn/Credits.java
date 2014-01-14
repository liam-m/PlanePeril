package scn;

import lib.TextBox;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import btc.Main;

public class Credits extends Scene {
	
	private float speed;
	private lib.TextBox[] people;

	public Credits(Main main) {
		super(main);
	}

	@Override
	public void mousePressed(int key, int x, int y) {}

	@Override
	public void mouseReleased(int key, int x, int y) {}

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
		speed = 1f;
		people = new TextBox[6];
		// dimensions of each textbox
		int w = window.width() / 6;
		int h = 96;
		// columns of textboxs
		int columns = 2;
		int rows = people.length / columns;
		// calculate positions;
		int paddingW = (window.width() - (w * columns)) / (columns + 1);
		int paddingH = (window.height() - (h * rows)) / (rows + 1);
		System.out.println("" + paddingW + ", " + paddingH);
		for (int i = 0; i < people.length; i ++) {
			int x = paddingW + (i % columns) * (w + paddingW);
			int y = paddingH + (i / columns) * (h + paddingH);
			System.out.println("" + x + ", " + y + ", " + w + ", " + h);
			people[i] = new TextBox(x, y, w, h, 4);
			people[i].setSpeed(0.03);
		}
		// Josh Adams
		people[0].addText("Josh Adams");
		// Gareth Handley
		people[1].addText("Gareth Handley");
		// Sanjit Samaddar
		people[2].addText("Sanjit Samaddar");
		// Alex Stuart
		people[3].addText("Alex Stuart");
		// Huw Taylor
		people[4].addText("Huw Taylor");
		// Stephen Webb
		people[5].addText("Stephen Webb");
	}

	@Override
	public void update(double dt) {
		boolean hurried = input.isKeyDown(input.KEY_SPACE) || input.isMouseDown(input.MOUSE_LEFT);
		speed = hurried ? 4f : 1f;
		for (int i = 0; i < people.length; i ++) {
			if (i == 0 || people[i-1].isUpToDate()) {
				people[i].update(dt * speed);
			}
		}
	}

	@Override
	public void draw() {
		graphics.setColour(0, 128, 0);
		graphics.printCentred("Bear Traffic Controller", 0, 48, 2, window.width());
		for (TextBox person : people) {
			person.draw();
		}
	}

	@Override
	public void close() {}

}
