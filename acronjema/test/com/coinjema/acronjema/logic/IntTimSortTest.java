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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

/**
 * @author michaelstover
 * 
 */
public class IntTimSortTest {

	@Test
	public void testSort() throws Exception {
		int[] values = randomArray();
		int[] sorted = copyArray(values);
		Arrays.sort(sorted);
		int[] shadow = copyArray(values);
		// printArray(values);
		IntTimSort.sort(values, null, shadow);
		// printArray(values);
		assertTrue(equals(values, sorted));
		// printArray(shadow);
		assertTrue(equals(shadow, sorted));
		values[0] = 10;
		long time = System.nanoTime();
		IntTimSort.sort(values, null, shadow);
		System.out.println("simple sort took " + (System.nanoTime() - time)
				+ " nanas");

	}

	/**
	 * @return
	 */
	private int[] randomArray() {
		Random rand = new Random();
		int[] r = new int[20000];
		for (int i = 0; i < r.length; i++) {
			r[i] = rand.nextInt();
		}
		return r;
	}

	/**
	 * @param values
	 * @return
	 */
	private int[] copyArray(int[] values) {
		int[] copy = new int[values.length];
		System.arraycopy(values, 0, copy, 0, values.length);
		return copy;
	}

	/**
	 * @param values
	 * @param sorted
	 * @return
	 */
	private boolean equals(int[] test, int[] base) {
		for (int i = 0; i < base.length; i++) {
			if (base[i] != test[i]) {
				return false;
			}
		}
		return true;
	}

	private void printArray(int[] arr) {
		System.out.println("Array [");
		for (int i : arr) {
			System.out.print(i + ", ");
		}
		System.out.println("]");
	}

}
