/**
 * 
 */
package de.htwsaar.stl.game.swinemeeper.core;

import java.util.Random;

/**
 * @author miede
 *
 */
public class Swinemeeper {

	private static final int MAX_WIDTH = 30;
	private static final int MAX_HEIGHT = 24;

	public enum FieldContent {
		EMPTY, // No mine here or in neighboring fields 
		ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, // Number of mines in neighboring fields 
		MINE, // What could this be?
		HIDDEN, // This field is not visible to the player currently, i.e., it has not yet been revealed
		MARKED // This field is marked to (possibly) contain a mine (for "marked mines" game mode)
	}
	
	public enum FieldNeighbors {
		FOUR_NEIGHBORS,
		EIGHT_NEIGHBORS
	}
	
	private static final int[][] FOUR_NEIGHBORS = {
	        {-1, 0},
	{ 0,-1},        {0, 1}, 
	        { 1, 0}
	};
	
	private static final int[][] EIGHT_NEIGHBORS = {
	{-1,-1},{-1, 0},{-1, 1},
	{ 0,-1},        { 0, 1}, 
	{ 1,-1},{ 1, 0},{ 1, 1}
	};
	
	private FieldContent[][] minefield_full; // The "real" minefield
	private FieldContent[][] minefield_visible; // The minefield visible to the player
	private int mines; // Number of mines in the minefield
	private FieldNeighbors neighbors; // Type of neighbors used for game
	
	// Constructor
	public Swinemeeper(int width, int height, int mines, FieldNeighbors neighbors) {	
		initialize(width, height, mines, neighbors);
	}
	
	
	// Setup the minefields
	public void initialize(int width, int height, int mines, FieldNeighbors neighbors) {
		// Make the input "right"
		width = sanitizeInt(width, MAX_WIDTH);
		height = sanitizeInt(height, MAX_HEIGHT);
		mines = sanitizeInt(mines, (width*height));
		
		// Set attributes
		setMinefield_full(new FieldContent[width][height]); 
		setMinefield_visible(new FieldContent[width][height]);
		this.setMines(mines); 
		this.setNeighbors(neighbors);
		
		// Initialize full minefield and visible minefield
		for (int row = 0; row < this.getFieldRows(); row++) {
			for (int col = 0; col < this.getFieldCols(); col++) {
				this.getMinefield_full()[row][col] = FieldContent.EMPTY;
				this.getMinefield_visible()[row][col] = FieldContent.HIDDEN; 
			}
		}
		
		// Randomly place mines
		Random randomGenerator = new Random(System.currentTimeMillis());
		for (int currentMine = 0; currentMine < mines; currentMine++) {
			// Get random coordinates
			int x = randomGenerator.nextInt(this.getFieldRows());
			int y = randomGenerator.nextInt(this.getFieldCols());
			//System.out.println("Mine place at: " + x + "," + y);
			
			// Is random coordinate already has a mine
			while(isMine(x,y)) {
				// Get new random coordinates
				x = randomGenerator.nextInt(this.getFieldRows());
				y = randomGenerator.nextInt(this.getFieldCols());
				//System.out.println("After collision: " + x + "," + y);
			}
			
			// Place mine
			minefield_full[x][y] = FieldContent.MINE;
			// Update all neighbors: +1 if not mine or if not on field
			updateNeighbors(x,y); 
		}
	}
	
	// Update all the neighbors after placing a mine (add mine count of respective field
	// https://codereview.stackexchange.com/questions/68627/getting-the-neighbors-of-a-point-in-a-2d-grid
	public void updateNeighbors(int x, int y) {
		int[][] neighborsToBeChecked;
		
		// Consider different neighboring concepts
		switch(this.getNeighbors()) {			
			case FOUR_NEIGHBORS:
				neighborsToBeChecked = FOUR_NEIGHBORS;
				break;
			case EIGHT_NEIGHBORS:
				neighborsToBeChecked = EIGHT_NEIGHBORS;
				break;
			default:
				neighborsToBeChecked = FOUR_NEIGHBORS;
				break;
		}
		
		// Iterate over all possible neighbors
	    for (int[] offsetRowCol : neighborsToBeChecked ) {
	        int nrow = x + offsetRowCol[0];
	        int ncol = y + offsetRowCol[1];
	        // Consider only neighbors actually on field
	        if (isOnField(nrow, ncol)) { 
	        	// Might not be the most elegant solution, but sticks with the enum
	        	switch(minefield_full[nrow][ncol]) {
	        	case EMPTY:
	        		minefield_full[nrow][ncol] = FieldContent.ONE;
	        		break;
	        	case ONE:
	        		minefield_full[nrow][ncol] = FieldContent.TWO;
	        		break;
	        	case TWO:
	        		minefield_full[nrow][ncol] = FieldContent.THREE;
	        		break;
	        	case THREE:
	        		minefield_full[nrow][ncol] = FieldContent.FOUR;
	        		break;
	        	case FOUR:
	        		minefield_full[nrow][ncol] = FieldContent.FIVE;
	        		break;
	        	case FIVE:
	        		minefield_full[nrow][ncol] = FieldContent.SIX;
	        		break;
	        	case SIX:
	        		minefield_full[nrow][ncol] = FieldContent.SEVEN;
	        		break;
	        	case SEVEN:
	        		minefield_full[nrow][ncol] = FieldContent.EIGHT;
	        		break;
	        	case EIGHT: // Cannot happen, but do nothing
	        	case MINE: // Do nothing	
	        	default:
	        		break;        	
	        	}
	        	//System.out.println(nrow + ", " + ncol);
	        }
	    }
		
	}
	
