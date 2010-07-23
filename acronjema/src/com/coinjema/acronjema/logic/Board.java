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

/**
 * @author michaelstover
 * 
 */
public class Board {
	private final static int FIRST_STEPS = Integer.MAX_VALUE >>> 16;
	private final static long SECOND_STEPS = FIRST_STEPS << 16;

	Square[] squares = new Square[64];

	int stepCount = 0;

	public Board() {
		createSquares();
	}

	public void reinit() {
		createSquares();
		stepCount = 0;
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
			return new Square(this, i, 2);
		}
		if ((i % 8 == 0) || (i < 8) || ((i + 1) % 8 == 0) || (i > 56)) {
			return new Square(this, i, 3);
		}
		if ((i == 18) || (i == 21) || (i == 42) || (i == 45)) {
			return new TrapSquare(this, i);
		}
		return new Square(this, i, 4);

	}

	/**
	 * @param step
	 */
	public void executeStep(int step) {
		if (Move.isNullMove(step)) {
			return;
		}
		int[] seq = Move.getStepSequence(step);
		squares[seq[0]].moveOccupantTo(squares[seq[1]]);
		killTraps();
		if (seq.length == 4) {
			squares[seq[2]].moveOccupantTo(squares[seq[3]]);
			killTraps();
		}
		makeNewHash = true;
		hashStack++;
		if (hashStack == 20) {
			hashStack = 4;
			boardHashes[3] = boardHashes[19];
			boardHashes[2] = boardHashes[18];
			boardHashes[1] = boardHashes[17];
			boardHashes[0] = boardHashes[16];
		}
	}

	/**
	 * 
	 */
	private void killTraps() {
		if (((TrapSquare) squares[18]).checkKill()) {
			((TrapSquare) squares[18]).killPiece(stepCount);
		}
		if (((TrapSquare) squares[21]).checkKill()) {
			((TrapSquare) squares[21]).killPiece(stepCount);
		}
		if (((TrapSquare) squares[42]).checkKill()) {
			((TrapSquare) squares[42]).killPiece(stepCount);
		}
		if (((TrapSquare) squares[45]).checkKill()) {
			((TrapSquare) squares[45]).killPiece(stepCount);
		}
		stepCount++;
	}

	public long getBoardHashAfterMove(int move) {
		long hash = getBoardHash();
		int[] seq = Move.getStepSequence(move);
		if (seq.length == 4) {
			hash = hash ^ (1l << seq[2]);
		} else {
			hash = hash ^ (1l << seq[0]);
		}
		hash = hash ^ (1l << seq[1]);
		return hash;
	}

	void findAllSteps(StepTree stepBuffer, boolean gold) {

		for (Square s : squares) {
			if (!s.isEmpty() && (s.getOccupant().gold == gold)) {
				s.getOccupant().getSteps(stepBuffer);
			}
		}
	}

	/**
	 * @param move
	 */
	public void rewindSteps(int move) {
		if (Move.isNullMove(move)) {
			return;
		}
		int[] seq = Move.getStepSequence(move);
		if (seq.length == 4) {
			reviveTraps();
			squares[seq[3]].moveOccupantTo(squares[seq[2]]);
		}
		reviveTraps();
		squares[seq[1]].moveOccupantTo(squares[seq[0]]);
		hashStack--;
	}

	/**
	 * 
	 */
	private void reviveTraps() {
		stepCount--;
		((TrapSquare) squares[18]).unkillPiece(stepCount);
		((TrapSquare) squares[21]).unkillPiece(stepCount);
		((TrapSquare) squares[42]).unkillPiece(stepCount);
		((TrapSquare) squares[45]).unkillPiece(stepCount);
	}

	long[] boardHashes = new long[20];
	int hashStack = 0;
	boolean makeNewHash = true;

	/**
	 * @return
	 */
	public long getBoardHash() {
		if (makeNewHash) {
			long hash = 0;
			for (Square s : squares) {
				if (!s.isEmpty()) {
					hash = hash | (1l << (s.index));
				}
			}
			boardHashes[hashStack] = hash;
			makeNewHash = false;
			return hash;
		} else {
			return boardHashes[hashStack];
		}
	}

	/**
	 * @param move
	 */
	public void executeMove(int move) {
		executeStep(Move.getFirstHalf(move));
		executeStep(Move.getSecondHalf(move));
	}

	public void rewindMove(int move) {
		try {
			rewindSteps(Move.getSecondHalf(move));
			rewindSteps(Move.getFirstHalf(move));
		} catch (Exception e) {

			Move.printStepsForMove(move);
			print(System.out);
			e.printStackTrace();
			System.exit(0);
		}
	}
}
