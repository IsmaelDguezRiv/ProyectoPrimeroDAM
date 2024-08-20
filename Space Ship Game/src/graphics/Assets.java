package graphics;

import java.awt.image.BufferedImage;

import gameObjects.Background;

public class Assets {
	//player
	public static BufferedImage player;
	//ufo
	public static BufferedImage ufo;
	//fondo pantalla
	public static BufferedImage background;
	//efectos
	public static BufferedImage speed;
	//explosion
	public static BufferedImage[] exp = new BufferedImage[4];
	//laser
	public static BufferedImage laser;
	//meteoritos
	public static BufferedImage[]bigs = new BufferedImage[2];
	public static BufferedImage[]meds = new BufferedImage[2];
	public static BufferedImage[]smalls = new BufferedImage[2];
	//numeros
	public static BufferedImage[]nums = new BufferedImage[10];
	//vidas
	public static BufferedImage []lives  = new BufferedImage[20];
	//bot
	public static BufferedImage[]bots = new BufferedImage[4];
	//icon
	public static BufferedImage icon[] =  new BufferedImage[2];
	
	public static void init() {
		background = Loader.ImageLoader("/background.png");
		player = Loader.ImageLoader("/playerShip.png");
		speed = Loader.ImageLoader("/speed.png");
		laser = Loader.ImageLoader("/laser.png");
		ufo = Loader.ImageLoader("/ufo.png");
		for(int i = 0; i< icon.length ; i++) {
			icon [i] = Loader.ImageLoader("/icon"+(i+1)+".png");
		}
		for(int i = 0; i< lives.length ; i++) {
			lives [i] = Loader.ImageLoader("/cont/cont"+(i+1)+".png");
		}
		
		for(int i = 0; i<lives.length ; i++) {
			lives [i] = Loader.ImageLoader("/cont/cont"+(i+1)+".png");
			}
		for(int i = 0; i< bigs.length ; i++) {
			bigs [i] = Loader.ImageLoader("/meteor/big"+(i+1)+".png");
			meds [i] = Loader.ImageLoader("/meteor/med"+(i+1)+".png");
			smalls [i] = Loader.ImageLoader("/meteor/small"+(i+1)+".png");
		}
		for(int i = 0; i< exp.length ; i++) {
			exp [i] = Loader.ImageLoader("/explosion/fire"+(i+1)+".png");
		}
		for(int i = 0; i< bots.length ; i++) {
			bots [i] = Loader.ImageLoader("/faces/cara"+(i+1)+".png");
		}
		for(int i = 0; i< nums.length ; i++) {
			nums [i] = Loader.ImageLoader("/number/num"+(i)+".png");
		}
	}
}

