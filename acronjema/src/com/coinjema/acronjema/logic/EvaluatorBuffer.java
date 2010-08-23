/**
 * --start-license-block--
 * 
 * (c) 2006 - present by the University of Rochester 
 * See the file DEDISCOVER-LICENSE.txt for License Details 
 * 
 * --end-license-block--
 * $Id$
 */
package com.coinjema.acronjema.logic;

import java.nio.IntBuffer;

class EvaluatorBuffer implements StepBuffer {

	IntBuffer steps = IntBuffer.allocate(200);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.acronjema.logic.StepBuffer#putStep(int)
	 */
	@Override
	public void putStep(int i) {
		try {
			steps.put(i);
		} catch (Exception e) {
			System.out.println("evaluator buffer position = "
					+ steps.position());
			e.printStackTrace();
			System.exit(0);
		}
	}
}