package Game;

import java.util.*;

/**
 * Class to represent the Board in the Qwirkle game.
 * @author Eric van Schaik and Birte Brunt
 *
 */
public class Board {
	
	private Map<Steen, int[]> vakjes;
	
	/**
	 * A new Board gets a new empty map, with room for entries with values
	 * of the type Steen and of the type int[].
	 */
	public Board() {
		vakjes = new HashMap<Steen, int[]>();
	}
	
	/**
	 * Another version of the normal Board constructor, where you give a non-empty map
	 * of entries with values of type Steen and type int[], which then make up the new Board.
	 * Only used for copying the board.
	 * @param vakjes, gives the fields which make up the new Board
	 */
	
	private Board(Map<Steen, int[]> vakjes) {
		this.vakjes = vakjes;
	}
	
	/**
	 * Deletes all fields of the Board, the board now only has an empty map, like in the standard constructor.
	 */
	public void reset() {
		vakjes = new HashMap<Steen, int[]>();
	}
	
	/**
	 * Places a Steen on the board on a given field. Before then, the field didn't exist.
	 * @param steen: The Steen to be placed.
	 * @param vakje: the field on which the Steen needs to be placed.
	 * @return true if the Steen can be placed on the field, and therefore is placed there,
	 * false if the Steen cannot be placed there.
	 */
	
	public boolean place(Steen steen, int[] vakje) {
		if (isValidMove(steen, vakje)) {
			vakjes.put(steen, vakje);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns an int-array representation of a field, given a x and y coordinate.
	 * @param x: the x-coordinate of the field.
	 * @param y: the y-coordinate of the field.
	 * @return an int-array representation of a field
	 */
	
	public int[] getVakje(int x, int y) {
		int[] vakje = new int[2];
		vakje[0] = x;
		vakje[1] = y;
		return vakje;
	}
	
	/**
	 * Returns the Steen which is on the given field.
	 * @param vakje: the int-array representation of a field.
	 * @return the Steen which is on the given field.
	 */
	
	public Steen getSteen(int[] vakje) {
		Steen steen = null;
		Set<Map.Entry<Steen, int[]>> entryset = vakjes.entrySet();
		for (Map.Entry<Steen, int[]> e: entryset) {
			if (Arrays.equals(vakje, e.getValue())) {
				steen = e.getKey();
			}
		}
		return steen;
	}
	
	/**
	 * Gives a copy of this Board.
	 * @return a copy of the board.
	 */
	
	public Board deepCopy() {
		return new Board(vakjes);
	}
	
	/**
	 * Checks if the given Steen can be placed on the given field
	 * @param steen: the Steen which can or cannot be placed.
	 * @param vakje: the field on which the Steen can or cannot be placed.
	 * @return true if the given Steen can be placed on the given field, false if not.
	 */
	
	public boolean isValidMove(Steen steen, int[] vakje) {
		int x = vakje[0];
		int y = vakje[1];
		if (vakjes.containsValue(vakje)) {
			return false;
		}
		else if (vakjes.containsValue(getVakje((x-1), y)) || vakjes.containsValue(getVakje((x+1), y)) || vakjes.containsValue(getVakje(x, (y-1))) || vakjes.containsValue(getVakje(x, (y-1)))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns a textual representation of the board.
	 */
	public String toString() {
		return null;
	}
}
