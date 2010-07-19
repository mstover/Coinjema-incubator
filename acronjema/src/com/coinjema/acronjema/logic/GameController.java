/**
 * --start-license-block--
 * 
 * (c) 2006 - present by the University of Rochester 
 * See the file DEDISCOVER-LICENSE.txt for License Details 
 * 
 * --end-license-block--
 * $Id$
 */
package com.coinjema.acronjema.logic;

import java.util.Random;

/**
 * @author michaelstover
 * 
 */
public class GameController {

	public static Random rand = new Random();
	static StepBuffer stepBuffer = new StepBuffer();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Board b = new Board();

		// Gold pieces

		// rabbits
		b.addPiece(Piece.RABBIT, true, new SquareDesignation("a1"));
		b.addPiece(Piece.RABBIT, true, new SquareDesignation("b1"));
		b.addPiece(Piece.RABBIT, true, new SquareDesignation("c1"));
		b.addPiece(Piece.RABBIT, true, new SquareDesignation("d1"));
		b.addPiece(Piece.RABBIT, true, new SquareDesignation("e1"));
		b.addPiece(Piece.RABBIT, true, new SquareDesignation("f1"));
		b.addPiece(Piece.RABBIT, true, new SquareDesignation("g1"));
		b.addPiece(Piece.RABBIT, true, new SquareDesignation("h1"));

		// big pieces
		b.addPiece(Piece.DOG, true, new SquareDesignation("a2"));
		b.addPiece(Piece.HORSE, true, new SquareDesignation("b2"));
		b.addPiece(Piece.CAT, true, new SquareDesignation("c2"));
		b.addPiece(Piece.ELEPHANT, true, new SquareDesignation("d2"));
		b.addPiece(Piece.CAMEL, true, new SquareDesignation("e2"));
		b.addPiece(Piece.CAT, true, new SquareDesignation("f2"));
		b.addPiece(Piece.HORSE, true, new SquareDesignation("g2"));
		b.addPiece(Piece.DOG, true, new SquareDesignation("h2"));

		// silver pieces

		// rabbits
		b.addPiece(Piece.RABBIT, false, new SquareDesignation("a8"));
		b.addPiece(Piece.RABBIT, false, new SquareDesignation("b8"));
		b.addPiece(Piece.RABBIT, false, new SquareDesignation("c8"));
		b.addPiece(Piece.RABBIT, false, new SquareDesignation("d8"));
		b.addPiece(Piece.RABBIT, false, new SquareDesignation("e8"));
		b.addPiece(Piece.RABBIT, false, new SquareDesignation("f8"));
		b.addPiece(Piece.RABBIT, false, new SquareDesignation("g8"));
		b.addPiece(Piece.RABBIT, false, new SquareDesignation("h8"));

		// big pieces
		b.addPiece(Piece.DOG, false, new SquareDesignation("a7"));
		b.addPiece(Piece.HORSE, false, new SquareDesignation("b7"));
		b.addPiece(Piece.CAT, false, new SquareDesignation("c7"));
		b.addPiece(Piece.ELEPHANT, false, new SquareDesignation("d7"));
		b.addPiece(Piece.CAMEL, false, new SquareDesignation("e7"));
		b.addPiece(Piece.CAT, false, new SquareDesignation("f7"));
		b.addPiece(Piece.HORSE, false, new SquareDesignation("g7"));
		b.addPiece(Piece.DOG, false, new SquareDesignation("h7"));

		b.print(System.out);

		try {
			findMoves(b, true);
			findMoves(b, false);
			b.print(System.out);
			findMoves(b, true);
			findMoves(b, false);
		} catch (GameEndException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		b.print(System.out);
		// for (int i = 0; i < 100; i++) {
		// runMoves(b, true);
		// b.print(System.out);
		// runMoves(b, false);
		// b.print(System.out);
		// }
		long time = System.currentTimeMillis();
		try {
			for (int i = 0; i < 10000; i++) {
				findMoves(b, true);
				findMoves(b, false);
				// b.print(System.out);
			}
		} catch (GameEndException e) {
			System.out.println("Somebody ran out of moves");
		}
		System.out.println("Processed "
				+ (20000d / (System.currentTimeMillis() - time))
				+ " movesets/ms");
	}

	private static void findMoves(Board b, boolean gold)
			throws GameEndException {
		stepBuffer.clear();
		stepBuffer.searchForSteps(b, gold, 4);
		int[] move = stepBuffer.getRandomMove(rand);

		for (int m : move) {
			b.executeMove(m);
		}

	}

	/**
	 * 
	 */
	private static void runMoves(Board b, boolean gold) {
		int count = 0;
		stepBuffer.setAcceptDoubleMove(true);
		while (count < 4) {
			stepBuffer.clear();
			if (count == 3) {
				stepBuffer.setAcceptDoubleMove(false);
			}
			b.findAllSteps(stepBuffer, gold);
			if (stepBuffer.getNumberChoices() == 0) {
				System.out.println("No possible moves");
				b.print(System.out);
				System.exit(0);
			}
			int choice = stepBuffer.get(rand.nextInt(stepBuffer
					.getNumberChoices()));
			b.executeMove(choice);
			// b.print(System.out);
			count += Move.getStepCount(choice);
		}
	}
}
