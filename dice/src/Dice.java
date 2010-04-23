import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * 2d6
 * 2d6+1
 * d6+3d8
 * d6+d4+3
 * @author mstover
 *
 */
public class Dice implements Rollable {
	
	List<Dice> parts;
	
	int constant;
	
	List<Die> dice;
	
	public Dice(String spec) {
		parts = new ArrayList<Dice>();
		dice = new ArrayList<Die>();
		if(spec.indexOf("+") == -1) {
			parseSpec(spec);
			return;
		}
		StringTokenizer specs = new StringTokenizer(spec,"+");
		
		while(specs.hasMoreTokens()) {
			parts.add(new Dice(specs.nextToken()));
		}
	}
	
	public int roll() {
		int sum = constant;
		for(Dice d : parts) {
			sum += d.roll();
		}
		for(Die d : dice) {
			sum += d.roll();
		}
		return sum;
	}
	
	public int getMax() {
		int max = constant;
		for(Dice d : parts) {
			max += d.getMax();
		}
		for(Die d : dice) {
			max += d.getMax();
		}
		return max;
	}
	
	private void parseSpec(String spec) {
		try {
		constant = Integer.parseInt(spec);
		return;
		}catch(Exception e) {
			
		}
		int dIndex = spec.indexOf("d");
		int num = 1;
		try {
			num = Integer.parseInt(spec.substring(0,dIndex));
		} catch(Exception e){}
		int die = Integer.parseInt(spec.substring(dIndex+1));
		for(int i = 0;i < num;i++) {
			dice.add(new Die(die));
		}
	}

}
