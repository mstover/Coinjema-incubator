import java.util.ArrayList;
import java.util.List;

public class TurnSequence {

	List<PlayTurn> turns = new ArrayList<PlayTurn>();

	public void addTurn(PlayTurn t) {
		turns.add(t);
	}

	public boolean canPlay(int turn, Card c) {
		return c.isPlayable(turn(turn).getAvailableMana());
	}

	public Mana getTotalManaUsed() {
		Mana m = new Mana();
		for (PlayTurn turn : turns) {
			m.addMana(turn.getManaUsedThisTurn());
		}
		return m;
	}

	public Mana getTotalManaUsed(int i) {
		Mana m = new Mana();
		int count = 1;
		for (PlayTurn turn : turns) {
			m.addMana(turn.getManaUsedThisTurn());
			count++;
			if (count > i)
				break;
		}
		return m;
	}

	public PlayTurn turn(int turn) {
		return turns.get(turn - 1);
	}

}
