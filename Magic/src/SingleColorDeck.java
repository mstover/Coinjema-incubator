import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * @author mstover
 * 
 */
public class SingleColorDeck extends Deck {

	public static class TestOne implements Runnable {
		Deck deck;
		static SingleColorDeck[] best5Decks = new SingleColorDeck[5];

		public TestOne(Deck d) {
			deck = d;
		}

		public void run() {
			try {
				while (true) {
					deck.testDeck(20);
					int place = testIfBest();
					if (place > -1) {
						deck.trials = 1000;
						deck.testDeck(20);
						deck.trials = 10;
						updateBestList();
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private int testIfBest() {
			int place = -1;
			for (int i = 0; i < 5; i++) {
				if (best5Decks[i] == null
						|| best5Decks[i].tempTestStats.averageMana().getTotal() < deck.tempTestStats
								.averageMana().getTotal()) {
					place = i;
					break;
				}
			}
			return place;
		}

		private void updateBestList() {
			synchronized (best5Decks) {
				int place;
				place = testIfBest();
				if (place > -1) {
					SingleColorDeck temp = best5Decks[place];
					SingleColorDeck d = new SingleColorDeck();
					d.deck = new CardList(deck.deck);
					d.tempTestStats = deck.tempTestStats;
					best5Decks[place] = d;
					while (place < 4) {
						SingleColorDeck temp2 = best5Decks[place + 1];
						best5Decks[place + 1] = temp;
						temp = temp2;
						place++;
					}
					System.out.println();
					for (Deck xx : best5Decks) {
						System.out.println(xx);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		SingleColorDeck deck = new SingleColorDeck();
		TestOne one = new TestOne(new SingleColorDeck());
		// TestOne two = new TestOne(new SingleColorDeck());
		new Thread(one).start();
		// new Thread(two).start();
	}

	Random rand = new Random();

	int iteration = 0;

	int numLands = 0;

	private CardList intrinsicCards;

	/**
	 * 
	 */
	public SingleColorDeck() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param size
	 */
	public SingleColorDeck(int size) {
		super(size);
		// TODO Auto-generated constructor stub
	}

	@DeckDefinition(name = "28 mana")
	public void makeDeck1() {
		setPriorities();
		add(Cards.card("plains"), 20 + rand.nextInt(8));
		add(Cards.card("plains growth"), rand.nextInt(4));
		Card[] cards = { Cards.card("W"), Cards.card("1W"), Cards.card("2W"),
				Cards.card("2WW"), Cards.card("3WW"), Cards.card("3WWW"),
				Cards.card("4WWW"), Cards.card("4WWWW"), Cards.card("6WWW"),
				Cards.card("7WWW") };
		int sum = deck.size();
		int[] numEach = new int[cards.length];
		int remainder = 60 - sum;
		for (int i = 0; i < numEach.length && remainder > 0; i++) {
			if (i == 9) {
				numEach[9] = remainder;
			} else {
				// if (i == 0) {
				// numEach[0] = rand.nextBoolean() ? rand.nextInt(4) : 0;
				// } else {
				numEach[i] = rand.nextInt(remainder);
				// }
				remainder -= numEach[i];
			}
		}
		for (int i = 0; i < numEach.length; i++) {
			add(cards[rand.nextInt(8)], numEach[i]);
		}
	}

	// @DeckDefinition(name = "Random")
	public void makeDeck5() {
		setPriorities();
		int sum = 0;
		int iteration = this.iteration % 16;
		int numWildGrowth = iteration / 4;

		sum += numWildGrowth;
		add(Cards.card("plains growth"), numWildGrowth);
		int numSignets = iteration % 4;
		sum += numSignets;
		add(Cards.card("orzhov signet"), numSignets);
		int numLands = (iteration == 0) ? (20 - sum) + rand.nextInt(13)
				: this.numLands - sum;
		this.numLands = (iteration == 0) ? numLands : this.numLands;
		add(Cards.card("plains"), numLands);
		sum += numLands;
		Card[] cards = { Cards.card("W"), Cards.card("1W"), Cards.card("2W"),
				Cards.card("2WW"), Cards.card("3WW"), Cards.card("3WWW"),
				Cards.card("4WWW"), Cards.card("4WWWW") };
		int[] numEach = new int[cards.length];
		int remainder = 60 - sum;
		for (int i = 0; i < numEach.length && remainder > 0; i++) {
			if (i == 7) {
				numEach[7] = remainder;
			} else {
				numEach[i] = rand.nextInt(remainder);
				remainder -= numEach[i];
			}
		}
		for (int i = 0; i < 8; i++) {
			add(cards[rand.nextInt(8)], numEach[i]);
		}
		this.iteration++;
	}

	private void setPriorities() {
		playOrder.clear();
		playOrder.put("Bottled Cloister", 0);
		playOrder.put("4WWWW", 1);
		playOrder.put("4WWW", 2);
		playOrder.put("3WWW", 3);
		playOrder.put("3WW", 4);
		playOrder.put("2WW", 5);
		playOrder.put("2W", 6);
		playOrder.put("1W", 7);
		playOrder.put("W", 8);
	}

	@TestMethod(name = "can cast ")
	public boolean test4Casting(int turn, TurnSequence mana) {
		return mana.canPlay(turn, Cards.card("2WW"));
	}

}
