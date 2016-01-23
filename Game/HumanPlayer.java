package Game;

import java.util.*;

public class HumanPlayer extends Player {
	
	private View view;
	
    public HumanPlayer(String name, Game game) {
    	super(name, game);
    	this.view = new TUIView();
    }
    
    public void trade(int[][] stenen) {
    	
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
