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
public class Game {

	private final Board board;
	private final Player goldPlayer;
	private final Player silverPlayer;
	private final Move moveService = new Move();

	public Game(Board board, Player gold, Player silver) {
		this.board = board;
		this.goldPlayer = gold;
		this.silverPlayer = silver;
	}

	public void playGame() {
		boolean turn = true;
		int count = 0;
		goldPlayer.setup();
		board.print(System.out);
		silverPlayer.setup();
		board.print(System.out);
		while ((board.getWinner() == null) && (count < 500)) {
			try {
				int move = turn ? goldPlayer.findMoves() : silverPlayer
						.findMoves();
				goldPlayer.executeMove(move);
				silverPlayer.executeMove(move);
				board.executeMove(move);
				board.toggleTurn();
				System.out.println((turn ? "gold" : "silver") + " moved");
				System.out.println(moveService.toString(move));
				turn = !turn;
				count++;
				board.print(System.out);
			} catch (GameEndException e) {
				board.setWinner(!turn);
			}
		}

	}

}
