package com.coinjema.acronjema.logic;

public class IntSet {
	private static final int NO_NEGATIVE = Integer.MAX_VALUE;
	private static final int SET_SIZE = 6037;

	private final int[][] set = new int[SET_SIZE][10];

	public IntSet() {
		clear();
	}

	/**
	 * 
	 */
	public void clear() {
		for (int[] inner : set) {
			for (int i = 0; i < inner.length; i++) {
				inner[i] = 0;
			}
		}
	}

	public boolean add(final int l) {
		final int index = (l & (NO_NEGATIVE)) % SET_SIZE;
		final int innerLength = set[index].length;
		final int[] innerArr = set[index];
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
		final int[] newInner = new int[innerLength * 2];
		System.arraycopy(innerArr, 0, newInner, 0, innerLength);
		set[index] = newInner;
		newInner[innerLength] = l;
		return true;
	}

}
