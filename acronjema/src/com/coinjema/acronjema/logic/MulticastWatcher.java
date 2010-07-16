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

/**
 * This class is to provide an efficient mechanism for holding lists of
 * Watchers. The vast majority of cases, a component holds one or two such
 * listeners. Instantiating a list object every time we want to hold one
 * listener is inefficient. By using this class, these cases are optimized to
 * just hold the one Watcher. In the event that more are added, a Linked List
 * type of structure is built to hold them.
 * 
 * @author michaelstover
 * 
 */
public class MulticastWatcher implements Watcher {

	private Watcher a, b;

	/**
	 * Constructor. Private because users of this class should use the provided
	 * static method for creating Watcher lists.
	 * 
	 * @param a
	 * @param b
	 */
	private MulticastWatcher(Watcher a, Watcher b) {
		this.a = a;
		if (a != b) {
			this.b = b;
		}
	}

	/**
	 * Users of MulticastWatcher should use this static method to add a new
	 * action listener to the list.
	 * 
	 * @param n1
	 *            The current Watcher object held by the component.
	 * @param n2
	 *            The new Watcher to be added.
	 * @return
	 */
	public static Watcher addListener(Watcher n1, Watcher n2) {
		if (n1 != null) {
			if (n2 != null) {
				if (n1 instanceof MulticastWatcher) {
					return ((MulticastWatcher) n1).insertListener(n2);
				} else if (n2 instanceof MulticastWatcher) {
					return ((MulticastWatcher) n2).insertListener(n1);
				} else {
					return new MulticastWatcher(n1, n2);
				}
			} else {
				return n1;
			}
		} else if (n2 != null) {
			return n2;
		} else {
			return null;
		}
	}

	public static Watcher removeListener(Watcher current, Watcher toBeRemoved) {
		if (current == null) {
			return null;
		}
		if (current instanceof MulticastWatcher) {
			((MulticastWatcher) current).removeListener(toBeRemoved);
		} else if (current == toBeRemoved) {
			return null;
		}
		return current;
	}

	/**
	 * @param toBeRemoved
	 * @return
	 */
	private boolean removeListener(Watcher toBeRemoved) {
		if (b == toBeRemoved) {
			b = null;
			return true;
		} else if (a == toBeRemoved) {
			a = null;
			a = b;
			b = null;
			return true;
		} else {
			if (a instanceof MulticastWatcher) {
				if (((MulticastWatcher) a).removeListener(toBeRemoved)) {
					return true;
				}
			}
			if (b instanceof MulticastWatcher) {
				if (((MulticastWatcher) b).removeListener(toBeRemoved)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add a new Watcher to the list.
	 * 
	 * @param n2
	 * @return
	 */
	private Watcher insertListener(Watcher ma) {
		if (!contains(ma)) {
			if (a == null) {
				a = ma;
			} else if (b == null) {
				b = ma;
			} else {
				b = new MulticastWatcher(b, ma);
			}
		}
		return this;
	}

	/**
	 * Does the multicaster already contain this listener?
	 * 
	 * @param ma
	 * @return
	 */
	private boolean contains(Watcher ma) {
		boolean contains = (a == ma) || (b == ma);
		if (!contains && (a instanceof MulticastWatcher)) {
			contains = contains || ((MulticastWatcher) a).contains(ma);
		}
		if (!contains && (b instanceof MulticastWatcher)) {
			contains = contains || ((MulticastWatcher) b).contains(ma);
		}
		return contains;
	}

	@Override
	public void change(BoardChangeEvent e) {
		if (a != null) {
			a.change(e);
			if (b != null) {
				b.change(e);
			}
		}
	}
}
