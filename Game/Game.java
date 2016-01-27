package Game;

import java.util.*;

/**
 * Overall model class to represent the Qwirkle game, consisting of players, a bag of cubes, a board and a scoreboard.
 * @author Eric van Schaik and Birte Brunt.
 *
 */
public class Game extends Thread {
	
	private List<Steen> zak;
	private List<Player> spelers;
	private Player currentPlayer;
	private Board board;
	private Map<Player, Integer> scoreboard;
	private int gamesize;
	public boolean isRunning;
	
	//@ requires spelers.size() >= 1 && spelers.size() <= 4;
	/**
	 * Constructs a new game with existing players, but makes a new board, a new bag with cubes and a new scoreboard (by calling reset()).
	 * @param spelers: the players participating in the game.
	 */
	public Game(List<Player> spelers, int gamesize) {
		this.gamesize = gamesize;
		this.spelers = spelers;
		this.board = new Board();
		this.zak = new ArrayList<Steen>(108);
		for (int i=0; i<3; i++) {
			for (int i2=0; i2<6; i2++) {
				for (int i3=0; i3<6; i3++) {
					zak.add(new Steen(i2,i3));
				}
			}
		}
		reset();
	}
	
	/**
	 * Starts the game if being called.
	 */
	public void run() {
		
	}
	
	public int gameSize() {
		return gamesize;
	}
	
	public List<Player> getSpelers() {
		return spelers;
	}
	
	public void addSpeler(Player speler) {
		spelers.add(speler);
		if (spelers.size() == gamesize) {
			isRunning = true;
			run();
		}
	}
	
	/**
	 * Places a given Steen on a given field, calls the similar method in Board and adds points to the current player.
	 * @param steen: Steen to be placed.
	 * @param vakje: Field on which the Steen needs to be placed.
	 * @return true if the Steen has been placed, false if it is not.
	 */
	public boolean place(Steen steen, int[] vakje) {
		boolean hasBeenPlaced = board.place(steen, vakje);
		Integer oldScore = scoreboard.get(currentPlayer);
		Integer newScore = new Integer(oldScore.intValue()+calculatePoints(steen, vakje));
		scoreboard.put(currentPlayer, newScore);
		return hasBeenPlaced;
	}
	
	/**
	 * Determines the points to be added to the current player when the method place is being called upon.
	 * @param steen: Steen given as argument of method place.
	 * @param vakje: Field given as argument of method place.
	 * @return the points to be added to the current player.
	 */
	private int calculatePoints(Steen steen, int[] vakje) {
		return 0;
	}
	
	/**
	 * When the game ends, this method is being called to inform the players.
	 */
	public void endGameMessage() {
		Set<Map.Entry<Player, Integer>> entryset = scoreboard.entrySet();
		Map.Entry<Player, Integer> highest = null;
		for (Map.Entry<Player, Integer> score: entryset) {
			if (highest.getValue() < score.getValue()) {
				highest = score;
			}
		}
		Player winner = highest.getKey();
		if (winner instanceof HumanPlayer) {
			((HumanPlayer) winner).getServerPeer().write("Congratulations! You've won!");	
		}
		for (Player s: spelers) {
			if (s instanceof HumanPlayer) {
				((HumanPlayer) s).getServerPeer().write("You've lost :( The winner is" + winner.getName());
			}
		}
	}
	
	/**
	 * The trade method as alternative to the players as opposed to the place method. 
	 * @param stenen: List of values of type Steen, to be placed in the bag.
	 * @return List of values of type Steen, to be given back to the player.
	 */
	public List<Steen> tradeStenen(List<Steen> stenen) {
		zak.addAll(stenen);
		int amount = stenen.size();
		List<Steen> teruggave = new ArrayList<Steen>();
		Random random = new Random();
		for (int i = 0; i < amount; i++) {
			Steen newsteen = zak.get(random.nextInt(amount));
			teruggave.add(newsteen);
			zak.remove(newsteen);
		}
		
		
		return teruggave;
	}
	
	/**
	 * Resets the game by resetting the scoreboard (given every Player 0 points) and by removing the Stenen owned by the players.
	 */
	public void reset() {
		for (Player s: spelers) {
			s.reset();	
		}
		scoreboard = new HashMap<Player, Integer>();
		for (Player s: spelers) {
			scoreboard.put(s, new Integer(0));
		}
	}
}
