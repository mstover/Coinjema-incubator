/**
 * 
 */
package drafting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * @author jaime
 * 
 */
public class DeckBuilder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new DeckBuilder(new File(args[1]), new File(args[0])).makeDeck();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	File deckFile;

	File magicDir;

	/**
	 * 
	 */
	public DeckBuilder(File deckFile, File magicDir) {
		this.deckFile = deckFile;
		this.magicDir = magicDir;
	}

	private File[] findFiles(List<String> cardList) {
		File[] files = new File[cardList.size()];
		int i = 0;
		String previous = null;
		File previousFile = null;
		for (String card : cardList) {
			if (card.equals(previous)) {
				files[i++] = previousFile;
			} else {
				files[i++] = searchForCardFile(card, magicDir);
				previousFile = files[i - 1];
				previous = card;
			}
			System.out.println("Looking for " + card + " found " + files[i - 1]);
			System.out.println("Found " + files[i - 1].getName());
		}
		return files;
	}

	private List<String> getCardList(BufferedReader in) throws IOException {
		List<String> cards = new ArrayList<String>();
		String line = in.readLine();
		while (line != null) {
			line = line.trim();
			StringTokenizer st = new StringTokenizer(line, "\t:");
			try {
				String cardName = st.nextToken().trim();
				int num = Integer.parseInt(st.nextToken().trim());
				for (int i = 0; i < num; i++) {
					System.out.println("Adding: " + cardName);
					cards.add(cardName);
				}
			} catch (NoSuchElementException e) {

			}
			line = in.readLine();
		}
		while (cards.size() % 9 != 0) {
			cards.add(cards.get(cards.size() - 1));
		}
		return cards;
	}

	public void makeDeck() throws IOException {
		montageCardList(findFiles(getCardList(new BufferedReader(
				new FileReader(deckFile)))));

	}

	private void montageCardList(File[] files) throws IOException {
		int count = 1;
		File newDir = new File(deckFile.getParentFile(), deckFile.getName()
				.substring(0, deckFile.getName().indexOf(".")));
		newDir.mkdirs();
		for (int i = 0; i < files.length; i += 9) {
			File[] page = new File[9];
			for (int j = 0; j < 9; j++) {
				page[j] = files[i + j];
			}
			Util.createMontagePDF(page, deckFile.getParentFile(), new File(
					newDir, "deck_" + count + ".pdf"));
			count++;
		}
	}

	private File searchForCardFile(final String card, File dir) {
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.toLowerCase().startsWith(card.toLowerCase() + ".")
						&& name.toLowerCase().endsWith(".png");
			}
		});
		if (files != null && files.length == 1 && !files[0].isDirectory()) {
			return files[0];
		}
		File[] dirs = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory()
						&& !pathname.getName().equals("Tokens")
						&& !pathname.getName().equals("Vanguard")
						&& !pathname.getName().equals("Promos")
						&& !pathname.getName().equals("Arena")
						&& !pathname.getName().equals("Full-Sized Unofficial")
						&& !pathname.getName().equals("Previews");
			}
		});
		for (File d : dirs) {
			File f = searchForCardFile(card, d);
			if (f != null)
				return f;
		}
		return null;
	}

}
