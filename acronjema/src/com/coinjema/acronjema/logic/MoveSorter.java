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
public interface MoveSorter {

	public void sort(IntBuffer moves, int length, Board board,
			boolean movingSide);

	/**
	 * @param key
	 * @param l
	 * @return
	 */
	public int compare(int key, int l);

}
