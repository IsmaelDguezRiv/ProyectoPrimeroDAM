package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.Assets;
import math.Vector2D;
import states.GameState;

public class Ufo extends MovingObject {

	private ArrayList<Vector2D> path;

	private Vector2D currentNode;

	private int index;

	private boolean following;
	
	private Chronometer fireRate;

	public Ufo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gamestate,
			ArrayList<Vector2D> path) {
		super(position, velocity, maxVel, texture, gamestate);
		this.path = path;
		index = 0;
		following = true;
		fireRate = new Chronometer();
		fireRate.run(Constants.UFO_FIRE_RATE);
	}

	private Vector2D pathFollowing() {
		currentNode = path.get(index);

		double distanceToNode = currentNode.substract(getCenter()).getMagnitude();

		if (distanceToNode < Constants.NODE_RADIUS) {
			index++;
			if (index >= path.size()) {
				following = false;
			}
		}
		return seekForce(currentNode);
	}

	private Vector2D seekForce(Vector2D target) {
		Vector2D desireVelocity = target.substract(getCenter());
		desireVelocity = desireVelocity.normalize().scale(maxVel);
		return desireVelocity.substract(velocity);
	}

	@Override
	public void update() {
		Vector2D pathFollowing;
		if (following)
			pathFollowing = pathFollowing();
		else
			pathFollowing = new Vector2D();

		pathFollowing = pathFollowing.scale(1 / Constants.UFO_MASS);

		velocity = velocity.add(pathFollowing);

		velocity = velocity.limit(maxVel);

		position = position.add(velocity);
		if (position.getX() > Constants.DEF_MAXANCHOJUGABLE || position.getY() > Constants.DEF_MAXLARGOJUGABLE
				|| position.getX() < Constants.DEF_MINANCHOJUGABLE || position.getY() < Constants.DEF_MINLARGOJUGABLE) {
			destroyed();
		}
			
			
			if(!fireRate.isRunning()) {
				Vector2D toPlayer = gamestate.getPlayer().getCenter().substract(getCenter());
				
				toPlayer = toPlayer.normalize();
				
				double currentAngle = toPlayer.getAngle();
				
				currentAngle += Math.random()*Constants.UFO_ANGLE_RANGE - Constants.UFO_ANGLE_RANGE /2;
				
				if(toPlayer.getX() <0) {
					currentAngle = -currentAngle + Math.PI;
				}
				
				toPlayer = toPlayer.setDirection(currentAngle);
				
				Laser laser = new Laser(getCenter().add(toPlayer.scale(width)), toPlayer, Constants.LASER_MAX_VEL, currentAngle+Math.PI/2, Assets.laser, gamestate);
			
				gamestate.getMovingObjects().add(0,laser);
				
				fireRate.run(Constants.UFO_FIRE_RATE);
				
			}
			angle += Constants.UFO_ANGLE;
			
			colliseWith();
			
			fireRate.update();

	}
	@Override
	public void destroyed() {
		gamestate.addScore(Constants.UFO_SCORE);
		super.destroyed();
	}
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		at.rotate(angle, width / 2, height / 2);

		g2d.drawImage(texture, at, null);
		
		g.setColor(Color.RED);
		
		/*for(int i = 0;i<path.size();i++) {
			g.drawRect((int)path.get(i).getX(), (int)path.get(i).getY(), 5, 5);
		}*/
	}

}
