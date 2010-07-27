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

/**
 * @author michaelstover
 * 
 */
public class BasicMoveSorter implements MoveSorter {

	Board board;
	boolean movingSide = true;

	public BasicMoveSorter(Board b) {
		board = b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.MoveSorter#compare(long, long)
	 */
	@Override
	public int compare(int k, int l) {
		int sumK = 0;
		int sumL = 0;
		board.executeMove(k);
		if (rabbitAtEnd(k)) {
			board.rewindMove(k);
			return 1;
		}
		sumK += 50 * (strengthTotal(movingSide) - strengthTotal(!movingSide));

		board.rewindMove(k);
		board.executeMove(l);
		if (rabbitAtEnd(l)) {
			board.rewindMove(l);
			return -1;
		}
		sumL += 50 * (strengthTotal(movingSide) - strengthTotal(!movingSide));
		board.rewindMove(l);
		sumK += movingPieceTotal(k);
		sumL += movingPieceTotal(l);
		return sumL - sumK;
	}

	/**
	 * @param k
	 * @param movingSide2
	 * @return
	 */
	private int strengthTotal(boolean gold) {
		int sum = 0;
		for (Square s : board.squares) {
			if (!s.isEmpty() && (s.getOccupant().gold == gold)) {
				sum++;
			}
		}
		return sum;
	}

	/**
	 * @param k
	 * @param movingSide2
	 * @return
	 */
	private int movingPieceTotal(int k) {
		int sum = 0;
		int steps = Move.getFirstHalf(k);
		if (!Move.isNullMove(steps)) {
			sum += addPieces(steps);
		} else {
			return sum;
		}
		steps = Move.getSecondHalf(k);
		if (!Move.isNullMove(steps)) {
			sum += addPieces(steps);
		}
		board.rewindMove(k);
		return sum;

	}

	private int addPieces(int steps) {
		int sum = 0;
		int[] seq = Move.getStepSequence(steps);
		sum += board.squares[seq[0]].getOccupant().strength
				* (board.squares[seq[0]].getOccupant().gold == movingSide ? 1
						: 2);
		if (seq.length == 4) {
			if (board.squares[seq[2]].getOccupant() != null) {
				sum += board.squares[seq[2]].getOccupant().strength
						* (board.squares[seq[2]].getOccupant().gold == movingSide ? 1
								: 2);
			} else {
				sum += board.squares[seq[0]].getOccupant().strength
						* (board.squares[seq[0]].getOccupant().gold == movingSide ? 1
								: 2);
			}
		}
		board.executeStep(steps);

		return sum;
	}

	/**
	 * @param k
	 * @return
	 */
	private boolean rabbitAtEnd(int k) {
		if (movingSide) {
			for (int i = 56; i < 64; i++) {
				if (!board.squares[i].isEmpty()
						&& (board.squares[i].getOccupant().gold == movingSide)
						&& (board.squares[i].getOccupant().strength == 1)) {
					return true;
				}
			}
		} else {
			for (int i = 0; i < 8; i++) {
				if (!board.squares[i].isEmpty()
						&& (board.squares[i].getOccupant().gold == movingSide)
						&& (board.squares[i].getOccupant().strength == 1)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.MoveSorter#sort(java.nio.LongBuffer,
	 * int, com.coinjema.acronjema.logic.Board)
	 */
	@Override
	public void sort(IntBuffer moves, int length, Board board,
			boolean movingSide) {
		this.movingSide = movingSide;
		IntTimSort.sort(moves.array(), moves.position() - length,
				moves.position(), this);
	}
}
