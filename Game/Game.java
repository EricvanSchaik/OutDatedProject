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
	private Server server;
	private Board board;
	private Map<ServerPeer, Integer> scoreboard;
	private int gamesize;
	public boolean isRunning;
	private boolean hasDecided = false;
	private boolean eindeSpel = false;
	
	//@ requires spelers.size() >= 1 && spelers.size() <= 4;
	/**
	 * Constructs a new game with existing players, but makes a new board, a new bag with cubes and a new scoreboard (by calling reset()).
	 * @param spelers: the players participating in the game.
	 */
	public Game(List<ServerPeer> spelers, int gamesize, Server server) {
		this.gamesize = gamesize;
		this.spelers = spelers;
		this.server = server;
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
		currentPlayer = spelers.get(0);
		while (!eindeSpel) {
			while (!hasDecided) {
				sendAllPlayers("turn " + currentPlayer.getName());
				sendAllPlayers(board.toString());
				hasDecided = currentPlayer.makeMove();
			}
			currentPlayer = spelers.get((spelers.indexOf(currentPlayer) + 1)%spelers.size());
		}
		endGameMessage();
	}
	
	public void sendAllPlayers(String message) {
		for (ServerPeer s: spelers) {
			s.write(message);
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
	public boolean place(Map<Steen, int[]> steentjes) {
		boolean hasBeenPlaced = true;
		for (Map.Entry<Steen, int[]> e: steentjes.entrySet()) {
			boolean placed = board.place(e.getKey(), e.getValue());
			if (!placed) {
				hasBeenPlaced = false;
			}
		}
		Integer oldScore = scoreboard.get(currentPlayer);
		Integer newScore = new Integer(oldScore.intValue()+calculatePoints(steentjes));
		scoreboard.put(currentPlayer, newScore);
		return hasBeenPlaced;
	}
	
	/**
	 * Determines the points to be added to the current player when the method place is being called upon.
	 * @param steen: Steen given as argument of method place.
	 * @param vakje: Field given as argument of method place.
	 * @return the points to be added to the current player.
	 */
	private int calculatePoints(Map<Steen, int[]> nieuwestenen) {
		int score = 0;
		boolean stop = false;
		List<Integer> ybonus = new ArrayList<Integer>();
		List<Integer> xbonus = new ArrayList<Integer>();
		List<Integer> y = new ArrayList<Integer>();
		List<Integer> x = new ArrayList<Integer>();
		Set<Map.Entry<Steen, int[]>> entryset = nieuwestenen.entrySet();
		for (Map.Entry<Steen, int[]> e: entryset){
			int[] vakje = e.getValue();
			if (!board.isEmpty(vakje[0], (vakje[1]+1)) || !board.isEmpty(vakje[0], (vakje[1]-1)) || !board.isEmpty((vakje[0]+1), vakje[1]) || !board.isEmpty((vakje[0]-1), vakje[1])){
				score = score +2;
			}
			else {score = score +1;}
			if (!x.contains(vakje[0])){
			while (!stop){
				for (int i=1; i<7; i++){
					if (!board.isEmpty(vakje[0], vakje[1]+i)&&!nieuwestenen.containsKey(vakje[1]+i)){
						score = score +1;
					}
					if (board.isEmpty(vakje[0], vakje[1]+i)){
						stop = true;
					}
				}
			}
			stop = false;
			while (!stop){
				for (int i=1; i<7; i++){
					if (!board.isEmpty(vakje[0], vakje[1]-i)&&!nieuwestenen.containsKey(vakje[1]-i)){
						score = score +1;
					}
					if (board.isEmpty(vakje[0], vakje[1]-i)){
						stop = true;
					}
				}
			}
			x.add(vakje[0]);
			}
			stop = false;
			if (!y.contains(vakje[1])){
			while (!stop){
				for (int i=1; i<7; i++){
					if (!board.isEmpty(vakje[0]+i, vakje[1])&&!nieuwestenen.containsKey(vakje[0]+i)){
						score = score +1;
					}
					if (board.isEmpty(vakje[0]+i, vakje[1])){
						stop = true;
					}
				}
			}
			stop = false;
			while (!stop){
				for (int i=1; i<7; i++){
					if (!board.isEmpty(vakje[0]-i, vakje[1])&&!nieuwestenen.containsKey(vakje[0]-i)){
						score = score +1;
					}
					if (board.isEmpty(vakje[0]-i, vakje[1])){
						stop = true;
					}
				}
			}
			y.add(vakje[1]);
			}
			if (!ybonus.contains(vakje[1])){
				for (int i = -5; i<1 ; i++){
					if (!board.isEmpty(vakje[0]+i, vakje[1]) && !board.isEmpty(vakje[0]+i+1, vakje[1]) && !board.isEmpty(vakje[0]+i+2, vakje[1]) && !board.isEmpty(vakje[0]+i+3, vakje[1]) && !board.isEmpty(vakje[0]+i+4, vakje[1])&&!board.isEmpty(vakje[0]+i+5, vakje[1])){
						score = score + 6;
						ybonus.add(vakje[1]);
					}
				}
			}
			if (!xbonus.contains(vakje[0])){
				for (int i = -5; i<1; i++){
					if (!board.isEmpty(vakje[0], vakje[1]+i) && !board.isEmpty(vakje[0], vakje[1]+i+1) && !board.isEmpty(vakje[0], vakje[1]+i+2) && !board.isEmpty(vakje[0], vakje[1]+i+3) && !board.isEmpty(vakje[0], vakje[1]+i+4) && !board.isEmpty(vakje[0], vakje[1]+i+5)){
						score = score + 6;
						xbonus.add(vakje[0]);
					}
				}	
			}
		}
		return score;
	}
	
	public void noStonesLeft(ServerPeer speler) {
		Integer oldScore = scoreboard.get(speler);
		Integer newScore = new Integer(oldScore.intValue()+6);
		scoreboard.put(currentPlayer, newScore);
		eindeSpel = true;
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
		eindeSpel = false;
	}

	
	public void update(Observable o, Object arg) {
		if (o.equals(currentPlayer)) {
			hasDecided = true;
		}
		else {
			((ServerPeer) o).write("error 0");
		}
		
	}
}
