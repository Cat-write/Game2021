//DESCRIPTION: creates the map which paints the background and the enemy/brick objects
//MAP MUST GET XVAL OF MARIO. if mario reaches a certain point, the map triggers the painting/creation of object
//also checks collision between mario and the objects, prevents mario from moving

//note: biggest non functional is horizontal collision
//note: enemies has basically no functionality; i focused on trying to get brick to work first

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

import javax.imageio.ImageIO;

public class Map {

	// creates the map/generates enemies
	// QUEUE FIFO
	Queue<Integer> triggerPoints = new LinkedList<Integer>(); // if reached this point (in the x-axis)
	Queue<Integer> objectInitialFields = new LinkedList<Integer>(); // send these values to create an object
	int triggerPointInt = 300;

	// enemies:
	// arraylist bad for deletion
	public LinkedList<Brick> bricks = new LinkedList<Brick>();
	ListIterator<Brick> iter = bricks.listIterator();
	LinkedList<Enemy> enemies = new LinkedList<Enemy>();

	// scroll variables
	private double scrollVal;
	private double maxX = 300;	//tracks how far Mario has "gone" to the right, if the screen was endless
	private Image background; // move background to the image
	private double xBG = 0; // help background wrap around

	// link to Mario so methods can be accessed for collision
	Mario m;

	public Map(Mario m) {
		this.m = m;

		// initialize graphics
		try {
			background = ImageIO.read(new File("background.jpg"));
			background = background.getScaledInstance(600, 400, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			System.out.print("IOException");
		}

		// Initialize the xPositions that (TRIGGER the object creation)
		// intervals of 30 for bricks to be side by side? in theory ??
		int create[] = { 330, 360, 350, 380, 420, 420, 450, 500, 530, 560, 590}; // 11
		for (Integer i : create) {
			triggerPoints.add(i);
		}

		// creates an object with 2 values
		// yVal, type; let 1= enemy, 0 = brick etc.
		int objectsCreate[] = { 1, 325, 0, 325, 0, 290, 0, 255, 0, 290, 0, 200, 0, 290, 0, 255, 0,255,0, 255, 0, 255 }; // 11
		for (Integer i : objectsCreate) {
			objectInitialFields.add(i);
		}

		/*
		 * USELESS MATH: small jump = 3*30 (3 times his height) blocks tend to be on the
		 * 4th big jump = 5*30 jump should be a little higher than 5th block so he can
		 * clear the 4th block easily.
		 * 
		 * 
		 * NEXT TIME: use a text based 2d array map, that reads in as the next value
		 * when scroll reaches a certain value -> this is too slow. because enemies
		 * move, this would also prevent creating objects on top of another (check if
		 * anything is there before you create an object)
		 *
		 */
	}

	// Mario calls and this draws the enemies + watches for collisions
	// parameters: same as mario

	public void draw(Graphics offScreenBuffer) {
		// two backgrounds that wrap around
		offScreenBuffer.drawImage(background, (int) xBG, 0, null);
		offScreenBuffer.drawImage(background, (int) xBG + 600, 0, null);
		if (xBG < -600)
			xBG = 0;
		// when mario has moved to the right, 50% of the screen, background + bricks
		// move

		// enemies: not really functional
		if (enemies.isEmpty() == false) {
			for (Enemy e : enemies) {
				e.move();
				// not functional: if check collision = true, changeDirection*. if
				// multithreading put collisions in the thread and update constantly?
				offScreenBuffer.drawImage(e.getImage(), e.getXVal(), e.getYVal(), null);
			}
		}

		// bricks, use iterator because removable/adding concurrently
		iter = bricks.listIterator();
		while (iter.hasNext()) {
			Brick b = iter.next();
			// check collision, 0 is to identify as brick -> which was supposed to help
			// identity when to addscore
			collide(b, 0);
			offScreenBuffer.drawImage(b.getImage(), b.getXVal(), b.getYVal(), null);
		}

	}

	private void collide(abstractObjects o, int x) {

		// how to read the variables:
		// first letter = object, second letter = direction [left, right, top, bottom]
		int mL = m.getXVal();
		int mR = m.getXVal() + m.getWidth();
		int mT = m.getYVal();
		int mB = m.getYVal() + m.getHeight();

		int oL = o.getXVal();
		int oR = o.getXVal() + o.getWidth();
		int oT = o.getYVal();
		int oB = o.getYVal() + o.getHeight();

		// NOT FUNTIONAL
		// NOT FUNTIONAL
		// NOT FUNTIONAL
		// collide from Left and right do not work:/
//		if ((mL < oL && mR >= oL) && (mT < oB && mB > oT)) {
//			
//			// a) movingLeftRight Collided
//			// b) if jump//fall -> should fall directly down;
//
//			if (m.getJump() == false && m.getFall() == false)
//				
//				m.setCollided(1);
//
//			else if (m.getJump() == true || m.getFall() == true) {
//				m.setFall(true);
//				m.setDirection(0, 1);
//			}
//
//			// collide with brick's RIGHT
//		} else if ((mR > oR & mL <= oR) && (mT < oB && mB > oT)) {
//
//			if (m.getJump() == false && m.getFall() == false)
//				m.setCollided(-1);
//			else if (m.getJump() == true || m.getFall() == true) {
//				m.setFall(true);
//				m.setDirection(0, -1);
//			}
//		}

		// collision at brick's bottom = pop the brick
		if ((mL <= oR && mR > oL) && (mT < oB && mB > oB)) {
			// if (within brick left and right) && mario top bumps into bottom of brick
			m.setFall(true);
			iter.remove();
			// m.updateScore();

		} else if
		// collision at top of brick = don't fall through it
		((mL <= oR && mR > oL) && (mB > oT && mT < oT)) {
			m.setFall(false);
			m.setLastMaxHeight(oT); // send it so mario can jump 5 times his height
			o.collidedTopDown = true; // sets collided with brick -> this will help determine when mario should fall
										// through, when he has left a brick

			// no longer on top of brick == fall
		} else if ((!(mL <= oR && mR >= oL)) && (mB >= oT && mT <= oT) && m.getJump() == false
				&& o.collidedTopDown == true) {
			m.setFall(true);
			o.collidedTopDown = false; // reset
			m.setLastMaxHeight(325); // ground height;

		}

	}

	// called in mario
	// DESCRIPTION: helps move the enemies + background ("scroll the screen") so that it looks like mario is
	// moving still but he's not actually passing 50% of the screen
	// PARAMETERS: takes the (speed+ mario's xMovement = how much to "scroll by")
	public void scrollUpdate(double d) {
		scrollVal = d;
		xBG -= scrollVal;

		for (Brick b : bricks) {
			b.decXVal(scrollVal);
		}
		for (Enemy b : enemies) {
			b.decXVal(scrollVal);
		}

		//updates how far mario has "gone"
		maxX += scrollVal;

		//if mario has reached a certain point, trigger the creation
		if (triggerPoints.isEmpty() == false) {
			if (maxX > triggerPointInt) {
				triggerPointInt = triggerPoints.poll();
				create(triggerPointInt);
			}
		}
	}

	
	// called by map's scrollUpdate()
	// DESCRIPTION: creates the initial enemy/brick
	//first var gives what type of object it is, second var gives the y-value.
	// PARAMETERS: x-value will be calculated in the class with the triggerpoint of creation.
	public void create(int x) {
		int a = objectInitialFields.remove();
		int y = objectInitialFields.remove();
		if (a == 0) {
			iter.add(new Brick(x, y));
		} else if (a == 1) {
			enemies.add(new Enemy(x, y));
		}

	}

}
