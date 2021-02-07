import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//description: bricks when collided with will stop mario's movement + popped bricks = score
public class Brick extends abstractObjects {
	private Image brick;
	boolean collided = false;
	//int typeOfBlock

	public Brick(int x, int y) {
		
		width = 30;
		height = 30;
		xVal = 300+x;		//screenwidth 600 - 300(mario at halfpoint causes scroll) = 300. this is added to x (creation val) to start the creation off screen
		yVal = y;
		
		try {
			brick = ImageIO.read(new File("brick.jpg"));
			brick = brick.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			System.out.print("IOException");
		}
	}
	
	public Image getImage () {
		return brick;
	}
	
	

}
