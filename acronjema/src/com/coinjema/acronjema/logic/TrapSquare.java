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
public class TrapSquare extends Square {

	/**
	 * @param adj
	 */
	public TrapSquare(int index) {
		super(index, 4);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.Square#print(java.io.PrintStream)
	 */
	@Override
	public void print(PrintStream out) {
		if (occupant == null) {
			out.print(" # ");
		} else {
			occupant.print(out);
		}
	}

}
