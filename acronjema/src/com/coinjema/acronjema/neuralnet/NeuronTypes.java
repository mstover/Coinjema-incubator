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

import java.util.HashMap;

/**
 * @author michaelstover
 * 
 */
public class NeuronTypes extends HashMap<Byte, Neuron> {

	private static NeuronTypes types = new NeuronTypes();

	static {
		types.put((byte) 0, new BasicNeuron());
	}

	/**
	 * @param b
	 * @return
	 */
	public static Neuron getNeuron(byte b) {
		return types.get(b);
	}

}
