package com.coinjema.acronjema.logic;

import java.nio.IntBuffer;

/**
 * The tree structure is defined by:
 * 
 * 1. int describing number of options for this ply
 * 
 * [x,x+1] long giving step, int pointing to tree branch after given step
 * 
 * Each branched position requires 12 bytes per legal move. Say average of
 * 20,000 moves per ply, that requires 240,000 bytes p
 * 
 * @author michael
 * 
 */
public class MoveTree {

	IntBuffer moves = IntBuffer.allocate(25000000);
	IntBuffer moveCountNextPly;
	IntBuffer addressNextPly;
	IntBuffer evaluations = IntBuffer.allocate(1000000);
	LongSet duplicates = new LongSet();
	Evaluator evaluator;

	private final Board board;

	public MoveTree(Board b, Evaluator evaluator) {
		this.board = b;
		duplicates.add(board.getBoardHash(), board.getBoardHash2());
		this.evaluator = evaluator;
	}

	public void addMove(int move) {
		if (duplicates.add(board.getBoardHash(), board.getBoardHash2())) {
			moves.put(move);
			evaluations.put(evaluator.evaluate(board));
		} else {

		}

	}

	public int getFirstNumber() {
		return moves.position();
	}

	public void rewind() {
		moves.rewind();
		evaluations.rewind();
	}

	public void sortPly(int start, int end, MoveSorter c) {
		IntTimSort.sort(evaluations.array(), start, end, c, moves.array());
		System.out.println("After sorting, top move has evaluation of "
				+ evaluations.get(0));
	}

	public void searchForMoves(StepTree stepTree, boolean gold) {
		stepTree.searchForMinSteps(gold, 0, 2, Move.EMPTY_MOVE);
		int moveCount = getFirstNumber();
		for (int i = 0; i < moveCount; i++) {
			stepTree.clear();
			int move = moves.get(i);
			int diff = Move.getStepCountOfMove(move);
			if (diff > 1) {
				board.executeMove(move);
				stepTree.searchForSteps(gold, 4 - diff, move);
				board.rewindMove(move);
			}
		}
	}

}
