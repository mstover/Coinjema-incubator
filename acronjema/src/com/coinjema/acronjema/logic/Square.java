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
public class Square {

	public final Square[] adjacent;
	protected Piece occupant;

	public final int index;
	public final Board board;

	public Square(Board b, int index, int numAdj) {
		adjacent = new Square[numAdj];
		this.index = index;
		this.board = b;
	}

	public void setAdjacent(Square... squares) {
		for (int i = 0; i < adjacent.length; i++) {
			adjacent[i] = squares[i];
		}
	}

	protected void fireOccupantChanged() {
		fireTertiaryOccupantChanged();
		for (Square s : adjacent) {
			s.fireSecondaryOccupantChanged(this);
		}
	}

	protected void fireSecondaryOccupantChanged(Square notS) {
		if (fireTertiaryOccupantChanged()) {
			for (Square s : adjacent) {
				if (s != notS) {
					s.fireTertiaryOccupantChanged();
				}
			}
		}
	}

	protected boolean fireTertiaryOccupantChanged() {
		if (occupant != null) {
			occupant.change();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the occupant
	 */
	public Piece getOccupant() {
		return occupant;
	}

	/**
	 * @param occupant
	 *            the occupant to set
	 */
	public void setOccupant(Piece occupant, boolean notify) {
		if ((occupant == null) && (this.occupant == null)) {
			return;
		}
		this.occupant = occupant;
		if (occupant != null) {
			occupant.setSquare(this);
		}
		if (notify) {
			fireOccupantChanged();
		}
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return occupant == null;
	}

	public void print(PrintStream out) {
		if (occupant == null) {
			out.print(" - ");
		} else {
			occupant.print(out);
		}
	}

	/**
	 * @param square
	 */
	public void moveOccupantTo(Square square, boolean notify) {
		Piece thisOccupant = occupant;
		setOccupant(null, notify);
		square.setOccupant(thisOccupant, notify);
	}

}
