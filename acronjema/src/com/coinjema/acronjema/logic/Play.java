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

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author michaelstover
 * 
 */
public class Play {

	/**
	 * 
	 */
	public Play() {
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		boolean computerSide = args[1] != null ? Boolean.parseBoolean(args[1])
				: false;
		Board b = new Board();
		MoveTree tree = new MoveTree(b, new BaseEvaluator(
				new GenePool(args[0]).getBestConfig()));
		Player gold = computerSide ? new ComputerPlayer(tree, true)
				: new HumanPlayer(b, true);
		Player silver = computerSide ? new HumanPlayer(b, false)
				: new ComputerPlayer(tree, false);
		Game g = new Game(b, gold, silver);
		g.playGame();
	}

}
