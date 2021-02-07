/*
 * DESCRIPTION: shows the game/main screen + begins the game
 */
import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial") // funky warning, just suppress it. It's not gonna do anything.
public class GamePanel extends JPanel implements Runnable, KeyListener {

	int FPS = 60;
	Thread thread;
	int screenWidth = 600;
	int screenHeight = 400;
	
	boolean gameBegin = false;	//start condition -> if game was fully functional, would be a method to set it false
	Mario m = new Mario();		//main character object

	Graphics offScreenBuffer = null;	//buffered image -> draws image off screen and then replaces each on screen image with the offscreen to reduce flicker
	Image offScreenImage;
	static JFrame frame = new JFrame("GAME");
	

//constructor sets up jpanel + puts it onto the frame + begins the thread
	public GamePanel() {

		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setVisible(true);

		thread = new Thread(this);
		thread.start();
		
		
		frame.add(this);
		frame.addKeyListener(this);
		frame.setVisible(true);
		frame.pack();

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

	}

	//thread's run method
	//DESCRIPTION: begins the game
	public void run() {
		while (gameBegin= true) { //intentional, as code is not complete
			update();
			try {
				Thread.sleep(1000 / FPS);
			} 
			catch (InterruptedException e) {
			}
			
		}
		//NOT IMPLEMENTED -> when ended game, was supposed to forward score back to start
		//begin.addScore();
		//frame.removeAll();
	}


	public void update() {
		// update stuff
		repaint();
		//NOT IMPLEMENTED m.getScore();	//score would then be shown in the paintComoponent with a jTextLabel
	}

	//draws stuff
	public void paintComponent(Graphics g) {
		//links Graphics buffer and Image offscreen
		if (offScreenBuffer == null) {
			offScreenImage = createImage(this.getWidth(), this.getHeight());
			offScreenBuffer = offScreenImage.getGraphics();
		}
		//must clear
		offScreenBuffer.clearRect(0, 0, this.getWidth(), this.getHeight()); 
		
		//animates the Mario character (which also causes the map to be drawn).
		//must give it the offscreenbuffer/graphics to draw; that draws it on the offscreenimage
		m.draw(offScreenBuffer);
		g.drawImage(offScreenImage, 0, 0, this);
		

	}

	
	/*
	 * KEYBOARD BUGGING?
	 * - sometimes delay in keyboard movement
	 * - if rapid key movement,
	 * sometimes sends the wrong keycode (ex. you might be pressing left and it won't register or moves right instead)
	 * - Tried to make big jumps and small jumps but the keyboard issue was making this impossible // i checked by flagging the method in Mario class and the actionListener
	 */
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			m.setDirection(1, 0); //moving right, not stationary
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			m.setDirection(-1, 0); //moving left, not stationary
		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {	//only let jump once if already jumping
			if (m.getJump() == false)
				m.setJump(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			m.setDirection(0, 1); // moving no, face right
		else if (e.getKeyCode() == KeyEvent.VK_LEFT)
			m.setDirection(0, -1); //moving no, face left

		// NOT FUNCTIONAL: make mininum short jumps
		//if no longer jumping, then is falling
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			m.setJump(false);
			m.setFall(true);

		}
	}




}

