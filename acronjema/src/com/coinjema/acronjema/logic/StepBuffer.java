package com.coinjema.acronjema.logic;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public class StepBuffer {

	int curSize = 0;

	private final int[] positionStack = new int[] { 0, 0, 0 };

	public int get(int index) {
		return steps.get(index);
	}

	public final Buffer position(int newPosition) {
		return steps.position(newPosition);
	}

	IntBuffer steps = ByteBuffer.allocateDirect(20000000).asIntBuffer();

	boolean acceptDoubleMove = true;

	protected boolean isAcceptDoubleMove() {
		return acceptDoubleMove;
	}

	protected void setAcceptDoubleMove(boolean acceptDoubleMove) {
		this.acceptDoubleMove = acceptDoubleMove;
	}

	public void put(int step) {
		if (acceptDoubleMove || (Move.getStepCount(step) == 1)) {
			steps.put(step);
			steps.put(0);
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
	 * @param b
	 * @param gold
	 */
	public void searchForSteps(Board b, boolean gold, int remainingStepCount) {
		if (remainingStepCount == 0) {
			return;
		} else if (remainingStepCount == 1) {
			acceptDoubleMove = false;
		} else {
			acceptDoubleMove = true;
		}
		int sizeCountPosition = steps.position();
		steps.put(0);
		int thisStart = steps.position();
		b.findAllSteps(this, gold);
		curSize = steps.position();
		steps.put(sizeCountPosition, ((curSize - sizeCountPosition) - 1) / 2);
		int thisEnd = steps.position();
		for (int i = thisStart; i < thisEnd; i += 2) {
			int move = steps.get(i);
			b.executeMove(move);
			steps.put(i + 1, curSize);
			searchForSteps(b, gold,
					remainingStepCount - Move.getStepCount(move));
			b.rewindMove(move);
		}

	}

	/**
	 * @return
	 */
	public int[] getRandomMove(Random rand) throws GameEndException {
		steps.rewind();
		int[] move = new int[4];
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
