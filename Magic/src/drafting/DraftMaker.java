package drafting;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DraftMaker {
	static Random rand = new Random();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		DraftMaker maker = new DraftMaker(new File(args[0]), Integer
				.parseInt(args[1]));
		maker.makePacks();
	}

	File dir;

	int numPacks;

	public DraftMaker(File dir, int numPacks) throws IOException {
		this.dir = dir;
		this.numPacks = numPacks;
	}

	private void createPack(File[] cards, File dir, int count)
			throws IOException {
		File packs = new File(dir, "packs");
		packs.mkdirs();
		Util
				.createMontagePDF(cards, dir, new File(packs, "p" + count
						+ ".pdf"));
		// Runtime r = Runtime.getRuntime();
		// StringBuilder buf = new StringBuilder();
		// buf.append("montage -density 300x300 -page \"2202x3117\" -geometry +0+0 ");
		// for (File c : cards) {
		// buf.append("\"" + c.getName() + "\" ");
		// }
		// File packDir = new File(dir, "packs");
		// packDir.mkdirs();
		// buf.append("packs/p" + count + ".pdf");
		// String command = buf.toString();
		// System.out.println("Running " + command);
		// Process p = r.exec(command, null, dir);
		// BufferedReader read = new BufferedReader(new InputStreamReader(p
		// .getErrorStream()));
		// BufferedReader out = new BufferedReader(new InputStreamReader(p
		// .getInputStream()));
		// String line = read.readLine();
		// while (line != null && line.length() > 0) {
		// System.out.println(line);
		// line = read.readLine();
		// }
		// read.close();
		// String outLine = out.readLine();
		// while (outLine != null && outLine.length() > 0) {
		// System.out.println(outLine);
		// outLine = out.readLine();
		// }
		// out.close();
	}

	private File[] getFiles(final String string) {
		return dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (!name.matches("(Mountain|Plains|Island|Forest|Swamp)\\..+")) {
					return name.indexOf("." + string + ".") > -1;
				}
				return false;
			}
		});
	}

	public void makePacks() throws IOException {

		File[] rares = getFiles("rare");
		File[] uncommons = getFiles("uncommon");
		File[] commons = getFiles("common");
		File[] mythic = getFiles("mythic");

		File[] cards = new File[9];
		for (int i = 0; i < numPacks; i++) {
			int r = rand.nextInt(8);
			if (mythic.length > 0 && r == 0) {
				r = rand.nextInt(mythic.length);
				cards[0] = mythic[r];
			} else {
				r = rand.nextInt(rares.length);
				cards[0] = rares[r];
			}
			r = rand.nextInt(uncommons.length);
			cards[1] = uncommons[r];
			Set<Integer> n = new HashSet<Integer>();
			n.add(r);
			do {
				r = rand.nextInt(uncommons.length);
			} while (n.contains(r));
			cards[2] = uncommons[r];
			n.clear();
			for (int j = 3; j < 9; j++) {
				do {
					r = rand.nextInt(commons.length);
				} while (n.contains(r));
				n.add(r);
				cards[j] = commons[r];
			}
			System.out.println("Cards = " + Arrays.asList(cards));
			createPack(cards, dir, i);
		}
	}

}
