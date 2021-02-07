import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//DESCRIPTION: intended to kill mario when collided. BASICALLY NOT FUNCTIONAL
public class Enemy extends abstractObjects {
	private Image enemy1;
	private Image enemy2;
	private int frame = 0;
	
	private int leftRightMove = -1;
	
	
	public Enemy (int x, int y) {
		width = 30;
		height = 30;
		xVal = 300+x;	//screenwidth 600 - 300(mario at halfpoint causes scroll) = 300. this is added to x (creation val) to start the creation off screen
		yVal = y;
		
		try {
			enemy1 = ImageIO.read(new File("e1.jpg"));
			enemy1 = enemy1.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			enemy2 = ImageIO.read(new File("e2.jpg"));
			enemy2 = enemy2.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			
			
		} catch (IOException e) {
			System.out.print("IOException");
		}
	}
	
	//send which image to be animated
	public Image getImage () {
		frame = (frame + 1) % 6; //mod images length * 3
		if (frame <3)
			return enemy1;
		else return enemy2;
	}

	//lets enemy move
	public void move () {
		if (leftRightMove ==-1)
			xVal-=2; 
		else if (leftRightMove ==1)
			xVal+=2;
	}
	
	//not implemented
//	public void changeDirection() {
//		leftRightMove*=-1;
//	}

	
	
}
