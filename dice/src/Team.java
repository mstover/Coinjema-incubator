import java.util.Arrays;
import java.util.LinkedList;

public class Team {

	LinkedList<Player> players = new LinkedList<Player>();
	LinkedList<Player> dead = new LinkedList<Player>();

	public Team(Player... p) {
		players.addAll(Arrays.asList(p));
	}

	public boolean isTakenOut() {
		for (Player p : players) {
			if (!p.isTakenOut()) {
				return false;
			}
		}
		return true;
	}

	public void removeDeadPlayers() {
		if (players.size() > 0 && players.getFirst().isTakenOut()) {
			dead.addFirst(players.removeFirst());
			removeDeadPlayers();
		}

	}

	public void attack(Team defenders) {
		for (Player p : players) {
			p.attackPlayer(defenders.getPrimaryDefender());
			if (defenders.isTakenOut()) {
				break;
			}
		}
	}

	public void newRound() {
		removeDeadPlayers();
		for (Player p : players) {
			p.newRound();
		}
	}

	private Player getPrimaryDefender() {
		return players.get((int) (Math.random() * (players.size() / 2)));
	}

	public void refresh() {
		for (Player d : dead) {

			players.addFirst(d);
		}
		dead.clear();
		for (Player p : players) {
			p.refresh();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Team [players=").append(players).append("]");
		return builder.toString();
	}

}
