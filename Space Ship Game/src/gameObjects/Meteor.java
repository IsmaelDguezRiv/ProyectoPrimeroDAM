package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Assets;
import math.Vector2D;
import states.GameState;

public class Meteor extends MovingObject {
	
	private Size size;

	public Meteor(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gamestate, Size size) {
		super(position, velocity, maxVel, texture, gamestate);
		this.size = size;
		this.velocity = velocity.scale(maxVel);
	}

	@Override
	public void update() {
		position = position.add(velocity);
		
		if (position.getX() > Constants.DEF_MAXANCHOJUGABLE)
			position.setX(Constants.DEF_MINANCHOJUGABLE);
		if (position.getY() > Constants.DEF_MAXLARGOJUGABLE)
			position.setY(Constants.DEF_MINLARGOJUGABLE);
		if (position.getX() < Constants.DEF_MINANCHOJUGABLE)
			position.setX(Constants.DEF_MAXANCHOJUGABLE);
		if (position.getY() < Constants.DEF_MINLARGOJUGABLE)
			position.setY(Constants.DEF_MAXLARGOJUGABLE);
		
		angle += Constants.DELTAANGLE/4;
	}
	@Override
	public void destroyed() {
		
		gamestate.divideMeteor(this);
		gamestate.addScore(Constants.METEOR_SCORE);
		super.destroyed();
		
	}
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g; 
		
		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		at.rotate(angle, width / 2, height / 2);

		g2d.drawImage(texture, at, null);
	}
	
	public Size getSize()
	{
		return size;
	}
}
