package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import javafx.concurrent.Task;

public class GameModel {
	//member variables
	private static final int MAX_CHARACTERS = 10;
	private ThreadLocalRandom rand = ThreadLocalRandom.current();
	private Node[][] model;
	private int row;
	private int col;
	
	// Threading
	private final ExecutorService exec = Executors.newFixedThreadPool(MAX_CHARACTERS, e -> {
        Thread t = new Thread(e);
        t.setDaemon(true);
        return t ;
    });
	
	// constructor
	public GameModel(int dimension){
		model = new Node[dimension][dimension];
		init();
		carve();
		addGameCharacters();
	}
	
	// member methods
	public void tearDown() {
		exec.shutdownNow();
	}
	
	/*
	 * Initialises the game model by creating a 2D node array filled with hedges  
	 */
	private void init(){
		for (int row = 0; row < model.length; row++){
			for (int col = 0; col < model[row].length; col++){
				model[row][col] = new Node(true, row, col, '\u0030');
			}
		}
	}
	
	/*
	 * Carve paths through the hedge to create passages.
	 * set each tile to '\u0020' and walls to false
	 */
	public void carve(){
		for (int row = 0; row < model.length; row++){
			for (int col = 0; col < model[row].length - 1; col++){
				if (row == 0) {
					model[row][col + 1].setTile('\u0020');
					model[row][col + 1].setWall(false);
				}else if (col == model.length - 1) {
					model[row - 1][col].setTile('\u0020');
					model[row][col - 1].setWall(false);
				}else if (rand.nextBoolean()) {
					model[row][col + 1].setTile('\u0020');
					model[row][col + 1].setWall(false);
				}else {
					model[row - 1][col].setTile('\u0020');
					model[row - 1][col].setWall(false);
				}
			}
		}
	}
	
	private void addGameCharacters() {
		// tasks to execute
		Collection<Task<Void>> tasks = new ArrayList<>();
		
		/*
		 * CharacterStats (aggression, attackPower, stamina)
		 * set aggression to zero and stamina to max on character creation
		 * attack power is set to different vales for each character to give them different behavior
		 * high attack power is more likely to chase the player, low attack is more likely to group up with other characters
		 */
		addGameCharacter(tasks, '\u0032', '\u0020', 1, new CharacterStats(0, 5, 5)); //0032 is a Red Enemy, 0020 is ground
		addGameCharacter(tasks, '\u0033', '\u0020', 1, new CharacterStats(0, 4, 5)); //0033 is a Pink Enemy, 0020 is ground
		addGameCharacter(tasks, '\u0034', '\u0020', 1, new CharacterStats(0, 3, 5)); //0034 is a Blue Enemy, 0020 is ground
		addGameCharacter(tasks, '\u0035', '\u0020', 1, new CharacterStats(0, 2, 5)); //0035 is a Red Green Enemy, 0020 is ground
		addGameCharacter(tasks, '\u0036', '\u0020', 1, new CharacterStats(0, 1, 5)); //0036 is a Orange Enemy, 0020 is ground
		
		tasks.forEach(exec::execute);
	}
	
	private void addGameCharacter(Collection<Task<Void>> tasks, char enemyID, char replace, int number, CharacterStats character){
		int counter = 0;
		while (counter < number){
			
			// randomly place a character on a valid ground tile
			do {
				row = rand.nextInt(model.length);
				col = rand.nextInt(model[0].length);
			} while(model[row][col].getTile() != replace);
			
			// set character details
			model[row][col].setTile(enemyID);
			model[row][col].setCharacterStats(character);
			
			// add character task
			tasks.add(new CharacterTask(this, enemyID, row, col));
			counter++;
		}
	}
	
	// check if move is valid
	public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, char character, CharacterStats stats){
		if (toRow <= this.size() - 1 && toCol <= this.size() - 1 && this.get(toRow, toCol) == ' '){
			this.set(fromRow, fromCol, '\u0020', null);
			this.set(toRow, toCol, character, stats);
			
			return true;
		}else{
			return false; //Can't move
		}
	}
	
	// gets/sets
	public Node[][] getModel(){
		return this.model;
	}
	
	public char get(int row, int col){
		return this.model[row][col].getTile();
	}
	
	public void set(int row, int col, char c, CharacterStats stats){
		this.model[row][col].setTile(c); 
		this.model[row][col].setCharacterStats(stats);; 
		
	}
	
	public int size(){
		return this.model.length;
	}
}