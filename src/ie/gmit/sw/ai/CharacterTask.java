package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import ie.gmit.sw.ai.fuzzy.FuzzyAggression;
import ie.gmit.sw.ai.nn.Utils;
import javafx.concurrent.Task;

public class CharacterTask extends Task<Void> {
	private static final int SLEEP_TIME = 300; 
	private static final int REST_TIME = 500; 
	private static ThreadLocalRandom rand = ThreadLocalRandom.current();
	private final double SEARCH_RADIUS = 20;
	private boolean alive = true;
	private GameModel model;
	private char enemyID;
	private int row;
	private int col;
	
	private CharacterStats stats;
	private Node targetPos;
	private Node sourcePos;
	private Node playerPos;
	
	private double distanceToPlayer;
	private int nearbyCharacters;

	public CharacterTask(GameModel model, char enemyID, int row, int col) {
		this.model = model;
		this.enemyID = enemyID;
		this.row = row;
		this.col = col;
	}

	@Override
	public Void call() throws Exception {
		/*
		 * This Task will remain alive until the call() method returns. This cannot
		 * happen as long as the loop control variable "alive" is set to true. You can
		 * set this value to false to "kill" the game character if necessary (or maybe
		 * unnecessary...).
		 */
		while (alive) {
			Thread.sleep(SLEEP_TIME);

			synchronized (model) {			
				
				// get character stats from current Node
				sourcePos = model.getModel()[row][col];
				stats = sourcePos.getCharacterStats();
				
				// search map for character locations
				Collection<Location> characters = new ArrayList<Location>();
				characters = ObjectTracker.locateCharacters(model.getModel(), sourcePos);
				
				// search through collection
				for(Location l : characters)
				{
					//reset counter
					nearbyCharacters = 0;
					
					// set target
					setTarget(l);
					
					// distance to target
					double distance = this.targetDistance();
					
					// if target is a character and inside the search radius
					if (targetPos.getCharacterStats() != null && distance < SEARCH_RADIUS) {
						nearbyCharacters++;
					}
					else if(targetPos.getTile() == '1') {
						distanceToPlayer = distance;
						playerPos = targetPos;
						
						// check range to player
						// 1 = horizontal/vertical
						// sqrt(2) => 1.414 diagonal 
						if(distanceToPlayer < 1.5) {
							System.out.println("GAME OVER...");
							System.out.println("Player was caught by Character: " + enemyID);
							System.exit(0);
						}
					}
				}
				
				// get aggression levels using fuzzy logic
				FuzzyAggression fa = new FuzzyAggression();
				double aggression = (fa.calculateAggression(distanceToPlayer, nearbyCharacters) / 10);
				aggression = Math.round(aggression * 100.0) / 100.0;
				stats.setAggression(aggression);
				
				// use neural network to decide action
				double[] test1 = {stats.getAggression(), stats.getAttackPower(), stats.getStamina()};
				double[] result = Runner.nn.process(test1);
				int action = Utils.getMaxIndex(result) + 1;

				if (action == 1) {
					// move towards player
					moveToPlayer();
				} else if (action == 2){
					// move to character
					moveToCharacter();
				} else if (action == 3){
					// move random
					moveRandom();
				} else if (action == 4){
					// regen stamina
					regenStamina();
				}	
			}
		}
		return null;
	}

	private void move(int row, int col, int next_row, int next_col, char enemyID, CharacterStats stats) {
		// move character to next tile
		model.set(next_row, next_col, enemyID, stats);

		// replace old tile with empty ground
		model.set(row, col, '\u0020', null);

		// reset row and col
		this.row = next_row;
		this.col = next_col;
	}

	private void moveRandom() {
		// temp variable
		int temp_row = row, temp_col = col;

		// Randomly pick a direction up, down, left or right
		if (rand.nextBoolean()) {
			temp_row += rand.nextBoolean() ? 1 : -1;
		} else {
			temp_col += rand.nextBoolean() ? 1 : -1;
		}

		// check if valid
		if (model.isValidMove(row, col, temp_row, temp_col, enemyID, stats)) {
			move(row, col, temp_row, temp_col, enemyID, stats);
		}
	}

	private void moveToCharacter() {
		// path to player
		ArrayList<Node> path = new ArrayList<Node>();
		PathFinding pf = new PathFinding();

		// return a list of path nodes
		path = pf.findPath(model.getModel(), sourcePos, targetPos);

		// System.out.println(path.toString());
		Node next = path.get(0);

		// check if valid
		if (model.isValidMove(row, col, next.getRow(), next.getCol(), enemyID, stats)) {
			move(row, col, next.getRow(), next.getCol(), enemyID, stats);
		}
	}
	
	private void moveToPlayer() {
		// path to player
		ArrayList<Node> path = new ArrayList<Node>();
		PathFinding pf = new PathFinding();

		// return a list of path nodes
		path = pf.findPath(model.getModel(), sourcePos, playerPos);

		// System.out.println(path.toString());
		Node next = path.get(0);

		// check if valid
		if (model.isValidMove(row, col, next.getRow(), next.getCol(), enemyID, stats)) {
			//System.out.println("Moving Character to Next Node...");
			move(row, col, next.getRow(), next.getCol(), enemyID, stats);
			// reduce stamina when chasing player
			reduceStamina();
		}
	}
	
	private void reduceStamina() {
		// reduce character stamina 
		int energy = stats.getStamina();
		stats.setStamina(energy-1);
	}
	
	private void regenStamina() throws InterruptedException {
		// rest character for 0.5 seconds
		Thread.sleep(REST_TIME);
		
		// replenish stamina
		stats.setStamina(5);	
	}

	private double targetDistance() {
		// get distance from target
		return ObjectTracker.getDistance(sourcePos, targetPos);
	}
	
	private void setTarget(Location location) {
		// get target node
		targetPos = model.getModel()[location.getX()][location.getY()];

		// get character node
		sourcePos = model.getModel()[row][col];
	}
}