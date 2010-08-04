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
	final Piece[] goldPieces = new Piece[16];
	int hashStack = 0;

	boolean makeNewHash = true;

	boolean makeNewHash2 = true;

	int pieceCount = 0;

	final Piece[] pieces = new Piece[32];

	int silverPieceCount = 0;
	final Piece[] silverPieces = new Piece[16];
	Square[] squares = new Square[64];

	int stepCount = 0;
	private Boolean winner;

	public boolean currentTurn = true;

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
		addPiece(strength, gold, desig.index);
	}

	/**
	 * @param i
	 * @return
	 */
	private Square createSquare(int i) {
		if ((i == 0) || (i == 7) || (i == 56) || (i == 63)) {
			return new Square(this, i, 2, 3);
		}
		if ((i == 1) || (i == 8) || (i == 6) || (i == 15) || (i == 48)
				|| (i == 57) || (i == 62) || (i == 55)) {
			return new Square(this, i, 3, 4);
		}
		if ((i == 9) || (i == 14) || (i == 49) || (i == 54)) {
			return new Square(this, i, 4, 6);
		}
		if ((i % 8 == 0) || (i < 8) || ((i + 1) % 8 == 0) || (i > 56)) {
			return new Square(this, i, 3, 5);
		}
		if ((i % 8 == 1) || (i < 16) || ((i + 2) % 8 == 0) || (i > 48)) {
			return new Square(this, i, 4, 7);
		}
		if ((i == 18) || (i == 21) || (i == 42) || (i == 45)) {
			return new TrapSquare(this, i);
		}
		return new Square(this, i, 4, 8);

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
		for (Square s : squares) {
			s.init();
		}
	}

	/**
	 * @param move
	 */
	public void executeMove(int move) {
		executeStep(Move.getFirstHalf(move), true);
		executeStep(Move.getSecondHalf(move), true);
		currentTurn = !currentTurn;
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
		killTraps(seq[0], seq[1], notify);
		if (seq.length == 4) {
			squares[seq[2]].moveOccupantTo(squares[seq[3]], notify);
			killTraps(seq[2], seq[3], notify);
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

	void findAllSteps(StepBuffer stepBuffer, boolean gold) {

		for (Piece p : gold ? goldPieces : silverPieces) {
			Square s = p.square;
			if (s != null) {
				p.getSteps(stepBuffer);
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
	private void killTraps(final int sq, final int sq2, boolean notify) {
		int trapIndex = squares[sq].nextToTrap;
		if (trapIndex >= 0) {
			if (((TrapSquare) squares[trapIndex]).checkKill()) {
				((TrapSquare) squares[trapIndex]).killPiece(stepCount, notify);
			}
		}
		int otherTrapIndex = squares[sq2].nextToTrap;
		if ((otherTrapIndex >= 0) && (otherTrapIndex != trapIndex)) {
			if (((TrapSquare) squares[otherTrapIndex]).checkKill()) {
				((TrapSquare) squares[otherTrapIndex]).killPiece(stepCount,
						notify);
			}
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
		pieceCount = 0;
		goldPieceCount = 0;
		silverPieceCount = 0;
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
			currentTurn = !currentTurn;
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

	public Boolean getWinner() {
		if (winner != null) {
			return winner;
		} else {
			int goldRabbits = 0;
			for (Piece p : goldPieces) {
				if (p.square != null) {
					if (p.strength == 1) {
						goldRabbits++;
						if (p.square.index / 8 == 7) {
							winner = Boolean.TRUE;
						}
					}
				}
			}
			if (goldRabbits == 0) {
				winner = Boolean.FALSE;
			}
			int silverRabbits = 0;
			for (Piece p : silverPieces) {
				if (p.square != null) {
					if (p.strength == 1) {
						silverRabbits++;
						if (p.square.index / 8 == 0) {
							winner = Boolean.FALSE;
						}
					}
				}
			}
			if (silverRabbits == 0) {
				winner = Boolean.TRUE;
			}
		}
		return winner;
	}

	public void setWinner(boolean b) {
		winner = b;

	}

	public void addPiece(int strength, boolean gold, int index) {
		if (index > -1) {
			Piece occupant = new Piece(strength, gold);
			pieces[pieceCount++] = occupant;
			if (gold) {
				goldPieces[goldPieceCount++] = occupant;
			} else {
				silverPieces[silverPieceCount++] = occupant;
			}
			squares[index].setOccupant(occupant, false);
		}

	}

	public Board copy() {
		Board b = new Board();
		for (Piece p : pieces) {
			b.addPiece(p.strength, p.gold, p.square != null ? p.square.index
					: -1);
		}
		return b;
	}
}
