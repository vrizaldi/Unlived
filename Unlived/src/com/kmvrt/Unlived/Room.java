package com.kmvrt.Unlived;

public class Room {
		
	private int typeID;
	private int x;
	private int y;
	public boolean north, south, east, west;
		// door location
	private boolean visited;

	public Room(int typeID, int x, int y) {

		this.typeID = typeID;
		
		north = south = east = west = false;
		this.x = x;
		this.y = y;
		visited = false;
	}

	public int getTypeID() {
	
		return typeID;
	}
		
	public int getX() {
			
		return x;
	}
		
	public int getY() {
			
		return y;
	} 

	protected void visit(GameChar c) {
			
		if(!visited && c.getID() == Constants.CHAR_MAIN) {
			visited = true;
		}
	}

	public boolean isVisited() {
			
		return visited;
	}
}