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

import java.io.PrintStream;

/**
 * @author michaelstover
 * 
 */
public class TrapSquare extends Square {

	private Piece[] killedPieces = new Piece[100];
	private final int stackPointer = 0;

	/**
	 * @param adj
	 */
	public TrapSquare(Board b, int index) {
		super(b, index, 4);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Square#print(java.io.PrintStream)
	 */
	@Override
	public void print(PrintStream out) {
		if (occupant == null) {
			out.print(" # ");
		} else {
			occupant.print(out);
		}
	}

	boolean checkKill() {
		if (occupant == null) {
			return false;
		}
		boolean kill = true;
		for (Square s : adjacent) {
			if (!s.isEmpty() && (s.getOccupant().gold == occupant.gold)) {
				kill = false;
				break;
			}
		}
		return kill;
	}

	boolean futureIsEmpty(int stepCount) {
		for (int i = stepCount; i < killedPieces.length; i++) {
			if (killedPieces[i] != null) {
				throw new RuntimeException();
			}
		}
		return true;
	}

	void unkillPiece(int stepCount) {
		while (stepCount >= killedPieces.length) {
			expandKilledPieces();
		}
		if (killedPieces[stepCount] != null) {
			setOccupant(killedPieces[stepCount]);
			killedPieces[stepCount] = null;
		}
	}

	/**
	 * @param occupant
	 */
	void killPiece(int stepCount) {
		futureIsEmpty(stepCount);
		while (stepCount >= killedPieces.length) {
			expandKilledPieces();
		}
		killedPieces[stepCount] = occupant;
		setOccupant(null);
	}

	/**
	 * 
	 */
	private void expandKilledPieces() {
		Piece[] tmp = new Piece[killedPieces.length * 2];
		System.arraycopy(killedPieces, 0, tmp, 0, killedPieces.length);
		killedPieces = tmp;
	}

}
