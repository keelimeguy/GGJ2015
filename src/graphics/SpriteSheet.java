package ggj2015.graphics;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	private String path;
	public final int SIZE_X, SIZE_Y;
	public int[] pixels;

	public static SpriteSheet playerSheet = new SpriteSheet("/textures/mobs/player_jump_sprite.png", 345, 80);

	/**
	 * Creates a sprite sheet from a resource path
	 * @param path : File path to image to create sprite sheet from
	 * @param sizeX : Horizontal size of the image to create sprite sheet from
	 * @param sizeY : Vertical size of the image to create sprite sheet from
	 */
	public SpriteSheet(String path, int sizeX, int sizeY) {
		this.path = path;
		SIZE_X = sizeX;
		SIZE_Y = sizeY;
		pixels = new int[SIZE_X * SIZE_Y];
		load();
	}

	/**
	 * Loads a sprite sheet from the image pointed to by the path
	 */
	private void load() {
		try {
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixels, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

