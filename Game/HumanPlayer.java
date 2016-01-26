package Game;

import java.util.*;
import Server.ServerPeer;

public class HumanPlayer extends Player {
	
	private ServerPeer serverpeer;
	
    public HumanPlayer(ServerPeer serverpeer, Game game) {
    	super(serverpeer.getName(), game);
    	this.serverpeer = serverpeer;
    }
    
    public void trade(List<Steen> stenen) {
    	game.tradeStenen(stenen);
    }
    
    public boolean place(Steen steen, int[] vakje) {
    	boolean hasBeenPlaced = game.place(steen, vakje);
    	return hasBeenPlaced;
    }
    
	public void determineMove() {
		
		
	}
	
	public ServerPeer getServerPeer() {
		return serverpeer;
	}
}
