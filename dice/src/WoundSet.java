public class WoundSet {

	int[] wounds = new int[Wound.values().length];

	public boolean contains(Wound w) {
		return wounds[w.ordinal()] > 0;
	}

	public void add(Wound w) {
		if (w == Wound.NONE) {
			return;
		}
		if (wounds[w.ordinal()] > 0) {
			if (w == Wound.TAKEN_OUT) {
				return;
			}
			add(Wound.values()[w.ordinal() + 1]);
		} else {
			wounds[w.ordinal()]++;
		}

	}

	public void clear() {
		wounds = new int[Wound.values().length];

	}

}
