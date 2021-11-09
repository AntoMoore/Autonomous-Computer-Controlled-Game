package ie.gmit.sw.ai;

public class Node {
	//member variables
	private boolean wall;
	private int row, col;
	private int g_cost;
	private int h_cost;
	private char tile;
	
	private Node parent;
	private CharacterStats characterStats;

	//constructor
	public Node(boolean wall, int row, int col, char tile) {
		this.wall = wall;
		this.row = row;
		this.col = col;
		this.tile = tile;

		g_cost = 0;
		h_cost = 0;
		setParent(null);
		setCharacterStats(null);
	}

	// gets/sets
	public int f_cost() {
		return h_cost + g_cost;
	}
	
	public void setTile(char tile) {
		this.tile = tile;
		
	}
	
	public char getTile() {
		return this.tile;
	}
	
	public void setWall(boolean wall) {
		this.wall = wall;
	}
	
	public boolean getWall() {
		return wall;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public CharacterStats getCharacterStats() {
		return characterStats;
	}

	public void setCharacterStats(CharacterStats characterStats) {
		this.characterStats = characterStats;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getG_cost() {
		return g_cost;
	}

	public void setG_cost(int g_cost) {
		this.g_cost = g_cost;
	}

	public int getH_cost() {
		return h_cost;
	}

	public void setH_cost(int h_cost) {
		this.h_cost = h_cost;
	}	
}
