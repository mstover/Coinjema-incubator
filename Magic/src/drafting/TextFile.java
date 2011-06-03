package drafting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TextFile {

	public static final String RARE = "rares.txt";
	public static final String UNCOMMON = "uncommons.txt";
	public static final String COMMON = "commons.txt";
	public static final String MYTHIC = "mythic.txt";

	Set<String> rares = new HashSet<String>();
	Set<String> uncommons = new HashSet<String>();
	Set<String> commons = new HashSet<String>();
	Set<String> mythic = new HashSet<String>();

	public TextFile() {
	}

	private void load(File dir, String rarity, Set<String> set)
			throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(new File(dir,
				rarity)));
		String line = f.readLine();
		while (line != null && line.length() > 0) {
			int tabIndex = line.indexOf("\t");
			if (line.charAt(0) >= '0' && line.charAt(0) <= '9' || line.charAt(0) == '\t') {
				line = line.substring(tabIndex + 1);
			}
			tabIndex = line.indexOf("\t");
			if (tabIndex > -1) {
				line = line.substring(0, tabIndex).trim();
			}
			set.add(line.trim().toLowerCase());
			line = f.readLine();
		}
	}

	public void load(File dir) throws IOException {
		load(dir, RARE, rares);
		load(dir, UNCOMMON, uncommons);
		load(dir, COMMON, commons);
		;
		load(dir, MYTHIC, mythic);
	}
	
	public List<String> getRares() {
		return new ArrayList<String>(rares);
	}
	
	public List<String> getUncommons() {
		return new ArrayList<String>(uncommons);
	}
	
	public List<String> getCommons() {
		return new ArrayList<String>(commons);
	}

	
	public List<String> getMythic() {
		return new ArrayList<String>(mythic);
	}

	public String getFileNameFlag(String name) {
		name = name.toLowerCase();
		if (rares.contains(name)) {
			return "rare";
		} else if (uncommons.contains(name)) {
			return "uncommon";
		} else if (commons.contains(name)) {
			return "common";
		} else if (mythic.contains(name)) {
			return "mythic";
		} else
			throw new RuntimeException("No Such card found");
	}
}
