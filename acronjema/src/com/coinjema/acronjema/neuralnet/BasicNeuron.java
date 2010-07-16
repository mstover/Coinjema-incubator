/**
 * --start-license-block--
 * 
 * (c) 2006 - present by the University of Rochester 
 * See the file DEDISCOVER-LICENSE.txt for License Details 
 * 
 * --end-license-block--
 * $Id$
 */
package com.coinjema.acronjema.neuralnet;

/**
 * @author michaelstover
 * 
 */
public class BasicNeuron implements Neuron {
	static private int THRESHHOLD = 10000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coinjema.acronjema.neuralnet.Neuron#calcAndFire(com.coinjema.acronjema
	 * .neuralnet.Brain, long)
	 */
	@Override
	public void calcAndFire(Brain brain, int thisNeuron) {
		// if (thisNeuron < 1000) {
		// System.out.println(thisNeuron + " value = "
		// + brain.currentValue(thisNeuron));
		// }
		if (brain.currentValue(thisNeuron) >= THRESHHOLD) {
			brain.fireNeuron(thisNeuron);
			brain.sendSignal(thisNeuron, -THRESHHOLD);
		} else {
			brain.sendSignal(thisNeuron,
					-(int) Math.ceil(brain.currentValue(thisNeuron) / 2d));
		}
	}

}
