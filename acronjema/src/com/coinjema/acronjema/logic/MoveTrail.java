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

import java.util.List;

/**
 * @author michaelstover
 * 
 */
public class MoveTrail {

	final boolean nextTurn;
	final List<Integer> indexes;

	public MoveTrail(boolean turn, List<Integer> moves) {
		nextTurn = turn;
		this.indexes = moves;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MoveTrail [nextTurn=" + nextTurn + ", indexes=" + indexes + "]";
	}

}
