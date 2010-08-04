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

	/**
	 * 
	 */
	public void copyToParent(MoveTrail trail) {
		moves.flip();
		evaluations.flip();
		parent.copyPly(trail, moves, evaluations);
	}

	public void searchForMoves(boolean gold, List<Integer> doMovesFirst) {
		System.out.println("child move tree Board = " + board);
		System.out.println("step tree board = " + stepTree.board);
		if (doMovesFirst != null) {
			for (int i : doMovesFirst) {
				System.out.println("Moving forward a move "
						+ parent.moves.get(i));
				board.executeMove(parent.moves.get(i));
			}
		}
		System.out.println("Thinking from position = ");
		board.print(System.out);
		super.searchForMoves(gold);
		System.out.println("Now done Thinking from position = ");
		board.print(System.out);
		if (doMovesFirst != null) {
			for (int i = doMovesFirst.size() - 1; i > -1; i--) {
				System.out.println("Moving backwards a move "
						+ parent.moves.get(doMovesFirst.get(i)));
				board.rewindMove(parent.moves.get(doMovesFirst.get(i)));
			}
		}
		System.out.println("Now putting position back = ");
		board.print(System.out);
	}

}
