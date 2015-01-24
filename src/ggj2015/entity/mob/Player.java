package ggj2015.entity.mob;

import ggj2015.Game;
import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;
import ggj2015.input.Keyboard;

public class Player extends Mob {

	public static final double MAX_VEL = 12.0, MIN_VEL = -MAX_VEL;

	private Keyboard input;
	private Sprite sprite;
	private int anim = 0, rollStep = 0, jumpStep = 0;
	double jumpSpeed = 0.0, timerY = 0.0;
	double timerX = 0.0, lastDir = 0.0, slowStep = 0.0;
	private boolean walking = false, slowing = false, jumping = false, landing = false;

	/**
	 * Creates a player at a default location
	 * @param input : The Keyboard controller for this player
	 */
	public Player(Keyboard input) {
		this.input = input;
		sprite = Sprite.player;
	}

	/**
	 * Creates a player at a specific (x,y) location
	 * @param x : The starting x coordinate (in pixel units)
	 * @param y : The starting y coordinate (in pixel units)
	 * @param input : The Keyboard controller for this player
	 */
	public Player(int x, int y, Keyboard input) {
		sprite = Sprite.player;
		this.x = x;
		this.y = y;
		this.input = input;
	}

	/**
	 * Updates the player animation and moves the player when necessary
	 */
	public void update() {

		// Increase the animation step, but don't let it increase indefinitely
		if (anim < 7500)
			anim++;
		else
			anim = 0;

		// Update the change in position when a movement key is pressed
		double dx = 0.0, dy = 0.0;
		//if (input.up) dy--;
		//if (input.down) dy++;
		if (input.left) dx--;
		if (input.right) dx++;

		// Jump when space key is pressed
		if (input.space && !jumping && !landing) {
			jumping = true;
			jumpStep = 1;
			timerY = 0;
			velY = -6.0;
			yinit = y;
		}

		// Move the player if its position will change, set walking flag accordingly
		if ((dx != 0.0 || dy != 0.0) && !slowing) {
			move(timerX, dx, 0.0); //dy);
			timerX += 0.02;
			if (velX < MAX_VEL) {
				velX = MAX_VEL;
			} else if (velX < MIN_VEL) {
				velX = MIN_VEL;
			}
			walking = true;
			if (dx != lastDir && lastDir != 0) {
				slowing = true;
				slowStep = 0.5;
			} else
				lastDir = dx;

		} else {
			walking = false;
			if (jumping)
				slowStep = 0.01;
			else
				slowStep = 0.5;
			slowing = true;
		}

		if (slowing) {
			if (velX != 0) {
				move(timerX, lastDir, 0.0);
				timerX -= slowStep;
			} else {
				timerX = 0;
				lastDir = 0;
				slowing = false;
			}
		}

		if (jumping) {
			jumping = jump(timerY);
			timerY++;
		}
		if (landing) {
			timerY++;
		}
	}

	/**
	 *  Renders the player according to its direction and animation step
	 */
	public void render(Screen screen) {

		// Flip variable (0=none, 1=horizontal, 2=vertical, 3=both)
		int flip = 0;

		if (jumping) {
			switch (jumpStep) {
			case 0:
				sprite = Sprite.player;
				break;
			case 1:
				sprite = Sprite.playerSquish1;
				break;
			case 2:
				sprite = Sprite.player;
				break;
			case 3:
				sprite = Sprite.playerJump1;
				break;
			case 4:
				sprite = Sprite.playerJump2;
				break;
			case 5:
				sprite = Sprite.playerJump1;
				break;
			case 6:
				sprite = Sprite.player;
				break;
			case 7:
				sprite = Sprite.playerSquish1;
				break;
			}
		}

		// right
		if (dir == 1 && (jumpStep == 6 || jumpStep == 0)) {
			sprite = Sprite.player;
			if (walking) {
				if (rollStep >= Sprite.playerRoll.length) rollStep = 0;
				if (rollStep <= -1) rollStep = Sprite.playerRoll.length - 1;
				sprite = Sprite.playerRoll[rollStep];
				rollStep--;
			} else
				rollStep = 0;
		}

		// left
		if (dir == 3 && (jumpStep == 6 || jumpStep == 0)) {
			sprite = Sprite.player;
			if (walking) {
				if (rollStep >= Sprite.playerRoll.length) rollStep = 0;
				if (rollStep <= -1) rollStep = Sprite.playerRoll.length - 1;
				sprite = Sprite.playerRoll[rollStep];
				rollStep++;
			} else
				rollStep = 0;
		}

		if (landing) {
			if (timerY >= 61 && timerY <= 65) sprite = Sprite.playerSquish2;
			if (timerY >= 66 && timerY <= 69) sprite = Sprite.playerSquish1;
			if (timerY >= 70) {
				sprite = Sprite.player;
				landing = false;
				jumpStep = 0;
			}
		}

		// Offset the position to center the player
		int xx = x - sprite.SIZE_X / 2;
		int yy = y - sprite.SIZE_Y / 2;

		// Render the player sprite
		screen.renderPlayer(xx, yy, sprite, flip);
	}

	public boolean jump(double timer) {
		double g = .01;
		if (!collision(0, g * timer, true)) {
			velY += g * timer;
			if (timer >= 2 && timer <= 4) jumpStep = 2;
			if (timer >= 4 && timer <= 6) jumpStep = 3;
			if (timer >= 7 && timer <= 18) jumpStep = 4;
			if (timer >= 19 && timer <= 28) jumpStep = 5;
			if (timer >= 29 && timer <= 30) jumpStep = 6;
			if (timer >= 58) {
				jumpStep = 7;
				jumping = false;
				landing = true;
			}
			y += Math.round(velY);
		} else {
			jumpStep = 7;
			if (velY < 0) {
				timer = 30;
			} else {
				timer = 60;
				return false;
			}
		}

		if (y >= yinit) return false;

		return true;
	}

	protected boolean collision(int dx, int dy, boolean goTillHit) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			int xt = ((x + dx) + c % 2 * 13 - 7) >> 4;
			int yt = ((y + dy) + c / 2 * 12 + 3) >> 4;
			solid = false;
		}
		return solid;
	}
}
