package Game;

import java.util.*;

import Server.*;
import Game.*;

/**
 * Overall model class to represent the Qwirkle game, consisting of players, a bag of cubes, a board and a scoreboard.
 * @author Eric van Schaik and Birte Brunt.
 *
 */
public class Game extends Thread implements Observer {
	
	private List<Steen> zak;
	private List<ServerPeer> spelers;
	private ServerPeer currentPlayer;
	private Board board;
	private Map<ServerPeer, Integer> scoreboard;
	private int gamesize;
	public boolean isRunning;
	
	//@ requires spelers.size() >= 1 && spelers.size() <= 4;
	/**
	 * Constructs a new game with existing players, but makes a new board, a new bag with cubes and a new scoreboard (by calling reset()).
	 * @param spelers: the players participating in the game.
	 */
	public Game(List<ServerPeer> spelers, int gamesize) {
		this.gamesize = gamesize;
		this.spelers = spelers;
		this.board = new Board();
		this.zak = new ArrayList<Steen>(108);
		for (int i=0; i<3; i++) {
			for (int i2=0; i2<6; i2++) {
				for (int i3=0; i3<6; i3++) {
					Steen steen = null;
					try {
						steen = new Steen(i2,i3);
					}
					catch (InvalidArgumentException e) {
						
					}
					zak.add(steen);
				}
			}
		}
		reset();
	}
	
	/**
	 * Starts the game if being called.
	 */
	public void run() {
		for (ServerPeer p: spelers) {
			for (int i = 0; i < 6; i++) {
				Steen s = zak.get((int)Math.random()*zak.size());
				p.addSteen(s);
				zak.remove(s);
			}			
		}
	}
	
	public int gameSize() {
		return gamesize;
	}
	
	public List<ServerPeer> getSpelers() {
		return spelers;
	}
	
	public ServerPeer getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void addSpeler(ServerPeer speler) {
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
		Set<Map.Entry<ServerPeer, Integer>> entryset = scoreboard.entrySet();
		Map.Entry<ServerPeer, Integer> highest = null;
		for (Map.Entry<ServerPeer, Integer> score: entryset) {
			if (highest.getValue() < score.getValue()) {
				highest = score;
			}
		}
		ServerPeer winner = highest.getKey();
		if (winner instanceof ServerPeer) {
			((ServerPeer) winner).write("Congratulations! You've won!");	
		}
		for (ServerPeer s: spelers) {
			if (s instanceof ServerPeer) {
				((ServerPeer) s).write("You've lost :( The winner is" + winner.getName());
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
	
	/*public boolean makeMove(String[] move) {
		boolean succeed = true;
		if (move[0].equals("place")) {
			for (int i = 1; i < move.length; i= i+2) {
				boolean succes = ;
			}
		}
		return succeed;
	}*/
	
	/**
	 * Resets the game by resetting the scoreboard (given every Player 0 points) and by removing the Stenen owned by the players.
	 */
	public void reset() {
		for (ServerPeer s: spelers) {
			s.reset();	
		}
		scoreboard = new HashMap<ServerPeer, Integer>();
		for (ServerPeer s: spelers) {
			scoreboard.put(s, new Integer(0));
		}
	}

	
	public void update(Observable o, Object arg) {
		
		
	}
}
