package ggj2015.entity.mob;

import ggj2015.Game;
import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;
import ggj2015.input.Keyboard;
import ggj2015.level.Level;

public class Player extends Mob {

	public static final double MAX_VEL_A = 4.0;
	public static double MAX_VEL = MAX_VEL_A, MIN_VEL = -MAX_VEL;

	private Keyboard input;
	private Sprite sprite;
	private Level level;
	private int anim = 0, rollStep = 0, jumpStep = 0;
	double jumpSpeed = 0.0, timerY = 0.0, rollTimer = 0.0;
	double timerX = 0.0, lastDir = 0.0, slowStep = 0.0;
	private boolean walking = false, slowing = false, jumping = false, landing = false;

	/**
	 * Creates a player at a default location
	 * @param input : The Keyboard controller for this player
	 */
	public Player(Keyboard input, Level level) {
		this.input = input;
		sprite = Sprite.player;
		this.level = level;
	}

	/**
	 * Creates a player at a specific (x,y) location
	 * @param x : The starting x coordinate (in pixel units)
	 * @param y : The starting y coordinate (in pixel units)
	 * @param input : The Keyboard controller for this player
	 */
	public Player(int x, int y, Keyboard input, Level level) {
		sprite = Sprite.player;
		this.x = x;
		this.y = y;
		this.input = input;
		this.level = level;
	}

	/**
	 * Updates the player animation and moves the player when necessary
	 */
	public void update(int width, int height, Screen screen) {

		if (Game.stage != 0) {
			if (Game.stage > 9000) {
				if (screen.lastTime == -1) move(0, 0, -1);
			}
			if (Game.stage == 3) {
				MAX_VEL = 1.0;
			} else
				MAX_VEL = MAX_VEL_A;

			// Increase the animation step, but don't let it increase indefinitely
			if (anim < 7500)
				anim++;
			else
				anim = 0;

			// Update the change in position when a movement key is pressed
			double dx = 0.0, dy = 0.0;
			if (input.up) dy--;
			//if (input.down) dy++;
			if (input.left) dx--;
			if (input.right) dx++;

			// Jump when space key is pressed
			if (input.space && !jumping && !landing) {
				String audioFilePath = "/audio/sounds/jump.wav";//System.getProperty("user.dir")
				Game.snd.playSound(audioFilePath);
				jumping = true;
				jumpStep = 1;
				timerY = 0;
				velY = -6.0;
				yinit = y;
			}

			// Move the player if its position will change, set walking flag accordingly
			if ((dx != 0.0 || dy != 0.0) && !slowing) {
				move(timerX, dx, dy);
				timerX += 0.02;
				if (timerX >= 1000) timerX = 0;
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
					slowStep = 0.1;
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
	}

	/**
	 *  Renders the player according to its direction and animation step
	 */
	public void render(Screen screen) {

		if (Game.stage != 0) {
			if (Game.stage > 9000) {
				sprite = Sprite.angel;
			}
			// Flip variable (0=none, 1=horizontal, 2=vertical, 3=both)
			int flip = 0;
			if (dir == 3) flip = 1;

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
			rollTimer++;
			if (rollTimer > 1000) rollTimer = 0;
			// right
			if (dir == 1 && (jumpStep == 6 || jumpStep == 0)) {
				sprite = Sprite.player;
				if (walking && rollTimer % 2 >= 1) {
					sprite = Sprite.playerMove;
				} else
					sprite = Sprite.player;
			}

			// left
			if (dir == 3 && (jumpStep == 6 || jumpStep == 0)) {
				sprite = Sprite.player;
				if (walking && rollTimer % 2 >= 1) {
					sprite = Sprite.playerMove;
				} else
					sprite = Sprite.player;
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
	}

	public boolean jump(double timer) {
		double g = .01;
		if (!collision(0, -g * timer, true) || (!collision(0, -7, true) && timer == 0)) {
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

		if (y >= 300) return false;

		return true;
	}

	protected boolean collision(double dx, double dy, boolean goTillHit) {
		boolean solid = false;
		if (level != null) solid = level.findCollision(x + dx, y + dy, dir);
		return solid;
	}

	public void init(Level level) {
		this.level = level;
	}
}
