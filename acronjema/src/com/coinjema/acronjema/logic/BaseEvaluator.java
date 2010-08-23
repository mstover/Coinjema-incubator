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
public class BaseEvaluator implements Evaluator {

	private final int frozenMult;
	private final int dominationMult;
	private final int dominationValue;
	private final int friendValue;
	private final int frozenValue;
	private final int adjTrapStrMult;
	private final int adjTrapPieceValue;
	private final int pieceValue;
	private final int winValue;
	private final int rabbitRowMult;
	private final int numMovesMult;
	private final BaseEvaluatorConfig config;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coinjema.acronjema.logic.Evaluator#evaluate(com.coinjema.acronjema
	 * .logic.Board)
	 */
	/**
	 * 
	 */
	public BaseEvaluator(BaseEvaluatorConfig config) {
		super();
		this.config = config;
		this.frozenMult = config.frozenMult;
		this.dominationMult = config.dominationMult;
		this.dominationValue = config.dominationValue;
		this.friendValue = config.friendValue;
		this.frozenValue = config.frozenValue;
		this.adjTrapPieceValue = config.adjTrapPieceValue;
		this.adjTrapStrMult = config.adjTrapStrMult;
		this.pieceValue = config.pieceValue;
		this.winValue = config.winValue;
		this.rabbitRowMult = config.rabbitRowMult;
		this.numMovesMult = config.numMovesMult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Evaluator#copy()
	 */
	@Override
	public Evaluator copy() {
		return new BaseEvaluator(config);
	}

	@Override
	public int evaluate(Board board) {
		if (board.getWinner() != null) {
			return board.getWinner() ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		}
		int sum = 0;
		for (Piece p : board.goldPieces) {
			if (p.square != null) {
				sum += numMovesMult * p.getStepCount();
			}
		}
		for (Piece p : board.silverPieces) {
			if (p.square != null) {
				sum -= numMovesMult * p.getStepCount();
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
}
