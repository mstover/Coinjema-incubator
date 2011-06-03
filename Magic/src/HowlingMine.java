/**
 * 
 */

/**
 * @author mstover
 * 
 */
public class HowlingMine extends Card {

	/**
	 * @param name
	 */
	public HowlingMine() {
		super("Howling Mine", new Mana(new Mana.Spec(2, Quality.COLORLESS)),
				Quality.ARTIFACT, Quality.COLORLESS);
	}
	
	@Override
	public int getCardDrawEffect() {
		return 1;
	}

	@Override
	public Card clone() {
		return new HowlingMine();
	}
}
