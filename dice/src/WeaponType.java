public enum WeaponType {
	SWORD_AND_MED_SHIELD("d10", 2), SWORD_AND_SMALL_SHIELD("d16", 1), SWORD_AND_LARGE_SHIELD(
			"d6", 3), TWO_HANDED("d20", -1), CLOSE_IN("d6", 0), TWO_WEAPON(
			"d8+d6", 0), NATURAL("2d6", 0), NONE("2d4", -2);

	private Rollable damageDice;
	private int defensiveBonus;

	private WeaponType(String dice, int defenseBonus) {
		this.damageDice = new Dice(dice);
		this.defensiveBonus = defenseBonus;
	}

	public Rollable getDamageDice() {
		return damageDice;
	}

	public int getDefensiveBonus() {
		return defensiveBonus;
	}

	public int rollDamage() {
		return damageDice.roll();
	}

}
