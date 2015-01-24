package ggj2015.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {

	public final int SIZE_X, SIZE_Y;
	private int x, y;
	public int[] pixels;
	private SpriteSheet sheet;
	private String path;

	public static Sprite voidSprite = new Sprite(16, 16, 0x1b87e0);

	// Player sprites:
	public static Sprite player = new Sprite(69, 80, 2, 0, SpriteSheet.playerSheet);
	public static Sprite playerJump1 = new Sprite(69, 80, 3, 0, SpriteSheet.playerSheet);
	public static Sprite playerJump2 = new Sprite(69, 80, 4, 0, SpriteSheet.playerSheet);
	public static Sprite playerSquish1 = new Sprite(69, 80, 1, 0, SpriteSheet.playerSheet);
	public static Sprite playerSquish2 = new Sprite(69, 80, 0, 0, SpriteSheet.playerSheet);
	public static Sprite playerMove = new Sprite(69, 80, "/textures/mobs/player_move_sprite.png");
	public static Sprite[] playerRoll = roll(playerMove, 2);

	private static Sprite[] roll(Sprite sprite, int angleStep) {
		Sprite[] sprites = new Sprite[360 / angleStep];
		for (int k = 0; k < 360 / angleStep; k++) {
			sprites[k] = new Sprite(sprite.SIZE_X, sprite.SIZE_Y, 0);
			sprites[k].pixels = rotate(sprite.pixels, sprite.SIZE_X, sprite.SIZE_Y, angleStep * k);
		}
		return sprites;
	}

	private static int[] rotate(int[] pixels, int width, int height, double angle) {
		int[] result = new int[width * height];

		double nx_x = rotX(-angle, 1.0, 0.0);
		double nx_y = rotY(-angle, 1.0, 0.0);
		double ny_x = rotX(-angle, 0.0, 1.0);
		double ny_y = rotY(-angle, 0.0, 1.0);

		double x0 = rotX(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
		double y0 = rotY(-angle, -width / 2.0, -height / 2.0) + height / 2.0;

		for (int y = 0; y < height; y++) {
			double x1 = x0;
			double y1 = y0;
			for (int x = 0; x < width; x++) {
				int xx = (int) x1;
				int yy = (int) y1;
				int col = 0;
				if (xx < 0 || xx >= width || yy < 0 || yy >= height) {
					col = 0xffff00ff;
				} else {
					col = pixels[xx + yy * width];

				}
				result[x + y * width] = col;
				x1 += nx_x;
				y1 += nx_y;
			}
			x0 += ny_x;
			y0 += ny_y;
		}

		return result;
	}

	private static double rotX(double angle, double x, double y) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return x * cos + y * -sin;
	}

	private static double rotY(double angle, double x, double y) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return x * sin + y * cos;
	}

	/**
	 * Creates a sprite from a sprite sheet
	 * @param sizeX : Horizontal size of the sprite (in pixels)
	 * @param sizeY : Vertical size of the sprite (in pixels)
	 * @param x : The x coordinate of the sprite in the sprite sheet (in size units)
	 * @param y : The y coordinate of the sprite in the sprite sheet (in size units)
	 * @param sheet : The sprite sheet to take the sprite image from
	 */
	public Sprite(int sizeX, int sizeY, int x, int y, SpriteSheet sheet) {
		SIZE_X = sizeX;
		SIZE_Y = sizeY;
		this.x = x * sizeX;
		this.y = y * sizeY;
		this.sheet = sheet;
		pixels = new int[SIZE_X * SIZE_Y];
		load();
	}

	/**
	 * Creates a sprite from a resource path
	 * @param sizeX : Horizontal size of the sprite (in pixels)
	 * @param sizeY : Vertical size of the sprite (in pixels)
	 * @param path : The path source to take the sprite image from
	 */
	public Sprite(int sizeX, int sizeY, String path) {
		SIZE_X = sizeX;
		SIZE_Y = sizeY;
		this.path = path;
		pixels = new int[SIZE_X * SIZE_Y];
		loadFromPath();
	}

	/**
	 * Creates a sprite of a given color
	 * @param sizeX : Horizontal size of the sprite (in pixels)
	 * @param sizeY : Vertical size of the sprite (in pixels)
	 * @param color : The color to fill the sprite
	 */
	public Sprite(int sizeX, int sizeY, int color) {
		SIZE_X = sizeX;
		SIZE_Y = sizeY;
		pixels = new int[SIZE_X * SIZE_Y];
		setColor(color);
	}

	/**
	 * Fills the sprite with a color
	 * @param color : The color to fill the sprite
	 */
	private void setColor(int color) {
		for (int i = 0; i < SIZE_X * SIZE_Y; i++) {
			pixels[i] = color;
		}
	}

	/**
	 * Loads the sprite from its sprite sheet
	 */
	private void load() {
		for (int y = 0; y < SIZE_Y; y++) {
			for (int x = 0; x < SIZE_X; x++) {
				pixels[x + y * SIZE_X] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE_X];
			}
		}
	}

	/**
	 * Loads the sprite from its path
	 */
	private void loadFromPath() {
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
