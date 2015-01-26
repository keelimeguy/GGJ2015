package ggj2015.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private static final int START = 0, TREE = 1, WALL = 2, MUD = 3;

	private boolean[] keys = new boolean[120];
	public boolean up, down, left, right, space, fly = false;;

	/**
	 * Sets a flag to determine what action should occur given associated key presses
	 */
	public void update(int keyType) {
		switch (keyType) {
		case START:
			up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
			down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
			left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
			right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
			space = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
			break;
		case TREE:
			up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
			down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
			left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
			right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
			space = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
			break;
		case WALL:
			up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
			down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
			left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
			right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
			space = keys[KeyEvent.VK_SPACE];
			break;
		case MUD:
			up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
			down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
			left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
			right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
			space = false;//keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
			break;
		case 9001:
			up = down = left = right = space = false;
			fly = true;
			break;
		}
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {

	}

}
