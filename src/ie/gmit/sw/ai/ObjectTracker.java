package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.Collection;

public class ObjectTracker {

	// returns the distance between 2 nodes
	public static double getDistance(Node source, Node target) {

		// get averages
		int dx = Math.abs(target.getRow() - source.getRow());
		int dy = Math.abs(target.getCol() - source.getCol());

		// get min/max
		int min = Math.min(dx, dy);
		int max = Math.max(dx, dy);

		// straight lines and diagonal lines
		int diagonal = min;
		int straight = max - min;

		return Math.sqrt(2) * (diagonal + straight);
	}
	
	// returns the coordinates of all characters (including player) on the map
	public static Collection<Location> locateCharacters(Node[][] gameMap, Node source) {
		// search model for characters
		Collection<Location> locations = new ArrayList<Location>();
		for (int i = 0; i < gameMap.length; i++) {
			for (int j = 0; j < gameMap[0].length; j++) {
				// add all characters to collection, excluding itself
				if (gameMap[i][j].getCharacterStats() != null && gameMap[i][j].getTile() != source.getTile()) {
					locations.add(new Location(i, j));
				}
				else if(gameMap[i][j].getTile() == '1') {
					// add player location to list
					locations.add(new Location(i, j));
					
				}
			}
		}
		return locations;
	}

}
