package ggj2015.entity;

import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;

public class Mud extends Entity{

	public Sprite sprite;
	
	public Mud(int x, int y){
		this.x = x;
		this.y = y;
		sprite = Sprite.mud;
	}
	public Mud(int x, int y, boolean over){
		this.x = x;
		this.y = y;
		sprite = Sprite.mud2;
	}
	
	public void update(Screen screen){
		
	}
	
	public void render(Screen screen){
		screen.renderMud(this, x, y);
	}
	
}
