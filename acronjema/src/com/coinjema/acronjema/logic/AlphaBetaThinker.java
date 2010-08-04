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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author michaelstover
 * 
 */
public class AlphaBetaThinker {

	int numThreads = Runtime.getRuntime().availableProcessors();
	private PlyThinker[] thinkers;
	private ExecutorService multiThinkService;
	private ExecutorService singleThinkService;
	private Future<?>[] futures;
	private MoveTrail[] trails;

	class PlyThinker implements Callable<MoveTrail> {
		MoveTrail trail;
		ChildMoveTree tree;

		public PlyThinker(ChildMoveTree myMoveTree) {
			this.tree = myMoveTree;
		}

		public void setParams(MoveTrail trail) {
			this.trail = trail;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public MoveTrail call() {
			tree.rewind();
			System.out.println("Searching for moves for "
					+ (trail.nextTurn ? "gold" : "silver"));
			tree.searchForMoves(trail.nextTurn, trail.indexes);
			System.out.println("DONE: size of first ply = "
					+ tree.getFirstNumber());
			if (tree.getFirstNumber() == 0) {
				System.out.println("Bad board!");
				tree.board.print(System.out);
			}
			return trail;
		}

		/**
		 * 
		 */
		public void sortAndSend() {
			tree.sizeOfFirstPly = tree.getFirstNumber();
			System.out.println("SORTING: size of first ply = "
					+ tree.sizeOfFirstPly);
			tree.sortPly(0, tree.sizeOfFirstPly,
					(trail.nextTurn ? IntTimSort.DESC_SORTER
							: IntTimSort.ASC_SORTER));
			tree.copyToParent(trail);
		}
	}

	public AlphaBetaThinker(MoveTree tree) {
		thinkers = new PlyThinker[numThreads];
		for (int i = 0; i < numThreads; i++) {
			thinkers[i] = new PlyThinker(tree.spawn());
		}
		System.out.println("Num threads = " + numThreads);
		multiThinkService = Executors.newFixedThreadPool(numThreads);
		singleThinkService = Executors.newFixedThreadPool(1);
		futures = new Future<?>[numThreads];
		trails = new MoveTrail[numThreads];
	}

	/**
	 * Find the best move
	 * 
	 * @param tree
	 * @return
	 */
	public int inSearchOf(MoveTree tree) throws GameEndException {
		tree.rewind();
		boolean turn = tree.board.currentTurn;
		tree.searchForMoves(turn);
		tree.sizeOfFirstPly = tree.getFirstNumber();
		if (tree.sizeOfFirstPly == 0) {
			throw new GameEndException();
		}
		tree.sortPly(0, tree.sizeOfFirstPly, (turn ? IntTimSort.DESC_SORTER
				: IntTimSort.ASC_SORTER));
		int moveTrailCount = 0;
		for (int i = 0; i < thinkers.length; i++) {
			MoveTrail trail = tree.getMoveTrail(moveTrailCount++);
			System.out.println("Move Trail = " + trail);
			thinkers[i].setParams(trail);
			futures[i] = multiThinkService.submit(thinkers[i]);
		}
		for (int i = 0; i < thinkers.length; i++) {
			try {
				trails[i] = (MoveTrail) futures[i].get();
				thinkers[i].sortAndSend();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tree.sortDirtyPlys();
		return tree.moves.get(0);
	}

	/**
	 * @param move
	 */
	public void executeMove(int move) {
		for (PlyThinker thinker : thinkers) {
			thinker.tree.board.executeMove(move);
		}
	}
}
