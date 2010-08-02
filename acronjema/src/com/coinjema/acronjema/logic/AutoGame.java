package com.coinjema.acronjema.logic;

import static com.coinjema.acronjema.logic.SquareDesignation.a1;
import static com.coinjema.acronjema.logic.SquareDesignation.a2;
import static com.coinjema.acronjema.logic.SquareDesignation.a7;
import static com.coinjema.acronjema.logic.SquareDesignation.a8;
import static com.coinjema.acronjema.logic.SquareDesignation.b1;
import static com.coinjema.acronjema.logic.SquareDesignation.b2;
import static com.coinjema.acronjema.logic.SquareDesignation.b7;
import static com.coinjema.acronjema.logic.SquareDesignation.b8;
import static com.coinjema.acronjema.logic.SquareDesignation.c1;
import static com.coinjema.acronjema.logic.SquareDesignation.c2;
import static com.coinjema.acronjema.logic.SquareDesignation.c7;
import static com.coinjema.acronjema.logic.SquareDesignation.c8;
import static com.coinjema.acronjema.logic.SquareDesignation.d1;
import static com.coinjema.acronjema.logic.SquareDesignation.d2;
import static com.coinjema.acronjema.logic.SquareDesignation.d7;
import static com.coinjema.acronjema.logic.SquareDesignation.d8;
import static com.coinjema.acronjema.logic.SquareDesignation.e1;
import static com.coinjema.acronjema.logic.SquareDesignation.e2;
import static com.coinjema.acronjema.logic.SquareDesignation.e7;
import static com.coinjema.acronjema.logic.SquareDesignation.e8;
import static com.coinjema.acronjema.logic.SquareDesignation.f1;
import static com.coinjema.acronjema.logic.SquareDesignation.f2;
import static com.coinjema.acronjema.logic.SquareDesignation.f7;
import static com.coinjema.acronjema.logic.SquareDesignation.f8;
import static com.coinjema.acronjema.logic.SquareDesignation.g1;
import static com.coinjema.acronjema.logic.SquareDesignation.g2;
import static com.coinjema.acronjema.logic.SquareDesignation.g7;
import static com.coinjema.acronjema.logic.SquareDesignation.g8;
import static com.coinjema.acronjema.logic.SquareDesignation.h1;
import static com.coinjema.acronjema.logic.SquareDesignation.h2;
import static com.coinjema.acronjema.logic.SquareDesignation.h7;
import static com.coinjema.acronjema.logic.SquareDesignation.h8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AutoGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<BaseEvaluatorConfig> evaluatorConfigs = new ArrayList<BaseEvaluatorConfig>();
		for (int i = 0; i < 100; i++) {
			evaluatorConfigs.add(new BaseEvaluatorConfig());
		}
		int numToPlay = Integer.parseInt(args[0]);
		int gamesPlayed = 0;
		while (gamesPlayed < numToPlay) {
			Board b = new Board(a1, a2, b1, c1, f1, g1, h1, h2, e1, d1, c2, f2,
					b2, g2, e2, d2, a7, a8, b8, c8, f8, g8, h8, h7, e8, d8, c7,
					f7, b7, g7, d7, e7);
			Board bb = new Board(a1, a2, b1, c1, f1, g1, h1, h2, e1, d1, c2,
					f2, b2, g2, e2, d2, a7, a8, b8, c8, f8, g8, h8, h7, e8, d8,
					c7, f7, b7, g7, d7, e7);
			BaseEvaluatorConfig config1 = chooseConfig(evaluatorConfigs);
			BaseEvaluatorConfig config2 = chooseConfig(evaluatorConfigs,
					config1);

			Player goldPlayer = new Player(b, true, new BaseEvaluator(config1));
			Player silverPlayer = new Player(bb, false, new BaseEvaluator(
					config2));

			Boolean winner = runGame(b, goldPlayer, silverPlayer);
			if (winner != null) {
				if (winner) {
					config1.win();
					config2.loss();
				} else {
					config2.win();
					config1.loss();
				}
			}
			System.out.println("Game ended ");
			b.print(System.out);
			gamesPlayed++;
		}
		Collections.sort(evaluatorConfigs);
		System.out.println("Played " + gamesPlayed + " games");
		for (BaseEvaluatorConfig c : evaluatorConfigs) {
			System.out.println(c.toString());
		}

	}

	private static BaseEvaluatorConfig chooseConfig(
			List<BaseEvaluatorConfig> evaluatorConfigs) {
		return chooseConfig(evaluatorConfigs, null);
	}

	private static BaseEvaluatorConfig chooseConfig(
			List<BaseEvaluatorConfig> evaluatorConfigs,
			BaseEvaluatorConfig notThis) {
		Random r = new Random();
		int index = r.nextInt(evaluatorConfigs.size());
		while (evaluatorConfigs.get(index) == notThis) {
			index = r.nextInt(evaluatorConfigs.size());
		}
		return evaluatorConfigs.get(index);
	}

	private static Boolean runGame(Board b, Player goldPlayer,
			Player silverPlayer) {
		boolean turn = true;
		int count = 0;
		while (b.getWinner() == null && count < 200) {
			try {
				int move = turn ? goldPlayer.findMoves() : silverPlayer
						.findMoves();
				goldPlayer.executeMove(move);
				silverPlayer.executeMove(move);
				turn = !turn;
				count++;
			} catch (GameEndException e) {
				b.setWinner(!turn);
			}
		}
		return b.getWinner();

	}

}
