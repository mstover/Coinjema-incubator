package com.coinjema.acronjema.logic;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenePool {
	private static final String SUFFIX = ".properties";
	protected static final String CONFIG_NAME = "baseEvalConfig_";
	private final File directory;

	public GenePool(String dirName) {
		directory = new File(dirName);
	}

	public List<BaseEvaluatorConfig> getConfigs() throws FileNotFoundException,
			IOException {
		final List<BaseEvaluatorConfig> configs = new ArrayList<BaseEvaluatorConfig>();
		final int[] fileNum = new int[] { 0 };
		directory.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile() && pathname.getName().endsWith(SUFFIX)) {
					try {
						BaseEvaluatorConfig config = new BaseEvaluatorConfig(
								pathname);
						fileNum[0] = Math.max(
								fileNum[0],
								Integer.parseInt(pathname.getName().substring(
										CONFIG_NAME.length(),
										pathname.getName().length()
												- SUFFIX.length())));
						if (config.alive) {
							configs.add(config);
							return true;
						}
					} catch (IOException e) {
						// we'll just be returning false\
					}
				}
				return false;
			}
		});
		fileNum[0]++;
		for (int i = configs.size(); i < 100; i++) {
			configs.add(new BaseEvaluatorConfig(new File(directory, CONFIG_NAME
					+ (fileNum[0]++) + SUFFIX)));
		}
		return configs;
	}

	private int getNextFileNum() {
		final int[] fileNum = new int[] { 0 };
		directory.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile() && pathname.getName().endsWith(SUFFIX)) {
					fileNum[0] = Math.max(
							fileNum[0],
							Integer.parseInt(pathname.getName().substring(
									CONFIG_NAME.length(),
									pathname.getName().length()
											- SUFFIX.length())));
					return true;
				}
				return false;
			}
		});
		return fileNum[0] + 1;
	}

	public List<BaseEvaluatorConfig> cull(
			List<BaseEvaluatorConfig> evaluatorConfigs)
			throws FileNotFoundException, IOException {
		Collections.sort(evaluatorConfigs);
		for (int i = 0; i < 10; i++) {
			BaseEvaluatorConfig config = evaluatorConfigs.remove(0);
			config.kill();
		}
		int nextNum = getNextFileNum();
		for (int i = 0; i < 10; i++) {
			evaluatorConfigs.add(new BaseEvaluatorConfig(new File(directory,
					CONFIG_NAME + (nextNum++) + SUFFIX)));
		}
		return evaluatorConfigs;
	}
}
