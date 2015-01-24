package ggj2015.graphics;

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

	

	/**
	 * Sets the screen offset
	 * @param xOffset : The x offset of the screen
	 * @param yOffset : The y offset of the screen
	 */
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
