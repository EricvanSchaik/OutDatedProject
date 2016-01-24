package Game;

import java.util.*;
import Client.ClientPeer;

public class HumanPlayer extends Player {
	
	private View view;
	private ClientPeer clientpeer;
	
    public HumanPlayer(ClientPeer clientpeer, Game game) {
    	super(clientpeer.getName(), game);
    	this.view = new TUIView();
    	this.clientpeer = clientpeer;
    }
    
    public void trade(List<Steen> stenen) {
    	game.tradeStenen(stenen);
    }
    
    public boolean place(Steen steen, int[] vakje) {
    	boolean hasBeenPlaced = game.place(steen, vakje);
    	return hasBeenPlaced;
    }
    
	public void determineMove() {
		int choice = view.askForInput();
		
	}
	
	public View getView() {
		return this.view;
	}
}
