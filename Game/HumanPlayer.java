package Game;

import java.util.*;
import Server.ServerPeer;

public class HumanPlayer extends Player {
	
	private ServerPeer serverpeer;
	private boolean decided;
	
    public HumanPlayer(ServerPeer serverpeer) {
    	super(serverpeer.getName(), null);
    	this.serverpeer = serverpeer;
    }
    
    public void trade(List<Steen> stenen) {
    	game.tradeStenen(stenen);
    }
    
    public boolean place(Steen steen, int[] vakje) {
    	boolean hasBeenPlaced = game.place(steen, vakje);
    	return hasBeenPlaced;
    }
	
	public ServerPeer getServerPeer() {
		return serverpeer;
	}
}
