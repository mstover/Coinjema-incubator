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

	public static final SquareDesignation a1 = new SquareDesignation("a1");
	public static final SquareDesignation a2 = new SquareDesignation("a2");
	public static final SquareDesignation a3 = new SquareDesignation("a3");
	public static final SquareDesignation a4 = new SquareDesignation("a4");
	public static final SquareDesignation a5 = new SquareDesignation("a5");
	public static final SquareDesignation a6 = new SquareDesignation("a6");
	public static final SquareDesignation a7 = new SquareDesignation("a7");
	public static final SquareDesignation a8 = new SquareDesignation("a8");
	public static final SquareDesignation b1 = new SquareDesignation("b1");
	public static final SquareDesignation b2 = new SquareDesignation("b2");
	public static final SquareDesignation b3 = new SquareDesignation("b3");
	public static final SquareDesignation b4 = new SquareDesignation("b4");
	public static final SquareDesignation b5 = new SquareDesignation("b5");
	public static final SquareDesignation b6 = new SquareDesignation("b6");
	public static final SquareDesignation b7 = new SquareDesignation("b7");
	public static final SquareDesignation b8 = new SquareDesignation("b8");
	public static final SquareDesignation c1 = new SquareDesignation("c1");
	public static final SquareDesignation c2 = new SquareDesignation("c2");
	public static final SquareDesignation c3 = new SquareDesignation("c3");
	public static final SquareDesignation c4 = new SquareDesignation("c4");
	public static final SquareDesignation c5 = new SquareDesignation("c5");
	public static final SquareDesignation c6 = new SquareDesignation("c6");
	public static final SquareDesignation c7 = new SquareDesignation("c7");
	public static final SquareDesignation c8 = new SquareDesignation("c8");
	public static final SquareDesignation d1 = new SquareDesignation("d1");
	public static final SquareDesignation d2 = new SquareDesignation("d2");
	public static final SquareDesignation d3 = new SquareDesignation("d3");
	public static final SquareDesignation d4 = new SquareDesignation("d4");
	public static final SquareDesignation d5 = new SquareDesignation("d5");
	public static final SquareDesignation d6 = new SquareDesignation("d6");
	public static final SquareDesignation d7 = new SquareDesignation("d7");
	public static final SquareDesignation d8 = new SquareDesignation("d8");
	public static final SquareDesignation e1 = new SquareDesignation("e1");
	public static final SquareDesignation e2 = new SquareDesignation("e2");
	public static final SquareDesignation e3 = new SquareDesignation("e3");
	public static final SquareDesignation e4 = new SquareDesignation("e4");
	public static final SquareDesignation e5 = new SquareDesignation("e5");
	public static final SquareDesignation e6 = new SquareDesignation("e6");
	public static final SquareDesignation e7 = new SquareDesignation("e7");
	public static final SquareDesignation e8 = new SquareDesignation("e8");
	public static final SquareDesignation f1 = new SquareDesignation("f1");
	public static final SquareDesignation f2 = new SquareDesignation("f2");
	public static final SquareDesignation f3 = new SquareDesignation("f3");
	public static final SquareDesignation f4 = new SquareDesignation("f4");
	public static final SquareDesignation f5 = new SquareDesignation("f5");
	public static final SquareDesignation f6 = new SquareDesignation("f6");
	public static final SquareDesignation f7 = new SquareDesignation("f7");
	public static final SquareDesignation f8 = new SquareDesignation("f8");
	public static final SquareDesignation g1 = new SquareDesignation("g1");
	public static final SquareDesignation g2 = new SquareDesignation("g2");
	public static final SquareDesignation g3 = new SquareDesignation("g3");
	public static final SquareDesignation g4 = new SquareDesignation("g4");
	public static final SquareDesignation g5 = new SquareDesignation("g5");
	public static final SquareDesignation g6 = new SquareDesignation("g6");
	public static final SquareDesignation g7 = new SquareDesignation("g7");
	public static final SquareDesignation g8 = new SquareDesignation("g8");
	public static final SquareDesignation h1 = new SquareDesignation("h1");
	public static final SquareDesignation h2 = new SquareDesignation("h2");
	public static final SquareDesignation h3 = new SquareDesignation("h3");
	public static final SquareDesignation h4 = new SquareDesignation("h4");
	public static final SquareDesignation h5 = new SquareDesignation("h5");
	public static final SquareDesignation h6 = new SquareDesignation("h6");
	public static final SquareDesignation h7 = new SquareDesignation("h7");
	public static final SquareDesignation h8 = new SquareDesignation("h8");
	public static final SquareDesignation xx = new SquareDesignation("xx");

	public final String desig;
	public final int index;

	public SquareDesignation(String d) {
		if ("xx".equals(d)) {
			index = -1;
			desig = d;
		} else {
			desig = d;
			index = calcIndex();
		}
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
