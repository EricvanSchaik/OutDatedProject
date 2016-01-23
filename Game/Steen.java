package Game;

public class Steen {

	private int kleur;
	private int vorm;
	
    public Steen(int kleur, int vorm) {
        this.kleur = kleur;
        this.vorm = vorm;
    }
    
	public int[] getType() {
		int[] typesteen;
		typesteen = new int[2];
		typesteen[0] = this.vorm;
		typesteen[1] = this.kleur;		
		return typesteen;
	}
}
