package com.coinjema.acronjema.logic;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

/**
 * The tree structure is defined by:
 * 
 * 1. int describing number of options for this ply
 * 
 * [x,x+1] int giving step, int pointing to tree branch after given step
 * 
 * @author michael
 * 
 */
public class StepTree {

	int curSize = 0;

	private final Board board;
	private final MoveTree moveTree;

	public StepTree(Board b, MoveTree moveTree) {
		this.board = b;
		this.moveTree = moveTree;
	}

	public int get(int index) {
		return steps.get(index);
	}

	public final Buffer position(int newPosition) {
		return steps.position(newPosition);
	}

	IntBuffer steps = ByteBuffer.allocateDirect(8000000).asIntBuffer();

	boolean acceptDoubleMove = true;

	private final LongSet duplicates = new LongSet();

	protected boolean isAcceptDoubleMove() {
		return acceptDoubleMove;
	}

	protected void setAcceptDoubleMove(boolean acceptDoubleMove) {
		this.acceptDoubleMove = acceptDoubleMove;
	}

	public void putStep(int step) {
		if (acceptDoubleMove || (Move.getStepCount(step) == 1)) {
			long hash = board.getBoardHashAfterMove(step);
			long altHash = board.stepCount + 1;
			if (duplicates.add(hash, altHash)) {
				steps.put(step);
				steps.put(0);
			}
		}

	}

	public final int getNumberChoices() {
		return steps.position() / 2;
	}

	public final Buffer clear() {
		curSize = 0;
		return steps.clear();
	}

	boolean traceon = false;

	/**
	 * Searches for all unique step sequences that make up a full Move.
	 * 
	 * @param b
	 * @param gold
	 */
	public void searchForSteps(boolean gold, int remainingStepCount,
			int wholeMove) {
		if (remainingStepCount == 0) {
			return;
		} else if (remainingStepCount == 4) {
			duplicates.clear();
			duplicates.add(board.getBoardHash(), board.stepCount);
		} else {
			if (remainingStepCount == 1) {
				acceptDoubleMove = false;
			} else {
				acceptDoubleMove = true;
			}
		}
		int sizeCountPosition = steps.position();
		steps.put(0);
		int thisStart = steps.position();
		board.findAllSteps(this, gold);
		curSize = steps.position();
		steps.put(sizeCountPosition, ((curSize - sizeCountPosition) - 1) / 2);
		int thisEnd = steps.position();
		for (int i = thisStart; i < thisEnd; i += 2) {
			int move = steps.get(i);
			int stepCount = Move.getStepCount(move);
			int tempWholeMove = Move.appendSteps(wholeMove, move,
					remainingStepCount, stepCount);
			board.executeStep(move);
			moveTree.addMove(tempWholeMove);
			steps.put(i + 1, curSize);
			searchForSteps(gold, remainingStepCount - stepCount, tempWholeMove);
			board.rewindSteps(move);
		}

	}

	/**
	 * Grabs a random 4-step move from the step buffer
	 * 
	 * @return
	 */
	public int[] getRandomMove(Random rand) throws GameEndException {
		steps.rewind();
		int[] move = new int[] { 2, 2, 2, 2 };
		int remainingSteps = 4;
		int index = 0;
		int sizeCount = steps.get();
		if (sizeCount == 0) {
			throw new GameEndException();
		}
		int choice = rand.nextInt(sizeCount);
		steps.position(choice * 2 + steps.position());
		move[index] = steps.get();
		remainingSteps -= Move.getStepCount(move[index++]);
		steps.position(steps.get());

		sizeCount = steps.get();
		if (sizeCount == 0) {
			return move;
		}
		choice = rand.nextInt(sizeCount);
		steps.position(choice * 2 + steps.position());
		move[index] = steps.get();
		remainingSteps -= Move.getStepCount(move[index++]);
		if (remainingSteps == 0) {
			return move;
		}
		steps.position(steps.get());

		sizeCount = steps.get();
		if (sizeCount == 0) {
			return move;
		}
		choice = rand.nextInt(sizeCount);
		steps.position(choice * 2 + steps.position());
		move[index] = steps.get();
		remainingSteps -= Move.getStepCount(move[index++]);
		if (remainingSteps == 0) {
			return move;
		}
		steps.position(steps.get());

		sizeCount = steps.get();
		if (sizeCount == 0) {
			return move;
		}
		choice = rand.nextInt(sizeCount);
		steps.position(choice * 2 + steps.position());
		move[index++] = steps.get();

		return move;

	}

}
