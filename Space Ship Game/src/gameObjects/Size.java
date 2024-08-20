package gameObjects;

import java.awt.image.BufferedImage;

import graphics.Assets;

public enum Size {

	BIG(2,Assets.meds),MED(4,Assets.smalls),SMALL(0,null);
	
	public int quantity;
	
	public BufferedImage[] textures;
	
	private Size(int quantity, BufferedImage[]textures) {
		this.quantity = quantity;
		this.textures = textures;
	}
}
