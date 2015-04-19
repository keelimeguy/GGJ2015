package ggj2015.entity.textElement;

import ggj2015.entity.Entity;
import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;

public class TextElement extends Entity {

	public Sprite sprite;

	public TextElement(Sprite sprite, int x, int y) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
	}

	public void update(int width, int height, Screen screen) {
		
	}

	public void render(Screen screen) {
		screen.renderTextElement(this, x, y);
	}

}
