import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ExplodingDice implements Rollable {
	private static final int maxCheck = 1000;
	Die checkDie = new Die(20);
	List<ExplodingDie> bonusPacks = new ArrayList<ExplodingDie>();

	public ExplodingDice(String spec) {
		StringTokenizer tokens = new StringTokenizer(spec, "/");
		while (tokens.hasMoreTokens()) {
			bonusPacks.add(new ExplodingDie(tokens.nextToken()));
		}
	}

	@Override
	public int getMax() {
		int max = 0;
		for (int i = 0; i < maxCheck; i++) {
			max = Math.max(max, roll());
		}
		return max;
	}

	@Override
	public int roll() {
		int sum = 0;
		boolean[] success = new boolean[bonusPacks.size()];
		for (int i = 0; i < success.length; i++)
			success[i] = true;
		do {
			int check = checkDie.roll();
			for (int i = 0; i < success.length; i++) {
				if (!success[i])
					continue;
				if (check >= bonusPacks.get(i).getTarget()) {
					sum += bonusPacks.get(i).roll();
				} else {
					success[i] = false;
				}
			}
		} while (hasSuccess(success));
		return sum;
	}

	private boolean hasSuccess(boolean[] success) {
		for (boolean b : success) {
			if (b)
				return true;

		}
		return false;
	}

	private class ExplodingDie implements Rollable {

		Dice bonusDice;
		int target;

		public ExplodingDie(String spec) {
			StringTokenizer t = new StringTokenizer(spec, ":");
			String targetSpec = null;
			if (t.hasMoreTokens()) {
				targetSpec = t.nextToken();
			}
			if (t.hasMoreTokens()) {
				bonusDice = new Dice(t.nextToken());
				target = Integer.parseInt(targetSpec);
			} else {
				bonusDice = new Dice(targetSpec);
				target = 20;
			}
		}

		public int getTarget() {
			return target;
		}

		public void setTarget(int target) {
			this.target = target;
		}

		public int roll() {
			return bonusDice.roll();
		}

		public int getMax() {
			return bonusDice.getMax();
		}
	}
}
