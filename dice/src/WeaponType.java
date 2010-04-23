public enum WeaponType {
	SWORD_AND_MED_SHIELD(new int[] { 2, 5, 8 }, 2), SWORD_AND_SMALL_SHIELD(
			new int[] { 3, 7, 11 }, 1), SWORD_AND_LARGE_SHIELD(new int[] { 2,
			3, 4 }, 3), TWO_HANDED(new int[] { 4, 10, 16 }, -1), CLOSE_IN(
			new int[] { 2, 3, 4 }, 0), TWO_WEAPON(new int[] { 5, 7, 9 }, 0), NATURAL(
			new int[] { 4, 6, 8 }, 0), NONE(new int[] { 3, 4, 5 }, -2), GUN(
			new int[] { 1, 10, 100 }, -2);

	private Rollable damageDice;
	private int[] lowMiddleHigh;
	private int defensiveBonus;

	private WeaponType(int[] lowMiddleHigh, int defenseBonus) {
		this.damageDice = new Dice("2d2");
		this.lowMiddleHigh = lowMiddleHigh;
		this.defensiveBonus = defenseBonus;
	}

	public Rollable getDamageDice() {
		return damageDice;
	}

	public int getDefensiveBonus() {
		return defensiveBonus;
	}

	public int rollDamage() {
		return lowMiddleHigh[damageDice.roll() - 2];
	}

}
