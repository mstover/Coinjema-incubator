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
import java.util.StringTokenizer;

/**
 * @author michaelstover
 * 
 */
public class Play {

	private static Board board;
	Game game;
	MoveTree tree;
	private boolean goldSetup;
	private AEIPlayer aeiPlayer;
	private long tcMove;

	/**
	 * 
	 */
	public Play() {
	}

	public void controllerLoop() throws IOException {
		board = new Board();
		tree = new MoveTree(board, new SuperEvaluator());
		byte[] inBytes = new byte[1000];
		while (true) {
			int numBytes = System.in.read(inBytes);
			respond(inBytes, numBytes);
		}

		// boolean computerSide = args[0] != null ?
		// Boolean.parseBoolean(args[0])
		// : false;
		// Board b = new Board();
		// MoveTree tree = new MoveTree(b, new SuperEvaluator());
		// Player gold = computerSide ? new ComputerPlayer(tree, true,
		// Long.parseLong(args[1]), new SuperEvaluator())
		// : new HumanPlayer(b, true);
		// Player silver = computerSide ? new HumanPlayer(b, false)
		// : new ComputerPlayer(tree, false, Long.parseLong(args[1]),
		// new SuperEvaluator());
		// Game g = new Game(b, gold, silver);
		// g.playGame();}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		Play p = new Play();
		p.controllerLoop();
	}

	/**
	 * @param inBytes
	 */
	private void respond(byte[] inBytes, int numBytes) {
		StringTokenizer cmds = new StringTokenizer(new String(inBytes, 0,
				numBytes).trim(), "\n");
		while (cmds.hasMoreTokens()) {
			String cmd = cmds.nextToken();
			System.err.println(cmd);
			if ("aei".equals(cmd)) {
				System.out.println("protocol-version 1");
				System.out.println("id name Acronjema");
				System.out.println("id author Michael Stover");
				System.out.println("id version 0.1");
				System.out.println("aeiok");
				System.out
						.println("log Responded with protocol, id, and aeiok");

			} else if (cmd.startsWith("makemove")) {
				makeMove(cmd.substring(9));
			} else if (cmd.equals("isready")) {
				System.out.println("readyok");
				System.out.println("log Telling gameroom I'm ready");

			} else if (cmd.startsWith("setoption name tcmove value")) {
				tcMove = Integer.parseInt(cmd
						.substring("setoption name tcmove value ".length()));
				System.err.println("time per move = " + tcMove);
			} else if ("go".equals(cmd)) {
				if (!goldSetup) {
					Player gold = new ComputerPlayer(tree, false, tcMove,
							new SuperEvaluator());
					AEIPlayer silver = new AEIPlayer();
					aeiPlayer = silver;
					game = new Game(board, gold, silver);
					startGame();
				}
			} else if ("newgame".equals(cmd)) {
				System.out.println("log created board");
			} else {
				System.out.println("log I have no response to this");
			}
		}
		System.out.flush();
	}

	/**
	 * @param substring
	 */
	private void makeMove(String substring) {
		if (!goldSetup) {
			AEIPlayer gold = new AEIPlayer();
			aeiPlayer = gold;
			Player silver = new ComputerPlayer(tree, false, tcMove,
					new SuperEvaluator());
			aeiPlayer.setSetup(substring);
			game = new Game(board, gold, silver);
		}
		startGame();
	}

	/**
	 * 
	 */
	private void startGame() {
	}
}
