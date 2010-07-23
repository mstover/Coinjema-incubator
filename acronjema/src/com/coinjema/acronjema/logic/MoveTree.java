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
	IntBuffer evaluations;

	private final Board board;

	public MoveTree(Board b) {
		this.board = b;
	}

	public void addMove(int move) {
		moves.put(move);

	}

	public int getFirstNumber() {
		return moves.position();
	}

	public void rewind() {
		moves.rewind();
	}

}
