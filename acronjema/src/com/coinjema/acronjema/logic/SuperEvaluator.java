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
 */
public class SuperEvaluator implements Evaluator {

	private final EvaluatorBuffer buffer = new EvaluatorBuffer();
	private final int frozenMult = 15;
	private final int dominationMult = 59;
	private final int dominationValue = 34;
	private final int friendValue = 59;
	private final int frozenValue = 35;
	private final int adjTrapStrMult = 15;
	private int adjTrapPieceValue = 60;
	private final int pieceValue = 3543;
	private final int rabbitRowMult = 40;
	private final int numMovesMult = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coinjema.acronjema.logic.Evaluator#evaluate(com.coinjema.acronjema
	 * .logic.Board)
	 */
	@Override
	public int evaluate(Board board) {
		if (board.getWinner() != null) {
			return board.getWinner() ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		}
		adjTrapPieceValue = 0;
		int sum = 0;
		buffer.steps.clear();
		board.findAllSteps(buffer, true);
		sum = numMovesMult * buffer.steps.position();
		buffer.steps.clear();
		board.findAllSteps(buffer, false);
		sum -= numMovesMult * buffer.steps.position();
		for (Piece p : board.goldPieces) {
			if (p.square != null) {
				sum += pieceValue;
				if (p.strength == 1) {
					sum += rabbitRowMult * (p.square.index / 8);
				}
				if (p.square.nextToTrap > 0) {
					sum += adjTrapPieceValue + adjTrapStrMult * p.strength;
				}
				if (p.isFrozen()) {
					sum -= p.strength * frozenMult + frozenValue;
				}
				for (Square s : p.square.adjacent) {
					if (!s.isEmpty()) {
						int str = s.getOccupant().strength;
						if (!s.getOccupant().gold) {
							if (str < p.strength) {
								sum += str * dominationMult + dominationValue;
							} else if (str > p.strength) {
								sum -= p.strength * dominationMult
										+ dominationValue;
							}
						} else {
							sum += friendValue;
						}
					}
				}
			}
		}
		for (Piece p : board.silverPieces) {
			if (p.square != null) {
				sum -= pieceValue;
				if (p.strength == 1) {
					sum -= rabbitRowMult * (7 - p.square.index / 8);
				}
				if (p.square.nextToTrap > 0) {
					sum -= adjTrapPieceValue + adjTrapStrMult * p.strength;
				}
				if (p.isFrozen()) {
					sum += p.strength * frozenMult + frozenValue;
				}
				for (Square s : p.square.adjacent) {
					if (!s.isEmpty()) {
						int str = s.getOccupant().strength;
						if (s.getOccupant().gold) {
							if (str < p.strength) {
								sum -= str * dominationMult + dominationValue;
							} else if (str > p.strength) {
								sum += p.strength * dominationMult
										+ dominationValue;
							}
						} else {
							sum -= friendValue;
						}
					}
				}
			}
		}
		sum += ((TrapSquare) board.squares[18]).isOwnedBy(true) ? 500 : 0;
		sum += ((TrapSquare) board.squares[21]).isOwnedBy(true) ? 500 : 0;
		sum += ((TrapSquare) board.squares[42]).isOwnedBy(true) ? 500 : 0;
		sum += ((TrapSquare) board.squares[45]).isOwnedBy(true) ? 500 : 0;
		sum -= ((TrapSquare) board.squares[18]).isOwnedBy(false) ? 500 : 0;
		sum -= ((TrapSquare) board.squares[21]).isOwnedBy(false) ? 500 : 0;
		sum -= ((TrapSquare) board.squares[42]).isOwnedBy(false) ? 500 : 0;
		sum -= ((TrapSquare) board.squares[45]).isOwnedBy(false) ? 500 : 0;

		return sum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Evaluator#copy()
	 */
	@Override
	public Evaluator copy() {
		return null;
	}

}
