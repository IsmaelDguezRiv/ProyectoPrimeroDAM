package gameObjects;

import java.awt.Dialog.ModalityType;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import input.KeyBoard;
import main.Ventana;
import math.Vector2D;
import states.GameState;

public class Bot extends MovingObject {


	public Bot(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gamestate) {
		super(position, velocity, maxVel, texture, gamestate);
	}

	public void update() {

		}



	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		at.rotate(angle, width / 2, height / 2);

		g2d.drawImage(texture, at, null);
	}
	
}
