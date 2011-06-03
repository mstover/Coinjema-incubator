import java.lang.reflect.InvocationTargetException;

/**
 * 
 */

/**
 * @author mstover
 * 
 */
public class EverPest extends Deck {

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		EverPest deck = new EverPest();
		deck.makeDeck_1();
		ManaStats stats = new ManaStats();
		for (int i = 0; i < 1000; i++) {
			deck.shuffle();
			stats.add(deck.manaOnTurn(4).turn(4).getAvailableMana());
		}
		System.out.println("Mana on turn 4 = " + stats.averageMana());
		deck.testDeck(8);
	}

	/**
	 * 
	 */
	public EverPest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param size
	 */
	public EverPest(int size) {
		super(size);
		// TODO Auto-generated constructor stub
	}

	public boolean hasPestilenceAndEnduring(int turn, TurnSequence mana) {
		return and(mana.canPlay(turn, Cards.card("Enduring Renewal")), mana
				.canPlay(turn, Cards.card("Pestilence")), or(has(
				draw(turn - 1), 2, Cards.card("Enlightened Tutor")), has(
				draw(turn), 1, "Enduring Renewal"), and(or(has(draw(turn),
				"demonic tutor"), has(draw(turn - 1), 1, "Enlightened Tutor")),
				has(draw(turn), 1, "Pestilence"))), or(has(draw(turn), 1,
				"Pestilence"), and(or(has(draw(turn), "demonic tutor"), has(
				draw(turn - 1), 1, "Enlightened Tutor")), has(draw(turn), 1,
				"Enduring Renewal"))));
	}

	@DeckDefinition(name = "Deck 1")
	public void makeDeck_1() {
		add(Cards.card("Scrublands"), 4);
		add(Cards.card("fetid heath"), 4);
		add(Cards.card("plains"), 6);
		add(Cards.card("swamp"), 6);
		add(Cards.card("forbidden orchard"), 4);
		add(Cards.card("Enduring Renewal"), 4);
		add(Cards.card("Pestilence"), 3);
		add(Cards.card("Enlightened Tutor"), 4);
		add(Cards.card("paradise mantle"), 4);
		add(Cards.card("Auriok Champion"), 4);
		add(Cards.card("carrion feeder"), 4);
		add(Cards.card("ornithopter"), 4);
		add(Cards.card("Soul warden"), 4);
		add("demonic tutor", 1);
		add("Stitch Together", 4);
	}

	@DeckDefinition(name = "Deck 2")
	public void makeDeck_2() {
		add(Cards.card("Scrublands"), 4);
		add(Cards.card("fetid heath"), 4);
		add(Cards.card("plains"), 6);
		add(Cards.card("swamp"), 6);
		add(Cards.card("forbidden orchard"), 4);
		add(Cards.card("Enduring Renewal"), 4);
		add(Cards.card("Pestilence"), 4);
		add(Cards.card("Enlightened Tutor"), 4);
		add(Cards.card("paradise mantle"), 4);
		add(Cards.card("Auriok Champion"), 4);
		add(Cards.card("carrion feeder"), 4);
		add(Cards.card("ornithopter"), 4);
		add(Cards.card("Soul warden"), 4);
		add("demonic tutor", 1);
		add("stitch together", 3);
	}

	@TestMethod(name = "EverPest")
	public boolean testEverPest(int turn, TurnSequence mana) {
		return testInfiniteLife(turn, mana) || testPestilenceHealth(turn, mana);

		// Cards.enduringRenewal.isPlayable(mana[turn - 1])
		// && Cards.pestilence.isPlayable(mana[turn - 1])
		// && (or(and(has(draw(turn - 1), 1, "Ornithopter")), and(has(
		// draw(turn - 1), 1, "Soul Warden"), Cards.soulWarden
		// .isPlayable(mana[turn - 2])), and(has(draw(turn - 1),
		// 1, "Auriok Champion"), Cards.auriokChampion
		// .isPlayable(mana[turn - 2])), and(has(draw(turn - 1),
		// 1, "Carrion Feeder"), Cards.carrionFeeder
		// .isPlayable(mana[turn - 2]))))
		// && (has(draw(turn), 1, "Enduring Renewal") || has(
		// draw(turn - 1), 1, "Enlightened Tutor"))
		// && (has(draw(turn), 1, "Pestilence") || has(draw(turn - 1), 1,
		// "Enlightened Tutor"))
		// && and(has(draw(turn), 1, "Auriok Champion", "Soul Warden"),
		// has(draw(turn), 1, "Carrion Feeder", "Ornithopter"),
		// has(draw(turn), 3, Quality.CREATURE));
	}

	@TestMethod(name = "Perfection")
	public boolean testInfiniteLife(int turn, TurnSequence mana) {
		return and(mana.canPlay(turn, Cards.card("enduring renewal")), mana
				.canPlay(turn, Cards.card("carrion feeder")), has(draw(turn),
				1, "Soul Warden", "Auriok Champion"), has(draw(turn), 1,
				"Carrion Feeder"), or(has(draw(turn), 1, "Ornithopter"), has(
				draw(turn - 1), 1, "Enlightened Tutor", "demonic tutor")), or(
				has(draw(turn), 1, "Enduring Renewal"), has(draw(turn - 1), 1,
						"Enlightened Tutor", "demonic tutor")));
	}

	public boolean testPestilenceHealth(int turn, TurnSequence mana) {
		return and(mana.canPlay(turn, Cards.card("enduring renewal")), mana
				.canPlay(turn, Cards.card("pestilence")), has(draw(turn), 1,
				"Soul Warden", "Auriok Champion"), has(draw(turn), 3,
				Quality.CREATURE), hasPestilenceAndEnduring(turn, mana));
	}

}
