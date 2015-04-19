package ggj2015.entity;

import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;
import ggj2015.input.Mouse;

public class Tree extends Entity {

	public Sprite sprite;
	private int clicks = 0;
	private boolean falling = false, breaking = false, solid = true, clickable = false, clicked = false;

	public void update(int width, int height, Screen screen) {
		if (Mouse.getX() > (x - screen.xOffset + 110) * width / screen.width && Mouse.getX() < (sprite.SIZE_X + x - screen.xOffset - 138) * width / screen.width && Mouse.getY() > (y - screen.yOffset + 100) * height / screen.height && Mouse.getY() < (sprite.SIZE_Y - screen.yOffset + y) * height / screen.height || Mouse.getX() > (x - screen.xOffset + 30) * width / screen.width && Mouse.getX() < (sprite.SIZE_X - screen.xOffset + x - 30) * width / screen.width && Mouse.getY() > (y - screen.yOffset + 30) * height / screen.height && Mouse.getY() < (y - screen.yOffset + 130) * height / screen.height) {
			if (Mouse.getB() == 1 && clickable && !clicked) {
				clicked = true;
				clicks++;
				if (clicks >= 5) {
					falling = true;
					y += 100;
					clickable = false;
					solid = false;
				}
			}
		}

		if (Mouse.getB() == -1) clicked = false;

		if (falling) sprite = Sprite.treeFallen;
	}

	public void render(Screen screen) {
		screen.renderTree(this, x, y);
	}

	public Tree() {
		x = y = 0;
		sprite = Sprite.tree;
	}

	public Tree(int x, int y) {
		this.x = x;
		this.y = y;
		sprite = Sprite.tree;
	}

	public Tree(int x, int y, boolean clickable) {
		this.x = x;
		this.y = y;
		sprite = Sprite.tree;
		this.clickable = clickable;
	}

	public boolean collide(double xt, double yt, int dir) {
		if (!solid) return false;
		if (xt > (x + 83)) return true;// && xt < sprite.SIZE_X + x
		return false;
	}
}
