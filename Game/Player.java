package Game;

import java.util.*;

public abstract class Player {
	
	protected String name;
    protected List<Steen> stenen;
    protected Game game;
    
    public Player(String name, Game game) {
        this.name = name;
        stenen = new ArrayList<Steen>();
        this.game = game;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Steen> getStenen() {
    	return stenen;
    }
    
    public Game getGame () {
    	return this.game;
    }
    
    public void newGame(Game game) {
    	this.game = game;
    }
    
    public void beginStenen(List<Steen> stenen) {
    	stenen.addAll(stenen);
    }
    
    public void tradeStenen(List<Steen> stenen) {
    	game.tradeStenen(stenen);
    }
    
    public boolean makeMove(Steen steen, int[] vakje) {
    	if (stenen.contains(steen)) {
    		boolean hasbeenplaced = getGame().place(steen, vakje);
    		if (hasbeenplaced = true) {
    			stenen.remove(steen);
    		}
    		return hasbeenplaced;
    	}
    	else {
    		return false;
    	}
    }
    
	public void reset() {
		stenen.clear();
	}
    
}
