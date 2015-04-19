package ggj2015;

import ggj2015.entity.Mud;
import ggj2015.entity.Tree;
import ggj2015.entity.Wall;
import ggj2015.entity.mob.Player;
import ggj2015.entity.textElement.CreditsElement;
import ggj2015.entity.textElement.PlayElement;
import ggj2015.entity.textElement.TextElement;
import ggj2015.graphics.Screen;
import ggj2015.graphics.Sprite;
import ggj2015.graphics.background.Background;
import ggj2015.input.Keyboard;
import ggj2015.input.Mouse;
import ggj2015.level.Level;
import ggj2015.sound.MusicPlayer;
import ggj2015.sound.SoundPlayer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static boolean flags[];

	public static int width = 800;
	public static int height = 450;
	public static String title = "The Adventure of Riley";
	public static int stage = 0;

	private Thread thread;
	private static JFrame frame;
	private Keyboard key;
	private Player player;
	private boolean running = false;

	private Level start;
	private Level main;
	private Level level;
	private Screen screen;
	public static MusicPlayer snd;
	public static SoundPlayer noise;

	// The image which will be drawn in the game window
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	/**
	 * Initiates the necessary variables of the game
	 */
	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		flags = new boolean[7];
		for (int i = 0; i < 7; i++) {
			flags[i] = false;
		}

		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();

		player = new Player(width / 3, 2 * height / 3, key, level);

		start = new Level(width, height);
		start.addBackground(Background.title, -24, 70);
		start.addOver(new CreditsElement(Sprite.credits, width / 2 - Sprite.credits.SIZE_X / 2, height / 4 + Sprite.title.SIZE_Y + Sprite.play.SIZE_Y + 30));
		start.addOver(new PlayElement(Sprite.play, width / 2 - Sprite.play.SIZE_X / 2, height / 4 + Sprite.title.SIZE_Y));
		start.addOver(new TextElement(Sprite.title, width / 2 - Sprite.title.SIZE_X / 2, height / 4 - Sprite.title.SIZE_Y / 2));

		main = new Level(1126, 450);
		main.addBackground(Background.path, -500, -411);
		main.addBackground(Background.end, Background.path.width - 600, -1100);
		main.addUnder(new Tree(player.x + 2650, player.y - 270, true));
		main.addUnder(new Wall(player.x + 4600, player.y - 60));
		main.addOver(new Mud(player.x + 6100, player.y - 60, true));
		main.addUnder(new Mud(player.x + 6100, player.y - 60));
		main.addOver(new Tree(player.x + 2250, player.y + 50));
		main.addOver(new Tree(player.x + 4850, player.y - 110));
		main.addOver(new Tree(player.x + 5603, player.y - 89));
		main.addOver(new Tree(player.x + 5503, player.y + 69));

		level = start;

		snd = new MusicPlayer();
		noise = new SoundPlayer();

		addKeyListener(key);

		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}

	public static int getWindowWidth() {
		return frame.getContentPane().getWidth();
	}

	public static int getWindowHeight() {
		return frame.getContentPane().getHeight();
	}

	/**
	 * Starts the game thread
	 */
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		createBufferStrategy(3);
		thread.start();
		snd.start();
	}

	/**
	 * Stops the game thread
	 */
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
			snd.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs the game
	 */
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0, dt = 0;
		int frames = 0, updates = 0;

		requestFocus();

		String audioFilePath = "/audio/music/Riley's Jig (Game Version).wav";

		snd.playMusic(audioFilePath, 0, 44100 * 64 + 22050);

		// The game loop
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			// Update 60 times a second
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			Graphics g = getGraphics();
			update(g);
			frames++;
			// Keep track of and display the game's ups and fps every second
			/*if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				frame.setTitle(title + " | ups: " + updates + ", fps: " + frames);
				updates = 0;
				frames = 0;
			}*/
		}

		// If we get out of the game loop stop the game
		stop();
	}

	/**
	 * Update the game
	 */
	public void update() {
		if (level != main && stage != 0 && stage != 90) {
			level = main;
			player.init(level);
		}
		key.update(stage);
		level.update(getWindowWidth(), getWindowHeight(), screen);
		player.update(getWindowWidth(), getWindowHeight(), screen);
	}

	/**
	 * Render the game
	 */
	public void paint(Graphics g) {

		int xScroll = player.x - screen.width / 3;
		int yScroll = player.y - 2 * screen.height / 3;

		if (!screen.lock) screen.setOffset(xScroll, yScroll);

		Screen scrn = new Screen(width, height);

		// Clear the screen to black before rendering
		scrn.clear(0xFF000000);

		if (stage != 90) {
			level.renderUnder(xScroll, yScroll, scrn);

			// Render the player
			player.render(scrn);

			level.renderOver(xScroll, yScroll, scrn);
		} else if (stage == 90) {
			for (int y = 0; y < Sprite.creditScreen.SIZE_Y; y++) {
				for (int x = 0; x < Sprite.creditScreen.SIZE_X; x++) {
					if (x >= width || y < 0 || y >= height) break;
					if (x < 0) continue;
					int col = Sprite.creditScreen.pixels[x + y * Sprite.creditScreen.SIZE_X];
					if (col != 0xffff00ff) scrn.pixels[x + y * width] = col;
				}
			}
		}

		System.arraycopy(scrn.pixels, 0, screen.pixels, 0, screen.pixels.length);

		// Copy the screen pixels to the image to be drawn
		System.arraycopy(screen.pixels, 0, pixels, 0, pixels.length);

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		/*g.setColor(Color.RED);
		g.setFont(new Font(Font.SERIF, 20, 20));
		g.drawString("Player: (" + player.x + " , " + player.y + ")", screen.width / 3, 20);
		g.drawString("Scroll: (" + xScroll + " , " + yScroll + ")", screen.width / 3, 60);
		*/

		g.dispose();
	}

	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * Start of the program
	 * @param args : Unused default arguments
	 */
	public static void main(String[] args) {

		System.setProperty("sun.awt.noerasebackground", "true");

		// Create the game
		Game game = new Game();
		game.frame.setResizable(true);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		// Start the game
		game.start();
	}
}
