import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Deck {

	private int deckSize = 60;

	protected int trials = 10;

	private Random rand = new Random();

	protected CardList deck = new CardList(deckSize);

	protected Map<String, Integer> playOrder = new HashMap<String, Integer>();

	protected ManaStats tempTestStats;

	public Deck() {

	}

	public Deck(int size) {
		deckSize = size;
		deck = new CardList(deckSize);
	}

	protected void add(Card card, int num) {
		for (int i = 0; i < num; i++) {
			addCard(card.clone());
		}
	}

	protected void add(String cardName, int num) {
		for (int i = 0; i < num; i++) {
			addCard(Cards.card(cardName).clone());
		}
	}

	protected void addCard(Card c) {
		deck.add(c);
	}

	protected boolean and(boolean... bs) {
		for (boolean b : bs) {
			if (!b)
				return b;
		}
		return true;
	}

	protected void assertSize() {
		if (!(deck.size() == deckSize))
			throw new RuntimeException("Wrong deck size (" + deck.size() + ")");
	}

	protected void clear() {
		deck.clear();
	}

	protected int draw(int turn) {
		return turn + 7;
	}

	protected CardList drawCards(int index, int numCards) {
		CardList cards = deck.subList(index, numCards);
		return cards;
	}

	protected int drawNumOccurrences(int numCards, Card card) {
		List<Card> draw = getDraw(numCards);
		int count = 0;
		for (Card c : draw) {
			if (c.getName().equals(card.getName())) {
				count++;
			}
		}
		return count;
	}

	protected int drawNumOccurrences(int numCards, Quality q) {
		List<Card> draw = getDraw(numCards);
		int count = 0;
		for (Card c : draw) {
			if (c.hasQuality(q)) {
				count++;
			}
		}
		return count;
	}

	protected int drawNumOccurrences(int numCards, String cardName) {
		List<Card> draw = getDraw(numCards);
		int count = 0;
		for (Card c : draw) {
			if (c.getName().equals(cardName)) {
				count++;
			}
		}
		return count;
	}

	protected boolean exists(int start, int end, Card card) {
		for (int i = start; i < end; i++) {
			if (deck.get(i).getName().equals(card.getName())) {
				return true;
			}
		}
		return false;
	}

	protected boolean exists(int start, int end, String cardName) {
		return exists(start, end, Cards.card(cardName));
	}

	private CardList findBestLand(CardList hand, Mana mana) {
		CardList result = new CardList();
		CardList lands = hand.getCards(Quality.LAND);
		CardList secondarySources = hand.getCards(new CardTester() {
			@Override
			public boolean accept(Card card) {
				return card.hasQuality(Quality.MANA_SOURCE)
						&& !card.hasQuality(Quality.LAND);
			}
		});
		if (lands.size() == 0) {
			result.add(null);
		}
		for (Card c : secondarySources) {
			for (int i = 0; i < 2; i++) {
				for (Card land : lands) {
					if (i == 1 || land.hasQuality(Quality.PRODUCES_IMMEDIATELY)) {
						if (c
								.isPlayable(new Mana(
										mana,
										land
												.hasQuality(Quality.PRODUCES_IMMEDIATELY) ? result
												.size() == 0 ? land : null
												: null))) {
							if (result.size() == 0) {
								result.add(land);
								if (land
										.hasQuality(Quality.PRODUCES_IMMEDIATELY)) {
									mana.add(land);
								}
							}
							result.add(c);
							mana.reduceBy(c.getCastingCost());
						}
					}
				}
			}
			if ((result.size() == 0 || result.get(0) == null)
					&& c.isPlayable(mana)) {
				result.add(c);
				mana.reduceBy(c.getCastingCost());
			}
		}
		if (result.size() == 0) {
			if (lands.size() > 0) {
				CardList l = lands.getCards(Quality.PRODUCES_IMMEDIATELY);
				if (l.size() > 0) {
					result.add(l.get(0));
					mana.add(l.get(0));
				} else {
					result.add(lands.get(0));
				}
			}
		}
		return result;
	}

	private CardList findPlayableCards(CardList hand, Mana mana) {
		CardList played = new CardList();
		Collections.sort(hand, new Comparator<Card>() {

			@Override
			public int compare(Card o1, Card o2) {
				return (playOrder.containsKey(o1.getName().toLowerCase()) ? playOrder
						.get(o1.getName().toLowerCase())
						: Integer.MAX_VALUE)
						- (playOrder.containsKey(o2.getName().toLowerCase()) ? playOrder
								.get(o2.getName().toLowerCase())
								: Integer.MAX_VALUE);
			}
		});
		for (Card c : hand) {
			if (!c.hasQuality(Quality.LAND) && c.isPlayable(mana)) {
				played.add(c);
				mana.reduceBy(c.getCastingCost());
			}
		}
		return played;
	}

	protected CardList getDraw(int numCards) {
		return deck.subList(0, numCards);
	}

	private int getDrawNumber(CardList playedCards) {
		int sum = 0;
		List<Card> removal = new LinkedList<Card>();
		for (Card c : playedCards) {
			sum += c.getCardDrawEffect();
			if (c.hasOneOfQuality(Quality.INSTANT, Quality.SORCERY)) {
				removal.add(c);
			}
		}
		for (Card c : removal)
			playedCards.remove(c);
		return sum;
	}

	private int getNewIndex(int index, int skip, List<Card> shuffled) {
		do {
			index = (index + 1) % deckSize;
			if (shuffled.get(index) == null) {
				skip--;
			}
		} while (skip > 0);
		return index;
	}

	private Collection<? extends Card> getPrimaryNonLandProducers(
			List<Card> hand) {
		List<Card> producers = new LinkedList<Card>();
		for (Card c : hand) {
			if (c.isPlayable(Mana.NULL_MANA)
					&& c.hasQuality(Quality.MANA_SOURCE)
					&& !c.hasQuality(Quality.LAND))
				producers.add(c);
		}
		return producers;
	}

	protected boolean has(int numCards, Card card) {
		return getDraw(numCards).contains(card.getName());
	}

	protected boolean has(int numCards, Card... cards) {
		return has(numCards, 1, cards);
	}

	protected boolean has(int numCards, int numOccurrences, Card card) {
		return drawNumOccurrences(numCards, card) >= numOccurrences;
	}

	protected boolean has(int numCards, int numOccurrences, Card... cards) {
		int sum = 0;
		for (Card c : cards) {
			sum += drawNumOccurrences(numCards, c);
		}
		return sum >= numOccurrences;
	}

	protected boolean has(int numCards, int numOccurrences,
			List<String> cardNames) {
		int sum = 0;
		for (String n : cardNames) {
			sum += drawNumOccurrences(numCards, n);
		}
		return sum >= numOccurrences;
	}

	protected boolean has(int numCards, int numOccurrences, Quality q) {
		return drawNumOccurrences(numCards, q) >= numOccurrences;
	}

	protected boolean has(int numCards, int numOccurrences, String cardName) {
		return has(numCards, numOccurrences, Cards.card(cardName));
	}

	protected boolean has(int numCards, int numOccurrences, String... cardNames) {
		int sum = 0;
		for (String n : cardNames) {
			sum += drawNumOccurrences(numCards, Cards.card(n));
		}
		return sum >= numOccurrences;
	}

	protected boolean has(int numCards, String cardName) {
		return has(numCards, Cards.card(cardName));
	}

	protected boolean has(int numCards, String... cardNames) {
		return has(numCards, 1, cardNames);
	}

	private void initList(List<Card> shuffled) {
		for (int i = 0; i < deckSize; i++) {
			shuffled.add(null);
		}
	}

	public TurnSequence manaOnTurn(int turn) {
		TurnSequence playTurns = new TurnSequence();
		Mana startingMana = new Mana();
		Mana currentMana = new Mana();
		CardList playedPermanents = new CardList();
		CardList currentTurnPermanents = new CardList();
		CardList hand = drawCards(0, 7);
		int index = 7;

		for (int i = 1; i <= turn; i++) {
			int numCardsToDraw = 1 + getDrawNumber(playedPermanents);
			hand.addAll(drawCards(index, numCardsToDraw));
			index += numCardsToDraw;
			// System.out.println("hand: " + hand);
			currentTurnPermanents.addAll(getPrimaryNonLandProducers(hand));
			currentMana.setMana(playedPermanents, currentTurnPermanents);
			startingMana.setMana(playedPermanents, currentTurnPermanents);
			CardList landAndSource = findBestLand(hand, currentMana);
			if (landAndSource.get(0) != null) {
				currentTurnPermanents.add(landAndSource.get(0));
				if (landAndSource.get(0).hasQuality(
						Quality.PRODUCES_IMMEDIATELY)) {
					startingMana.add(landAndSource.get(0));
				}
			}
			if (landAndSource.size() > 1) {
				currentMana.addMana(landAndSource.subList(1, landAndSource
						.size() - 1));
				currentTurnPermanents.addAll(landAndSource.subList(1,
						landAndSource.size() - 1));
			}
			hand.removeAll(currentTurnPermanents);
			currentTurnPermanents.addAll(findPlayableCards(hand, currentMana));
			playedPermanents.addAll(currentTurnPermanents);
			hand.removeAll(currentTurnPermanents);
			// System.out.println("Played on turn " + i + ": "
			// + currentTurnPermanents);
			playTurns.addTurn(new PlayTurn(i, startingMana,
					currentTurnPermanents, playedPermanents));
			currentTurnPermanents.clear();
		}
		return playTurns;
	}

	protected boolean or(boolean... bs) {
		for (boolean b : bs) {
			if (b)
				return b;
		}
		return false;
	}

	private TestResult runTest(Method m, int maxTurns)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		TestResult result = new TestResult(maxTurns, trials);
		ManaStats manaStats = new ManaStats();
		for (int i = 0; i < trials; i++) {
			shuffle();
			TurnSequence playTurns = manaOnTurn(maxTurns);
			Mana manaInterval = new Mana();
			for (int turn = 1; turn <= maxTurns; turn++) {
				if ((Boolean) m.invoke(this, new Object[] { turn, playTurns })) {
					result.success(turn - 1);
				}
				manaInterval.addMana(playTurns.getTotalManaUsed(turn));
			}
			manaStats.add(manaInterval);
		}
		result.setManaStats(manaStats);
		tempTestStats = manaStats;
		return result;
	}

	protected void shuffle() {
		CardList shuffled = new CardList(deckSize);
		initList(shuffled);
		int count = deckSize;
		int index = 0;
		for (int i = 0; i < deckSize; i++) {
			int skip = rand.nextInt(count--) + 1;
			index = getNewIndex(index, skip, shuffled);
			shuffled.set(index, deck.get(i));
		}
		deck = shuffled;
	}

	public void testDeck(int turns) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		for (Method m : this.getClass().getMethods()) {
			TestMethod dtm = m.getAnnotation(TestMethod.class);
			if (dtm != null) {
				// System.out.println("Test: " + dtm.name());
				for (Method d : getClass().getMethods()) {
					DeckDefinition dd = d.getAnnotation(DeckDefinition.class);
					if (dd != null) {
						// System.out.println(dd.name() + ": ");
						clear();
						d.invoke(this);
						assertSize();
						runTest(m, turns);
					}
				}
				// System.out.println();
			}
		}
	}

	@Override
	public String toString() {
		int count = 0;
		StringBuilder builder = new StringBuilder();
		Map<String, Integer> cardCount = new HashMap<String, Integer>();
		for (Card c : deck) {
			if (!cardCount.containsKey(c.getName())) {
				cardCount.put(c.getName(), 0);
			}
			cardCount.put(c.getName(), cardCount.get(c.getName()) + 1);
		}
		builder.append(cardCount.toString());
		builder.append("\n\t");
		if (tempTestStats != null)
			builder.append(tempTestStats.averageMana());
		return builder.toString();
	}
}
