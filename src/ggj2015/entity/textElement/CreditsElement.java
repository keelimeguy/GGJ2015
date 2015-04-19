package ggj2015.entity.textElement;

import ggj2015.Game;
import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;
import ggj2015.input.Mouse;

public class CreditsElement extends TextElement {

	public CreditsElement(Sprite sprite, int x, int y) {
		super(sprite, x, y);
	}

	public void update(int width, int height, Screen screen) {
		if (Mouse.getX() > x * width / screen.width && Mouse.getX() < (x + sprite.SIZE_X) * width / screen.width && Mouse.getY() > y * height / screen.height && Mouse.getY() < (y + sprite.SIZE_Y) * height / screen.height) {
			if (Mouse.getB() == 1) {
				Game.stage = 90;
			}
		}
	}

}
