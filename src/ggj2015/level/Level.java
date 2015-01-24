package ggj2015.level;

import ggj2015.entity.Entity;
import ggj2015.graphics.Screen;
import ggj2015.graphics.background.Background;
import ggj2015.graphics.background.ScrollingBackground;

import java.util.ArrayList;
import java.util.List;

public class Level {

	protected int width, height;
	protected int[] pixels;

	private Background background;
	private List<Entity> entities = new ArrayList<Entity>();

	/**
	 * Creates a new level
	 * @param width : The width of the level (in tile units)
	 * @param height : The height of the level (in tile units)
	 */
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setBackground(Background background, int x, int y) {
		this.background = background;
		this.background.setX(x);
		this.background.setY(y);
	}

	public void update() {

		background.update();

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}

	private void time() {
	}

	/**
	 * Renders all the tiles on the screen
	 * @param xScroll : The scroll offset of the screen in the x direction (in pixels)
	 * @param yScroll : The scroll offset of the screen in the y direction (in pixels)
	 * @param screen : The screen to render to
	 */
	public void render(int xScroll, int yScroll, Screen screen) {

		// Tells the screen how much it is to be offset
		screen.setOffset(xScroll, yScroll);

		background.render(screen);

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
	}

	public void add(Entity e) {
		entities.add(e);
	}
}
