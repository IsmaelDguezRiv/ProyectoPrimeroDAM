package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import graphics.Assets;
import states.GameState;


public class JOptionCondition {

	 private static String[] phrases = new String[2];
	 public static void mostrarMensaje(int condition) {
		 phrases[0] = "HAS PERDIDO";
		 phrases[1] = "HAS GANADO";
		 if (condition ==0)
	        JOptionPane.showMessageDialog(null , phrases[condition] , "ERROR 404", JOptionPane.PLAIN_MESSAGE, (Icon) new ImageIcon(Assets.icon[1]));
		 else 
			 JOptionPane.showMessageDialog(null , phrases[condition],  "I´M FREE", JOptionPane.PLAIN_MESSAGE, (Icon) new ImageIcon(Assets.icon[0]));
	    }
	 public static int messageTypes(GameState gamestate) {
		 int condition =-1;
		 if (gamestate.getMeteors()>4)
			 condition=  0;
		 if (gamestate.getMeteors()<4 && gamestate.getLives()<1)
			 condition=  1;
		return condition;
	 }
	
}
