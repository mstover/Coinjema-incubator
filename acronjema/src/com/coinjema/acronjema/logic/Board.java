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

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author michaelstover
 * 
 */
public class Board {

	private ExecutorService executor;

	private class BoardExecutor implements Runnable {
		private final List<Square> squares = new LinkedList<Square>();
		private final int mark;

		public BoardExecutor(int workerNo) {
			this.mark = workerNo * 1000;

		}

		public void addSquare(Square s) {
			squares.add(s);
		}

		public void findMoves(StepBuffer buf) {

		}

		@Override
		public void run() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	}

	Square[] squares = new Square[64];
	private BoardExecutor[] executors;

	public Board() {
		createSquares();
		createExecutor();
	}

	private void createExecutor() {
		int numProcessors = Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(numProcessors);
		executors = new BoardExecutor[numProcessors];
		int count = 0;
		for (int i = 0; i < numProcessors; i++) {
			executors[i] = new BoardExecutor(i);
		}
		for (int i = 0; i < squares.length; i++) {
			executors[i % numProcessors].addSquare(squares[i]);
		}

	}

	/**
	 * 
	 */
	private void createSquares() {
		for (int i = 0; i < squares.length; i++) {
			squares[i] = createSquare(i);
		}
		for (int i = 0; i < squares.length; i++) {
			squares[i].setAdjacent(getAdjacent(i));
		}
	}

	public void addPiece(int strength, boolean gold, SquareDesignation desig) {
		squares[desig.index].setOccupant(new Piece(strength, gold));
	}

	public void print(PrintStream out) {
		out.println();
		out.println("****    Board    ****");
		out.println();
		for (int i = 56; i >= 0; i -= 8) {
			for (int j = 0; j < 8; j++) {
				squares[i + j].print(out);
			}
			out.println();
		}
		out.println("------------------------------------");
	}

	/**
	 * @param i
	 * @return
	 */
	private Square[] getAdjacent(int i) {
		if ((i == 0)) {
			return new Square[] { squares[1], squares[8] };
		}
		if (i == 7) {
			return new Square[] { squares[6], squares[15] };
		}
		if (i == 56) {
			return new Square[] { squares[57], squares[48] };
		}
		if (i == 63) {
			return new Square[] { squares[62], squares[55] };
		}
		if (i % 8 == 0) {
			return new Square[] { squares[i + 1], squares[i + 8],
					squares[i - 8] };
		}
		if (i < 8) {
			return new Square[] { squares[i - 1], squares[i + 8],
					squares[i + 1] };
		}
		if ((i + 1) % 8 == 0) {
			return new Square[] { squares[i - 1], squares[i + 8],
					squares[i - 8] };
		}
		if (i > 56) {
			return new Square[] { squares[i - 1], squares[i + 1],
					squares[i - 8] };
		}
		return new Square[] { squares[i - 1], squares[i + 1], squares[i - 8],
				squares[i + 8] };

	}

	/**
	 * @param i
	 * @return
	 */
	private Square createSquare(int i) {
		if ((i == 0) || (i == 7) || (i == 56) || (i == 63)) {
			return new Square(i, 2);
		}
		if ((i % 8 == 0) || (i < 8) || ((i + 1) % 8 == 0) || (i > 56)) {
			return new Square(i, 3);
		}
		if ((i == 18) || (i == 21) || (i == 42) || (i == 45)) {
			return new TrapSquare(i);
		}
		return new Square(i, 4);

	}

	/**
	 * @param choice
	 */
	public void executeMove(int choice) {
		int[] seq = Move.getStepSequence(choice);
		squares[seq[0]].moveOccupantTo(squares[seq[1]]);
		if (seq.length == 4) {
			squares[seq[2]].moveOccupantTo(squares[seq[3]]);
		}
	}

	void findAllSteps(StepBuffer stepBuffer, boolean gold) {

		for (Square s : squares) {
			if (!s.isEmpty() && (s.getOccupant().gold == gold)) {
				s.getOccupant().getSteps(stepBuffer);
			}
		}
	}

}
