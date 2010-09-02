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

import java.util.Random;

/**
 * @author michaelstover
 * 
 */
public class Zobrist {

	Random rand = new Random(3);

	public long getZobristNumber() {
		return rand.nextLong();
	}

}
