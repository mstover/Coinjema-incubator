import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Card implements Cloneable {

	private String name;

	private Set<Quality> qualities = new HashSet<Quality>();

	private Mana castingCost = Mana.NULL_MANA;

	private Mana producedMana = Mana.NULL_MANA;

	private boolean manaProducing;

	private String type;

	public Card(String name) {
		this.name = name;
	}

	public Card(String name, Mana castingCost) {
		this.name = name;
		this.castingCost = castingCost;
	}

	public Card(String name, Mana castingCost, Mana produces,
			Quality... qualities) {
		this.name = name;
		this.castingCost = castingCost;
		this.qualities.addAll(Arrays.asList(qualities));
		this.producedMana = produces;
	}

	public Card(String name, Mana castingCost, Quality... qualities) {
		this.name = name;
		this.castingCost = castingCost;
		this.qualities.addAll(Arrays.asList(qualities));
	}

	@Override
	public Card clone() {
		return new Card(name, castingCost, producedMana, qualities
				.toArray(new Quality[qualities.size()]));
	}

	public int getCardDrawEffect() {
		return 0;
	}

	public Mana getCastingCost() {
		return castingCost;
	}

	public String getName() {
		return name;
	}

	public Mana getProducedMana() {
		return producedMana;
	}

	public String getType() {
		return type;
	}

	public boolean hasOneOfQuality(Quality... quals) {
		for (Quality q : quals) {
			if (qualities.contains(q)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasQuality(Quality... quals) {
		for (Quality q : quals) {
			if (!qualities.contains(q)) {
				return false;
			}
		}
		return true;
	}

	public boolean isManaProducing() {
		return manaProducing;
	}

	public boolean isPlayable(Mana mana) {
		return mana.covers(castingCost);
	}

	public void setManaProducing(boolean manaProducing) {
		this.manaProducing = manaProducing;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProducedMana(Mana producedMana) {
		this.producedMana = producedMana;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return name;
	}

}
