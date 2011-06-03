package drafting;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileRename {
	File dir;

	public FileRename(File dir) {
		this.dir = dir;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileRename renamer = new FileRename(new File(args[0]));
		renamer.rename();
	}

	public void rename() {
		File parent = dir.getParentFile();
		TextFile tf = new TextFile();
		try {
			tf.load(parent);
		} catch (IOException e) {
			System.out.println("Invalid directory: " + dir.getAbsolutePath());
			return;
		}
		for (File f : dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String name) {
				if (name.toLowerCase().endsWith("full.jpg.png")) {
					return true;
				}
				return false;
			}
		})) {
			int index = f.getName().indexOf(".");
			String name = f.getName().substring(0, index);
			try {
				String key = tf.getFileNameFlag(name);
				f.renameTo(new File(dir, name + "." + key + ".png"));

			} catch (RuntimeException e) {
				if (name.endsWith("1")) {
					try {
						name = name.substring(0, name.length() - 1);
						String key = tf.getFileNameFlag(name);
						f.renameTo(new File(dir, name + "." + key + ".png"));
					} catch (RuntimeException e1) {
						if (!f.getName().matches(
								"[a-zA-Z \\-',]*\\d.full.jpg.png")) {
							System.out.println("no key for " + f.getAbsolutePath());
						}
					}
				} else {
					if (!f.getName().matches(
							"[a-zA-Z '\\-,]*\\d.full.jpg.png")) {
						System.out.println("no key for " + f.getAbsolutePath());
					}
				}
			}
		}
	}
}
