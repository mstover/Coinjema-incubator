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

	long[] boardHashes = new long[20];

	long[] boardHashes2 = new long[20];
	int goldPieceCount = 0;
	private final Piece[] goldPieces = new Piece[16];
	int hashStack = 0;

	boolean makeNewHash = true;

	boolean makeNewHash2 = true;

	int pieceCount = 0;

	private final Piece[] pieces = new Piece[32];

	int silverPieceCount = 0;
	private final Piece[] silverPieces = new Piece[16];
	Square[] squares = new Square[64];

	int stepCount = 0;

	public Board() {
		createSquares();
	}

	/**
	 * gold rabits, cats, dogs, horses, camel, elephant, then silver rabbits,
	 * cats, dogs, horses, camel, elephant
	 * 
	 * @param position
	 */
	public Board(SquareDesignation... position) {
		this();
		int count = 0;
		for (int i = 0; i < 8; i++) {
			addPiece(1, true, position[count++]);
		}
		addPiece(2, true, position[count++]);
		addPiece(2, true, position[count++]);
		addPiece(3, true, position[count++]);
		addPiece(3, true, position[count++]);
		addPiece(4, true, position[count++]);
		addPiece(4, true, position[count++]);
		addPiece(5, true, position[count++]);
		addPiece(6, true, position[count++]);
		for (int i = 0; i < 8; i++) {
			addPiece(1, false, position[count++]);
		}
		addPiece(2, false, position[count++]);
		addPiece(2, false, position[count++]);
		addPiece(3, false, position[count++]);
		addPiece(3, false, position[count++]);
		addPiece(4, false, position[count++]);
		addPiece(4, false, position[count++]);
		addPiece(5, false, position[count++]);
		addPiece(6, false, position[count++]);
	}

	public void addPiece(int strength, boolean gold, SquareDesignation desig) {
		if (desig.index > -1) {
			Piece occupant = new Piece(strength, gold);
			pieces[pieceCount++] = occupant;
			if (gold) {
				goldPieces[goldPieceCount++] = occupant;
			} else {
				silverPieces[silverPieceCount++] = occupant;
			}
			squares[desig.index].setOccupant(occupant, false);
		}
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
	 * 
	 */
	private void createSquares() {
		for (int i = 0; i < pieces.length; i++) {
			pieces[i] = new Piece(1, true);
		}
		for (int i = 0; i < goldPieces.length; i++) {
			goldPieces[i] = new Piece(1, true);
		}
		for (int i = 0; i < silverPieces.length; i++) {
			silverPieces[i] = new Piece(1, false);
		}
		for (int i = 0; i < squares.length; i++) {
			squares[i] = createSquare(i);
		}
		for (int i = 0; i < squares.length; i++) {
			squares[i].setAdjacent(getAdjacent(i));
		}
	}

	/**
	 * @param move
	 */
	public void executeMove(int move) {
		executeStep(Move.getFirstHalf(move), true);
		executeStep(Move.getSecondHalf(move), true);
	}

	/**
	 * @param step
	 */
	public void executeStep(int step, boolean notify) {
		if (Move.isNullMove(step)) {
			return;
		}
		int[] seq = Move.getStepSequence(step);
		squares[seq[0]].moveOccupantTo(squares[seq[1]], notify);
		killTraps(notify);
		if (seq.length == 4) {
			squares[seq[2]].moveOccupantTo(squares[seq[3]], notify);
			killTraps(notify);
		}
		makeNewHash = true;
		makeNewHash2 = true;
		hashStack++;
		if (hashStack == 20) {
			hashStack = 4;
			boardHashes[3] = boardHashes[19];
			boardHashes[2] = boardHashes[18];
			boardHashes[1] = boardHashes[17];
			boardHashes[0] = boardHashes[16];
			boardHashes2[3] = boardHashes2[19];
			boardHashes2[2] = boardHashes2[18];
			boardHashes2[1] = boardHashes2[17];
			boardHashes2[0] = boardHashes2[16];
		}
	}

	void findAllSteps(StepTree stepBuffer, boolean gold) {

		for (Square s : squares) {
			if (!s.isEmpty() && (s.getOccupant().gold == gold)) {
				s.getOccupant().getSteps(stepBuffer);
			}
		}
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
	 * @return
	 */
	public long getBoardHash() {
		if (makeNewHash) {
			long hash = 0;
			for (Piece p : pieces) {
				Square s = p.square;
				if (s != null) {
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

	public long getBoardHash2() {
		if (makeNewHash2) {
			long hash = 0;
			for (Piece p : pieces) {
				Square s = p.square;
				if (s != null) {
					hash ^= (p.strength << (s.index));
				}
			}
			boardHashes2[hashStack] = hash;
			makeNewHash2 = false;
			return hash;
		} else {
			return boardHashes2[hashStack];
		}
	}

	/**
	 * @param step
	 * @return
	 */
	public long getBoardHash2AfterMove(int step) {
		return 0;
	}

	/**
	 * 
	 */
	private void killTraps(boolean notify) {
		if (((TrapSquare) squares[18]).checkKill()) {
			((TrapSquare) squares[18]).killPiece(stepCount, notify);
		}
		if (((TrapSquare) squares[21]).checkKill()) {
			((TrapSquare) squares[21]).killPiece(stepCount, notify);
		}
		if (((TrapSquare) squares[42]).checkKill()) {
			((TrapSquare) squares[42]).killPiece(stepCount, notify);
		}
		if (((TrapSquare) squares[45]).checkKill()) {
			((TrapSquare) squares[45]).killPiece(stepCount, notify);
		}
		stepCount++;
	}

	public int pieceCount() {
		int sum = 0;
		for (Square s : squares) {
			if (!s.isEmpty()) {
				sum++;
			}
		}
		return sum;
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

	public void reinit() {
		createSquares();
		stepCount = 0;
	}

	/**
	 * 
	 */
	private void reviveTraps(boolean notify) {
		stepCount--;
		((TrapSquare) squares[18]).unkillPiece(stepCount, notify);
		((TrapSquare) squares[21]).unkillPiece(stepCount, notify);
		((TrapSquare) squares[42]).unkillPiece(stepCount, notify);
		((TrapSquare) squares[45]).unkillPiece(stepCount, notify);
	}

	public void rewindMove(int move) {
		try {
			rewindSteps(Move.getSecondHalf(move), true);
			rewindSteps(Move.getFirstHalf(move), true);
		} catch (Exception e) {

			Move.printStepsForMove(move);
			print(System.out);
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * @param move
	 */
	public void rewindSteps(int move, boolean notify) {
		if (Move.isNullMove(move)) {
			return;
		}
		int[] seq = Move.getStepSequence(move);
		if (seq.length == 4) {
			reviveTraps(notify);
			squares[seq[3]].moveOccupantTo(squares[seq[2]], notify);
		}
		reviveTraps(notify);
		squares[seq[1]].moveOccupantTo(squares[seq[0]], notify);
		hashStack--;
	}
}
