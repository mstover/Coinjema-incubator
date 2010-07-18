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
import java.util.HashSet;
import java.util.Set;

/**
 * @author michaelstover
 * 
 */
public class Square {

	public final Square[] adjacent;
	private final Set<Watcher> watchers = new HashSet<Watcher>();
	protected Piece occupant;
	final BoardChangeEvent event;

	public final int index;

	public Square(int index, int numAdj) {
		adjacent = new Square[numAdj];
		event = new BoardChangeEvent(this);
		this.index = index;
	}

	public void setAdjacent(Square... squares) {
		for (int i = 0; i < adjacent.length; i++) {
			adjacent[i] = squares[i];
		}
	}

	public void addWatcher(Watcher w) {
		// watchers.add(w);
	}

	public void removeWatcher(Watcher w) {
		// watchers.remove(w);
	}

	protected void fireOccupantChanged() {
		fireTertiaryOccupantChanged();
		for (Square s : adjacent) {
			s.fireSecondaryOccupantChanged(this);
		}
	}

	protected void fireSecondaryOccupantChanged(Square notS) {
		fireTertiaryOccupantChanged();
		for (Square s : adjacent) {
			if (s != notS) {
				s.fireTertiaryOccupantChanged();
			}
		}
	}

	protected void fireTertiaryOccupantChanged() {
		if (occupant != null) {
			occupant.change(null);
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
	public void setOccupant(Piece occupant) {
		this.occupant = occupant;
		if (occupant != null) {
			occupant.setSquare(this);
		}
		fireOccupantChanged();
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
	public void moveOccupantTo(Square square) {
		square.setOccupant(occupant);
		setOccupant(null);
	}

}
