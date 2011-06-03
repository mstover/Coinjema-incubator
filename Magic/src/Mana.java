import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Mana {

	public static class Spec implements Comparable {

		private final Quality color1;
		private final Quality color2;
		private final Quality color3;
		private final Quality color4;
		private final Quality color5;
		private final float quantity;

		public Spec(float quantity, Quality... color) {
			if (color != null) {
				switch (color.length) {
				case 1:
					color1 = color[0];
					color2 = color3 = color4 = color5 = null;
					break;
				case 2:
					color1 = color[0];
					color2 = color[1];
					color3 = color4 = color5 = null;
					break;
				case 3:
					color1 = color[0];
					color2 = color[1];
					color3 = color[2];
					color4 = color5 = null;
					break;
				case 4:
					color1 = color[0];
					color2 = color[1];
					color3 = color[2];
					color4 = color[3];
					color5 = null;
					break;
				case 5:
					color1 = color[0];
					color2 = color[1];
					color3 = color[2];
					color4 = color[3];
					color5 = color[4];
					break;
				default:
					color1 = color2 = color3 = color4 = color5 = null;
				}
			} else
				color1 = color2 = color3 = color4 = color5 = null;
			this.quantity = quantity;
		}

		public Spec(float quantity, Quality c1, Quality c2, Quality c3,
				Quality c4, Quality c5) {
			this.quantity = quantity;
			color1 = c1;
			color2 = c2;
			color3 = c3;
			color4 = c4;
			color5 = c5;
		}

		public Spec(Quality color, float quantity) {
			this.color1 = color;
			color2 = color3 = color4 = color5 = null;
			this.quantity = quantity;
		}

		@Override
		public int compareTo(Object arg0) {
			Spec other = (Spec) arg0;
			if (color1 != other.color1 && color1 == Quality.COLORLESS) {
				return -1;
			} else if (color1 != other.color1
					&& other.color1 == Quality.COLORLESS) {
				return 1;
			}
			if (quantity == other.quantity) {
				if (color1 == other.color1) {
					return 0;
				} else {
					return color1.ordinal() - other.color1.ordinal();
				}

			} else {
				return (int) (other.quantity - quantity);
			}
		}

		private boolean coversAColor(Spec other) {
			boolean res = other.color1 == Quality.COLORLESS
					|| color1 == other.color1;
			if (res)
				return res;
			else {
				if (color2 != null) {
					res = color2 == other.color1 || color2 == other.color2
							|| color2 == other.color3 || color2 == other.color4
							|| color2 == other.color5;
					if (res)
						return res;
				}
				if (color3 != null) {
					res = color3 == other.color1 || color3 == other.color2
							|| color3 == other.color3 || color3 == other.color4
							|| color3 == other.color5;
					if (res)
						return res;
				}
				if (color4 != null) {
					res = color4 == other.color1 || color4 == other.color2
							|| color4 == other.color3 || color4 == other.color4
							|| color4 == other.color5;
					if (res)
						return res;
				}
				if (color5 != null) {
					res = color5 == other.color1 || color5 == other.color2
							|| color5 == other.color3 || color5 == other.color4
							|| color5 == other.color5;
					if (res)
						return res;
				}
			}
			return false;
		}

		private boolean equals(Quality color1, Quality color2) {
			return color1 == color2;
		}

		public Set<Quality> getColors() {
			Set<Quality> colors = new HashSet<Quality>();
			if (color1 != null) {
				colors.add(color1);
			}
			if (color2 != null) {
				colors.add(color2);
			}
			if (color3 != null) {
				colors.add(color3);
			}
			if (color4 != null) {
				colors.add(color4);
			}
			if (color5 != null) {
				colors.add(color5);
			}
			return colors;
		}

		public int getNumColors() {
			int sum = 0;
			if (color1 != null) {
				sum++;
			}
			if (color2 != null) {
				sum++;
			}
			if (color3 != null) {
				sum++;
			}
			if (color4 != null) {
				sum++;
			}
			if (color5 != null) {
				sum++;
			}
			return sum;
		}

		public float getQuantity() {
			return quantity;
		}

		public boolean paysForAll(Spec other) {
			if (coversAColor(other)) {
				if (quantity >= other.quantity) {
					return true;
				}
			}
			return false;
		}

		public boolean paysForPart(Spec other) {
			if (coversAColor(other)) {
				return quantity < other.quantity;
			}
			return false;
		}

		public Spec reduceBy(Spec sp) {
			return new Spec(quantity - sp.quantity, color1, color2, color3,
					color4, color5);
		}

		@Override
		public String toString() {
			return color1.toString() + (color2 != null ? "/" + color2 : "")
					+ (color3 != null ? "/" + color3 : "")
					+ (color4 != null ? "/" + color4 : "")
					+ (color5 != null ? "/" + color5 : "") + ":" + quantity;
		}
	}

	public static final Mana COLORLESS = new Mana(
			new Spec(1, Quality.COLORLESS));
	public static Mana NULL_MANA = new Mana();
	public static Mana GREEN = new Mana(new Spec(1, Quality.GREEN));
	public static Mana BLUE = new Mana(new Spec(1, Quality.BLUE));
	public static Mana WHITE = new Mana(new Spec(1, Quality.WHITE));
	public static Mana BLACK = new Mana(new Spec(1, Quality.BLACK));
	public static Mana RED = new Mana(new Spec(1, Quality.RED));

	public static void main(String[] args) {
		Mana pool = new Mana(new Spec(1, Quality.GREEN, Quality.WHITE),
				new Spec(Quality.BLACK, 2));
		Mana cost = new Mana(new Spec(Quality.GREEN, 2), new Spec(
				Quality.BLACK, 1));
		System.out.println(pool.covers(cost));
		cost = new Mana(new Spec(Quality.GREEN, 1), new Spec(Quality.BLACK, 1),
				new Spec(Quality.BLACK, 1));
		System.out.println(pool.covers(cost));
		cost = new Mana(new Spec(Quality.WHITE, 1), new Spec(Quality.COLORLESS,
				1), new Spec(Quality.BLACK, 1));
		System.out.println(pool.covers(cost));
		cost = new Mana(new Spec(Quality.COLORLESS, 2), new Spec(
				Quality.COLORLESS, 1), new Spec(Quality.BLACK, 1));
		System.out.println(pool.covers(cost));
		pool = new Mana(new Spec(Quality.BLACK, 2), new Spec(1, Quality.GREEN,
				Quality.WHITE));
		cost = new Mana(new Spec(Quality.COLORLESS, 1), new Spec(
				Quality.COLORLESS, 1), new Spec(Quality.BLACK, 1));
		System.out.println(pool.covers(cost));
	}

	private List<Spec> manaCount = new ArrayList<Spec>();

	private float totalMana = 0;

	public Mana() {

	}

	public Mana(Mana currentMana) {
		this(currentMana, null);
	}

	public Mana(Mana currentMana, Card land) {
		add(currentMana);
		if (land != null) {
			add(land);
		}
		sort();
	}

	public Mana(Spec... specs) {
		manaCount.addAll(Arrays.asList(specs));
		sort();
	}

	public synchronized void add(Card land) {
		updateTotalMana();
		add(land.getProducedMana());
		sort();
	}

	private void add(Mana producedMana) {
		manaCount.addAll(producedMana.manaCount);
	}

	public void addMana(CardList currentTurnPerms) {
		updateTotalMana();
		for (Card card : currentTurnPerms) {
			if (card.hasQuality(Quality.MANA_SOURCE,
					Quality.PRODUCES_IMMEDIATELY)) {
				add(card.getProducedMana());
			}
		}
		sort();
	}

	public void addMana(Mana m) {
		updateTotalMana();
		add(m);
		sort();
	}

	public boolean covers(Mana castingCost) {
		if (getTotal() >= castingCost.getTotal()) {
			List<Spec> temp = new ArrayList<Spec>(manaCount);
			for (Spec sp : castingCost.manaCount) {
				Spec remainder = sp;
				while (remainder != null) {
					Spec extra = null;
					Spec t = null;
					for (Spec pool : temp) {
						if (pool.coversAColor(remainder)) {
							if (pool.quantity < remainder.quantity) {
								t = pool;
								remainder = remainder.reduceBy(pool);
								extra = null;
								break;
							} else {
								t = pool;
								extra = pool.reduceBy(remainder);
								remainder = null;
								break;
							}
						}

					}
					if (t != null) {
						temp.remove(t);
					} else
						return false;
					if (extra != null) {
						temp.add(extra);
					}
				}
			}
			return true;
		}
		return false;
	}

	public List<Spec> getManaCount() {
		return manaCount;
	}

	public synchronized float getTotal() {
		if (totalMana > 0)
			return totalMana;
		for (Spec sp : manaCount) {
			totalMana += sp.quantity;
		}
		return totalMana;
	}

	public synchronized void reduceBy(Mana castingCost) {
		List<Spec> temp = manaCount;
		for (Spec sp : castingCost.manaCount) {
			Spec remainder = sp;
			while (remainder != null) {
				Spec extra = null;
				Spec t = null;
				for (Spec pool : temp) {
					if (pool.paysForPart(remainder)) {
						t = pool;
						remainder = remainder.reduceBy(pool);
						extra = null;
						break;
					} else if (pool.paysForAll(remainder)) {
						t = pool;
						extra = pool.reduceBy(remainder);
						remainder = null;
						break;
					}

				}
				if (t != null) {
					temp.remove(t);
				} else
					throw new RuntimeException(
							"Can't reduce by casting cost that can't be paid");
				if (extra != null) {
					temp.add(extra);
				}
			}
		}
		updateTotalMana();
	}

	public synchronized void setMana(CardList playedPermanents,
			CardList currentTurnPerms) {
		manaCount.clear();
		updateTotalMana();
		for (Card card : playedPermanents) {
			if (card.hasQuality(Quality.MANA_SOURCE)) {
				add(card.getProducedMana());
			}
		}
		for (Card card : currentTurnPerms) {
			if (card.hasQuality(Quality.MANA_SOURCE,
					Quality.PRODUCES_IMMEDIATELY)) {
				add(card.getProducedMana());
			}
		}
		sort();
	}

	private void sort() {
		Collections.sort(manaCount);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getTotal() + "[");
		for (Spec sp : manaCount) {
			sb.append(sp);
			sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	private void updateTotalMana() {
		totalMana = 0;
	}
}
