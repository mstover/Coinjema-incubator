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

import java.util.List;

/**
 * @author michaelstover
 * 
 */
class ChildMoveTree extends MoveTree {

	MoveTree parent;

	/**
	 * @param moveTree
	 * @param copy
	 * @param evaluator
	 * @param size
	 */
	public ChildMoveTree(MoveTree moveTree, Board copy, Evaluator evaluator,
			int size) {
		super(copy, evaluator, size);
		this.parent = moveTree;
	}

	@Override
	public void sortPly(int start, int end, MoveSorter c) {
		IntTimSort.sort(evaluations.array(), start, end, c, moves.array());
	}

	/**
	 * 
	 */
	public void copyToParent(MoveTrail trail) {
		moves.flip();
		evaluations.flip();
		parent.copyPly(trail, moves, evaluations);
		moves.clear();
		evaluations.clear();
	}

	public void searchForMoves(boolean gold, List<Integer> doMovesFirst) {
		if (doMovesFirst != null) {
			for (int i : doMovesFirst) {
				board.executeMove(parent.moves.get(i));
				board.toggleTurn();
			}
		}
		super.searchForMoves(gold);
		if (doMovesFirst != null) {
			for (int i = doMovesFirst.size() - 1; i > -1; i--) {
				board.rewindMove(parent.moves.get(doMovesFirst.get(i)));
				board.toggleTurn();
			}
		}
	}

	@Override
	protected void preservePosition() {
		duplicates.add(board.getBoardHash());
	}

}
