
public class Compare {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int x = 1000000;
		int max = 0;
		int sum = 0;
		int numHits = 0;
		int sumHits = 0;
		FullRoll att = new FullRoll(args[0],args[1]);
		FullRoll def = new FullRoll(args[2],args[3]);
		for (int i = 0; i < x; i++) {
			int attRes = att.roll();
			int defRes = def.roll();
			int diff = attRes - defRes;
			if(diff > 0) {
				numHits++;
				sumHits += diff;
				max = Math.max(max,diff);
			}
			sum += Math.max(0, diff);
			if (i % (x/100) == 0) {
				System.out.println("damage = " + Math.max(0,diff));
			}
		}
		System.out.println("Average: " + ((double) sum / x));
		System.out.println("Hit percent: " + ((double)numHits/x));
		System.out.println("Average damage: " + ((double)sumHits)/numHits);
		System.out.println("Max damage: " + max);

	}

}
