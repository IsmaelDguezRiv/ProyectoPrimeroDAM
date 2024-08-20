package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Assets;
import math.Vector2D;
import states.GameState;

public class Laser extends MovingObject {

	public Laser(Vector2D position, Vector2D velocity, double maxVel,double angle, BufferedImage texture,GameState gamestate) {
		super(position, velocity, maxVel, texture, gamestate);
		this.angle = angle;
		this.velocity = velocity.scale(maxVel);
	}

	@Override
	public void update() {
	position = position.add(velocity);
	if (position.getX() > Constants.DEF_MAXANCHOJUGABLE + Constants.WIDTH_NAVE ||position.getY() > Constants.DEF_MAXLARGOJUGABLE + Constants.HEIGHT_NAVE
	|| position.getX() < Constants.DEF_MINANCHOJUGABLE || position.getY() < Constants.DEF_MINLARGOJUGABLE) {
	destroyed();
	}
	colliseWith();
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		at = AffineTransform.getTranslateInstance(position.getX()-width/2, position.getY());
		at.rotate(angle,width/2,0);

		g2d.drawImage(Assets.laser, at, null);
		
	}
	@Override
	public Vector2D getCenter() {
		return new Vector2D(position.getX() + width / 2, position.getY() + width / 2);
	}

}
