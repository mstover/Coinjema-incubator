import java.util.Random;


public class Die implements Rollable {
	
	int sides;
	Random roller;
	
	public Die(int sides) {
		this.sides = sides;
		roller = new Random();
	}
	
	public int roll() {
		return roller.nextInt(sides) + 1;
	}
	
	public int getMax() {
		return sides;
	}

}
