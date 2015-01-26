package ggj2015.level;

import ggj2015.entity.Entity;
import ggj2015.graphics.Screen;
import ggj2015.graphics.background.Background;

import java.util.ArrayList;
import java.util.List;

public class Level {

	protected int width, height;
	protected int[] pixels;

	private List<Background> backgrounds = new ArrayList<Background>();
	private List<Entity> entitiesUnder = new ArrayList<Entity>();
	private List<Entity> entitiesOver = new ArrayList<Entity>();

	/**
	 * Creates a new level
	 * @param width : The width of the level (in tile units)
	 * @param height : The height of the level (in tile units)
	 */
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void addBackground(Background background, int x, int y) {
		background.setX(x);
		background.setY(y);
		backgrounds.add(background);
	}

	public void update(Screen screen) {

		for (int i = 0; i < backgrounds.size(); i++) {
			backgrounds.get(i).update(screen);
		}
		for (int i = 0; i < entitiesUnder.size(); i++) {
			entitiesUnder.get(i).update(screen);
		}
		for (int i = 0; i < entitiesOver.size(); i++) {
			entitiesOver.get(i).update(screen);
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
	public void renderUnder(int xScroll, int yScroll, Screen screen) {

		// Tells the screen how much it is to be offset
		screen.setOffset(xScroll, yScroll);

		for (int i = 0; i < backgrounds.size(); i++) {
			backgrounds.get(i).render(screen);
		}
		for (int i = 0; i < entitiesUnder.size(); i++) {
			entitiesUnder.get(i).render(screen);
		}
	}

	public boolean findCollision(double x, double y, int dir) {
		for (int i = 0; i < entitiesUnder.size(); i++) {
			Entity e = entitiesUnder.get(i);
			if(e.collide(x, y, dir))return true;
		}
		return false;
	}

	public void renderOver(int xScroll, int yScroll, Screen screen) {

		// Tells the screen how much it is to be offset
		screen.setOffset(xScroll, yScroll);

		for (int i = 0; i < entitiesOver.size(); i++) {
			entitiesOver.get(i).render(screen);
		}
	}

	public void addUnder(Entity e) {
		entitiesUnder.add(e);
	}

	public void addOver(Entity e) {
		entitiesOver.add(e);
	}
}
