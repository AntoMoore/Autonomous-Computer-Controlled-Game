package ie.gmit.sw.ai;

import java.util.ArrayList;

public class PathFinding {

	public ArrayList<Node> findPath(Node[][] gameModel, Node start, Node target) {
		// list of open and closed paths
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> closed = new ArrayList<Node>();

		open.add(start);

		while (!open.isEmpty()) {
			
			// get first node from open list
			Node currentNode = open.get(0);

			for (int i = 1; i < open.size(); i++) {
				Node q = open.get(i);
				// look for the lowest f_cost (shortest path)
				if (q.f_cost() < currentNode.f_cost()
						|| q.f_cost() == currentNode.f_cost() && q.getH_cost() < currentNode.getH_cost()) {
					currentNode = q;
				}
			}
			
			// remove current node from open list and add to closed
			open.remove(currentNode);
			closed.add(currentNode);

			// check if current node is the target
			if (currentNode.equals(target)) {
				// return list with the valid path
				return retracePath(start, target);
			}
			
			//check surrounding nodes for walls or previously traveled paths
			for (Node n : getNeighbours(currentNode, gameModel)) {
				if (n.getWall() || closed.contains(n)) {
					continue;
				}

				// re-assign cost to each neighboring node
				int newCostToNeighbour = currentNode.getG_cost() + getDistance(currentNode, n);
				if (newCostToNeighbour < n.getG_cost() || !open.contains(n)) {
					n.setG_cost(newCostToNeighbour);
					n.setH_cost(getDistance(n, target));
					n.setParent(currentNode);

					if (!open.contains(n))
						open.add(n);
				}
			}
		}

		System.out.println("Path not found	");
		return null;
	}

	private ArrayList<Node> retracePath(Node start, Node end) {
		// list to hold valid path
		ArrayList<Node> path = new ArrayList<Node>();

		Node currentNode = end;

		while (!currentNode.equals(start)) {
			// add the current node
			path.add(currentNode);
			// set current node to its parent
			currentNode = currentNode.getParent();
		}

		for (int i = 0; i < path.size() / 2; i++) {

			Node n = path.get(i);
			path.set(i, path.get(path.size() - i - 1));
			path.set(path.size() - i - 1, n);
		}
		return path;
	}

	private int getDistance(Node a, Node b) {
		// Manhattan distance calculation
		int distX = Math.abs(a.getCol() - b.getCol());
		int distY = Math.abs(a.getRow() - b.getRow());
		
		// diagonal distance 1 vertical and 1 horizontal tile
		// this is the square root of 2 (ie 1.4122)
		// round off vales to 14 and 10 to reduce calculation overhead
		if (distX > distY)
			return 14 * distY + 10 * (distX - distY);
			

		return 14 * distX + 10 * (distY - distX);
	}

	private ArrayList<Node> getNeighbours(Node n, Node[][] maze) {
		// list of neighbours
		ArrayList<Node> neighbours = new ArrayList<Node>();
		
		// only have neighbors for up, down, left and right
		neighbours.add(maze[n.getRow() - 1][n.getCol()]);
		neighbours.add(maze[n.getRow() + 1][n.getCol()]);
		neighbours.add(maze[n.getRow()][n.getCol() - 1]);
		neighbours.add(maze[n.getRow()][n.getCol() + 1]);

		return neighbours;
	}

}
