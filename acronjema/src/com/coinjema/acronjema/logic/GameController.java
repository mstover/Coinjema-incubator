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

import java.nio.IntBuffer;
import java.util.Random;

/**
 * @author michaelstover
 * 
 */
public class GameController {

	public static Random rand = new Random();

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
		for (int i = 0; i < 20; i++) {
			runMoves(b, true);
			runMoves(b, false);
		}
	}

	/**
	 * 
	 */
	private static void runMoves(Board b, boolean gold) {
		int count = 0;
		IntBuffer stepBuffer = IntBuffer.allocate(1000);
		while (count < 4) {
			for (Square s : b.squares) {
				if (!s.isEmpty() && (s.getOccupant().gold == gold)) {
					for (int step : s.getOccupant().getSteps()) {
						if (count + Move.getStepCount(step) < 5) {
							stepBuffer.put(step);
						}
					}
				}
			}
			System.out.println("Number of steps = " + stepBuffer.position());
			int choice = stepBuffer.get(rand.nextInt(stepBuffer.position()));
			System.out.println("Choice = " + choice);
			b.executeMove(choice);
			b.print(System.out);
			count += Move.getStepCount(choice);
			stepBuffer.clear();
		}
	}
}
