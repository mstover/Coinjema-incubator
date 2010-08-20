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
	private final int frozenMult = 89;
	private final int dominationMult = 73;
	private final int dominationValue = 82;
	private final int friendValue = 48;
	private final int frozenValue = 86;
	private final int adjTrapStrMult = 27;
	private int adjTrapPieceValue = 79;
	private final int pieceValue = 8289;
	private final int rabbitRowMult = 17;
	private final int numMovesMult = 50;

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
		for (Piece p : board.pieces) {
			if (p.strength > 1) {
				if (p.gold && (p.square != null)) {
					p.getSteps(buffer);
					sum += p.strength / 2 * numMovesMult
							* buffer.steps.position();
					buffer.steps.clear();
				} else if (!p.gold && (p.square != null)) {
					p.getSteps(buffer);
					sum -= p.strength / 2 * numMovesMult
							* buffer.steps.position();
					buffer.steps.clear();
				}
			}
		}
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
