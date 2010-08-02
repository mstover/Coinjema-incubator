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

import java.util.StringTokenizer;

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

	private final static int[] CLEAR_MOVE = new int[4];

	static {
		CLEAR_MOVE[0] = 1 | 1 << 1 | 1 << 2 | 1 << 3 | 1 << 4 | 1 << 5 | 1 << 6
				| 1 << 7 | 1 << 8 | 1 << 9 | 1 << 10 | 1 << 11 | 1 << 12
				| 1 << 13 | 1 << 14 | 1 << 15 | 1 << 16 | 1 << 17 | 1 << 18
				| 1 << 19 | 1 << 20 | 1 << 21 | 1 << 22 | 1 << 23;
		CLEAR_MOVE[1] = 1 | 1 << 1 | 1 << 2 | 1 << 3 | 1 << 4 | 1 << 5 | 1 << 6
				| 1 << 7 | 1 << 8 | 1 << 9 | 1 << 10 | 1 << 11 | 1 << 12
				| 1 << 13 | 1 << 14 | 1 << 15 | 1 << 24 | 1 << 25 | 1 << 26
				| 1 << 27 | 1 << 28 | 1 << 29 | 1 << 30 | 1 << 31;
		CLEAR_MOVE[2] = 1 | 1 << 1 | 1 << 2 | 1 << 3 | 1 << 4 | 1 << 5 | 1 << 6
				| 1 << 7 | 1 << 16 | 1 << 17 | 1 << 18 | 1 << 19 | 1 << 20
				| 1 << 21 | 1 << 22 | 1 << 23 | 1 << 24 | 1 << 25 | 1 << 26
				| 1 << 27 | 1 << 28 | 1 << 29 | 1 << 30 | 1 << 31;
		CLEAR_MOVE[3] = 1 << 8 | 1 << 9 | 1 << 10 | 1 << 11 | 1 << 12 | 1 << 13
				| 1 << 14 | 1 << 15 | 1 << 16 | 1 << 17 | 1 << 18 | 1 << 19
				| 1 << 20 | 1 << 21 | 1 << 22 | 1 << 23 | 1 << 24 | 1 << 25
				| 1 << 26 | 1 << 27 | 1 << 28 | 1 << 29 | 1 << 30 | 1 << 31;
	}

	public final static int EMPTY_MOVE = 2 | 2 << 8 | 2 << 16 | 2 << 24;

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
		int move = (from.index << 2) | EMPTY_SECOND_MOVE
				| getDirection(from.index, to.index);

		return move;

	}

	public static int getStep(int from, int to) {
		int move = (from << 2) | EMPTY_SECOND_MOVE | getDirection(from, to);

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

	public static int getStepCountOfMove(int move) {
		if ((move & (~CLEAR_MOVE[0])) != (2 << 24)) {
			return 4;
		} else if ((move & (~CLEAR_MOVE[1])) != (2 << 16)) {
			return 3;
		} else if ((move & (~CLEAR_MOVE[2])) != (2 << 8)) {
			return 2;
		} else {
			return 1;
		}
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

		int[] steps = new int[] { getStep(63, 62), getStep(63, 62),
				getStep(63, 62), getStep(63, 62) };
		int move = appendSteps(EMPTY_MOVE, steps[0], 4, 1);
		move = appendSteps(move, steps[1], 3, 1);
		move = appendSteps(move, steps[2], 2, 1);
		move = appendSteps(move, steps[3], 1, 1);
		System.out.println("move = " + move);
		for (int i : steps) {
			System.out.println("Steps = " + i);
		}
		for (int i : getStepSequence(getFirstHalf(move))) {
			System.out.println("move = " + i);
		}
		for (int i : getStepSequence(getSecondHalf(move))) {
			System.out.println("move = " + i);
		}
	}

	/**
	 * @param wholeMove
	 * @param move
	 * @param remainingStepCount
	 * @return
	 */
	public static int appendSteps(int wholeMove, int move,
			int remainingStepCount, int numSteps) {
		int tmp = wholeMove & CLEAR_MOVE[remainingStepCount - 1];
		if (numSteps == 2) {
			tmp = tmp & CLEAR_MOVE[remainingStepCount - 2];
		}
		return tmp | (move << (8 * (4 - remainingStepCount)));
	}

	/**
	 * @param move
	 * @return
	 */
	public static int getFirstHalf(int move) {
		return move & CLEAR_MOVE[0] & CLEAR_MOVE[1];
	}

	/**
	 * @param move
	 * @return
	 */
	public static int getSecondHalf(int move) {
		return move >>> 16;
	}

	/**
	 * @param step
	 * @return
	 */
	public static boolean isNullMove(int step) {
		return (step & CLEAR_MOVE[2]) == 2;
	}

	/**
	 * @param move
	 */
	public static void printStepsForMove(int move) {
		System.out.println("Move = " + move);
		for (int i : getStepSequence(getFirstHalf(move))) {
			System.out.println(i);
		}
		for (int i : getStepSequence(getSecondHalf(move))) {
			System.out.println(i);
		}
	}

	public static String toString(int move) {
		StringBuilder out = new StringBuilder();
		int[] seq = getStepSequence(getFirstHalf(move));
		for (int i = 0; i < seq.length; i += 2) {
			out.append(String.valueOf((char) ('a' + (seq[i] % 8)))
					+ ((seq[i] / 8) + 1));
			switch (seq[i] - seq[i + 1]) {
			case 1:
				out.append("w");
				break;
			case -1:
				out.append("e");
				break;
			case 8:
				out.append("s");
				break;
			case -8:
				out.append("n");
				break;
			}
			out.append(" ");
		}
		seq = getStepSequence(getSecondHalf(move));
		for (int i = 0; i < seq.length; i += 2) {
			out.append(String.valueOf((char) ('a' + (seq[i] % 8)))
					+ ((seq[i] / 8) + 1));
			switch (seq[i] - seq[i + 1]) {
			case 1:
				out.append("w");
				break;
			case -1:
				out.append("e");
				break;
			case 8:
				out.append("s");
				break;
			case -8:
				out.append("n");
				break;
			}
			out.append(" ");
		}
		return out.toString();
	}

	public static int parse(String instructions) {
		StringTokenizer tokenizer = new StringTokenizer(instructions, " ,;");
		int move = EMPTY_MOVE;
		int remainingStepCount = 4;
		while (tokenizer.hasMoreTokens()) {
			String s = tokenizer.nextToken();

			SquareDesignation sq = new SquareDesignation(s.substring(1, 3));
			String dir = s.substring(3, 4);
			int toIndex = sq.index;
			if (dir.equals("e")) {
				toIndex++;
			} else if (dir.equals("w")) {
				toIndex--;
			} else if (dir.equals("n")) {
				toIndex += 8;
			} else {
				toIndex -= 8;
			}
			move = appendSteps(move, getStep(sq.index, toIndex),
					remainingStepCount--, 1);
		}
		return move;
	}

}
