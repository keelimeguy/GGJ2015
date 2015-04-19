package ggj2015.graphics;

import ggj2015.Game;
import ggj2015.entity.Mud;
import ggj2015.entity.Tree;
import ggj2015.entity.Wall;
import ggj2015.entity.textElement.TextElement;
import ggj2015.graphics.background.Background;
import ggj2015.graphics.background.ScrollingBackground;

public class Screen {

	public int width, height;
	public int[] pixels;

	public int xOffset, yOffset;

	/**
	 * Creates a screen of a given width and height
	 * @param width : Width of the screen
	 * @param height : Height of the screen
	 */
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	/**
	 * Clears the screen to the given color
	 * @param color : Color to clear the screen
	 */
	public void clear(int color) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}

	/**
	 * Renders the player sprite 
	 * @param xp : The x coordinate of the player (in tile units)
	 * @param yp : The y coordinate of the player (in tile units)
	 * @param sprite : The sprite to render
	 * @param flip : How to flip the sprite (none = 0, horizontal = 1, vertical = 2, both ways = 3)
	 */
	public void renderPlayer(int xp, int yp, Sprite sprite, int flip) {
		if (Game.stage > 9000) {
			xp -= xOffset;
			yp -= yOffset;

			for (int y = 0; y < sprite.SIZE_Y; y++) {
				int ya = y + yp;
				for (int x = 0; x < sprite.SIZE_X; x++) {
					int xa = x + xp;
					if (xa >= width || ya < 0 || ya >= height) break;
					if (xa < 0) continue;
					int col = sprite.pixels[x + y * sprite.SIZE_X];
					if (col != 0xffff00ff) pixels[xa + ya * width] = col;
				}
			}
		} else {
			// Updates the position given the screen offset
			yp -= yOffset;
			xp -= xOffset;

			for (int y = 0; y < sprite.SIZE_Y; y++) {
				int ya = y + yp, ys = y;

				// Flip vertical
				if (flip > 1) ys = sprite.SIZE_Y - 1 - y;

				for (int x = 0; x < sprite.SIZE_X; x++) {
					int xa = x + xp, xs = x;

					// Flip horizontal
					if (flip == 1 || flip == 3) xs = sprite.SIZE_X - 1 - x;

					if (xa < -sprite.SIZE_X || xa >= width || ya < 0 || ya >= height) break;
					if (xa < 0) continue;

					int[] rpixels;
					//if (angle != 0)
					//rpixels = rotate(sprite.pixels, sprite.SIZE_X, sprite.SIZE_Y, angle);
					//else
					rpixels = sprite.pixels;

					// Takes the color of the sprite pixel
					int col = rpixels[xs + ys * sprite.SIZE_X];

					// If the color is 0xFF00FF don't render that pixel
					if (col != 0xffff00ff) pixels[xa + ya * width] = col;
				}
			}
		}
	}

	public void renderBackground(Background background, int xp, int yp) {
		xp -= xOffset;
		yp -= yOffset;

		for (int y = 0; y < background.height; y++) {
			int ya = y + yp;
			for (int x = 0; x < background.width; x++) {
				int xa = x + xp;
				if (xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) continue;
				pixels[xa + ya * width] = background.pixels[x + y * background.width];
			}
		}
	}

	public void renderScrollingBackground(ScrollingBackground background, int xp, int yp) {
		xp -= xOffset;
		yp -= yOffset;

		for (int y = 0; y < height; y++) {
			int ya = y - yp;
			int yy = ya;

			for (int x = 0; x < width; x++) {
				int xa = x - xp;
				int xx = xa;

				if (xx >= background.width) xx = xx - background.width;

				if (xx < 0) xx += background.width - 1;
				if (yy >= background.height) yy -= (background.height - 1);
				if (yy <= 0) yy += background.height - 1;

				pixels[x + y * width] = background.pixels[xx % background.width + yy % background.height * background.width];
			}
		}
	}

	public void renderTree(Tree tree, int xp, int yp) {
		xp -= xOffset;
		yp -= yOffset;

		for (int y = 0; y < tree.sprite.SIZE_Y; y++) {
			int ya = y + yp;
			for (int x = 0; x < tree.sprite.SIZE_X; x++) {
				int xa = x + xp;
				if (xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) continue;
				int col = tree.sprite.pixels[x + y * tree.sprite.SIZE_X];
				if (col != 0xffff00ff) pixels[xa + ya * width] = col;
			}
		}
	}

	public void renderWall(Wall wall, int xp, int yp) {
		xp -= xOffset;
		yp -= yOffset;

		for (int y = 0; y < wall.sprite.SIZE_Y; y++) {
			int ya = y + yp;
			for (int x = 0; x < wall.sprite.SIZE_X; x++) {
				int xa = x + xp;
				if (xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) continue;
				int col = wall.sprite.pixels[x + y * wall.sprite.SIZE_X];
				if (col != 0xffff00ff) pixels[xa + ya * width] = col;
			}
		}
	}

	public void renderMud(Mud mud, int xp, int yp) {
		xp -= xOffset;
		yp -= yOffset;

		for (int y = 0; y < mud.sprite.SIZE_Y; y++) {
			int ya = y + yp;
			for (int x = 0; x < mud.sprite.SIZE_X; x++) {
				int xa = x + xp;
				if (xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) continue;
				int col = mud.sprite.pixels[x + y * mud.sprite.SIZE_X];
				if (col != 0xffff00ff) pixels[xa + ya * width] = col;
			}
		}
	}

	public void renderTextElement(TextElement elmnt, int xp, int yp) {
		for (int y = 0; y < elmnt.sprite.SIZE_Y; y++) {
			int ya = y + yp;
			for (int x = 0; x < elmnt.sprite.SIZE_X; x++) {
				int xa = x + xp;
				if (xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) continue;
				int col = elmnt.sprite.pixels[x + y * elmnt.sprite.SIZE_X];
				if (col != 0xffff00ff) pixels[xa + ya * width] = col;
			}
		}
	}

	/**
	 * Sets the screen offset
	 * @param xOffset : The x offset of the screen
	 * @param yOffset : The y offset of the screen
	 */
	public void setOffset(int xOffset, int yOffset) {
		if (Game.stage != 0 && Game.stage!=90) Game.stage = 1;
		if (xOffset >= 3800) {
			Game.stage = 2;
		}
		if (xOffset >= 6200 && yOffset >= 0) {
			Game.stage = 3;
		}
		if (xOffset >= 6750) {
			Game.stage = 1;
		}
		if (xOffset >= 2600 && !Game.flags[2]) {
			Game.flags[2] = true;
			String audioFilePath = "/audio/narration/GGJ-narration2.wav";
			Game.snd.playSound(audioFilePath);
		}
		if (xOffset >= 3000 && !Game.flags[3]) {
			Game.flags[3] = true;
			String audioFilePath = "/audio/narration/GGJ-narration3.wav";
			Game.snd.playSound(audioFilePath);
		}
		if (xOffset >= 4300 && !Game.flags[4]) {
			Game.flags[4] = true;
			String audioFilePath = "/audio/narration/GGJ-narration4.wav";
			Game.snd.playSound(audioFilePath);
		}
		if (xOffset >= 6000 && !Game.flags[5]) {
			Game.flags[5] = true;
			String audioFilePath = "/audio/narration/GGJ-narration5.wav";
			Game.snd.playSound(audioFilePath);
		}
		if (xOffset >= 8050 && !Game.flags[6]) {
			Game.flags[6] = true;
			String audioFilePath = "/audio/narration/GGJ-narration6.wav";
			Game.snd.playSound(audioFilePath);
		}
		if (xOffset >= 8345) {
			Game.stage = 9001;
		}
		if (yOffset <= -1045) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Game.stage = 90;
		}

		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
