package ggj2015.graphics.background;

import ggj2015.graphics.Screen;
import ggj2015.graphics.SpriteSheet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background {

	public static Background path = new ScrollingBackground("/levels/background_by_nkorth.png",0,0);
	
	public int[] pixels;
	public int width, height, x, y;

	public Background(String path, int x, int y) {
		this.x = x;
		this.y = y;
		try {
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width*height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void update() {

	}

	public void render(Screen screen) {
		screen.renderBackground(this, x, y);
	}

}
