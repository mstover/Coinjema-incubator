public class PlayTurn {

	Mana availableMana;
	CardList playedCards;
	CardList cardsPlayedThisTurn;
	int turn = 1;
	Mana manaUsedThisTurn;

	public PlayTurn(int turn, Mana m, CardList cardsPlayedThisTurn,
			CardList played) {
		this.turn = turn;
		availableMana = new Mana(m);
		playedCards = new CardList(played);
		this.cardsPlayedThisTurn = new CardList(cardsPlayedThisTurn);
		calcManaUsed();
	}

	private void calcManaUsed() {
		manaUsedThisTurn = new Mana();
		for (Card c : cardsPlayedThisTurn) {
			if (c.hasQuality(Quality.INTRINSIC)) {
				manaUsedThisTurn.addMana(c.getCastingCost());
			}
		}
	}

	public Mana getManaUsedThisTurn() {
		return manaUsedThisTurn;
	}

	public Mana getAvailableMana() {
		return availableMana;
	}

	public void setAvailableMana(Mana availableMana) {
		this.availableMana = new Mana(availableMana);
	}

	public boolean canPlay(Card card) {
		return card.isPlayable(getAvailableMana());
	}

	public CardList getCardsPlayedThisTurn() {
		return cardsPlayedThisTurn;
	}

}
