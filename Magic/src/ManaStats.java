import java.util.Set;

public class ManaStats {

	private static final int GREEN = 1;
	private static final int BLUE = 2;
	private static final int RED = 3;
	private static final int WHITE = 4;
	private static final int COLORLESS = 5;
	private static final int BLACK = 0;

	float[] manaCounts = new float[6];

	int count = 0;

	public void add(Mana m) {
		for (Mana.Spec sp : m.getManaCount()) {
			Set<Quality> colors = sp.getColors();
			for (Quality c : colors) {
				switch (c) {
				case COLORLESS:
					manaCounts[COLORLESS] += (float) sp.getQuantity()
							/ (float) colors.size();
					break;
				case GREEN:
					manaCounts[GREEN] += (float) sp.getQuantity()
							/ (float) colors.size();
					break;
				case BLUE:
					manaCounts[BLUE] += (float) sp.getQuantity()
							/ (float) colors.size();
					break;
				case WHITE:
					manaCounts[WHITE] += (float) sp.getQuantity()
							/ (float) colors.size();
					break;
				case BLACK:
					manaCounts[BLACK] += (float) sp.getQuantity()
							/ (float) colors.size();
					break;
				case RED:
					manaCounts[RED] += (float) sp.getQuantity()
							/ (float) colors.size();
					break;

				}
			}
		}
		count++;
	}

	public Mana averageMana() {
		return new Mana(
				new Mana.Spec(Quality.GREEN, manaCounts[GREEN] / count),
				new Mana.Spec(Quality.RED, manaCounts[RED] / count),
				new Mana.Spec(Quality.BLACK, manaCounts[BLACK] / count),
				new Mana.Spec(Quality.BLUE, manaCounts[BLUE] / count),
				new Mana.Spec(Quality.WHITE, manaCounts[WHITE] / count),
				new Mana.Spec(Quality.COLORLESS, manaCounts[COLORLESS] / count));
	}

}
