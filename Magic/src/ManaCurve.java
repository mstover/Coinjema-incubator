import java.lang.reflect.InvocationTargetException;

/**
 * 
 */

/**
 * @author mstover
 * 
 */
public class ManaCurve extends Deck {

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		ManaCurve deck = new ManaCurve();
		deck.make20Deck();

		ManaStats[] stats = new ManaStats[] { new ManaStats(), new ManaStats(),
				new ManaStats(), new ManaStats(), new ManaStats(),
				new ManaStats() };
		for (int i = 0; i < 100000; i++) {
			deck.shuffle();
			TurnSequence turns = deck.manaOnTurn(6);
			for (int j = 0; j < 6; j++) {
				stats[j].add(turns.turn(j + 1).getAvailableMana());
			}
		}
		System.out.println("20-deck Mana on turn 6 = ");
		for (ManaStats stat : stats)
			System.out.println(stat.averageMana());

		deck.clear();
		deck.make16Deck();

		stats = new ManaStats[] { new ManaStats(), new ManaStats(),
				new ManaStats(), new ManaStats(), new ManaStats(),
				new ManaStats() };
		for (int i = 0; i < 100000; i++) {
			deck.shuffle();
			TurnSequence turns = deck.manaOnTurn(6);
			for (int j = 0; j < 6; j++) {
				stats[j].add(turns.turn(j + 1).getAvailableMana());
			}
		}
		System.out.println("20-deck Mana on turn 6 = ");
		for (ManaStats stat : stats)
			System.out.println(stat.averageMana());

		deck.clear();
		deck.makeMainDeck();

		stats = new ManaStats[] { new ManaStats(), new ManaStats(),
				new ManaStats(), new ManaStats(), new ManaStats(),
				new ManaStats() };
		for (int i = 0; i < 100000; i++) {
			deck.shuffle();
			TurnSequence turns = deck.manaOnTurn(6);
			for (int j = 0; j < 6; j++) {
				stats[j].add(turns.turn(j + 1).getAvailableMana());
			}
		}
		System.out.println("20-deck Mana on turn 6 = ");
		for (ManaStats stat : stats)
			System.out.println(stat.averageMana());
		// deck.testDeck(8);
	}

	private void getFibonnaciGoal(int goal) {
		if (goal == 1) {
			goal = 2;
		} else if (goal == 2) {
			goal = 3;
		} else if (goal == 3) {
			goal = 4;
		} else if (goal == 4) {
			goal = 6;
		} else if (goal == 5) {
			goal = 7;
		} else if (goal == 6) {
			goal = 9;
		} else if (goal == 7) {
			goal = 10;
		} else if (goal == 8) {
			goal = 12;
		}
	}

	@DeckDefinition(name = "16")
	public void make16Deck() {
		add(Cards.card("forest"), 22);
		add(Cards.card("birds of paradise"), 5);
		add("Other", 33);
	}

	@DeckDefinition(name = "20")
	public void make20Deck() {
		add(Cards.card("forest"), 20);
		add(Cards.card("birds of paradise"), 7);
		add(Cards.card("wild growth"), 0);
		add("Other", 33);
	}

	@DeckDefinition(name = "main")
	public void makeMainDeck() {
		add(Cards.card("forest"), 21);
		add(Cards.card("birds of paradise"), 6);
		add(Cards.card("wall of roots"), 0);
		add("Other", 33);
	}

	@TestMethod(name = "+1 times")
	public boolean testFibbonacciManaCurve(int turn) {
		int goal = turn + 1;
		boolean success = has(turn + 7, goal, "Basic Land",
				"Secondary Mana Source - 1", "Secondary Mana Source - 2");
		success = success && has(9, 2, "Basic Land");
		success = success
				&& has(turn + 7, 1, "Secondary Mana Source - 1",
						"Secondary Mana Source - 2");
		return success;
	}

	@TestMethod(name = "1x mana")
	public boolean testLinearManaCurve(int turn) {
		int goal = turn;
		getFibonnaciGoal(goal);
		boolean success = has(turn + 7, turn, "Basic Land",
				"Secondary Mana Source - 1", "Secondary Mana Source - 2");
		success = success && has(9, 2, "Basic Land");
		return success;
	}
}
