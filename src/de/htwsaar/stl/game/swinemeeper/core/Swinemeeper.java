/**
 * 
 */
package de.htwsaar.stl.game.swinemeeper.core;

/**
 * @author miede
 *
 */
public interface Swinemeeper {

	public static final int MAX_WIDTH = 30;
	public static final int MAX_HEIGHT = 24;

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
	
	public static final int[][] FOUR_NEIGHBORS = {
	        {-1, 0},
	{ 0,-1},        {0, 1}, 
	        { 1, 0}
	};
	
	public static final int[][] EIGHT_NEIGHBORS = {
	{-1,-1},{-1, 0},{-1, 1},
	{ 0,-1},        { 0, 1}, 
	{ 1,-1},{ 1, 0},{ 1, 1}
	};
	
	// Initialize all minefields based on basic parameters
	public void initialize(int width, int height, int mines, FieldNeighbors neighbors);
	
	// Reveals what at a given position is hidden
	// and updates the visible field accordingly
	public FieldContent revealField(int x, int y);
	
	// Check whether a coordinate is on the minefield
	public boolean isOnField(int x, int y);
	
	// Size of the minefield (y-axis)
	public int getFieldRows();
	
	// Size of the minefield (x-axis)
	public int getFieldCols();
	
	// Return only copy of object as the full minefield must not be changed by the client
	public FieldContent[][] getMinefield_full();
	
	// Return object reference as the visible field must be modified by the client 
	public FieldContent[][] getMinefield_visible();
	
	// Number of mines
	public int getMines();
	
	// Kind of neighbors (4, 8, ...)
	public FieldNeighbors getNeighbors();
	
	// Mark field (contains mine possibly)
	public void markField(int x, int y);
	
	// Remove mark
	public void unmarkField(int x, int y);
}