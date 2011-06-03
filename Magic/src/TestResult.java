/**
 * 
 */

/**
 * @author mstover
 * 
 */
public class TestResult {

	private int maxTurns;
	private int trials;
	private int[] success;
	private ManaStats manaStats;

	/**
	 * 
	 */
	public TestResult(int maxTurns, int trials) {
		this.maxTurns = maxTurns;
		success = new int[maxTurns];
		for (int i = 0; i < maxTurns; i++)
			success[i] = 0;
		this.trials = trials;
	}

	public void success(int turn) {
		success[turn]++;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < success.length; i++) {
			buf.append("Turn " + (i + 1) + ": "
					+ (((double) success[i] / (double) trials) * 100D));
			buf.append("%");
			buf.append("\n");
		}
		buf.append("Total Mana Used: ");
		buf.append(manaStats.averageMana());
		return buf.toString();
	}

	public void setManaStats(ManaStats manaStats) {
		this.manaStats = manaStats;

	}

}
