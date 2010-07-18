package com.coinjema.acronjema.logic;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class StepBuffer {

	public int get(int index) {
		return steps.get(index);
	}

	public final Buffer position(int newPosition) {
		return steps.position(newPosition);
	}

	IntBuffer steps = ByteBuffer.allocateDirect(40000).asIntBuffer();

	public IntBuffer put(int[] src, int offset, int length) {
		return steps.put(src, offset, length);
	}

	boolean acceptDoubleMove = true;

	protected boolean isAcceptDoubleMove() {
		return acceptDoubleMove;
	}

	protected void setAcceptDoubleMove(boolean acceptDoubleMove) {
		this.acceptDoubleMove = acceptDoubleMove;
	}

	public void put(int step) {
		if (acceptDoubleMove || Move.getStepCount(step) == 1) {
			steps.put(step);
		}

	}

	public final int position() {
		return steps.position();
	}

	public final Buffer clear() {
		return steps.clear();
	}

}
