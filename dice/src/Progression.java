import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

public class Progression {

	static int[] sides = new int[] { 4, 6, 8, 10, 12, 20 };

	static TreeMap<Double, String> results = new TreeMap<Double, String>();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		for (int c = 0; c < 1; c++) {
			for (int num1 = 1; num1 < 3; num1++) {
				for (int sides1 = 0; sides1 < 5; sides1++) {
					for (int j = 20; j > 14; j--) {
						int die1 = sides[sides1];

						int checkDie = 20;

						String base = ((num1 > 1) ? num1 : "") + "d" + die1;
						String bonus = "d" + checkDie + ":" + j + ":" + base;
						FullRoll roll = new FullRoll("0", bonus);
						results.put(getAverage(roll), "0 " + bonus);
					}
				}
			}
		}
		File f = new File("results.txt");
		PrintWriter fp = new PrintWriter(new FileWriter(f));
		for (Double k : results.keySet()) {
			fp.println(results.get(k) + " : " + k);
		}
		fp.close();

	}

	private static double getAverage(FullRoll roll) {
		int sum = 0;
		int x = 100000;
		for (int i = 0; i < x; i++) {
			sum += roll.roll();
		}
		return (double) sum / x;
	}

}
