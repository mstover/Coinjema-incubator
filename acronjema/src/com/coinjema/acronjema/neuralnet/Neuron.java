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
 * The methods neurons must implement
 * 
 * @author michaelstover
 * 
 */
public interface Neuron {

	void calcAndFire(Brain brain, int thisNeuron);
}
