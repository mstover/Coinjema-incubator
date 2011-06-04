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
public class AEIPlayer implements Player {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Player#findMoves()
	 */
	@Override
	public int findMoves() throws GameEndException {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Player#executeMove(int)
	 */
	@Override
	public void executeMove(int move) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Player#setup()
	 */
	@Override
	public void setup() {
	}

	/**
	 * @param substring
	 */
	public void setSetup(String substring) {

	}

}
