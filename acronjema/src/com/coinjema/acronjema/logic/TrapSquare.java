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
		super(b, index, 4, 8);
		nextToTrap = index;
	}

	@Override
	public void setAdjacent(Square... squares) {
		for (int i = 0; i < adjacent.length; i++) {
			adjacent[i] = squares[i];
			adjacent[i].nextToTrap = index;
		}
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

	void unkillPiece(int stepCount, boolean notify) {
		if (stepCount >= killedPieces.length) {
			return;
		}
		if (killedPieces[stepCount] != null) {
			setOccupant(killedPieces[stepCount], notify);
			killedPieces[stepCount] = null;
		}
	}

	/**
	 * @param occupant
	 */
	void killPiece(int stepCount, boolean notify) {
		while (stepCount >= killedPieces.length) {
			expandKilledPieces();
		}
		killedPieces[stepCount] = occupant;
		occupant.setSquare(null);
		setOccupant(null, notify);
	}

	/**
	 * 
	 */
	private void expandKilledPieces() {
		Piece[] tmp = new Piece[killedPieces.length * 2];
		System.arraycopy(killedPieces, 0, tmp, 0, killedPieces.length);
		killedPieces = tmp;
	}

	/**
	 * @param gold
	 * @return
	 */
	public boolean isOwnedBy(boolean b) {
		int goldSum = 0;
		int goldStrength = 0;
		int silverStrength = 0;
		int silverSum = 0;
		for (Square s : adjacent) {
			if (!s.isEmpty() && !s.occupant.isDominated()) {
				if ((s.occupant.strength == Piece.ELEPHANT)
						&& (b == s.occupant.gold)) {
					return true;
				}
				if (s.occupant.gold) {
					goldSum++;
					goldStrength += s.occupant.strength;
				} else {
					silverSum++;
					silverStrength += s.occupant.strength;
				}
			}
		}
		if (goldSum > silverSum) {
			return true == b;
		} else if (silverSum > goldSum) {
			return false == b;
		} else {
			if (goldStrength > silverStrength) {
				return true == b;
			} else if (silverStrength > goldStrength) {
				return false == b;
			}
		}
		return false;
	}

}
