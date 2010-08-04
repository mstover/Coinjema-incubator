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
 * Each branched position requires 12 bytes per legal move. Say average of
 * 20,000 moves per ply, that requires 240,000 bytes p
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

	final Board board;

	public MoveTree(Board b, Evaluator evaluator) {
		this(b, evaluator, 25000000);
	}

	protected MoveTree(Board b, Evaluator evaluator, int size) {
		this.board = b;
		duplicates.add(board.getBoardHash(), board.getBoardHash2());
		this.evaluator = evaluator;
		moves = IntBuffer.allocate(size);
		evaluations = IntBuffer.allocate(size);
		addressNextPly = IntBuffer.allocate(size / 2);
		moveCountNextPly = IntBuffer.allocate(size / 2);
		stepTree = new StepTree(board, this);

	}

	/**
	 * Write operation - not thread safe
	 * 
	 * @param move
	 */
	public void addMove(int move) {
		if (duplicates.add(board.getBoardHash(), board.getBoardHash2())) {
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
		ChildMoveTree tree = new ChildMoveTree(this, board.copy(), evaluator,
				500000);
		return tree;
	}

	/**
	 * Write operation - not threadsafe.
	 * 
	 * @param parentMoveIndex
	 * @param moveSrc
	 * @param evalSrc
	 */
	void copyPly(MoveTrail trail, IntBuffer moveSrc, IntBuffer evalSrc) {
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

	}

	public void rewind() {
		System.out.println("Rewinding " + this);
		moves.rewind();
		evaluations.rewind();
		addressNextPly.rewind();
		moveCountNextPly.rewind();
		duplicates.clear();
		currentPosition = 0;
		next = 0;
		sizeOfFirstPly = 0;
		dirtyPlys.clear();
	}

	public void sortPly(int start, int end, MoveSorter c) {
		IntTimSort.sort(evaluations.array(), start, end, c, moves.array(),
				addressNextPly.array(), moveCountNextPly.array());
	}

	public void searchForMoves(boolean gold) {
		stepTree.clear();
		preservePosition();
		System.out.println("Starting step search");
		board.print(System.out);
		stepTree.searchForMinSteps(gold, 0, 2, Move.EMPTY_MOVE);
		System.out.println("Ending half of step search");
		board.print(System.out);
		int moveCount = getFirstNumber();
		for (int i = 0; i < moveCount; i++) {
			stepTree.refresh();
			int move = moves.get(i);
			int diff = Move.getStepCountOfMove(move);
			if (diff > 1) {
				board.executeMove(move);
				stepTree.searchForSteps(gold, 4 - diff, move);
				board.rewindMove(move);
			}
		}
	}

	protected void preservePosition() {
		permanentDups.add(new long[] { board.getBoardHash(),
				board.getBoardHash2() });
		for (long[] pos : permanentDups) {
			duplicates.add(pos[0], pos[1]);
		}
	}

	/**
	 * @param i
	 * @return
	 */
	public MoveTrail getMoveTrail(int i) {
		int moveCount = 0;
		List<Integer> trail = new LinkedList<Integer>();
		while (i > -1) {
			if (addressNextPly.get(moveCount) == 0) {
				if (i > 0) {
					i--;
					moveCount++;
					// if we have thousands of processors, this might break;
				} else {
					i--;
					System.out.println("Addming move " + moves.get(moveCount)
							+ " to trail");
					trail.add(moveCount);
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
		for (Ply plySet : dirtyPlys) {
			if (depth != plySet.depth) {
				sortPly(addressNextPly.get(plySet.index),
						addressNextPly.get(plySet.index)
								+ moveCountNextPly.get(plySet.index),
						board.currentTurn ? ((plySet.depth % 2 == 0) ? IntTimSort.DESC_SORTER
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
	}
}
