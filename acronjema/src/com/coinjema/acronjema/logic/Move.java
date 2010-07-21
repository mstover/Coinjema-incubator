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

/**
 * @author michaelstover
 * 
 *         A move is 6-bit from square, 6-bit to square; or,
 * 
 *         6-bit from square, 6-bit to square, 6-bit from square, 6-bit to
 *         square;
 * 
 */
public class Move {

	private final static int FIRST_SQUARE = (1 | 1 << 1 | 1 << 2 | 1 << 3
			| 1 << 4 | 1 << 5) << 2;
	private final static int FIRST_DIR = 1 | (1 << 1);
	private final static int SECOND_SQUARE = FIRST_SQUARE << 8;
	private final static int SECOND_DIR = FIRST_DIR << 8;

	private final static int EMPTY_SECOND_MOVE = 2 << 8;

	private final static int[] singleSeq = new int[2];
	private final static int[] doubleSeq = new int[4];

	private final static int UP = 0;
	private final static int DOWN = 1;
	private final static int LEFT = 2;
	private final static int RIGHT = 3;

	/**
	 * @param square
	 * @param s
	 * @return
	 */
	public static int getStep(Square from, Square to) {
		int move = from.index << 2;
		move = EMPTY_SECOND_MOVE | move | getDirection(from.index, to.index);

		return move;

	}

	/**
	 * @param square
	 * @param s
	 * @return
	 */
	private static int getSubStep(Square from, Square to) {
		int move = from.index << 2;
		move = move | getDirection(from.index, to.index);

		return move;

	}

	/**
	 * @param square
	 * @param s
	 * @param pushTo
	 * @return
	 */
	public static int getPushStep(Square me, Square other, Square pushTo) {
		int move = getSubStep(other, pushTo);
		move = (getSubStep(me, other) << 8) | move;
		return move;
	}

	/**
	 * @param square
	 * @param s
	 * @param pullTo
	 * @return
	 */
	public static int getPullStep(Square me, Square other, Square pullTo) {
		int move = getSubStep(me, pullTo);
		move = (getSubStep(other, me) << 8) | move;
		return move;
	}

	/**
	 * @param move
	 * @return
	 */
	public static int[] getStepSequence(int move) {
		int[] seq;
		if (move >>> 8 != 2) {
			seq = doubleSeq;
			seq[2] = (move & SECOND_SQUARE) >>> 10;
			seq[3] = seq[2] + resolveDirection((move & SECOND_DIR) >>> 8);
		} else {
			seq = singleSeq;
		}
		seq[0] = (move & FIRST_SQUARE) >>> 2;
		seq[1] = seq[0] + resolveDirection(move & FIRST_DIR);
		return seq;
	}

	private static int resolveDirection(int i) {
		switch (i) {
		case UP:
			return 8;
		case DOWN:
			return -8;
		case LEFT:
			return -1;
		case RIGHT:
			return 1;
		}
		throw new RuntimeException("Bad direction hash " + i);
	}

	public static int getStepCount(int step) {
		return step >>> 8 == 2 ? 1 : 2;
	}

	private static int getDirection(int from, int to) {
		switch (from - to) {
		case 1:
			return LEFT;
		case -1:
			return RIGHT;
		case 8:
			return DOWN;
		case -8:
			return UP;
		}
		throw new RuntimeException("Bad direction squares " + from + " to "
				+ to);
	}

	public static void main(String[] args) {
		Square one = new Square(null, 33, 4);
		Square two = new Square(null, 34, 4);
		Square three = new Square(null, 25, 4);
		int move = getPushStep(three, one, two);
		System.out.println("int move from 33 to 34 = " + move);
		System.out.println("step count = " + getStepCount(move));
		int[] seq = getStepSequence(move);
		for (int i : seq) {
			System.out.println("Moving square " + i);
		}
	}

}
