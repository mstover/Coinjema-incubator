package com.coinjema.acronjema.logic;

import java.util.Random;

public class ComputerPlayer implements Player {
	private final MoveTree tree;
	Board b;
	private boolean gold;
	AlphaBetaThinker brain;
	private Move moveService = new Move();
	private long timePerMove;
	public long timeRemaining = 0;
	private Evaluator evaluator;

	public ComputerPlayer(MoveTree tree, boolean color, long timePerMove,
			Evaluator eval) {
		this.tree = tree;
		this.b = tree.board;
		gold = color;
		this.timePerMove = timePerMove;
		this.evaluator = eval;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Player#setup()
	 */
	@Override
	public void setup() {
		for (int i = (gold ? 0 : 56); i < (gold ? 8 : 64); i++) {
			if (b.squares[i].isEmpty()) {
				b.addPiece(1, gold, i);
			}
		}
		Random rand = new Random();
		for (int i = 0; i < 8; i++) {
			int count = rand.nextInt(8 - i);
			for (int j = (gold ? 8 : 48); (count >= 0)
					&& (j < (gold ? 16 : 56)); j++) {
				if (b.squares[j].isEmpty()) {
					if (count == 0) {
						b.addPiece(getStrength(i), gold, j);
					}
					count--;
				}

			}
		}
	}

	private static int getStrength(int i) {
		switch (i) {
		case 0:
			return 6;
		case 1:
			return 5;
		case 2:
		case 3:
			return 4;
		case 4:
		case 5:
			return 3;
		case 6:
		case 7:
			return 2;
		}
		return 0;
	}

	private void playGame() {
		boolean turn = true;
		int count = 0;
		while (b.getWinner() == null) {
			if (turn == gold) {
				try {
					int move = findMoves();
					b.executeMove(move);
					b.toggleTurn();
					System.out.println("Move " + (count++) + (turn ? "g" : "s")
							+ ": " + moveService.toString(move));
					b.print(System.out);
					turn = !turn;
				} catch (GameEndException e) {
					b.setWinner(!turn);
				}
			} else {
			}
		}
		System.out.println("The winner is "
				+ (b.getWinner() ? "gold" : "silver"));

	}

	public int findMoves() throws GameEndException {
		if (brain == null) {
			brain = new AlphaBetaThinker(tree, evaluator);
		}
		return brain.inSearchOf(tree, timePerMove, timePerMove
				+ (timeRemaining / 2));
		// sorter.sort(tree.moves, tree.moves.position(), b, gold);
		// if (tree.moves.position() == 0) {
		// throw new GameEndException();
		// }
		// tree.sortPly(0, tree.getFirstNumber(), gold ? IntTimSort.DESC_SORTER
		// : IntTimSort.ASC_SORTER);
		// return tree.moves.get(0);

	}

	public void executeMove(int move) {
		if (brain == null) {
			brain = new AlphaBetaThinker(tree, evaluator);
		}
		brain.executeMove(move);

	}

}
