package ggj2015.entity;

import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;

public class Wall extends Entity {

	public Sprite sprite;

	public Wall(int x, int y) {
		this.x = x;
		this.y = y;
		sprite = Sprite.wall;
	}

	public void update(int width, int height, Screen screen) {

	}

	public void render(Screen screen) {
		screen.renderWall(this, x, y);
	}

	public boolean collide(double xt, double yt, int dir) {
		if (xt > x-21 && xt < sprite.SIZE_X + x+21 && yt>y-21) return true;
		return false;
	}
}
