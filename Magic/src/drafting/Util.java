package drafting;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Util {

	public static void createMontagePDF(File[] cards, File workingDirectory,
			File outputFile) throws IOException {
		Runtime r = Runtime.getRuntime();
		List<String> buf = new ArrayList<>();
		buf				.add("montage");
		buf.add("-density");
		buf.add("300x300");
		buf.add("-page");
		buf.add("2202x3117");
		buf.add("-geometry");
		buf.add("+0+0");
		for (File c : cards) {
			buf.add(c.getAbsolutePath());
		}
		buf.add(outputFile.getAbsolutePath());
		String[] command = (String[])buf.toArray(new String[buf.size()]);
		System.out.println("Running " + command);
		Process p = r.exec(command, null, workingDirectory);
		BufferedReader read = new BufferedReader(new InputStreamReader(p
				.getErrorStream()));
		BufferedReader out = new BufferedReader(new InputStreamReader(p
				.getInputStream()));
		String line = read.readLine();
		while (line != null && line.length() > 0) {
			System.out.println(line);
			line = read.readLine();
		}
		read.close();
		String outLine = out.readLine();
		while (outLine != null && outLine.length() > 0) {
			System.out.println(outLine);
			outLine = out.readLine();
		}
		out.close();
	}

}
