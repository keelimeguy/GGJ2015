package ggj2015.graphics.background;

import ggj2015.graphics.Screen;

public class ScrollingBackground extends Background {

	int direction, speed;
	
	public ScrollingBackground(String path, int x, int y) {
		super(path, x, y);
	}

	public void update(){
	}
	
	public void render(Screen screen){
		screen.renderScrollingBackground(this, x, y);
	}
}
