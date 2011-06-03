import java.lang.reflect.InvocationTargetException;

/**
 * 
 */

/**
 * @author jaime
 * 
 */
public class AetherWarp extends Deck {

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		AetherWarp deck = new AetherWarp();
		deck.makeDeck_1();
		deck.shuffle();
		System.out.println("Cards =  " + deck.drawCards(0, 12));
		System.out.println("Played cards = "
				+ deck.manaOnTurn(6).turn(6).playedCards);
		// deck.testDeck(8);
	}

	/**
	 * 
	 */
	public AetherWarp() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param size
	 */
	public AetherWarp(int size) {
		super(size);
		// TODO Auto-generated constructor stub
	}

	@DeckDefinition(name = "Deck 1")
	public void makeDeck_1() {
		add("wall of roots", 2);
		// add(Cards.chromeMox_green, 1);
		add("forbidden orchard", 4);
		add("flooded grove", 3);
		add("vivid grove", 4);
		add("volcanic island", 4);
		add("badlands", 3);
		add("vivid crag", 4);
		add("paradise mantle", 4);
		add("aether flash", 4);
		add("dingus staff", 4);
		add("warp world", 3);
		add("hunted phantasm", 4);
		add("hunted troll", 4);
		add("infernal genesis", 3);
		add("siege-gang commander", 4);
		add("mogg infestation", 2);
		add("eternal witness", 2);
		add("sarkhan vol", 2);
	}

	@TestMethod(name = "aether Flash")
	public boolean testAetherFlash(int turn, Mana[] mana) {
		return and(has(draw(turn + 7), 1, "aether flash"), Cards.card(
				"aether flash").isPlayable(mana[turn - 1]));
	}

	@TestMethod(name = "Dingus Staff")
	public boolean testDingusStaff(int turn, Mana[] mana) {
		return and(has(draw(turn + 7), 1, "dingus staff"), Cards.card(
				"dingus staff").isPlayable(mana[turn - 1]));
	}

	@TestMethod(name = "Hunted Phantasm")
	public boolean testHuntedPhantasm(int turn, Mana[] mana) {
		return and(has(draw(turn + 7), 1, "hunted phantasm"), Cards.card(
				"hunted phantasm").isPlayable(mana[turn - 1]));
	}

	@TestMethod(name = "Hunted Troll")
	public boolean testHuntedTroll(int turn, Mana[] mana) {
		return and(has(draw(turn + 7), 1, "hunted troll"), Cards.card(
				"hunted troll").isPlayable(mana[turn - 1]));
	}

	@TestMethod(name = "Infernal Genesis")
	public boolean testInfernalGenesis(int turn, Mana[] mana) {
		return and(has(draw(turn + 7), 1, "infernal genesis"), Cards.card(
				"infernal genesis").isPlayable(mana[turn - 1]));
	}

	@TestMethod(name = "Warp World")
	public boolean testWarpWorld(int turn, Mana[] mana) {
		return and(has(draw(turn + 7), 1, "warp world"), Cards.card(
				"warp world").isPlayable(mana[turn - 1]));
	}

}
