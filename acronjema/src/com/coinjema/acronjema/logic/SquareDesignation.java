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
public class SquareDesignation {

	public final String desig;
	public final int index;

	public SquareDesignation(String d) {
		desig = d;
		index = calcIndex();
	}

	/**
	 * 
	 */
	private int calcIndex() {
		return calcCol(desig.charAt(0)) + 8
				* (Integer.parseInt(desig.substring(1)) - 1);
	}

	/**
	 * @param charAt
	 * @return
	 */
	private int calcCol(char charAt) {
		return Character.toLowerCase(charAt) - 'a';
	}

	@Override
	public String toString() {
		return desig + " = " + index;
	}

	public static void main(String[] args) {
		System.out.println(new SquareDesignation("a1"));
		System.out.println(new SquareDesignation("e1"));
		System.out.println(new SquareDesignation("e2"));
		System.out.println(new SquareDesignation("g5"));
		System.out.println(new SquareDesignation("g8"));
		System.out.println(SquareDesignation.toString(8));
		System.out.println(SquareDesignation.toString(38));
	}

	/**
	 * @param index2
	 * @return
	 */
	public static String toString(int i) {
		return String.valueOf((char) ('a' + (i % 8))) + ((i / 8) + 1);
	}

}
