package Game;

import java.util.List;

public class ComputerPlayer extends Player {
	
	private Strategy strategy;
	
	public ComputerPlayer(String name, Game game, Strategy strategy) {
		super(name, game);
		this.strategy = strategy;
	}
	
	public void determineMove() {
		
		
	}

	
	
}
