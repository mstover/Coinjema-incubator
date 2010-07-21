package com.coinjema.acronjema.logic;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

	public StepTree(Board b) {
		this.board = b;
	}

	public int get(int index) {
		return steps.get(index);
	}

	public final Buffer position(int newPosition) {
		return steps.position(newPosition);
	}

	IntBuffer steps = ByteBuffer.allocateDirect(2000000).asIntBuffer();

	boolean acceptDoubleMove = true;

	private Set<Long> duplicates = new HashSet<Long>();

	protected boolean isAcceptDoubleMove() {
		return acceptDoubleMove;
	}

	protected void setAcceptDoubleMove(boolean acceptDoubleMove) {
		this.acceptDoubleMove = acceptDoubleMove;
	}

	public void putStep(int step) {
		if (acceptDoubleMove || (Move.getStepCount(step) == 1)) {
			long hash = board.getBoardHashAfterMove(step);
			if (duplicates.add(hash)) {
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

	/**
	 * Searches for all unique step sequences that make up a full Move.
	 * 
	 * @param b
	 * @param gold
	 */
	public void searchForSteps(boolean gold, int remainingStepCount) {
		if (remainingStepCount == 0) {
			return;
		} else if (remainingStepCount == 4) {
			duplicates = new HashSet<Long>(1009);
			duplicates.add(board.getBoardHash());
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
			board.executeMove(move);
			steps.put(i + 1, curSize);
			searchForSteps(gold, remainingStepCount - Move.getStepCount(move));
			board.rewindMove(move);
		}

	}

	public void fillMoveTree(MoveTree tree) {
		steps.rewind();
		fillTree(tree, 0l, 4);
	}

	private void fillTree(MoveTree tree, long move, int stepsRemaining) {
		if (stepsRemaining == 0) {
			tree.addMove(move);
			return;
		} else {
			int numSteps = (steps.get() * 2);
			numSteps += steps.position();
			while (steps.position() < numSteps) {
				int m = steps.get();
				move = move ^ (m << (8 * (4 - stepsRemaining)));
				assert (stepsRemaining != 1 || Move.getStepCount(m) == 1);
				int nextPos = steps.get();
				if (nextPos != 0) {
					int thisSpot = steps.position();
					steps.position(nextPos);
					fillTree(tree, move, stepsRemaining - Move.getStepCount(m));
					steps.position(thisSpot);
				} else {
					tree.addMove(move);
				}
			}

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
