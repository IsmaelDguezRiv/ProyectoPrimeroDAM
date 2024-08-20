package gameObjects;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import math.Vector2D;
import states.GameState;

public abstract class MovingObject extends GameObject {

	protected Vector2D velocity;
	protected AffineTransform at;
	protected double angle;
	protected double maxVel;
	protected double width;
	protected double height;
	protected GameState gamestate;

	public MovingObject(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture,
			GameState gamestate) {
		super(position, texture);
		this.velocity = velocity;
		this.maxVel = maxVel;
		this.gamestate = gamestate;
		width = texture.getWidth();
		height = texture.getHeight();
		angle = 0;
	}

	protected void colliseWith() {
		
		ArrayList<MovingObject> movingObjects = gamestate.getMovingObjects();
		
		for(int i = 0; i < movingObjects.size();i++) {
			
			MovingObject m = movingObjects.get(i);
			//comprobar que no colisione consigo mismo, cada objeto tiene su colsion.
			if (m.equals(this))
				continue;   
			
			double distance = m.getCenter().substract(getCenter()).getMagnitude(); 
			
			if(distance < m.width/2 + width/2 && movingObjects.contains(this)) {
				objectColission(m, this);
			}
		}
	}
	private void objectColission(MovingObject a,MovingObject b) {
		if (a instanceof Player && ((Player)a).isSpawning()) {
			return;
		}
		if (b instanceof Player && ((Player)b).isSpawning()) {
			return;
		}
		if(!(a instanceof Meteor && b instanceof Meteor)) {
			gamestate.playExplosion(getCenter());
			a.destroyed();
			b.destroyed();
		}

	}
	
	protected void destroyed() {
		gamestate.getMovingObjects().remove(this);
	}
	protected Vector2D getCenter() {
		return new Vector2D(position.getX() + width / 2, position.getY() + height / 2);
	}

}
