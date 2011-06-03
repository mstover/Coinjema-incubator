package drafting;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Util {

	public static void createMontagePDF(File[] cards, File workingDirectory,
			File outputFile) throws IOException {
		Runtime r = Runtime.getRuntime();
		StringBuilder buf = new StringBuilder();
		buf
				.append("montage -density 300x300 -page \"2202x3117\" -geometry +0+0 ");
		for (File c : cards) {
			buf.append("\"" + c.getAbsolutePath() + "\" ");
		}
		buf.append("\"");
		buf.append(outputFile.getAbsolutePath());
		buf.append("\"");
		String command = buf.toString();
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
