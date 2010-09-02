package com.coinjema.acronjema.logic;

import java.nio.Buffer;
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
public class StepTree implements StepBuffer {

	boolean acceptDoubleMove = true;
	private Move moveService = new Move();

	final Board board;
	int curSize = 0;

	private final LongSet duplicates = new LongSet();

	public final MoveTree moveTree;

	public int numTimes;

	IntBuffer steps = IntBuffer.allocate(2000000);

	boolean traceon = false;

	public StepTree(Board b, MoveTree moveTree) {
		this.board = b;
		this.moveTree = moveTree;
		System.out.println("Board = " + board);
	}

	public final Buffer clear() {
		acceptDoubleMove = true;
		curSize = 0;
		duplicates.clear();
		return steps.clear();
	}

	public int get(int index) {
		return steps.get(index);
	}

	public final int getNumberChoices() {
		return steps.position() / 2;
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
		remainingSteps -= moveService.getStepCount(move[index++]);
		steps.position(steps.get());

		sizeCount = steps.get();
		if (sizeCount == 0) {
			return move;
		}
		choice = rand.nextInt(sizeCount);
		steps.position(choice * 2 + steps.position());
		move[index] = steps.get();
		remainingSteps -= moveService.getStepCount(move[index++]);
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
		remainingSteps -= moveService.getStepCount(move[index++]);
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

	protected boolean isAcceptDoubleMove() {
		return acceptDoubleMove;
	}

	public final Buffer position(int newPosition) {
		return steps.position(newPosition);
	}

	public void putStep(int step) {
		if (acceptDoubleMove || (moveService.getStepCount(step) == 1)) {
			board.executeStep(step, false);
			long hash = board.getBoardHash();

			board.rewindSteps(step, false);
			if (duplicates.add(hash)) {
				steps.put(step);
				steps.put(0);
			}
		}

	}

	public void refresh() {
		acceptDoubleMove = true;
		curSize = 0;
		steps.clear();
	}

	public void searchForMinSteps(boolean gold, int startStepCount,
			int remainingStepCount, int wholeMove) {
		if (remainingStepCount <= 0) {
			return;
		} else if (remainingStepCount == 4) {
			duplicates.add(board.getBoardHash());
		}
		int sizeCountPosition = steps.position();
		steps.put(0);
		int thisStart = steps.position();
		numTimes++;
		board.findAllSteps(this, gold);
		curSize = steps.position();
		steps.put(sizeCountPosition, ((curSize - sizeCountPosition) - 1) / 2);
		int thisEnd = steps.position();
		for (int i = thisStart; i < thisEnd; i += 2) {
			int move = steps.get(i);
			int stepCount = moveService.getStepCount(move);
			int tempWholeMove = moveService.appendSteps(wholeMove, move,
					4 - startStepCount, stepCount);
			board.executeStep(move, true);
			moveTree.addMove(tempWholeMove);
			steps.put(i + 1, curSize);
			searchForMinSteps(gold, stepCount, remainingStepCount - stepCount,
					tempWholeMove);
			board.rewindSteps(move, true);
		}
	}

	/**
	 * Searches for all unique step sequences that make up a full moveService.
	 * 
	 * @param b
	 * @param gold
	 */
	public void searchForSteps(boolean gold, int remainingStepCount,
			int wholeMove) {
		if (remainingStepCount == 0) {
			return;
		} else if (remainingStepCount == 4) {
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
		numTimes++;
		board.findAllSteps(this, gold);
		curSize = steps.position();
		steps.put(sizeCountPosition, ((curSize - sizeCountPosition) - 1) / 2);
		int thisEnd = steps.position();
		for (int i = thisStart; i < thisEnd; i += 2) {
			int move = steps.get(i);
			int stepCount = moveService.getStepCount(move);
			int tempWholeMove = moveService.appendSteps(wholeMove, move,
					remainingStepCount, stepCount);
			board.executeStep(move, true);
			moveTree.addMove(tempWholeMove);
			steps.put(i + 1, curSize);
			searchForSteps(gold, remainingStepCount - stepCount, tempWholeMove);
			board.rewindSteps(move, true);
		}

	}

	protected void setAcceptDoubleMove(boolean acceptDoubleMove) {
		this.acceptDoubleMove = acceptDoubleMove;
	}

}
