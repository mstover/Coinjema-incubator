/**
 * 
 */

/**
 * @author jaime
 * 
 */
public class GimmeTwoCards extends Card {

	/**
	 * @param name
	 */
	public GimmeTwoCards() {
		super("Gimme Two Cards", new Mana(new Mana.Spec(2, Quality.WHITE)),
				Quality.INSTANT, Quality.WHITE);
	}

	@Override
	public Card clone() {
		return new GimmeTwoCards();
	}

	@Override
	public int getCardDrawEffect() {
		return 2;
	}

}
