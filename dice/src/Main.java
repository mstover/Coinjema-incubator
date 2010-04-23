import java.util.SortedSet;
import java.util.TreeSet;

public class Main {

	/**
	 * (2d6+1) (d20:20:d6)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int x = 1000000;
		int max = 0;
		int sum = 0;
		FullRoll roll = new FullRoll(args[0],args[1]);
		for (int i = 0; i < x; i++) {
			int result = roll.roll();
			if (i % (x/100) == 0) {
				System.out.println("roll = " + result);
			}
			sum += result;
			max = Math.max(max,result);
		}
		System.out.println("Average: " + ((double) sum / x));
		System.out.println("Max: " + roll.getMax());

	}

}
