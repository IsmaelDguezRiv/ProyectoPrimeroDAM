package states;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

import gameObjects.Background;
import gameObjects.Bot;
import gameObjects.Constants;
import gameObjects.Meteor;
import gameObjects.MovingObject;
import gameObjects.Player;
import gameObjects.Size;
import gameObjects.Ufo;
import graphics.Animation;
import graphics.Assets;
import math.Vector2D;

public class GameState {

	private Player player;
	private Background background;
	private ArrayList<MovingObject> movingObjects = new ArrayList<MovingObject>();
	private ArrayList<Animation> explosions = new ArrayList<Animation>();
	private ArrayList<Animation> cont = new ArrayList<Animation>();
	
	public static int score =0;
	
	private int lives =3;

	private int meteors;

	public GameState() {
		player = new Player(Constants.PLAYER_POS_INIT,
				new Vector2D(), Constants.PLAYER_MAX_VEL, Assets.player, this);
		movingObjects.add(player);
		background = new Background(new Vector2D(), Assets.background);

		meteors = 1;

		startWave();
	
	}
	
	public void addScore(int value) {
		score += value;
	}
	public void divideMeteor(Meteor meteor) {
		Size size = meteor.getSize();

		BufferedImage[] textures = size.textures;

		Size newSize = null;

		switch (size) {
		case BIG:
			newSize = Size.MED;
			break;
		case MED:
			newSize = Size.SMALL;
			break;
		default:
			return;
		}

		for (int i = 0; i < size.quantity; i++) {
			movingObjects
					.add(new Meteor(meteor.getPosition(), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2),
							Constants.METEOR_MAX_VEL * Math.random() + 1,
							textures[(int) (Math.random() * textures.length)], this, newSize));
		}
	}

	private void startWave() {
		double x, y;

		for (int i = 0; i < meteors; i++) {

			x = i % 2 == 0
					? Math.random() * (Constants.DEF_MAXANCHOJUGABLE - Constants.DEF_MINANCHOJUGABLE + 1)
							+ Constants.DEF_MINANCHOJUGABLE
					: Constants.DEF_MINANCHOJUGABLE;
			y = i % 2 == 0 ? Constants.DEF_MINLARGOJUGABLE
					: Math.random() * (Constants.DEF_MAXLARGOJUGABLE - Constants.DEF_MINLARGOJUGABLE + 1)
							+ Constants.DEF_MINLARGOJUGABLE;

			BufferedImage texture = Assets.bigs[(int) (Math.random() * Assets.bigs.length)];

			movingObjects
					.add(new Meteor(new Vector2D(x, y), new Vector2D(0, 1).setDirection(Math.random() * Math.PI * 2),
							Constants.METEOR_MAX_VEL * Math.random() + 1, texture, this, Size.BIG));
		}
		meteors++;
		spawnUfo();
		spawnBot();
	}

	private void spawnUfo() {

		int rand = (int) (Math.random() * 2);
		double x, y;
		if (rand == 0) {
			x = Math.random() * (Constants.DEF_MAXANCHOJUGABLE - Constants.DEF_MINANCHOJUGABLE + 1)
					+ Constants.DEF_MINANCHOJUGABLE;
			y = Constants.DEF_MINLARGOJUGABLE+1;
		} else {
			x = Constants.DEF_MINANCHOJUGABLE+1;
			y = Math.random() * (Constants.DEF_MAXLARGOJUGABLE - Constants.DEF_MINLARGOJUGABLE + 1)
					+ Constants.DEF_MINLARGOJUGABLE;
		}

		ArrayList<Vector2D> path = new ArrayList<Vector2D>();

		double posX, posY;
		// Cuadrante superior izquierdo jugable
		posX = Math.random() * (Constants.DEF_MAXANCHOJUGABLE - Constants.DEF_MINANCHOJUGABLE) * 0.5
				+ Constants.DEF_MINANCHOJUGABLE;
		posY = Math.random() * (Constants.DEF_MAXLARGOJUGABLE - Constants.DEF_MINLARGOJUGABLE) * 0.5
				+ Constants.DEF_MINLARGOJUGABLE;
		path.add(new Vector2D(posX, posY));

		// Cuadrante superior derecho jugable
		posX = Math.random() * (Constants.DEF_MAXANCHOJUGABLE - Constants.DEF_MINANCHOJUGABLE) * 0.5
				+ (Constants.DEF_MAXANCHOJUGABLE + Constants.DEF_MINANCHOJUGABLE) * 0.5;
		posY = Math.random() * (Constants.DEF_MAXLARGOJUGABLE - Constants.DEF_MINLARGOJUGABLE) * 0.5
				+ Constants.DEF_MINLARGOJUGABLE;
		path.add(new Vector2D(posX, posY));

		// Cuadrante inferior izquierdo jugable
		posX = Math.random() * (Constants.DEF_MAXANCHOJUGABLE - Constants.DEF_MINANCHOJUGABLE) * 0.5
				+ Constants.DEF_MINANCHOJUGABLE;
		posY = Math.random() * (Constants.DEF_MAXLARGOJUGABLE - Constants.DEF_MINLARGOJUGABLE) * 0.5
				+ (Constants.DEF_MAXLARGOJUGABLE + Constants.DEF_MINLARGOJUGABLE) * 0.5;
		path.add(new Vector2D(posX, posY));

		// Cuadrante inferior derecho jugable
		posX = Math.random() * (Constants.DEF_MAXANCHOJUGABLE - Constants.DEF_MINANCHOJUGABLE) * 0.5
				+ (Constants.DEF_MAXANCHOJUGABLE + Constants.DEF_MINANCHOJUGABLE) * 0.5;
		posY = Math.random() * (Constants.DEF_MAXLARGOJUGABLE - Constants.DEF_MINLARGOJUGABLE) * 0.5
				+ (Constants.DEF_MAXLARGOJUGABLE + Constants.DEF_MINLARGOJUGABLE) * 0.5;
		path.add(new Vector2D(posX, posY));

		movingObjects.add(new Ufo(new Vector2D(x, y), new Vector2D(), Constants.UFO_MAX_VEL, Assets.ufo, this, path));
	}

	private void spawnBot() {
		movingObjects.add(new Bot(new Vector2D(20, 20), new Vector2D(), Constants.UFO_MAX_VEL, Assets.bots[meteors-2], this));
		
	}
	public void playExplosion(Vector2D position) {
		explosions.add(new Animation(Assets.exp, 1,
				position.substract(new Vector2D(Assets.exp[0].getWidth() / 2, Assets.exp[0].getHeight() / 2))));
	}

	public void update() {
		for (int i = 0; i < movingObjects.size(); i++) {
			movingObjects.get(i).update();
		}
		for (int i = 0; i < explosions.size(); i++) {
			Animation anim = explosions.get(i);
			anim.update();
			if (!anim.isRunning()) {
				explosions.remove(i);
			}
		}
		for (int i = 0; i < cont.size(); i++) {
		    Animation anim = cont.get(i);
		    anim.update();
		    if (!anim.isRunning()) {
		        cont.remove(i);
		    }
		}
		for (int i = 0; i < movingObjects.size(); i++) {
			if (movingObjects.get(i) instanceof Meteor)
				return;
		}

		startWave();
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		/*
		 * g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		 * RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		 */

		background.draw(g);
		for (int i = 0; i < movingObjects.size(); i++) {
			movingObjects.get(i).draw(g);
		}
		for (int i = 0; i < explosions.size(); i++) {
			Animation anim = explosions.get(i);
			g2d.drawImage(anim.getCurrentFrame(), (int) anim.getPosition().getX(), (int) anim.getPosition().getY(),
					null);
		}
		drawScore(g);
		drawLive(g);

	}
	
	private void drawScore(Graphics g) {
		Vector2D pos = new Vector2D(160,290);
		
		String scoreToString = Integer.toString(score);
		
		for(int i = scoreToString.length(); i>0 ;i--) {
			
			g.drawImage(Assets.nums[Integer.parseInt(scoreToString.substring(i-1,i))], (int)pos.getX(), (int)pos.getY(),null);
			pos.setX(pos.getX()-12);
		}
	}
	private void drawLive(Graphics g) {
		Vector2D pos = new Vector2D(80, 480);
		
		 if (cont.isEmpty()) {
		        cont.add(new Animation(Assets.lives, 1, pos));
		    }
		for (int i = 0; i < lives; i++) {
				Animation anim = cont.get(0);
				g.drawImage(anim.getCurrentFrame(), (int) pos.getX(), (int) pos.getY(), null);
			pos.setY(pos.getY() - 30);
			if (!anim.isRunning()) {
		        anim.reset();
		    }
		}
		
	}
	
	

	public ArrayList<MovingObject> getMovingObjects() {
		return movingObjects;
	}

	public Player getPlayer() {
		return player;
	}
	
	public int getMeteors() {
		return meteors;
	}
	
	public int getLives() {
		return lives;
	}
	
	

	public void substrackLife() {
		lives--;
	}
	
	

}
