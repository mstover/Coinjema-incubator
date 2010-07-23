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
public class LongSet {
	private static final int NO_NEGATIVE = Integer.MAX_VALUE;
	private static final int SET_SIZE = 6037;

	private long[][] set = new long[SET_SIZE][10];

	public LongSet() {
		clear();
	}

	/**
	 * 
	 */
	public void clear() {
		for (long[] inner : set) {
			for (int i = 0; i < inner.length; i++) {
				inner[i] = 0;
			}
		}
	}

	public boolean add(final long l) {
		final int index = ((int) (l ^ (l >>> 32)) & (NO_NEGATIVE)) % SET_SIZE;
		final int innerLength = set[index].length;
		final long[] innerArr = set[index];
		for (int i = 0; i < innerLength; i++) {
			if (innerArr[i] == 0) {
				innerArr[i] = l;
				return true;
			} else if (innerArr[i] == l) {
				return false;
			}
		}
		// System.out.println("Have to increase inner array size to "
		// + (innerLength * 2));
		// have to increase array and try again.
		final long[] newInner = new long[innerLength * 2];
		System.arraycopy(innerArr, 0, newInner, 0, innerLength);
		set[index] = newInner;
		newInner[innerLength] = l;
		return true;
	}

}
