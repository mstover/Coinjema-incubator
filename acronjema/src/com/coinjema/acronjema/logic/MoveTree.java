package com.coinjema.acronjema.logic;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The tree structure is defined by:
 * 
 * 1. int describing number of options for this ply
 * 
 * [x,x+1] long giving step, int pointing to tree branch after given step
 * 
 * Each branched position requires 12 bytes per legal moveService. Say average
 * of 20,000 moves per ply, that requires 240,000 bytes p
 * 
 * @author michael
 * 
 */
public class MoveTree {

	IntBuffer moves;
	IntBuffer moveCountNextPly;
	IntBuffer addressNextPly;
	IntBuffer evaluations;
	LongSet duplicates = new LongSet();
	List<long[]> permanentDups = new ArrayList<long[]>();
	Evaluator evaluator;
	int currentPosition = 0;
	int sizeOfFirstPly = 0;
	int next = 0;
	StepTree stepTree;
	SortedSet<Ply> dirtyPlys = new TreeSet<Ply>();
	private final Move moveService = new Move();

	final Board board;

	public MoveTree(Board b, Evaluator evaluator) {
		this(b, evaluator, 10000000);
		System.out.println("Making really big move tree");
	}

	protected MoveTree(Board b, Evaluator evaluator, int size) {
		this.board = b;
		duplicates.add(board.getBoardHash());
		this.evaluator = evaluator;
		moves = IntBuffer.allocate(size);
		evaluations = IntBuffer.allocate(size);
		addressNextPly = IntBuffer.allocate(size);
		moveCountNextPly = IntBuffer.allocate(size);
		stepTree = new StepTree(board, this);

	}

	/**
	 * Write operation - not thread safe
	 * 
	 * @param move
	 */
	public void addMove(int move) {
		if (duplicates.add(board.getBoardHash())) {
			moves.put(move);
			evaluations.put(evaluator.evaluate(board));
			next = moves.position();
		}
	}

	public int getFirstNumber() {
		return moves.position();
	}

	/**
	 * Get a MoveTree that uses the same evaluator and starting position.
	 * 
	 * @return
	 */
	public ChildMoveTree spawn() {
		ChildMoveTree tree = new ChildMoveTree(this, board.copy(),
				evaluator.copy(), 300000);
		return tree;
	}

	void compactPly(int pointerIndex, MoveTrail trail) {
		System.out.println("Eliminating ply at pointer " + pointerIndex);
		if (addressNextPly.get(addressNextPly.get(pointerIndex)) > 0) {
			compactPly(addressNextPly.get(pointerIndex), trail);
		}
		int length = moveCountNextPly.get(pointerIndex);
		moveCountNextPly.put(pointerIndex, 1);
		int[] arr = moves.array();
		int destPos = addressNextPly.get(pointerIndex) + 1;
		int srcPos = addressNextPly.get(pointerIndex) + length;
		System.arraycopy(arr, srcPos, arr, destPos, next - srcPos);

		arr = evaluations.array();
		System.out.println("Copying from " + srcPos + " to " + (next) + " to "
				+ destPos);
		System.arraycopy(arr, srcPos, arr, destPos, next - srcPos);

		arr = addressNextPly.array();
		if (srcPos < addressNextPly.capacity()) {
			int copyLen = Math.min(next, addressNextPly.capacity()) - srcPos;
			System.out.println("compacting addressbuffer srcpos = " + srcPos
					+ " destPos = " + destPos + " leng = " + copyLen);
			System.arraycopy(arr, srcPos, arr, destPos, copyLen);
			arr = moveCountNextPly.array();
			System.arraycopy(arr, srcPos, arr, destPos, copyLen);
		}
		length--;
		next -= length;
		for (int i = 0; (i < addressNextPly.capacity()) && (i < next); i++) {
			int point = addressNextPly.get(i);
			if (point > srcPos) {
				addressNextPly.put(i, point - length);
			}
		}
		for (int i = 0; i < trail.indexes.size(); i++) {
			int point = trail.indexes.get(i);
			if (point > srcPos) {
				trail.indexes.set(i, point - length);
			}
		}
	}

	/**
	 * Write operation - not threadsafe.
	 * 
	 * @param parentMoveIndex
	 * @param moveSrc
	 * @param evalSrc
	 */
	void copyPly(MoveTrail trail, IntBuffer moveSrc, IntBuffer evalSrc) {
		try {
			int lastIndex = trail.indexes.get(trail.indexes.size() - 1);
			moveCountNextPly.put(lastIndex, moveSrc.remaining());
			addressNextPly.put(lastIndex, next);
			moves.position(next);
			evaluations.position(next);
			moves.put(moveSrc);
			evaluations.put(evalSrc);
			next = moves.position();
			int depth = 0;
			for (int i : trail.indexes) {
				dirtyPlys.add(new Ply(depth++, i));
			}
			applyKillerMove(trail, moveSrc.get(0), evalSrc.get(0));
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println("next = " + next);
		}
	}