	// Is there a mine at the given position?
	private boolean isMine(int x, int y) {
		FieldContent result = minefield_full[x][y];
		
		return (result == FieldContent.MINE) ? true : false;
	}
	
	// Reveals what at a given position is hidden
	// and updates the visible field accordingly
	public FieldContent revealField(int x, int y) {
		FieldContent result = minefield_full[x][y];
		
		// For empty fields, reveal all neighbors, if empty as well, repeat (only do this for still hidden fields)
		// TODO: Remove code duplication (requires separate method for getting all neighbors of a specific point)
		if ( (result == FieldContent.EMPTY) && ( (minefield_visible[x][y] == FieldContent.HIDDEN) || (minefield_visible[x][y] == FieldContent.MARKED) ) ) {
			// Make result field visible
			minefield_visible[x][y] = result;
			int[][] neighborsToBeChecked;
			
			// Consider different neighboring concepts
			switch(this.getNeighbors()) {			
				case FOUR_NEIGHBORS:
					neighborsToBeChecked = FOUR_NEIGHBORS;
					break;
				case EIGHT_NEIGHBORS:
					neighborsToBeChecked = EIGHT_NEIGHBORS;
					break;
				default:
					neighborsToBeChecked = FOUR_NEIGHBORS;
					break;
			}
			
			// Iterate over all possible neighbors
		    for (int[] offsetRowCol : neighborsToBeChecked ) {
		        int nrow = x + offsetRowCol[0];
		        int ncol = y + offsetRowCol[1];
		        // Consider only neighbors actually on field
		        if (isOnField(nrow, ncol)) {
		        	revealField(nrow, ncol);
		        }
		    }
		} else {// Just make result field visible
			minefield_visible[x][y] = result;
		}
		return result;
	}
	
	// Check whether a coordinate is on the minefield
	public boolean isOnField(int x, int y) {
		return (x >= 0) && (x < this.getFieldRows()) && (y >= 0) && (y < this.getFieldCols());
	}

	// Return only positive, non-zero int below a given maximum
	public int sanitizeInt(int dirtyInt, int maximum) {
		int cleanInt;
		
		cleanInt = Math.abs(dirtyInt);
		cleanInt = (cleanInt == 0) ? 1 : cleanInt;
		cleanInt = (cleanInt <= maximum) ? cleanInt : maximum;
		
		return cleanInt;
	}
	
	public int getFieldRows() {
		return this.getMinefield_full().length;
	}
	
	public int getFieldCols() {
		if(this.getMinefield_full().length > 0) {
			return this.getMinefield_full()[0].length;
		} else return 0;
	}
	
	public FieldContent[][] getMinefield_full() {
		return minefield_full;
	}

	private void setMinefield_full(FieldContent[][] minefield_full) {
		this.minefield_full = minefield_full;
	}

	public FieldContent[][] getMinefield_visible() {
		return minefield_visible;
	}

	private void setMinefield_visible(FieldContent[][] minefield_visible) {
		this.minefield_visible = minefield_visible;
	}

	public int getMines() {
		return mines;
	}

	private void setMines(int mines) {
		// Sanitize here? 
		this.mines = mines;
	}

	public FieldNeighbors getNeighbors() {
		return neighbors;
	}

	private void setNeighbors(FieldNeighbors neighbors) {
		this.neighbors = neighbors;
	}
	
}
