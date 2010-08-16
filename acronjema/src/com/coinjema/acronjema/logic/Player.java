package com.coinjema.acronjema.logic;

import java.io.IOException;

public class Player {
	private final MoveTree tree;
	Board b;
	private boolean gold;
	AlphaBetaThinker brain;
	private Move moveService = new Move();

	public Player(MoveTree tree, boolean color) {
		this.tree = tree;
		this.b = tree.board;
		gold = color;
		brain = new AlphaBetaThinker(tree);

	}

	public void startUp() throws Exception {
		byte[] in = new byte[10];
		for (int i = 0; i < 32; i++) {
			if (i < 8) {
				System.out.print("gold rabbit? ");
				System.in.read(in);
				b.addPiece(1, true, new SquareDesignation(in));
			} else if (i < 10) {
				System.out.print("gold cat? ");
				System.in.read(in);
				b.addPiece(2, true, new SquareDesignation(in));
			} else if (i < 12) {
				System.out.print("gold dog? ");
				System.in.read(in);
				b.addPiece(3, true, new SquareDesignation(in));
			} else if (i < 14) {
				System.out.print("gold horse? ");
				System.in.read(in);
				b.addPiece(4, true, new SquareDesignation(in));
			} else if (i < 15) {
				System.out.print("gold camel? ");
				System.in.read(in);
				b.addPiece(5, true, new SquareDesignation(in));
			} else if (i < 16) {
				System.out.print("gold elephant? ");
				System.in.read(in);
				b.addPiece(6, true, new SquareDesignation(in));
			} else if (i < 24) {
				System.out.print("silver rabbit? ");
				System.in.read(in);
				b.addPiece(1, false, new SquareDesignation(in));
			} else if (i < 26) {
				System.out.print("silver cat? ");
				System.in.read(in);
				b.addPiece(2, false, new SquareDesignation(in));
			} else if (i < 28) {
				System.out.print("silver dog? ");
				System.in.read(in);
				b.addPiece(3, false, new SquareDesignation(in));
			} else if (i < 30) {
				System.out.print("silver horse? ");
				System.in.read(in);
				b.addPiece(4, false, new SquareDesignation(in));
			} else if (i < 31) {
				System.out.print("silver camel? ");
				System.in.read(in);
				b.addPiece(5, false, new SquareDesignation(in));
			} else {
				System.out.print("silver elephant? ");
				System.in.read(in);
				b.addPiece(6, false, new SquareDesignation(in));
			}
		}
		gold = true;
		b.print(System.out);
		System.out.print("What color am I? ");
		System.in.read(in);
		if (in[0] == 'g') {
			gold = true;
		} else {
			gold = false;
		}
		System.out.println("I am playing " + (gold ? "gold" : "silver"));
		playGame();

	}

	private void playGame() {
		boolean turn = true;
		int count = 0;
		while (b.getWinner() == null) {
			if (turn == gold) {
				try {
					int move = findMoves();
					b.executeMove(move);
					System.out.println("Move " + (count++) + (turn ? "g" : "s")
							+ ": " + moveService.toString(move));
					b.print(System.out);
					turn = !turn;
				} catch (GameEndException e) {
					b.setWinner(!turn);
				}
			} else {
				String instructions = null;
				while (instructions == null) {
					try {
						instructions = readIn();

					} catch (Exception e) {
						System.out.print("Come again? ");
					}
				}
				int move = moveService.parse(instructions);

				b.executeMove(move);
				System.out.println("Move " + move + (turn ? "g" : "s") + ": "
						+ moveService.toString(move));
				b.print(System.out);
				turn = !turn;
			}
		}
		System.out.println("The winner is "
				+ (b.getWinner() ? "gold" : "silver"));

	}

	private String readIn() {
		byte[] in = new byte[40];
		try {
			System.out.print("Enter Move: ");
			System.in.read(in);
			String s = new String(in);
			s = s.substring(0, s.length() - 1);
			return s;
		} catch (IOException e) {
			System.out.println("Bad move text.  Please try again");
			return readIn();
		}
	}

	public int findMoves() throws GameEndException {
		return brain.inSearchOf(tree);
		// sorter.sort(tree.moves, tree.moves.position(), b, gold);
		// if (tree.moves.position() == 0) {
		// throw new GameEndException();
		// }
		// tree.sortPly(0, tree.getFirstNumber(), gold ? IntTimSort.DESC_SORTER
		// : IntTimSort.ASC_SORTER);
		// return tree.moves.get(0);

	}

	public void executeMove(int move) {
		brain.executeMove(move);

	}

}
