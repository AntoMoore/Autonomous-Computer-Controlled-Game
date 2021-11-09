package ie.gmit.sw.ai;

/*
 * Class used to provide the x,y coordinates of a given location on the map
 */
public class Location {
	// member variables
	private int x;
	private int y;
	
	// constructor
	public Location(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	// gets/sets
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
