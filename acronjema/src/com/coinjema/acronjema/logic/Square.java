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

	int nextToTrap = -1;
	public final Square[] adjacent;
	public final Square[] twoAway;
	protected Piece occupant;

	public final int index;
	public final Board board;

	public Square(Board b, int index, int numAdj, int numTwoAway) {
		adjacent = new Square[numAdj];
		twoAway = new Square[numTwoAway];
		this.index = index;
		this.board = b;
	}

	public void setAdjacent(Square... squares) {
		for (int i = 0; i < adjacent.length; i++) {
			adjacent[i] = squares[i];
		}
	}

	public void init() {
		int count = 0;
		for (Square a : adjacent) {
			for (Square s : a.adjacent) {
				if ((s != this) && !contains(adjacent, s)
						&& !contains(twoAway, s)) {
					twoAway[count++] = s;
				}
			}
		}
	}

	private boolean contains(Square[] adjacent2, Square s) {
		for (Square test : adjacent2) {
			if (s == test) {
				return true;
			}
		}
		return false;
	}

	protected void fireOccupantChanged() {
		fireTertiaryOccupantChanged();
		for (Square s : adjacent) {
			s.fireTertiaryOccupantChanged();
		}
		for (Square s : twoAway) {
			s.fireTertiaryOccupantChanged();
		}
	}

	protected void fireTertiaryOccupantChanged() {
		if (occupant != null) {
			occupant.change();
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
		if ((occupant != null) && (this.occupant != null)) {
			board.print(System.out);
			throw new RuntimeException("Can't replace an occupant!");
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
