/**
 * 
 */

public enum Wound {
	NONE(0), TRIVIAL(1), MINOR(2), NORMAL(3), MAJOR(4), TAKEN_OUT(8);

	private float multiplier;

	private Wound(float mult) {
		this.multiplier = mult;
	}

	public float multiplier() {
		return multiplier;
	}
}