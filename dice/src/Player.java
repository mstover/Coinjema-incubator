public class Player {
	public static int averageWound = 0;

	public static int numWounds;

	Rollable checkRoll = new FullRoll("d10", "d6");

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player [armorCapacity=" + armorCapacity + ", attackSkill="
				+ attackSkill + ", bodyCapacity=" + bodyCapacity
				+ ", defenseSkill=" + defenseSkill + ", weaponForm="
				+ weaponForm + "]";
	}

	WoundSet wounds = new WoundSet();

	WeaponType weaponForm;

	int bodyCapacity;

	int armorCapacity;

	int attackSkill;

	int defenseSkill;

	private int numAttackers;

	public Player(WeaponType form, int body, int armor, int attackSkill,
			int defenseSkill) {
		this.weaponForm = form;
		this.bodyCapacity = body;
		this.armorCapacity = armor;
		this.attackSkill = attackSkill;
		this.defenseSkill = defenseSkill;
	}

	public void newRound() {
		numAttackers = 0;
	}

	public boolean isTakenOut() {
		return wounds.contains(Wound.TAKEN_OUT);
	}

	public void attackPlayer(Player player2) {
		// System.out.println("Attack:");
		int diff = (checkRoll.roll() - checkRoll.roll())
				+ (attackSkill - player2.defenseSkill)
				- player2.getDefensiveBonus();
		// System.out.println("\t diff=" + diff);
		int damTotal = rollTimes(weaponForm.getDamageDice(), diff);
		// System.out.println("\tDamage = " + damTotal);
		int factor = calcFactor(player2.bodyCapacity + player2.armorCapacity,
				damTotal);
		// System.out.println("\tfactor = " + factor);
		player2.wound(factor);
	}

	private int getDefensiveBonus() {
		// TODO Auto-generated method stub
		int bonus = weaponForm.getDefensiveBonus() - numAttackers;
		numAttackers++;
		return bonus;
	}

	private void wound(int factor) {
		if (factor > 0) {
			Wound actual = Wound.NONE;
			for (Wound w : Wound.values()) {
				if (factor >= w.multiplier()) {
					actual = w;
				}
			}
			// System.out.println("Inflicted " + actual);
			averageWound += actual.multiplier();
			numWounds++;
			wounds.add(actual);
		}
	}

	private int calcFactor(int cap, int damTotal) {
		if (damTotal < cap) {
			return 0;
		} else {
			return 1 + calcFactor(cap, damTotal - cap);
		}
	}

	private int rollTimes(Rollable damageDice, int diff) {
		if (diff <= 0) {
			return 0;
		} else {
			return damageDice.roll() + rollTimes(damageDice, diff - 1);
		}
	}

	public void refresh() {
		wounds.clear();
	}

}
