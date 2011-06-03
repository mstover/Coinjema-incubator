import java.lang.reflect.InvocationTargetException;

/**
 * 
 */

/**
 * @author mstover
 * 
 */
public class TwoColorDeck extends Deck {

	@DeckDefinition(name = "best 26-mana deck")
	public void makeBest26Deck() {
		add(Cards.card("plains"), 4);
		add(Cards.card("swamp"), 4);
		add(Cards.card("forbidden orchard"), 4);
		add(Cards.card("fetid heath"), 4);
		add(Cards.card("scrublands"), 4);
		add(Cards.card("Orzhov signet"), 2);
		add(Cards.card("chrome mox"), 4);
		add(Cards.card("2WW"), 34);
	}

	@DeckDefinition(name = "best deck")
	public void makeBestDeck() {
		add(Cards.card("plains"), 5);
		add(Cards.card("swamp"), 4);
		add(Cards.card("forbidden orchard"), 4);
		add(Cards.card("fetid heath"), 4);
		add(Cards.card("scrublands"), 4);
		add(Cards.card("Orzhov signet"), 7);
		add(Cards.card("2WW"), 32);
	}

	@DeckDefinition(name = "best 20-land deck")
	public void makeBest20landDeck() {
		add(Cards.card("plains"), 4);
		add(Cards.card("swamp"), 4);
		add(Cards.card("forbidden orchard"), 4);
		add(Cards.card("fetid heath"), 4);
		add(Cards.card("scrublands"), 4);
		add(Cards.card("Orzhov signet"), 4);
		add(Cards.card("chrome mox"), 4);
		add(Cards.card("2WW"), 32);
	}

	@DeckDefinition(name = "all land deck")
	public void makeExperimentalDeck() {
		add(Cards.card("plains"), 8);
		add(Cards.card("swamp"), 8);
		add(Cards.card("forbidden orchard"), 4);
		add(Cards.card("fetid heath"), 4);
		add(Cards.card("scrublands"), 4);
		add(Cards.card("Orzhov signet"), 0);
		add(Cards.card("2WW"), 32);
	}

	@DeckDefinition(name = "only simple land deck")
	public void makeSimpleLandDeck() {
		add(Cards.card("plains"), 10);
		add(Cards.card("swamp"), 10);
		add(Cards.card("fetid heath"), 4);
		add(Cards.card("scrublands"), 4);
		add(Cards.card("2WW"), 32);
	}

	@DeckDefinition(name = "only basic land deck")
	public void makeBasicLandDeck() {
		add(Cards.card("plains"), 14);
		add(Cards.card("swamp"), 14);
		add(Cards.card("2WW"), 32);
	}

	@DeckDefinition(name = "signet deck")
	public void makeExperimental2Deck() {
		add(Cards.card("plains"), 5);
		add(Cards.card("swamp"), 5);
		add(Cards.card("forbidden orchard"), 4);
		add(Cards.card("fetid heath"), 4);
		add(Cards.card("Scrublands"), 4);
		add(Cards.card("orzhov signet"), 6);
		add(Cards.card("2WW"), 32);
	}

	@DeckDefinition(name = "mox deck")
	public void makeMoxDeck() {
		add(Cards.card("plains"), 6);
		add(Cards.card("swamp"), 6);
		add(Cards.card("fetid heath"), 4);
		add(Cards.card("Scrublands"), 4);
		add(Cards.card("forbidden orchard"), 4);
		add(Cards.card("chrome mox"), 4);
		add(Cards.card("2WW"), 32);
	}

	@TestMethod(name = "can cast 2/2 white/black cards")
	public boolean test4Casting(int turn, TurnSequence mana) {
		return mana.canPlay(turn, Cards.card("2WW"));
	}

	// @TestMethod(name = "can cast 1/1 white/black cards")
	// public boolean test2Casting(int turn, TurnSequence mana) {
	// return mana.canPlay(turn, Cards.card("1W"));
	// }
	//
	// @TestMethod(name = "can cast 3/3 white/black cards")
	// public boolean test6Casting(int turn, TurnSequence mana) {
	// return mana.canPlay(turn, Cards.card("3WWW"));
	// }
	//
	// @TestMethod(name = "can cast 4/4 white/black cards")
	// public boolean test8Casting(int turn, TurnSequence mana) {
	// return mana.canPlay(turn, Cards.card("4WWWW"));
	// }

	/**
	 * @param args
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		TwoColorDeck deck = new TwoColorDeck();
		deck.testDeck(10);

	}

}
