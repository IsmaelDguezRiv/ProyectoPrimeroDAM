package gameObjects;

import graphics.Assets;
import math.Vector2D;

public class Constants {
	// frame dimensions
	public static final int DEF_ANCHO = 816;
	public static final int DEF_LARGO = 638;
	public static final int DEF_MAXANCHOJUGABLE = 748;
	public static final int DEF_MAXLARGOJUGABLE = 548;
	public static final int DEF_MINANCHOJUGABLE = 200;
	public static final int DEF_MINLARGOJUGABLE = 20;
	// player properties
	public static final double WIDTH_NAVE = Assets.player.getWidth();
	public static final double HEIGHT_NAVE = Assets.player.getHeight();
	public static final int FIRERATE = 200;
	public static final double DELTAANGLE = 0.12;
	public static final double ACC = 0.06;
	public static final double PLAYER_MAX_VEL = 5;
	public static final long FLICKER_TIME = 200;
	public static final long SPWANING_TIME = 3000;
	public static final Vector2D PLAYER_POS_INIT = new Vector2D(Constants.DEF_MAXANCHOJUGABLE / 2 + 100, Constants.DEF_MAXLARGOJUGABLE / 2);
	// laser properties
	public static final double LASER_MAX_VEL = 15;
	//Meteor properties
	public static final double METEOR_MAX_VEL = 2;
	public static final int METEOR_SCORE = 10;
	//ufo properties
	public static final int NODE_RADIUS = 160;
	public static final double UFO_MASS = 60;
	public static final double UFO_ANGLE = 0.05;
	public static final double UFO_MAX_VEL = 3;
	public static final long UFO_FIRE_RATE = 2000;
	public static final double UFO_ANGLE_RANGE =Math.PI/2;
	public static final int UFO_SCORE = 40;
	//bot
	public static final long BOT_MAX_TIME = 8000;
	public static final long BOT_MIN_TIME = 6000;
}
