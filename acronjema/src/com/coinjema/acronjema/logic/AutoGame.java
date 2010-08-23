package com.coinjema.acronjema.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AutoGame {

	public static Object GLOBAL_SYNC = new Object();

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		GenePool pool = new GenePool(args[0]);
		List<BaseEvaluatorConfig> evaluatorConfigs = pool.getConfigs();
		int numToPlay = Integer.parseInt(args[1]);
		int gamesPlayed = 0;
		System.out.println("Best config is "
				+ pool.getBestConfig().saveTo.getAbsolutePath());
		while (gamesPlayed < numToPlay) {
			if (gamesPlayed % 800 == 799) {
				evaluatorConfigs = pool.cull(evaluatorConfigs);
			}
			Board b = new Board();
			BaseEvaluatorConfig config1 = pool.getBestConfig();
			MoveTree tree = new MoveTree(b, new BaseEvaluator(config1));
			Player goldPlayer = new ComputerPlayer(tree, true,
					Long.parseLong(args[2]),
					args[3] == null ? new BaseEvaluator(config1)
							: new SuperEvaluator());
			Player silverPlayer = new ComputerPlayer(tree, false,
					Long.parseLong(args[3] != null ? args[3] : args[2]),
					new SuperEvaluator());
			goldPlayer.setup();
			silverPlayer.setup();
			b.print(System.out);
			System.out.println("Game On!");
			Boolean winner = runGame(b, goldPlayer, silverPlayer);
			if (winner != null) {
				if (winner) {
					config1.win();
					// config2.loss();
				} else {
					// config2.win();
					config1.loss();
				}
			}
			System.out.println("Game ended winner was " + b.getWinner());
			b.print(System.out);
			gamesPlayed++;
		}
		Collections.sort(evaluatorConfigs);
		// System.out.println("Played " + gamesPlayed + " games");
		// for (BaseEvaluatorConfig c : evaluatorConfigs) {
		// System.out.println(c.toString());
		// }

	}

	public static Board makeRandomBoard() {
		Board b = new Board();
		Random rand = new Random();
		for (int side = 0; side < 2; side++) {
			boolean gold = (side == 0);
			for (int i = 0; i < 8; i++) {
				int count = rand.nextInt(16 - i);
				for (int j = (gold ? 0 : 48); (count >= 0)
						&& (j < (gold ? 16 : 64)); j++) {
					if (b.squares[j].isEmpty()) {
						if (count == 0) {
							b.addPiece(getStrength(i), gold, j);
						}
						count--;
					}

				}
			}
			for (int i = (gold ? 0 : 48); i < (gold ? 16 : 64); i++) {
				if (b.squares[i].isEmpty()) {
					b.addPiece(1, gold, i);
				}
			}
		}
		return b;
	}

	private static int getStrength(int i) {
		switch (i) {
		case 0:
			return 6;
		case 1:
			return 5;
		case 2:
		case 3:
			return 4;
		case 4:
		case 5:
			return 3;
		case 6:
		case 7:
			return 2;
		}
		return 0;
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
		while ((b.getWinner() == null) && (count < 200)) {
			try {
				int move = turn ? goldPlayer.findMoves() : silverPlayer
						.findMoves();
				goldPlayer.executeMove(move);
				silverPlayer.executeMove(move);
				b.executeMove(move);
				System.out.println((turn ? "gold" : "silver") + " moved");
				turn = !turn;
				count++;
				b.print(System.out);
			} catch (GameEndException e) {
				b.setWinner(!turn);
			}
		}
		return b.getWinner();

	}

}
