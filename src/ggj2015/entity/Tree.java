package ggj2015.entity;

import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;
import ggj2015.input.Mouse;

public class Tree extends Entity {

	public Sprite sprite;
	private int clicks = 0;
	private boolean falling = false, breaking = false, solid = true, clickable = false, clicked = false;

	public void update(Screen screen) {

		if (Mouse.getX() > x - screen.xOffset + 110 && Mouse.getX() < sprite.SIZE_X + x - screen.xOffset - 138 && Mouse.getY() > y - screen.yOffset + 100 && Mouse.getY() < sprite.SIZE_Y - screen.yOffset + y || Mouse.getX() > x - screen.xOffset + 30 && Mouse.getX() < sprite.SIZE_X - screen.xOffset + x - 30 && Mouse.getY() > y - screen.yOffset + 30 && Mouse.getY() < y - screen.yOffset + 130) {
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
