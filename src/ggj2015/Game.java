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
	private JFrame frame;
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
		return width;
	}

	public static int getWindowHeight() {
		return height;
	}

	/**
	 * Starts the game thread
	 */
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
		snd.start();
		//noise.start();
	}

	/**
	 * Stops the game thread
	 */
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
			snd.join();
			//noise.join();
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

		String audioFilePath = System.getProperty("user.dir") + "/res/audio/music/Riley's Jig (Game Version).wav";
		//String audioFilePath = System.getProperty("user.dir") + "/res/audio/sounds/jump.wav";

		snd.playMusic(audioFilePath, 0, 44100 * 64 + 22050);

		// The game loop
		while (running) {
			if (Game.stage == -1) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				running = false;
			}
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			// Update 60 times a second
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			// Keep track of and display the game's ups and fps every second
			if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				frame.setTitle(title + " | ups: " + updates + ", fps: " + frames);
				updates = 0;
				frames = 0;
			}
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
		level.update(screen);
		player.update(screen);
	}

	/**
	 * Render the game
	 */
	public void render() {

		// Create a buffer strategy for the game
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		// Clear the screen to black before rendering
		screen.clear(0xff00ff00);

		int xScroll = player.x - screen.width / 3;
		int yScroll = player.y - 2 * screen.height / 3;

		level.renderUnder(xScroll, yScroll, screen);

		// Render the player
		player.render(screen);

		level.renderOver(xScroll, yScroll, screen);

		// Copy the screen pixels to the image to be drawn
		System.arraycopy(screen.pixels, 0, pixels, 0, pixels.length);

		// Draw the image
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		/*g.setColor(Color.RED);
		g.setFont(new Font(Font.SERIF, 20, 20));
		g.drawString("Player: (" + player.x + " , " + player.y + ")", screen.width / 3, 20);
		g.drawString("Scroll: (" + xScroll + " , " + yScroll + ")", screen.width / 3, 60);
		*/

		if (stage == 90) {
			System.out.println("Credits");
			g.drawString("Creative Director", width / 3, 60);
			g.drawString("Mac \'broughtthegangtogether\' Ira", width / 3, 60+12);

			g.drawString("Head Producer", width / 3, 60+12+12+12);
			g.drawString("Mac \'photosynthesis\' Ira", screen.width / 3, 60+12+12+12+12);

			g.drawString("Brainstorm Trust", screen.width / 3, 60+12+12+12+12+12+12);
			g.drawString("Eric Burt, Mac Ira, Eric Lin, Nathan Milne, & Keelin Wheeler", screen.width / 3, 60+12+12+12+12+12+12+12);

			g.drawString("Lead Animator", screen.width / 3, 60+12+12+12+12+12+12+12+12+12);
			g.drawString("Keelin \'waltDisney\' Wheeler", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Programming Head Wizard", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Keelin \'merlin\'  Wheeler", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Programming Intern", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Mac \'shittyprogrammer\' Ira", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Head Level Designer", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Nathan \'Mario\' Milne", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Special Thanks", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Obama", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Artists", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Eric \'biglog\' Lin", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Shawn \'sandman\' Gale", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Head Composer", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Eric \'MofoMozart\' Burt", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Caterer", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Nathan \'hasMoney\' Milne", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Audio Engineer", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Eric \'Apollo\' Burt", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Motivational Leader", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("The USG room", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("Beta Tester", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Keelin \'mecha\' Wheeler", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("No Show", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			g.drawString("Matt \'TheScrub\' Nolan", screen.width / 3, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);

			g.drawString("GlobalGameJam2015", screen.width / 6, 60+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12+12);
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stage =-1;
		}

		g.dispose();
		bs.show();
	}

	/**
	 * Start of the program
	 * @param args : Unused default arguments
	 */
	public static void main(String[] args) {

		// Create the game
		Game game = new Game();
		game.frame.setResizable(false);
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
