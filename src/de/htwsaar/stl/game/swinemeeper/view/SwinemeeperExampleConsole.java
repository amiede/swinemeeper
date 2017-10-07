/**
 * 
 */
package de.htwsaar.stl.game.swinemeeper.view;

import java.util.Arrays;

import de.htwsaar.stl.game.swinemeeper.core.*;
import de.htwsaar.stl.game.swinemeeper.core.Swinemeeper.*;

/**
 * @author miede
 *
 */
public class SwinemeeperExampleConsole {

	public static Swinemeeper example;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		example = new Swinemeeper(8, 8, 10, FieldNeighbors.EIGHT_NEIGHBORS);
		//example = new Swinemeeper(16, 16, 40, FieldNeighbors.EIGHT_NEIGHBORS);
		//example = new Swinemeeper(30, 16, 99, FieldNeighbors.EIGHT_NEIGHBORS);
		//example = new Swinemeeper(4, 4, 1, FieldNeighbors.EIGHT_NEIGHBORS);
		
		printFullField();
		System.out.println(example.isOnField(5, 4));
		//example.updateNeighbors(1, 1);
		example.revealField(1, 1);
		printVisibleField();
		
	}

	public static void printVisibleField() {
		FieldContent[][] minefield_vis = example.getMinefield_visible();
		System.out.println(Arrays.deepToString(minefield_vis).replace("], ", "]\n").replace("[[", "[").replace("]]", "]").replace(",","\t"));
	}

	public static void printFullField() {
		FieldContent[][] minefield_full = example.getMinefield_full();
		System.out.println(Arrays.deepToString(minefield_full).replace("], ", "]\n").replace("[[", "[").replace("]]", "]").replace(",","\t"));
	}
	
}

