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
public interface Player {

	/**
	 * @return
	 */
	int findMoves() throws GameEndException;

	/**
	 * @param move
	 */
	void executeMove(int move);

	void setup();

}
