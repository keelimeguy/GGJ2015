package ggj2015.entity.mob;

import ggj2015.entity.Entity;
import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;

public abstract class Mob extends Entity {

	protected Sprite sprite;
	protected int dir = 0;
	protected double velX = 0, velY;
	protected boolean moving = false;

	protected int yinit = 0;

	/**
	 * Moves the player and updates its direction
	 * @param dx : The change in x of the mob's location
	 * @param dy : The change in y of the mob's location
	 */
	public void move(double timer, double dx, double dy) {

		// Update the direction the mob is moving in
		if (dy < 0.0) dir = 0;
		if (dx > 0.0) dir = 1;
		if (dy > 0.0) dir = 2;
		if (dx < 0.0) dir = 3;

		if (timer >= Math.PI / 2) timer = Math.PI / 2;
		if (timer <= 0) timer = 0;

		velX = dx * Player.MAX_VEL * Math.sin(timer);
		// If the mob won't collide, move
		if (!collision( 0, velX, true)) y += dy;

		if (!collision(velX, 0, true)) x += Math.round(velX);

	}

	public void update(Screen screen) {
	}

	protected void shoot(int x, int y, double dir) {
	}

	public void render(Screen screen) {
	}

	/**
	 * Determines if the mob will collide should it move in the given direction
	 * @param dx : The change in x of the mob's location
	 * @param dy : The change in y of the mob's location
	 * @return 
	 * True if it will collide, false otherwise
	 */
	protected boolean collision(double dx, double dy, boolean goTillHit) {
		return false;
	}
}
