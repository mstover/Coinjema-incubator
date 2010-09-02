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
	private final PlyThinker[] thinkers;
	private final ExecutorService multiThinkService;
	private final ExecutorService singleThinkService;
	private final Future<?>[] futures;
	private MoveTrail[] trails;
	private final Move moveService = new Move();
	private final Evaluator evaluator;

	class PlyThinker implements Callable<MoveTrail> {
		MoveTrail trail;
		ChildMoveTree tree;
		boolean readyToSend = false;

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
			if (trail != null) {
				if (!readyToSend) {
					tree.rewind();
					try {
						// System.out.println("Searching trail " + trail);
						tree.searchForMoves(trail.nextTurn, trail.indexes);
						tree.sizeOfFirstPly = tree.getFirstNumber();
						if (tree.sizeOfFirstPly > 1) {
							tree.sortPly(0, tree.sizeOfFirstPly,
									(trail.nextTurn ? IntTimSort.DESC_SORTER
											: IntTimSort.ASC_SORTER));
						}
						readyToSend = true;
						Future<MoveTrail> fut = singleThinkService.submit(this);
						trail = fut.get();
					} catch (Exception e) {
						e.printStackTrace();
						System.out
								.println("Thread = " + Thread.currentThread());
					}
				} else {
					sortAndSend();
					readyToSend = false;
				}
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

	public AlphaBetaThinker(MoveTree tree, Evaluator evaluator) {
		thinkers = new PlyThinker[numThreads * 6];
		for (int i = 0; i < thinkers.length; i++) {
			thinkers[i] = new PlyThinker(tree.spawn());
		}
		System.out.println("Num threads = " + numThreads);
		multiThinkService = Executors.newFixedThreadPool(numThreads);
		singleThinkService = Executors.newFixedThreadPool(1);
		futures = new Future<?>[thinkers.length];
		trails = new MoveTrail[thinkers.length];
		this.evaluator = evaluator;
	}

	/**
	 * Find the best move
	 * 
	 * @param tree
	 * @return
	 */
	public int inSearchOf(MoveTree tree, long normalTime, long maxTime)
			throws GameEndException {
		if (tree.board.stepCount == 0) {
			normalTime = Math.max(10000, normalTime);
			maxTime = Math.max(10000, maxTime);
		}
		tree.evaluator = evaluator;
		for (PlyThinker t : thinkers) {
			t.tree.evaluator = evaluator;
		}
		System.out.println("normal time = " + normalTime + " maxTime = "
				+ maxTime);
		tree.rewind();
		boolean turn = tree.board.currentTurn;
		tree.searchForMoves(turn);
		tree.sizeOfFirstPly = tree.getFirstNumber();
		if (tree.sizeOfFirstPly == 0) {
			throw new GameEndException();
		}
		tree.sortPly(0, tree.sizeOfFirstPly, (turn ? IntTimSort.DESC_SORTER
				: IntTimSort.ASC_SORTER));
		trails = new MoveTrail[thinkers.length];
		int best = tree.evaluations.get(0);
		int depth = 0;
		int count = 0;
		long time = System.currentTimeMillis();
		if ((tree.board.currentTurn && (tree.evaluations.get(0) == Integer.MAX_VALUE))
				|| (!tree.board.currentTurn && (tree.evaluations.get(0) == Integer.MIN_VALUE))) {
			// winner
			System.out.println("We win NOW!");
		} else {
			setupThinkers(tree);
			waitForThinkers();
			long since = System.currentTimeMillis() - time;
			while ((trails[0] != null)
					&& (since < maxTime)
					&& ((since < normalTime) || (depth < 3) || (tree.board.currentTurn ? best < 0
							: best > 0))) {
				tree.sortDirtyPlys();
				count++;
				int bestCalced = tree.getBestCalculatedIndex();
				// System.out.println("number of moves found = " + tree.next
				// + " best move = "
				// + moveService.toString(tree.moves.get(bestCalced))
				// + "," + tree.evaluations.get(bestCalced));
				// System.out.println("Best Response = "
				// + moveService.toString(tree.moves
				// .get(tree.addressNextPly.get(bestCalced))));
				setupThinkers(tree);
				waitForThinkers();
				depth = trails[0] != null ? trails[0].indexes.size() : 0;
				best = tree.evaluations.get(0);
				since = System.currentTimeMillis() - time;

			}
		}
		int bestIndex = tree.getBestCalculatedIndex();
		System.out.println("Trails[0] = " + trails[0]);
		System.out.println("Seaching took "
				+ (System.currentTimeMillis() - time)
				+ " ms and "
				+ tree.next
				+ " positions.  Best Index = "
				+ bestIndex
				+ " Score = "
				+ tree.evaluations.get(bestIndex)
				+ " best response = "
				+ moveService.toString(tree.moves.get(tree.addressNextPly
						.get(bestIndex))));
		return tree.moves.get(bestIndex);
	}

	private void waitForThinkers() {
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
	}

	private void setupThinkers(MoveTree tree) {
		int moveTrailCount = 0;
		for (int i = 0; i < thinkers.length; i++) {
			MoveTrail trail = tree.getMoveTrail(moveTrailCount++);
			thinkers[i].setParams(trail);
			futures[i] = multiThinkService.submit(thinkers[i]);
		}
	}

	/**
	 * @param move
	 */
	public void executeMove(int move) {
		for (PlyThinker thinker : thinkers) {
			thinker.tree.board.executeMove(move);
			thinker.tree.board.toggleTurn();
		}
	}
}
