import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Cards {

	static Map<String, Card> cards = parseCards();

	private static Map<String, Card> parseCards() {
		try {
			Map<String, Card> cards = new HashMap<String, Card>();
			BufferedReader f = new BufferedReader(new FileReader(System
					.getProperty("cardlist", "cardlist.txt")));
			String line = f.readLine();
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line, ":");
				String name = st.nextToken();
				String cost = st.nextToken();
				String produces = st.nextToken();
				StringTokenizer qualities = new StringTokenizer(st.nextToken(),
						",");
				Card c = new Card(name, getCost(cost), getCost(produces),
						getQualities(qualities));
				cards.put(name.toUpperCase(), c);
				line = f.readLine();
			}
			// System.out.println("Cards = " + cards);
			return cards;
		} catch (Exception e) {
			System.out.println("Failed to load cardlist");
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

	public static Card card(String name) {
		if (!cards.containsKey(name.toUpperCase())) {
			throw new RuntimeException("Failed to find card named " + name);
		}
		return cards.get(name.toUpperCase());
	}

	private static Quality[] getQualities(StringTokenizer st) {
		List<Quality> qualities = new ArrayList<Quality>();
		while (st.hasMoreTokens()) {
			qualities.add(Quality.valueOf(st.nextToken().toUpperCase()));
		}
		return (Quality[]) qualities.toArray(new Quality[qualities.size()]);
	}

	private static Mana.Spec makePart(Set<Character> parts) {
		try {
			int amount = Integer.parseInt(String.valueOf(parts.iterator()
					.next()));
			if (amount > 0) {
				return new Mana.Spec(amount, Quality.COLORLESS);
			} else {
				return new Mana.Spec(0, Quality.COLORLESS);
			}
		} catch (NumberFormatException e) {
			// noop
		}
		List<Quality> qualities = new ArrayList<Quality>();
		for (char c : parts) {
			qualities.add(getColorQuality(c));
		}
		return new Mana.Spec(1, (Quality[]) qualities
				.toArray(new Quality[qualities.size()]));
	}

	private static Quality getColorQuality(char c) {
		switch (c) {
		case 'r':
		case 'R':
			return Quality.RED;
		case 'u':
		case 'U':
			return Quality.BLUE;
		case 'b':
		case 'B':
			return Quality.BLACK;
		case 'w':
		case 'W':
			return Quality.WHITE;
		case 'g':
		case 'G':
			return Quality.GREEN;
		default:
			return Quality.COLORLESS;
		}
	}

	private static Mana getCost(String cost) {
		List<Mana.Spec> parts = new ArrayList<Mana.Spec>();
		boolean slash = false;
		Set<Character> set = new HashSet<Character>();
		char current;
		for (char c : cost.toCharArray()) {
			current = c;
			if (slash) {
				if (current != '/') {
					set.add(c);
				}
			} else if (current != '/') {
				if (set.size() > 0) {
					parts.add(makePart(set));
				}
				set.clear();
				set.add(c);
			} else {
				slash = true;
			}

		}
		if (set.size() > 0)
			parts.add(makePart(set));
		// System.out.println("Cost = "
		// + cost
		// + " = "
		// + new Mana((Mana.Spec[]) parts.toArray(new Mana.Spec[parts
		// .size()])));
		return new Mana((Mana.Spec[]) parts
				.toArray(new Mana.Spec[parts.size()]));
	}

	private final static Card orochiLeafcaller = new Card("Orochi Leafcaller",
			Mana.GREEN, new Mana(new Mana.Spec(1, Quality.RED, Quality.WHITE,
					Quality.BLACK, Quality.BLUE, Quality.GREEN)), Quality.GREEN);

	private final static Card floodedGrove = new Card("Flooded Grove",
			Mana.NULL_MANA, new Mana(new Mana.Spec(1, Quality.GREEN,
					Quality.BLUE)), Quality.PRODUCES_IMMEDIATELY,
			Quality.MANA_SOURCE, Quality.LAND);

	private final static Card vividCreek = new Card("Vivid Creek",
			Mana.NULL_MANA, new Mana(new Mana.Spec(1, Quality.GREEN,
					Quality.WHITE, Quality.BLACK, Quality.BLUE, Quality.RED)),
			Quality.LAND, Quality.MANA_SOURCE);

	private final static Card vividCrag = new Card("Vivid Crag",
			Mana.NULL_MANA, new Mana(new Mana.Spec(1, Quality.GREEN,
					Quality.WHITE, Quality.BLACK, Quality.BLUE, Quality.RED)),
			Quality.LAND, Quality.MANA_SOURCE);

	private final static Card vividGrove = new Card("Vivid Grove",
			Mana.NULL_MANA, new Mana(new Mana.Spec(1, Quality.GREEN,
					Quality.WHITE, Quality.BLACK, Quality.BLUE, Quality.RED)),
			Quality.LAND, Quality.MANA_SOURCE);

	private final static Card wallOfRoots = new Card("Wild Growth", new Mana(
			new Mana.Spec(2, Quality.GREEN)), Mana.GREEN, Quality.MANA_SOURCE,
			Quality.GREEN, Quality.PRODUCES_IMMEDIATELY);

	private final static Card birdsOfParadise = new Card("Bird Paradise",
			Mana.GREEN, new Mana(new Mana.Spec(1, Quality.GREEN, Quality.WHITE,
					Quality.BLACK, Quality.BLUE, Quality.RED)),
			Quality.MANA_SOURCE, Quality.GREEN);

	private static final Card blackWhite4 = new Card(
			"Black/White 2/2 mana test", new Mana(new Mana.Spec(2,
					Quality.WHITE), new Mana.Spec(2, Quality.BLACK)),
			Quality.BLACK, Quality.WHITE);

	private static final Card blackWhite2 = new Card(
			"Black/White 1/1 mana test", new Mana(new Mana.Spec(1,
					Quality.WHITE), new Mana.Spec(1, Quality.BLACK)),
			Quality.BLACK, Quality.WHITE);

	private static final Card blackWhite6 = new Card(
			"Black/White 3/3 mana test", new Mana(new Mana.Spec(3,
					Quality.WHITE), new Mana.Spec(3, Quality.BLACK)),
			Quality.BLACK, Quality.WHITE);

	private static final Card blackWhite8 = new Card(
			"Black/White 4/4 mana test", new Mana(new Mana.Spec(4,
					Quality.WHITE), new Mana.Spec(4, Quality.BLACK)),
			Quality.BLACK, Quality.WHITE);

	private static final Card forbiddenOrchard = new Card("Forbidden Orchard",
			Mana.NULL_MANA, new Mana(new Mana.Spec(1, Quality.GREEN,
					Quality.WHITE, Quality.BLACK, Quality.BLUE, Quality.RED)),
			Quality.LAND, Quality.MANA_SOURCE, Quality.PRODUCES_IMMEDIATELY);

	private static final Card dingusStaff = new Card("Dingus Staff", new Mana(
			new Mana.Spec(4, Quality.COLORLESS)), Quality.COLORLESS,
			Quality.ARTIFACT);

	private static final Card aetherFlash = new Card("Aether Flash",
			new Mana(new Mana.Spec(2, Quality.COLORLESS), new Mana.Spec(2,
					Quality.RED)), Quality.RED, Quality.ENCHANTMENT);

	private static final Card warpWorld = new Card("Warp World",
			new Mana(new Mana.Spec(5, Quality.COLORLESS), new Mana.Spec(3,
					Quality.RED)), Quality.SORCERY, Quality.RED);

	private static final Card huntedPhantasm = new Card("Hunted Phantasm",
			new Mana(new Mana.Spec(1, Quality.COLORLESS), new Mana.Spec(2,
					Quality.BLUE)), Quality.CREATURE, Quality.BLUE);

	private static final Card huntedTroll = new Card("Hunted Troll", new Mana(
			new Mana.Spec(2, Quality.COLORLESS),
			new Mana.Spec(2, Quality.GREEN)), Quality.CREATURE, Quality.GREEN);

	private static final Card infernalGenesis = new Card("Infernal Genesis",
			new Mana(new Mana.Spec(4, Quality.COLORLESS), new Mana.Spec(2,
					Quality.BLACK)), Quality.ENCHANTMENT, Quality.BLACK);

	private static final Card siegeGangCommander = new Card(
			"Siege-Gang Commander", new Mana(
					new Mana.Spec(3, Quality.COLORLESS), new Mana.Spec(2,
							Quality.RED)), Quality.CREATURE, Quality.RED);

	private static final Card moggInfestation = new Card("Mogg Infestation",
			new Mana(new Mana.Spec(3, Quality.COLORLESS), new Mana.Spec(2,
					Quality.RED)), Quality.SORCERY, Quality.RED);

	private static final Card eternalWitness = new Card("Eternal Witness",
			new Mana(new Mana.Spec(1, Quality.COLORLESS), new Mana.Spec(2,
					Quality.GREEN)), Quality.CREATURE, Quality.GREEN);

	private static final Card sarkhanVol = new Card("Sarkhan Vol", new Mana(
			new Mana.Spec(2, Quality.COLORLESS), new Mana.Spec(1, Quality.RED),
			new Mana.Spec(1, Quality.GREEN)), Quality.PLANESWALKER,
			Quality.GREEN, Quality.RED);

	private static final Card desolationGiant = new Card("Desolation Giant",
			new Mana(new Mana.Spec(2, Quality.COLORLESS), new Mana.Spec(2,
					Quality.RED), new Mana.Spec(2, Quality.WHITE)),
			Quality.CREATURE, Quality.RED);

	private final static Card wildGrowth = new Card("Wild Growth", Mana.GREEN,
			Mana.GREEN, Quality.MANA_SOURCE, Quality.GREEN);

	private static final Card mysticTutor = new Card("Mystic Tutor", Mana.BLUE,
			Quality.INSTANT, Quality.BLUE);

	private static final Card oathOfDruids = new Card("Oath of Druids",
			new Mana(new Mana.Spec(1, Quality.COLORLESS), new Mana.Spec(1,
					Quality.GREEN)), Quality.ENCHANTMENT, Quality.GREEN);

	private static final Card index = new Card("Index", Mana.BLUE,
			Quality.BLUE, Quality.SORCERY);
}
