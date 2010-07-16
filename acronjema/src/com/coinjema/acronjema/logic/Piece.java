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
public class Piece implements Watcher {
	public final static int RABBIT = 1;
	public final static int CAT = 2;
	public final static int DOG = 3;
	public final static int HORSE = 4;
	public final static int CAMEL = 5;
	public final static int ELEPHANT = 6;

	protected static final int[] NO_STEPS = new int[0];

	private int[] potentialSteps = new int[24];

	public final int strength;
	public final boolean gold;
	private Square square;
	private boolean recalcSteps = true;
	private int[] steps;

	public Piece(int str, boolean g) {
		this.strength = str;
		this.gold = g;
	}

	/**
	 * @return the square
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @param square
	 *            the square to set
	 */
	public void setSquare(Square square) {
		this.square = square;
	}

	public int[] getSteps() {
		if (recalcSteps) {
			if (!isFrozen()) {
				int count = 0;
				count += addSimpleSteps(count, potentialSteps);
				count += addPushSteps(count, potentialSteps);
				steps = new int[count];
				System.arraycopy(potentialSteps, 0, steps, 0, count);
			} else {
				steps = NO_STEPS;
			}
		}
		return steps;
	}

	/**
	 * @param count
	 * @param potentialSteps2
	 * @return
	 */
	private int addPushSteps(int count, int[] temp) {
		int subCount = 0;
		for (Square s : square.adjacent) {
			s.addWatcher(this);
			if (!s.isEmpty()) {
				if ((s.getOccupant().gold != gold)
						&& (s.getOccupant().strength < strength)) {
					for (Square pushTo : s.adjacent) {
						if (pushTo != square) {
							pushTo.addWatcher(this);
							if (pushTo.isEmpty()) {
								temp[count + subCount] = Move.getPushStep(
										square, s, pushTo);
								subCount++;
							}
						}
					}
					for (Square pullTo : square.adjacent) {
						if ((pullTo != s) && pullTo.isEmpty()) {
							temp[count + subCount] = Move.getPullStep(square,
									s, pullTo);
							subCount++;
						}
					}
				}
			}
		}
		return subCount;
	}

	/**
	 * @param temp
	 */
	private int addSimpleSteps(int count, int[] temp) {
		int subCount = 0;
		for (Square s : square.adjacent) {
			s.addWatcher(this);
			if (s.isEmpty()) {
				temp[count + subCount] = Move.getStep(square, s);
				subCount++;
			}
		}
		return subCount;
	}

	protected boolean isFrozen() {
		boolean frozen = false;
		for (Square s : square.adjacent) {
			s.addWatcher(this);
			if (!s.isEmpty()) {
				if (s.getOccupant().gold == gold) {
					return false;
				} else if (s.getOccupant().strength > strength) {
					frozen = true;
				}
			}
		}
		return frozen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coinjema.acronjema.logic.Watcher#change(com.coinjema.acronjema.logic
	 * .BoardChangeEvent)
	 */
	@Override
	public void change(BoardChangeEvent e) {
		recalcSteps = true;
	}

	public void print(PrintStream out) {
		switch (strength) {
		case CAMEL:
			if (gold) {
				out.print(" M ");
			} else {
				out.print(" m ");
			}
			break;
		case CAT:
			if (gold) {
				out.print(" C ");
			} else {
				out.print(" c ");
			}
			break;
		case DOG:
			if (gold) {
				out.print(" D ");
			} else {
				out.print(" d ");
			}
			break;
		case ELEPHANT:
			if (gold) {
				out.print(" E ");
			} else {
				out.print(" e ");
			}
			break;
		case HORSE:
			if (gold) {
				out.print(" H ");
			} else {
				out.print(" h ");
			}
			break;
		case RABBIT:
			if (gold) {
				out.print(" R ");
			} else {
				out.print(" r ");
			}
			break;
		}
	}

}
