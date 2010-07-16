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
	private static int FIRST = 1 | 1 << 1 | 1 << 2 | 1 << 3 | 1 << 4 | 1 << 5;
	private static int SECOND = FIRST << 6;
	private static int THIRD = SECOND << 6;
	private static int FOURTH = THIRD << 6;

	/**
	 * @param square
	 * @param s
	 * @return
	 */
	public static int getStep(Square from, Square to) {
		assert (from != to);
		int move = from.index;
		move = to.index << 6 | move;
		assert (move >> 12 == 0);
		return move;

	}

	/**
	 * @param square
	 * @param s
	 * @param pushTo
	 * @return
	 */
	public static int getPushStep(Square me, Square other, Square pushTo) {
		assert (false);
		int move = getStep(other, pushTo);
		move = getStep(me, other) << 12 | move;
		assert (move >> 12 != 0);
		return move;
	}

	/**
	 * @param square
	 * @param s
	 * @param pullTo
	 * @return
	 */
	public static int getPullStep(Square me, Square other, Square pullTo) {
		assert (false);
		int move = getStep(me, pullTo);
		move = getStep(other, me) << 12 | move;
		return move;
	}

	/**
	 * @param choice
	 * @return
	 */
	public static int[] getStepSequence(int choice) {
		int[] seq;
		if (choice >> 12 > 0) {
			seq = new int[4];
			seq[2] = (choice & THIRD) >> 12;
			seq[3] = (choice & FOURTH) >> 18;
		} else {
			seq = new int[2];
		}
		seq[0] = choice & FIRST;
		seq[1] = (choice & SECOND) >> 6;
		return seq;
	}

	public static int getStepCount(int step) {
		return step >> 12 > 0 ? 2 : 1;
	}

}
