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
	private static final int SET_SIZE = 15881;

	private final long[][] set = new long[SET_SIZE][10];
	private final long[][] secSet = new long[SET_SIZE][10];

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
		for (long[] inner : secSet) {
			for (int i = 0; i < inner.length; i++) {
				inner[i] = 1;
			}
		}
	}

	public boolean add(final long l, final long alt) {
		final int index = ((int) ((alt) ^ (l >>> 32)) & (NO_NEGATIVE))
				% SET_SIZE;
		final int innerLength = set[index].length;
		final long[] innerArr = set[index];
		final long[] innerAltArr = secSet[index];
		for (int i = 0; i < innerLength; i++) {
			if ((innerArr[i] == 0) && (innerAltArr[i] == 1)) {
				innerArr[i] = l;
				innerAltArr[i] = alt;
				return true;
			} else if ((innerArr[i] == l) && (innerAltArr[i] == alt)) {
				return false;
			}
		}
		// have to increase array and try again.
		final long[] newInner = new long[innerLength * 2];
		System.arraycopy(innerArr, 0, newInner, 0, innerLength);
		final long[] newAltInner = new long[innerLength * 2];
		System.arraycopy(innerAltArr, 0, newAltInner, 0, innerLength);
		for (int i = innerLength; i < newAltInner.length; i++) {
			newAltInner[i] = 1;
		}
		set[index] = newInner;
		secSet[index] = newAltInner;
		newInner[innerLength] = l;
		newAltInner[innerLength] = alt;
		return true;
	}

}
