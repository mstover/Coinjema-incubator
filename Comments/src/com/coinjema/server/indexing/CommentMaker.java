package com.coinjema.server.indexing;

import java.util.Random;

public class CommentMaker {

	private CommentCollection comments;

	private static Random rand = new Random();

	public CommentMaker(CommentCollection c) {
		comments = c;
	}

	public void add(int num,long storyId) {
		for (int i = 0; i < num; i++) {
			comments.addComment("Mike", generateText(), comments
					.getRandomComment(),storyId);
		}

	}

	private String generateText() {
		StringBuilder buf = new StringBuilder();
		int size = rand.nextInt(80);
		for (int i = 0; i < size; i++) {
			int wordSize = rand.nextInt(10);
			for (int j = 0; j < wordSize; j++) {
				if (j == 0) {
					buf.append("T");
				} else {
					buf.append("a");
				}
			}
			buf.append(" ");
		}
		return buf.toString();
	}

}
