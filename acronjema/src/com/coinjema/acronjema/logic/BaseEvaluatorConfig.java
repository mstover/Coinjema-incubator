package com.coinjema.acronjema.logic;

import java.util.Random;

public class BaseEvaluatorConfig implements Comparable<BaseEvaluatorConfig> {

	public final int frozenMult;
	public final int dominationMult;
	public final int dominationValue;
	public final int friendValue;
	public final int frozenValue;
	public final int adjTrapPieceValue;
	public final int adjTrapStrMult;
	public final int pieceValue;
	public final int winValue;
	public final int rabbitRowMult;
	public final int numMovesMult;
	public int wins = 0;
	public int losses = 0;
	public int streak = 0;

	public BaseEvaluatorConfig() {
		Random rand = new Random();
		frozenMult = rand.nextInt(100);
		dominationMult = rand.nextInt(100);
		dominationValue = rand.nextInt(100);
		friendValue = rand.nextInt(100);
		frozenValue = rand.nextInt(100);
		adjTrapPieceValue = rand.nextInt(100);
		adjTrapStrMult = rand.nextInt(100);
		pieceValue = rand.nextInt(10000);
		winValue = 1000000;
		rabbitRowMult = rand.nextInt(100);
		numMovesMult = rand.nextInt(100);
	}

	public BaseEvaluatorConfig(BaseEvaluatorConfig config) {
		this();
		Random rand = new Random();
	}

	public void win() {
		wins++;
		if (streak > -1) {
			streak++;
		}
	}

	public void loss() {
		losses++;
		if (streak < 1) {
			streak--;
		}
	}

	@Override
	public String toString() {
		return "BaseEvaluatorConfig [frozenMult=" + frozenMult
				+ "\n\t dominationMult=" + dominationMult
				+ "\n\t dominationValue=" + dominationValue
				+ "\n\t friendValue=" + friendValue + "\n\t frozenValue="
				+ frozenValue + "\n\t adjTrapPieceValue=" + adjTrapPieceValue
				+ "\n\t adjTrapStrMult=" + adjTrapStrMult + "\n\t pieceValue="
				+ pieceValue + "\n\t winValue=" + winValue
				+ "\n\t rabbitRowMult=" + rabbitRowMult + "\n\t numMovesMult="
				+ numMovesMult + "\n\t wins=" + wins + "\n\t losses=" + losses
				+ "\n\t streak=" + streak + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adjTrapPieceValue;
		result = prime * result + adjTrapStrMult;
		result = prime * result + dominationMult;
		result = prime * result + dominationValue;
		result = prime * result + friendValue;
		result = prime * result + frozenMult;
		result = prime * result + frozenValue;
		result = prime * result + numMovesMult;
		result = prime * result + pieceValue;
		result = prime * result + rabbitRowMult;
		result = prime * result + winValue;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseEvaluatorConfig other = (BaseEvaluatorConfig) obj;
		if (adjTrapPieceValue != other.adjTrapPieceValue) {
			return false;
		}
		if (adjTrapStrMult != other.adjTrapStrMult) {
			return false;
		}
		if (dominationMult != other.dominationMult) {
			return false;
		}
		if (dominationValue != other.dominationValue) {
			return false;
		}
		if (friendValue != other.friendValue) {
			return false;
		}
		if (frozenMult != other.frozenMult) {
			return false;
		}
		if (frozenValue != other.frozenValue) {
			return false;
		}
		if (numMovesMult != other.numMovesMult) {
			return false;
		}
		if (pieceValue != other.pieceValue) {
			return false;
		}
		if (rabbitRowMult != other.rabbitRowMult) {
			return false;
		}
		if (winValue != other.winValue) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(BaseEvaluatorConfig o) {
		double val = wins / (wins + losses + .0001d);
		double otherVal = o.wins / (o.wins + o.losses + .0001d);
		return val > otherVal ? 1 : (val == otherVal ? 0 : -1);
	}

}
