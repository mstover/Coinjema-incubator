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
public class NeuronRunner implements Runnable {

	public final Brain brain;
	public final int startNeuron;
	public final int length;

	public NeuronRunner(Brain brain, int start, int len) {
		this.brain = brain;
		this.startNeuron = start;
		this.length = len;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		for (int i = startNeuron; i < startNeuron + length; i++) {
			try {
				brain.processNeuron(i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
		}
		brain.rethink(this);
	}

}
