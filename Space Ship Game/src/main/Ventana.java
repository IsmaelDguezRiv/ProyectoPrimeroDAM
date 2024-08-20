package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import database.dataBase;
import gameObjects.Constants;
import gameObjects.JOptionCondition;
import graphics.Assets;
import states.GameState;
import input.KeyBoard;

public class Ventana extends JFrame implements Runnable {

	private static Ventana instance;

	private Canvas canvas;
	private Thread thread;
	private boolean running = false;

	private BufferStrategy bs;
	private Graphics g;

	private final int FPS = 60;
	private double TARGETTIME = 1000000000 / FPS;
	private double deltaTime = 0;
	private int AVERAGEFPS = FPS;

	private GameState gamestate;
	private KeyBoard keyBoard;

	public Ventana() {

		setTitle("GALACTIC TROJAN");
		setSize(Constants.DEF_ANCHO, Constants.DEF_LARGO);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);

		canvas = new Canvas();
		keyBoard = new KeyBoard();

		canvas.setPreferredSize(new Dimension(Constants.DEF_ANCHO, Constants.DEF_LARGO));
		canvas.setMaximumSize(new Dimension(Constants.DEF_ANCHO, Constants.DEF_LARGO));
		canvas.setMinimumSize(new Dimension(Constants.DEF_ANCHO, Constants.DEF_LARGO));
		canvas.setFocusable(true);

		add(canvas);
		canvas.addKeyListener(keyBoard);
		setVisible(true);
	}

	public static void main(String[] args) {
		Ventana instance = new Ventana();
		instance.start();
		Ventana.setInstance(instance);

	}

	private void update() {

		keyBoard.update();
		gamestate.update();
	}

	private void draw() {
		bs = canvas.getBufferStrategy();

		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}

		g = bs.getDrawGraphics();
		// ------------------------------------
		g.setColor(Color.BLACK);

		g.fillRect(0, 0, Constants.DEF_ANCHO, Constants.DEF_LARGO);

		gamestate.draw(g);
		g.setColor(Color.WHITE);
		g.drawString("" + AVERAGEFPS + " FPS", 10, 10);
		// ------------------------------------
		g.dispose();
		bs.show();
	}

	private void init() {
		dataBase.connection(null);
		Assets.init();
		gamestate = new GameState();
	}

	public void run() {

		long now = 0;
		long lastTime = System.nanoTime();
		int frames = 0;
		long time = 0;

		init();

		while (running) {
			now = System.nanoTime();
			deltaTime += (now - lastTime) / TARGETTIME;
			time += (now - lastTime);
			lastTime = now;

			if (deltaTime >= 1) {
				update();
				draw();
				deltaTime--;
				frames++;
			}
			if (time >= 1000000000) {
				AVERAGEFPS = frames;
				frames = 0;
				time = 0;
			}
		}
		stop();
	}

	private void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	private void stop() {
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cerrarVentana() {
		setVisible(false);
		dispose();
		System.exit(0);
	}

	public static void setInstance(Ventana instance) {
		Ventana.instance = instance;
	}

	public static Ventana getInstance() {
		return instance;
	}

}
