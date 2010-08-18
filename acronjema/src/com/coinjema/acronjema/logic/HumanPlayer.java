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

import java.io.IOException;

/**
 * @author michaelstover
 * 
 */
public class HumanPlayer implements Player {

	Move moveService = new Move();
	private Board board;
	private boolean gold;

	/**
	 * 
	 */
	public HumanPlayer(Board b, boolean gold) {
		this.board = b;
		this.gold = gold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Player#findMoves()
	 */
	@Override
	public int findMoves() throws GameEndException {
		String instructions = null;
		while (instructions == null) {
			try {
				instructions = readIn();

			} catch (Exception e) {
				System.out.print("Come again? ");
			}
		}
		return moveService.parse(instructions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Player#setup()
	 */
	@Override
	public void setup() {
		try {
			byte[] in = new byte[10];
			for (int i = 0; i < 16; i++) {
				if (i < 8) {
					System.out.print(getColor() + " rabbit? ");
					System.in.read(in);
					board.addPiece(1, gold, new SquareDesignation(in));
				} else if (i < 10) {
					System.out.print(getColor() + " cat? ");
					System.in.read(in);
					board.addPiece(2, gold, new SquareDesignation(in));
				} else if (i < 12) {
					System.out.print(getColor() + " dog? ");
					System.in.read(in);
					board.addPiece(3, gold, new SquareDesignation(in));
				} else if (i < 14) {
					System.out.print(getColor() + " horse? ");
					System.in.read(in);
					board.addPiece(4, gold, new SquareDesignation(in));
				} else if (i < 15) {
					System.out.print(getColor() + " camel? ");
					System.in.read(in);
					board.addPiece(5, gold, new SquareDesignation(in));
				} else if (i < 16) {
					System.out.print(getColor() + " elephant? ");
					System.in.read(in);
					board.addPiece(6, gold, new SquareDesignation(in));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	String getColor() {
		return gold ? "Gold" : "Silver";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Player#executeMove(int)
	 */
	@Override
	public void executeMove(int move) {
		// nothing to do
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

}
