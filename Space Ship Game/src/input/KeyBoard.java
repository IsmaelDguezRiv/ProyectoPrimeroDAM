package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Ventana;

public class KeyBoard implements KeyListener {

	private boolean[] keys = new boolean[256];

	public static boolean UP, LEFT, RIGHT, SHOOT, SCP;

	public KeyBoard() {
		UP = false;
		LEFT = false;
		RIGHT = false;
		SHOOT = false;
		SCP = false;
	}

	public void update() {
		UP = keys[KeyEvent.VK_UP];
		LEFT = keys[KeyEvent.VK_LEFT];
		RIGHT = keys[KeyEvent.VK_RIGHT];
		SHOOT = keys[KeyEvent.VK_S];
		SCP = keys[KeyEvent.VK_ESCAPE];
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	

}
