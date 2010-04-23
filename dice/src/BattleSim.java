public class BattleSim {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Team[] teams = new Team[] {
				new Team(new Player(WeaponType.SWORD_AND_MED_SHIELD, 5, 20, 4,
						4)),
				new Team(new Player(WeaponType.SWORD_AND_SMALL_SHIELD, 5, 20,
						4, 4)),
				new Team(new Player(WeaponType.SWORD_AND_LARGE_SHIELD, 5, 20,
						4, 4)),
				new Team(new Player(WeaponType.TWO_HANDED, 5, 20, 4, 4)),
				new Team(new Player(WeaponType.GUN, 5, 20, 4, 4)) };
		for (int i = 0; i < teams.length - 1; i++) {
			for (int j = i + 1; j < teams.length; j++) {
				Team player1 = teams[i];
				Team player2 = teams[j];

				int winOne = 0;
				int winTwo = 0;
				int averageRounds = 0;
				while (winOne + winTwo < 100000) {
					int numRounds = 0;
					while (!player1.isTakenOut() && !player2.isTakenOut()) {
						player1.newRound();
						player2.newRound();
						player1.attack(player2);
						player2.attack(player1);
						numRounds++;
					}
					averageRounds += numRounds;
					if (player1.isTakenOut()) {
						winTwo++;
					}
					if (player2.isTakenOut()) {
						winOne++;
					}
					player1.refresh();
					player2.refresh();
				}
				System.out.println("player 1" + player1 + " wins "
						+ (winOne / 100000f) + "%");
				System.out.println("player 2" + player2 + " wins "
						+ (winTwo / 100000f) + "%");
				System.out.println("Number rounds = "
						+ (averageRounds / 100000f));
				System.out.println("Average wound = " + Player.averageWound
						/ (float) Player.numWounds);
				Player.averageWound = 0;
				Player.numWounds = 0;
			}
		}

	}
}