	/**
	 * @param nextTurn
	 * @param i
	 */
	private void applyKillerMove(MoveTrail trail, int move, int threshhold) {
		int plyToKill = trail.indexes.size() > 1 ? trail.indexes
				.get(trail.indexes.size() - 2) : -1;
		int start = plyToKill == -1 ? 0 : addressNextPly.get(plyToKill);
		int end = start
				+ (plyToKill == -1 ? sizeOfFirstPly : moveCountNextPly
						.get(plyToKill));
		int count = 0;
		for (int i = start; i < end; i++) {
			if (addressNextPly.get(i) == 0) {
				for (int j = 0; j < trail.indexes.size() - 1; j++) {
					board.executeMove(moves.get(trail.indexes.get(j)));
				}
				board.executeMove(moves.get(i));
				board.toggleTurn();
				evaluations.put(
						i,
						board.executeMoveIfLegal(move, evaluator,
								evaluations.get(i), trail.nextTurn));

				board.rewindMove(moves.get(i));
				board.toggleTurn();
				for (int j = trail.indexes.size() - 2; j > -1; j--) {
					board.rewindMove(moves.get(trail.indexes.get(j)));
					board.toggleTurn();
				}
			} else if (count > Runtime.getRuntime().availableProcessors()) {
				break;
			}
			count++;
		}
	}

	public void rewind() {
		moves.clear();
		evaluations.clear();
		for (int i = 0; i < addressNextPly.capacity(); i++) {
			addressNextPly.put(i, 0);
		}
		addressNextPly.clear();
		moveCountNextPly.clear();
		duplicates.clear();
		currentPosition = 0;
		next = 0;
		sizeOfFirstPly = 0;
		dirtyPlys.clear();
	}

	public void sortPly(int start, int end, MoveSorter c) {
		if ((end < addressNextPly.capacity())) {
			IntTimSort.sort(evaluations.array(), start, end, c, moves.array(),
					addressNextPly.array(), moveCountNextPly.array());
		} else {
			IntTimSort.sort(evaluations.array(), start, end, c, moves.array());

		}
	}

	public void searchForMoves(boolean gold) {
		stepTree.clear();
		preservePosition();
		int moveCount = 0;
		stepTree.searchForMinSteps(gold, 0, 2, moveService.EMPTY_MOVE);
		moveCount = getFirstNumber();
		for (int i = 0; i < moveCount; i++) {
			stepTree.refresh();
			int move = moves.get(i);
			int diff = moveService.getStepCountOfMove(move);
			if (diff > 1) {
				board.executeMove(move);
				// no toggle turn
				stepTree.searchForSteps(gold, 4 - diff, move);
				board.rewindMove(move);
				// no toggle turn
			}
		}
	}

	protected void preservePosition() {
		permanentDups.add(new long[] { board.getBoardHash(),
				board.getBoardHash2() });
		for (long[] pos : permanentDups) {
			duplicates.add(pos[0]);
		}
	}

	/**
	 * @param i
	 * @return
	 */
	public MoveTrail getMoveTrail(int i) {
		if ((board.currentTurn && (evaluations.get(0) == Integer.MIN_VALUE))
				|| (!board.currentTurn && (evaluations.get(0) == Integer.MAX_VALUE))) {
			return null;
		}
		int moveCount = 0;
		List<Integer> trail = new LinkedList<Integer>();
		while (i > -1) {
			if ((moveCount >= addressNextPly.capacity())
					|| (addressNextPly.get(moveCount) == 0)) {
				if (i > 0) {
					i--;
					moveCount++;
					// if we have thousands of processors, this might break;
				} else {
					i--;
					trail.add(moveCount);
					break;
				}
			} else {
				trail.add(moveCount);
				moveCount = addressNextPly.get(moveCount);
			}
		}
		return new MoveTrail(board.currentTurn ? (trail.size() % 2 == 0)
				: (trail.size() % 2 == 1), trail);
	}

	/**
	 * @param trails
	 */
	public void sortDirtyPlys() {
		int depth = dirtyPlys.first().depth;
		int topDepth = depth;
		for (Ply plySet : dirtyPlys) {
			if ((depth != plySet.depth)
					&& ((topDepth > 0) || (plySet.depth > 0))) {
				sortPly(addressNextPly.get(plySet.index),
						addressNextPly.get(plySet.index)
								+ moveCountNextPly.get(plySet.index),
						board.currentTurn ? ((plySet.depth % 2 == 1) ? IntTimSort.DESC_SORTER
								: IntTimSort.ASC_SORTER)
								: ((plySet.depth % 2 == 1) ? IntTimSort.ASC_SORTER
										: IntTimSort.DESC_SORTER));
				depth = plySet.depth;
			}
			evaluations.put(plySet.index,
					evaluations.get(addressNextPly.get(plySet.index)));
		}
		sortPly(0, sizeOfFirstPly, board.currentTurn ? IntTimSort.DESC_SORTER
				: IntTimSort.ASC_SORTER);
		dirtyPlys.clear();
	}

	class Ply implements Comparable<Ply> {
		final int depth;
		final int index;

		public Ply(int d, int i) {
			depth = d;
			index = i;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + depth;
			result = prime * result + index;
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Ply other = (Ply) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (depth != other.depth) {
				return false;
			}
			if (index != other.index) {
				return false;
			}
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Ply o) {
			int ret = o.depth - depth;
			if (ret == 0) {
				ret = o.index - index;
			}
			return ret;
		}

		private MoveTree getOuterType() {
			return MoveTree.this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Ply [depth=" + depth + ", index=" + index + "]";
		}
	}

	/**
	 * @return
	 */
	public int getBestCalculatedIndex() {
		for (int i = 0; i < sizeOfFirstPly; i++) {
			if (addressNextPly.get(i) > 0) {
				return i;
			}
		}
		return sizeOfFirstPly - 1;
	}
}
