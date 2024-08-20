package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Assets;
import math.Vector2D;

public class Background extends GameObject{

	public Background(Vector2D position, BufferedImage texture) {
		super(position, texture);
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(Assets.background, 0,0, null);
	}
	
}
