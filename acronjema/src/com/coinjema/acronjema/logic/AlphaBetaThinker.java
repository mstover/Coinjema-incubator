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

	int numThreads = 2;// Runtime.getRuntime().availableProcessors();
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
			try {
				tree.searchForMoves(trail.nextTurn, trail.indexes);
				tree.sizeOfFirstPly = tree.getFirstNumber();
				tree.sortPly(0, tree.sizeOfFirstPly,
						(trail.nextTurn ? IntTimSort.DESC_SORTER
								: IntTimSort.ASC_SORTER));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Thread = " + Thread.currentThread());
			}
			return trail;
		}

		/**
		 * 
		 */
		public void sortAndSend() {
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
		trails = new MoveTrail[numThreads];
		int count = 0;
		long time = System.currentTimeMillis();
		while ((trails[0] == null) || (trails[0].indexes.size() < 2)) {
			int moveTrailCount = 0;
			for (int i = 0; i < thinkers.length; i++) {
				MoveTrail trail = tree.getMoveTrail(moveTrailCount++);
				thinkers[i].setParams(trail);
				futures[i] = multiThinkService.submit(thinkers[i]);
			}
			for (int i = 0; i < thinkers.length; i++) {
				try {
					trails[i] = (MoveTrail) futures[i].get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for (int i = 0; i < thinkers.length; i++) {
				thinkers[i].sortAndSend();
			}
			tree.sortDirtyPlys();
			if (tree.board.currentTurn) {
				if (tree.evaluations.get(0) == Integer.MAX_VALUE) {
					break;
				}
			} else if (tree.evaluations.get(0) == Integer.MIN_VALUE) {
				break;
			}
			count++;
			if (count % 100 == 0) {
				System.out.println("number of moves found = " + tree.next
						+ " best move = " + tree.moves.get(0) + ","
						+ tree.evaluations.get(0));
				int bestCalced = tree.getBestCalculatedIndex();
				System.out.println("Best calculated move = "
						+ tree.moves.get(bestCalced) + ","
						+ tree.evaluations.get(bestCalced));
			}
		}
		System.out.println("Seaching 2 ply took "
				+ (System.currentTimeMillis() - time) + " ms and " + tree.next
				+ " positions.  Score = " + tree.evaluations.get(0));
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
