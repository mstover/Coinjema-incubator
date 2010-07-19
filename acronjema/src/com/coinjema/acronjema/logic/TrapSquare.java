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

	private final Piece[] killedPieces = new Piece[32];
	private int stackPointer = 0;

	/**
	 * @param adj
	 */
	public TrapSquare(int index) {
		super(index, 4);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coinjema.acronjema.logic.Square#moveOccupantTo(com.coinjema.acronjema
	 * .logic.Square)
	 */
	@Override
	public void moveOccupantTo(Square square) {
		if (this.occupant != null) {
			super.moveOccupantTo(square);
		} else {
			square.setOccupant(killedPieces[--stackPointer]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coinjema.acronjema.logic.Square#setOccupant(com.coinjema.acronjema
	 * .logic.Piece)
	 */
	@Override
	public void setOccupant(Piece occupant) {
		if (occupant == null) {
			if (this.occupant != null) {
				this.occupant = null;
				fireOccupantChanged();
			}
			return;
		}
		boolean kill = true;
		for (Square s : adjacent) {
			if (!s.isEmpty() && (s.getOccupant().gold == occupant.gold)) {
				kill = false;
				break;
			}
		}
		if (kill) {
			killPiece(occupant);
		} else {
			this.occupant = occupant;
			this.occupant.setSquare(this);
			fireOccupantChanged();
		}
	}

	/**
	 * @param occupant
	 */
	private void killPiece(Piece occupant) {
		killedPieces[stackPointer++] = occupant;
	}

}
