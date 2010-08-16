package com.coinjema.acronjema.logic;

public class EmptyEvaluator implements Evaluator {

	@Override
	public int evaluate(Board board) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Evaluator#copy()
	 */
	@Override
	public Evaluator copy() {
		return this;
	}

}
