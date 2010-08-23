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
public class Piece {
	public final static int GOLD_RABBIT = 1;
	public final static int GOLD_CAT = 2;
	public final static int GOLD_DOG = 3;
	public final static int GOLD_HORSE = 4;
	public final static int GOLD_CAMEL = 5;
	public final static int GOLD_ELEPHANT = 6;
	public final static int SILVER_RABBIT = 7;
	public final static int SILVER_CAT = 8;
	public final static int SILVER_DOG = 9;
	public final static int SILVER_HORSE = 10;
	public final static int SILVER_CAMEL = 11;
	public final static int SILVER_ELEPHANT = 12;

	public final static int RABBIT = 1;
	public final static int CAT = 2;
	public final static int DOG = 3;
	public final static int HORSE = 4;
	public final static int CAMEL = 5;
	public final static int ELEPHANT = 6;

	protected static final int[] NO_STEPS = new int[0];
	private final Move moveService = new Move();

	private final int[] potentialSteps = new int[24];

	public final int strength;
	public final boolean gold;
	Square square;
	private boolean recalcSteps = true;
	private int validStepCount;
	public final int bit;

	private boolean frozen = false;
	private boolean dominated = false;

	public Piece(int str, boolean g) {
		this.strength = str;
		this.gold = g;
		if (gold) {
			switch (strength) {
			case CAMEL:
				bit = GOLD_CAMEL;
				break;
			case CAT:
				bit = GOLD_CAT;
				break;
			case DOG:
				bit = GOLD_DOG;
				break;
			case ELEPHANT:
				bit = GOLD_ELEPHANT;
				break;
			case HORSE:
				bit = GOLD_HORSE;
				break;
			case RABBIT:
				bit = GOLD_RABBIT;
				break;
			default:
				bit = 0;
			}
		} else {
			switch (strength) {
			case CAMEL:
				bit = SILVER_CAMEL;
				break;
			case CAT:
				bit = SILVER_CAT;
				break;
			case DOG:
				bit = SILVER_DOG;
				break;
			case ELEPHANT:
				bit = SILVER_ELEPHANT;
				break;
			case HORSE:
				bit = SILVER_HORSE;
				break;
			case RABBIT:
				bit = SILVER_RABBIT;
				break;
			default:
				bit = 0;
			}
		}
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

	public void getSteps(StepBuffer stepBuffer) {
		if (recalcSteps) {
			if (!isFrozen()) {
				validStepCount = 0;
				validStepCount += addSimpleSteps(validStepCount, potentialSteps);
				validStepCount += addPushSteps(validStepCount, potentialSteps);
			} else {
				validStepCount = 0;
			}
			recalcSteps = false;
		}
		copyToBuffer(stepBuffer);
	}

	public int getStepCount() {
		if (recalcSteps) {
			if (!isFrozen()) {
				validStepCount = 0;
				validStepCount += addSimpleSteps(validStepCount, potentialSteps);
				validStepCount += addPushSteps(validStepCount, potentialSteps);
			} else {
				validStepCount = 0;
			}
			recalcSteps = false;
		}
		return validStepCount;
	}

	private void copyToBuffer(StepBuffer stepBuffer) {
		for (int i = 0; i < validStepCount; i++) {
			stepBuffer.putStep(potentialSteps[i]);
		}
	}

	public boolean isInDanger() {
		boolean nearTrap = (square instanceof TrapSquare)
				&& !((TrapSquare) square).isOwnedBy(gold);
		boolean nearStrongEnemies = false;

		for (Square s : square.adjacent) {
			if (s instanceof TrapSquare) {
				nearTrap = nearTrap || !((TrapSquare) s).isOwnedBy(gold);
				if (!s.isEmpty() && (s.occupant.gold != gold)
						&& (s.occupant.strength > strength)) {
					nearStrongEnemies = true;
				}
			}
			for (Square ss : s.adjacent) {
				if (ss instanceof TrapSquare) {
					nearTrap = nearTrap || !((TrapSquare) ss).isOwnedBy(gold);
					if (!s.isEmpty() && (s.occupant.gold != gold)
							&& (s.occupant.strength > strength)) {
						nearStrongEnemies = true;
					}
				}
			}
		}
		return nearTrap && nearStrongEnemies; // if()
	}

	public boolean isDominated() {
		if (recalcSteps) {
			dominated = false;
			for (Square s : square.adjacent) {
				if (!s.isEmpty()) {
					int str = s.getOccupant().strength;
					if (s.getOccupant().gold != gold) {
						if (s.occupant.strength > strength) {
							dominated = true;
							break;
						}
					}
				}
			}
		}
		return dominated;
	}

	/**
	 * @param count
	 * @param potentialSteps2
	 * @return
	 */
	private int addPushSteps(int count, int[] temp) {
		int subCount = 0;
		for (Square s : square.adjacent) {
			if (!s.isEmpty()) {
				if ((s.getOccupant().gold != gold)
						&& (s.getOccupant().strength < strength)) {
					for (Square pushTo : s.adjacent) {
						if (pushTo != square) {
							if (pushTo.isEmpty()) {
								temp[count + subCount] = moveService
										.getPushStep(square, s, pushTo);
								subCount++;
							}
						}
					}
					for (Square pullTo : square.adjacent) {
						if ((pullTo != s) && pullTo.isEmpty()) {
							temp[count + subCount] = moveService.getPullStep(
									square, s, pullTo);
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
			if (s.isEmpty()) {
				if ((strength != Piece.RABBIT)
						|| (gold && (s.index > square.index - 8))
						|| (!gold && (s.index < square.index + 8))) {
					temp[count + subCount] = moveService.getStep(square, s);
					subCount++;
				}
			}
		}
		return subCount;
	}

	protected boolean isFrozen() {
		if (recalcSteps) {
			frozen = false;
			for (Square s : square.adjacent) {
				if (!s.isEmpty()) {
					if (s.getOccupant().gold == gold) {
						frozen = false;
						break;
					} else if (s.getOccupant().strength > strength) {
						frozen = true;
					}
				}
			}
		}
		return frozen;
	}

	public boolean change() {
		final boolean cur = recalcSteps;
		recalcSteps = true;
		return !cur;
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
