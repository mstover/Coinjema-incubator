import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 */

/**
 * @author mstover
 *
 */
public class CardList extends ArrayList<Card> {
	
	
	
	public CardList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CardList(Collection<? extends Card> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CardList(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CardList getCards(Quality...qualities) {
		CardList cl = new CardList();
		for(Card c : this) {
			if(c.hasQuality(qualities)) {
				cl.add(c);
			}
		}
		return cl;
	}
	
	public CardList getCards(CardTester ct) {
		CardList cl = new CardList();
		for(Card c : this) {
			if(ct.accept(c)) {
				cl.add(c);
			}
		}
		return cl;
	}
	
	public boolean contains(String name) {
		for(Card c : this) {
			if(c.getName().equals(name)) return true;
		}
		return false;
	}
	
	@Override
	public CardList subList(int index, int num) {
		CardList cl = new CardList(num);
		for(int i = index;i < index+num;i++) {
			cl.add(get(i));
		}
		return cl;
	}

}
