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

import java.nio.IntBuffer;

/**
 * @author michaelstover
 * 
 */
public class BaseEvaluator implements Evaluator {

	class EvaluatorBuffer implements StepBuffer {
		IntBuffer steps = IntBuffer.allocate(200);

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.coinjema.acronjema.logic.StepBuffer#putStep(int)
		 */
		@Override
		public void putStep(int i) {
			steps.put(i);
		}
	}

	private EvaluatorBuffer buffer;

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
	public BaseEvaluator() {
		super();
		buffer = new EvaluatorBuffer();
	}

	@Override
	public int evaluate(Board board) {
		int sum = 0;
		buffer.steps.clear();
		board.findAllSteps(buffer, true);
		sum = buffer.steps.position();
		buffer.steps.clear();
		board.findAllSteps(buffer, false);
		sum -= buffer.steps.position();
		for (Piece p : board.goldPieces) {
			if (p.square != null) {
				sum += 25 + p.strength;
				if ((p.strength == 1) && (p.square.index / 8 == 7)) {
					sum += 1000000;
				}
				if (p.strength == 1) {
					sum += (p.square.index / 8);
				}
				if (p.square.nextToTrap > 0) {
					sum += p.strength;
				}
				if (p.isFrozen()) {
					sum -= p.strength;
				}
			}
		}
		for (Piece p : board.silverPieces) {
			if (p.square != null) {
				sum -= 25 + p.strength;
				if ((p.strength == 1) && (p.square.index / 8 == 0)) {
					sum -= 1000000;
				}
				if (p.strength == 1) {
					sum -= (7 - p.square.index / 8);
				}
				if (p.square.nextToTrap > 0) {
					sum -= p.strength;
				}
				if (p.isFrozen()) {
					sum += p.strength;
				}
			}
		}
		return sum;
	}
}
