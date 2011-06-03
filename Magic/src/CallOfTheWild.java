import java.lang.reflect.InvocationTargetException;

public class CallOfTheWild extends Deck {

	// @DeckDefinition(name = "Current")
	// public void makeDeck_current() {
	// addForest(14);
	// add("Flooded Grove",4);
	// add("Wild Growth", 2);
	// add("Vivid Grove", 4);
	// add("Call of the Wild", 4);
	// add("Sylvan Library", 4);
	// add("Bird Paradise", 2);
	// add("Wall of Roots", 4);
	// add("Sterling Grove", 2);
	// add("Show and Tell", 4);
	// addMonsters(16);
	// }
	//
	// @DeckDefinition(name = "Max turn 2")
	// public void makeDeck_3() {
	// addForest(13);
	// add("Wild Growth", 4);
	// add("Llanowar Elf", 4);
	// add("Flooded Grove", 4);
	// add("Vivid Creek", 4);
	// add("Vivid Grove", 4);
	// add("Call of the Wild", 3);
	// // add("Sylvan Library", 3);
	// add("Bird Paradise", 4);
	// // add("Sterling Grove", 3);
	// add("Show and Tell", 4);
	// addMonsters(16);
	// }

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		CallOfTheWild deck = new CallOfTheWild();
		deck.makeDeck_Experiment();
		ManaStats[] stats = new ManaStats[] { new ManaStats(), new ManaStats(),
				new ManaStats(), new ManaStats(), new ManaStats(),
				new ManaStats() };
		for (int i = 0; i < 10; i++) {
			deck.shuffle();
			TurnSequence turns = deck.manaOnTurn(6);
			for (int j = 1; j <= 6; j++) {
				System.out.println("Played by turn " + j + ": "
						+ turns.turn(j).playedCards);
			}
		}
		// System.out.println("Experimental Mana on turn 6 = ");
		// for (ManaStats stat : stats)
		// System.out.println(stat.averageMana());
		//
		// deck.clear();
		// deck.makeDeck_Best();
		// stats = new ManaStats[] { new ManaStats(), new ManaStats(),
		// new ManaStats(), new ManaStats(), new ManaStats(),
		// new ManaStats() };
		// for (int i = 0; i < 10000; i++) {
		// deck.shuffle();
		// TurnSequence turns = deck.manaOnTurn(6);
		// for (int j = 0; j < 6; j++) {
		// stats[j].add(turns.turn(j + 1).getAvailableMana());
		// }
		// }
		// System.out.println("Best Mana on turn 6 = ");
		// for (ManaStats stat : stats)
		// System.out.println(stat.averageMana());
		//
		// deck.testDeck(8);
		// // System.out.println("for deck " + deck);
	}

	private void addForest(int num) {
		for (int i = 0; i < num; i++) {
			addCard(Cards.card("forest"));
		}
	}

	private void addMonsters(int num) {
		for (int i = 0; i < num; i++) {
			addCard(Cards.card("monster"));
		}
	}

	@TestMethod(name = "Monster Out")
	public boolean callOfTheWildMonster(int turn, TurnSequence mana) {
		if (turn < 2)
			return false;
		return showAndTellMonster(turn, mana)
				|| oathMonster(turn, mana)
				|| and(
						mana.canPlay(turn - 1, Cards.card("call of the wild")),
						(((has(draw(turn - 1), 1, "Call of the Wild") || (has(
								draw(turn - 2), 1, "Sterling Grove")
								|| (has(draw(turn - 2), 1, "Sylvan Library") && has(
										draw(turn + 1), 1, "Call of the Wild")) || and(
								has(turn + 11, "Call of the Wild"), has(
										turn + 6, "Index"))))) && (((has(
								draw(turn - 1), 1, "Sylvan Library") || has(
								draw(turn - 2), 1, "Sterling Grove")) && exists(
								draw(turn + 1), draw(turn + 4), "Monster"))
								|| and(has(turn + 6, "Index"), exists(turn + 7,
										turn + 11, "Monster")) || exists(
								draw(turn + 1), draw(turn + 2), "Monster"))));
	}

	@DeckDefinition(name = "Best")
	public void makeDeck_Best() {
		setPriorities();
		addForest(8);
		add("island", 6);
		add("forbidden orchard", 4);
		add("wild growth", 4);
		add("flooded grove", 4);
		add("tropical island", 4);
		add("index", 2);
		add("mystical tutor", 4);
		add("oath of druids", 4);
		add("show and tell", 4);
		addMonsters(16);
	}

	private void setPriorities() {
		playOrder.clear();
		playOrder.put("show and tell", 1);
		playOrder.put("oath of druids", 2);
		playOrder.put("mystical tutor", 3);
		playOrder.put("index", 3);
		playOrder.put("break asunder", 100);
		playOrder.put("monster", 0);
	}

	@DeckDefinition(name = "Experimental")
	public void makeDeck_Experiment() {
		setPriorities();
		addForest(8);
		add("island", 6);
		add("forbidden orchard", 4);
		add("wild growth", 4);
		add("flooded grove", 4);
		add("tropical island", 4);
		add("index", 0);
		add("break asunder", 2);
		add("mystical tutor", 4);
		add("oath of druids", 4);
		add("show and tell", 4);
		addMonsters(16);
	}

	public boolean oathMonster(int turn, TurnSequence mana) {
		if (turn < 3)
			return false;
		return and(has(turn + 6, "oath of druids"), mana.canPlay(turn, Cards
				.card("oath of druids")));
	}

	public boolean showAndTellMonster(int turn, TurnSequence mana) {
		if (turn < 2)
			return false;
		return and(mana.canPlay(turn, Cards.card("show and tell")), has(
				turn + 7, "Monster"), or(and(has(turn + 6, "mystical tutor"),
				mana.canPlay(turn - 1, Cards.card("mystical tutor"))), has(
				turn + 7, "Show and Tell"), and(mana.canPlay(turn - 1, Cards
				.card("index")), has(turn + 6, "Index"), has(turn + 11,
				"Show and Tell")), and(mana.canPlay(turn - 1, Cards
				.card("sylvan library")), has(turn + 6, "Sylvan Library"), has(
				turn + 9, "Show and Tell"))));

		// or(has(turn + 6, 1, "Bird Paradise", "Orochi Leafcaller"), has(
		// turn + 7, 1, "Vivid Grove", "Flooded Grove",
		// "Vivid Creek")), has(turn + 6, 1, "Forest",
		// "Vivid Creek", "Vivid Grove"), or(and(turn > 2, has(
		// turn + 7, 3, "Forest", "Vivid Grove", "Flooded Grove",
		// "Vivid Creek")), and(has(turn + 7, 2, "Forest",
		// "Vivid Grove", "Flooded Grove", "Vivid Creek"), has(
		// turn + 6, 1, "Llanowar Elf", "Wild Growth",
		// "Bird Paradise", "Orochi Leafcaller")), and(turn > 2,
		// has(turn + 5, 1, "Forest", "Vivid Grove",
		// "Flooded Grove", "Vivid Creek"), has(turn + 6,
		// 2, "Wall of Roots", "Wild Growth",
		// "Bird Paradise", "Orochi Leafcaller"))), or(
		// has(turn + 7, 1, "Show and Tell"), or(and(has(turn + 6,
		// 1, "Sylvan Library", "Mystic Speculation"),
		// has(turn + 9, 1, "Show and Tell")), and(has(
		// turn + 6, 1, "Index"), has(turn + 11, 1,
		// "Show and Tell")))), or(has(turn + 7, 1,
		// "Monster"), or(and(has(turn + 6, 1, "Sylvan Library",
		// "Mystic Speculation"), has(turn + 9, 1, "Monster")),
		// and(has(turn + 6, 1, "Index"), has(turn + 11, 1,
		// "Monster")))));
	}

}
