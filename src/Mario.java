import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ListIterator;

import javax.imageio.ImageIO;

//DESCRIPTION: creates a mario character/object. only one, therefore variables are all static. 
//this class controls his movements and his animations
//non functional: horizontal collision wtih objects
public class Mario extends abstractObjects {
	private static final int groundHeight = 325; // 400-75, where to draw him
	private static int yVal = groundHeight;
	private static int xVal = 150;
	private static int height = 30;
	private static int width = 30;
	private static int leftRightMoving = 0;
	private static int leftRightFacing = 1;
	private static boolean startJump = false;
	private static boolean startFall = false;
	private static double speed = 0; // the longer held down, the faster mario moves horizontally (to an extent)

	// NOT FUNTIONAL private int collidedWithObject = 0;
	// NOT IMPLEMENTED private int score = 0;

	private static int lastMaxHeight = groundHeight; // helps set his max jump

	// graphics for mario's animations
	private int frame = 0;
	private Image img[] = new Image[4];
	private Image jump;
	private Map map = new Map(this);

	public Mario() {
		// INITALIZE THE GRAPHICS
		String s = "";
		for (int i = 0; i < 4; i++) {
			s = "" + (i + 1);
			try {
				img[i] = ImageIO.read(new File(s + ".jpg"));
				img[i] = img[i].getScaledInstance(width, height, Image.SCALE_DEFAULT);
			} catch (IOException e) {
				System.out.print("IOException");
			}
		}

		try {
			jump = ImageIO.read(new File("jump.jpg"));
			jump = jump.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			System.out.print("IOException");
		}

	}

	// MOST IMPORTANT METHOD:
	// PARAMETERS: Takes in the offscreenbuffer so it can draw on an offscreen image
	// DESCRIPTION: decides Mario's movement and animation
	// NOTE: very messy code, didn't separate movement calculations from animation
	// or collision
	public void draw(Graphics offScreenBuffer) {

		// draw background first, mario is drawn on top of it.
		map.draw(offScreenBuffer);

		// moving right (no jumping/falling)
		if (leftRightMoving == 1 && startJump == false && startFall == false) {

			// accelerate a little bit
			if (speed < 3) {
				speed += 0.25;
			}

			// if moving to the right, do not go past 3/4 of the screen
			if (xVal < 300) {
				xVal += 3 + speed;
			} else {
				// everything besides mario looks like it's moving to the left so it looks like
				// mario is moving to the right
				map.scrollUpdate(3 + speed);
			}

			// calculate frames; formula was gotten online -
			// https://edux.pjwstk.edu.pl/mat/268/lec/lect6/lecture6.htmls
			offScreenBuffer.drawImage(img[frame], xVal, yVal, null);
			frame = (frame + 1) % img.length;

			// moving left (no jump, no fall)
		} else if (leftRightMoving == -1 && startJump == false && startFall == false) {
			// accelerate
			if (speed < 3) {
				speed += 0.25;
			}
			// do not go offscreen left
			if (xVal >= 0) {
				xVal -= (3 + speed);
			}

			// formula gotten online, flips image horizontally
			offScreenBuffer.drawImage(img[frame], xVal + width, yVal, -width, height, null);
			frame = (frame + 1) % img.length;

			// JUMPING
		} else if (startJump == true && startFall == false) {
// BUG: it keeps calling this??? MORE THAN ONCE PER KEYPRESSED

			// JUMP UP to 5times his height
			if (yVal > lastMaxHeight - (30 * 5)) {// NOT FUNCTIONA: && collidedWithObject == false) {
				yVal -= 15;
			} else { // if hits that height, fall
				startJump = false;
				startFall = true;
			}

			// jumping and moving at the same time
			drawJumpFall(offScreenBuffer);

			// falling
		} else if (startFall == true) {
			// Not Functional/not implemented: minimum jump -> mario jumps at least a
			// certain height

			// fall until reach ground heights
			if (yVal < groundHeight)
				yVal += 5;
			else {
				startFall = false;
				startJump = false;
			}
			drawJumpFall(offScreenBuffer);

		}

		// facing only, not moving, reset acceleration to 0;
		else if (leftRightFacing == 1) {
			speed = 0;
			offScreenBuffer.drawImage(img[0], xVal, yVal, null);
		} else if (leftRightFacing == -1) {
			speed = 0;
			offScreenBuffer.drawImage(img[0], xVal + width, yVal, -width, height, null);
		}

		try {
			Thread.sleep(40);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// DESCRIPTION: controls jump/fall for right side and left side outofbounds and
	// animates the jumping
	public void drawJumpFall(Graphics offScreenBuffer) {
		if (leftRightFacing == 1) {
			offScreenBuffer.drawImage(jump, xVal, yVal, null);
		} else if (leftRightFacing == -1) {
			offScreenBuffer.drawImage(jump, xVal + width, yVal, -width, height, null);
		} else if (leftRightMoving == 1) {
			if (xVal <= 300)
				xVal += 5;
			else {// if at 3/4 of the screen, assumes running at full speed
				map.scrollUpdate(6);
			}
			offScreenBuffer.drawImage(jump, xVal, yVal, null);
		} else if (leftRightMoving == -1) {
			if (xVal > 0)
				xVal -= 5;
			offScreenBuffer.drawImage(jump, xVal + width, yVal, -width, height, null);
		}
	}

	// DESCRIPTION: called in keyListeners in the gamePanel class -> in order to
	// decide what movement and therefore what animation frame to use
	// two values, respectively decide movementYes (right, left, or no movement) AND
	// decides if
	// movementStationary (not stationary, facing left, facing right)
	public void setDirection(int moving, int direction) {
		leftRightMoving = moving;
		leftRightFacing = direction;
	}

	// gets the YValueheight of the last brick (mario jumped onto), to set a max
	// height mario can jump to.
	// up to (5 times his size)
	public void setLastMaxHeight(int oT) {
		lastMaxHeight = oT;
	}

	// other getters and setters, used by Map class mostly but also the gamePanel
	// class/key listeners
	public void setJump(boolean x) {
		startJump = x;
	}

	public boolean getJump() {
		return startJump;
	}

	public void setFall(boolean b) {
		startFall = b;
	}

	public boolean getFall() {
		return startFall;
	}

	// other getters and setters:
	// WEIRD? all of these methods are inherited from the superclass but for some
	// reason, i am required
	// to override them????? ->
	public int getYVal() {
		return yVal;
	}

	public int getXVal() {
		return xVal;
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	// NOT FUNCTIONAL
//	public void setCollided(int x) {
//		collidedWithObject = x;
//	}

	// NOT IMPLEMENTED
//	public int getScore () {
//		return score;
//	}
//	public void updateScore() {
//		score+=100;
//		
//	}

}
