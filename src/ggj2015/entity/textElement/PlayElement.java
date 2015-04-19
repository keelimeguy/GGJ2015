package ggj2015.entity.textElement;

import ggj2015.Game;
import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;
import ggj2015.input.Mouse;

public class PlayElement extends TextElement {

	public PlayElement(Sprite sprite, int x, int y) {
		super(sprite, x, y);
	}

	public void update(Screen screen) {

		if (Mouse.getX() > x && Mouse.getX() < x + sprite.SIZE_X && Mouse.getY() > y && Mouse.getY() < y + sprite.SIZE_Y) {
			if (Mouse.getB() == 1) {
				Game.stage = 1;
				String audioFilePath = "/audio/narration/GGJ-narration1.wav";
				Game.snd.playSound(audioFilePath);
			}
		}
	}

}
