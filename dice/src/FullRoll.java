
public class FullRoll implements Rollable {
	Dice base;
	ExplodingDice bonus;
	
	public FullRoll(String base,String bonus) {
		this.base = new Dice(base);
		this.bonus = new ExplodingDice(bonus);
	}

	@Override
	public int getMax() {
		return base.getMax() + bonus.getMax();
	}

	@Override
	public int roll() {
		return base.roll() + bonus.roll();
	}

}
