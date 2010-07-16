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

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 current value int neuron type byte pointer to other neuron + connection
 * weight short + byte
 * 
 * 4 + 1 + ((2+1) * #conn) = 5 + 3c /neuron.
 * 
 * if 10,000 c on average, then 30,009 bytes/neuron = 30k/neuron.
 * 
 * 1,000 neurons = 30m 1,000,000 = 30g 10,000 neurons = 300m
 * 
 * if 1,000 c on average, then 3k/neuron
 * 
 * 1,000 neurons = 3m 1,000,000 neurons = 3g 100,000 neurons = 300m
 * 
 * 30,000 * 100,000 = 3,000,000,000
 * 
 * Or:
 * 
 * value:int:4 type:byte:1 relative-pointer:int:4 num consecutive
 * neurons:short:2 weights:2*num
 * 
 * 10,000c in 2 groups gives 5 + 6*2 +1*10,000 = 10k+17/neuron
 * 
 * 10,000c in 100 groups gives 5+6*100+1*10,000 = 10k+605/neuron
 * 
 * 100,000 neurons * 11k = 1,100,000
 * 
 * @author michaelstover
 * 
 */
public class Brain {
	static Random rand = new Random();
	private ExecutorService ego = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors());

	/**
	 * 
	 */
	private static final int BYTES_PER_NEURON = 11000;
	private final ByteBuffer neurons;
	private final int numNeurons;

	public Brain(int numNeurons) {
		this.numNeurons = numNeurons;
		neurons = ByteBuffer.allocateDirect(BYTES_PER_NEURON * numNeurons);
	}

	int currentValue(int neuronIndex) {
		return neurons.getInt(neuronIndex * BYTES_PER_NEURON);
	}

	/**
	 * A neuron fires. Send signals to all connected neurons
	 * 
	 * @param neuronIndex
	 */
	void fireNeuron(int neuronIndex) {
		System.out.println("Firing neuron " + neuronIndex);
		int pos = neuronIndex * BYTES_PER_NEURON + 5;
		int relativeAddress = 0;
		while ((relativeAddress = neurons.getInt(pos)) != 0) {
			int mark = pos + 4;
			int num = neurons.getShort(mark);
			mark += 2;
			for (int i = 0; i < num; i++) {
				sendSignal((neuronIndex + relativeAddress + i) % numNeurons,
						calcWeight(neurons.get(i + mark)));
			}
			pos = mark + num;
		}
	}

	void processNeuron(int neuronIndex) {
		Neuron n = getNeuron(neurons.get(neuronIndex * BYTES_PER_NEURON + 4));
		n.calcAndFire(this, neuronIndex);
	}

	/**
	 * @param b
	 * @return
	 */
	private Neuron getNeuron(byte b) {
		return NeuronTypes.getNeuron(b);
	}

	/**
	 * @param i
	 * @param calcWeight
	 */
	void sendSignal(int neuronIndex, int weight) {
		int pos = neuronIndex * BYTES_PER_NEURON;
		try {
			neurons.putInt(pos, neurons.getInt(pos) + weight);
		} catch (RuntimeException e) {
			System.out.println("Error in sendSignal: pos = " + pos
					+ ", neuronIndex = " + neuronIndex);
			throw e;
		}
	}

	/**
	 * @param b
	 * @return
	 */
	private int calcWeight(byte b) {
		int absValue = Math.abs(b);
		int sign = b == 0 ? 1 : b / absValue;
		return (int) (sign * Math.pow(2, absValue / 5d));
	}

	public void generateNeuron(int neuronIndex, byte neuronType, int numGroups,
			int numConnections) {
		int pos = neuronIndex * BYTES_PER_NEURON;
		neurons.putInt(pos, 0);
		neurons.put(pos + 4, neuronType);
		pos += 5;
		short aveGroupSize = (short) (numConnections / numGroups);
		short leftover = (short) (numConnections % numGroups);
		for (int i = 0; i < numGroups; i++) {
			int relAddr = rand.nextInt(numNeurons);
			neurons.putInt(pos, relAddr);
			pos += 4;
			short groupSize = aveGroupSize;
			if (i + 1 == numGroups) {
				groupSize = (short) (aveGroupSize + leftover);
			}
			neurons.putShort(pos, groupSize);
			pos += 2;
			for (int j = 0; j < groupSize; j++) {
				neurons.put(pos++, (byte) (rand.nextInt(160) - 92));
			}
		}
	}

	public void think() {
		for (int i = 0; i < numNeurons; i += 100) {
			if (i + 100 > numNeurons) {
				ego.submit(new NeuronRunner(this, i, numNeurons - i));
			} else {
				ego.submit(new NeuronRunner(this, i, 100));
			}
		}
	}

	void rethink(NeuronRunner runner) {
		ego.submit(runner);
	}

	public static void main(String[] args) {
		System.out.println("Creating brain");
		Brain brain = new Brain(100000);
		System.out.println("Initializing state");
		for (int i = 0; i < 100000; i++) {
			brain.generateNeuron(i, (byte) 0, rand.nextInt(99) + 1, 10000);
		}
		System.out.println("Thinking...");
		for (int i = 0; i < 5; i++) {
			brain.sendSignal(i, 10000);
		}
		brain.think();
	}
}
