public class BottledCloister extends Card {

	/**
	 * @param name
	 */
	public BottledCloister() {
		super("Bottled Cloister",
				new Mana(new Mana.Spec(3, Quality.COLORLESS)), Quality.SORCERY,
				Quality.COLORLESS);
	}

	@Override
	public Card clone() {
		return new BottledCloister();
	}

	@Override
	public int getCardDrawEffect() {
		return 3;
	}
}
