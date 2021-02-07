/*
 * DESCRIPTION: mario, enemy and brick all use these fields + methods
 * 
 */
abstract public class abstractObjects {
	//subclass accessible
	protected int xVal, yVal;
	protected int width, height;
	protected boolean collidedTopDown;
	

	//enemy and brick use, Mario does not ->
	//scroll (called by Map class)
	public void decXVal(double d) {
		xVal -= d;
	}
	
//getters setters -> for some reason Mario required I override methods...don't know why.
	public int getYVal() {
		return yVal;
	}

	public int getXVal() {
		return xVal;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}


}
