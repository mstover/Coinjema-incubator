package com.coinjema.Deem;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Corner {

	int x, y;

	private final Set<Corner> adjacent = new HashSet<Corner>(11);
	private final Set<Point> closestCenters;
	private Set<Corner> linked;

	public Corner(int x, int y, Point... closestCenters) {
		this.x = x;
		this.y = y;
		this.closestCenters = new HashSet<Point>(Arrays.asList(closestCenters));
	}

	public void addAdjacent(Corner c) {
		adjacent.add(c);
		c.adjacent.add(this);
	}

	@Override
	public String toString() {
		return "Corner [adjacent=" + adjacent + ", closestCenters="
				+ closestCenters + ", x=" + x + ", y=" + y + "]";
	}

	public Collection<Corner> getAdjacentCorners() {
		return adjacent;
	}

	public boolean shareCenter(Corner c) {
		Set<Point> and = new HashSet<Point>(closestCenters);
		and.retainAll(c.closestCenters);
		return and.size() == 2;
	}

	public boolean shareAllCenters(Corner c) {
		return closestCenters.containsAll(c.closestCenters)
				&& c.closestCenters.containsAll(closestCenters);
	}

	public boolean shareTooManyCenters(Corner c) {
		Set<Point> and = new HashSet<Point>(closestCenters);
		and.retainAll(c.closestCenters);
		return and.size() > 2;
	}

	public boolean shareTooManyCenters(Corner c, Corner d) {
		Set<Point> and = new HashSet<Point>(closestCenters);
		and.retainAll(c.closestCenters);
		and.retainAll(d.closestCenters);
		return and.size() >= 2;

	}

	public Corner getWorse(Corner c) {
		double highThis = 0;
		double highThat = 0;
		for (Point p : closestCenters) {
			highThis = Math.max(highThis, p.distance(x, y));
		}
		for (Point p : c.closestCenters) {
			highThat = Math.max(highThat, p.distance(x, y));
		}
		if (highThat > highThis)
			return c;
		else if (highThat == highThis) {
			int oddThis = x % 2 + y % 2;
			int oddThat = c.x % 2 + c.y % 2;
			if (oddThat > oddThis)
				return c;
			else
				return this;
		} else
			return this;
	}

	public void addCenters(Corner c) {
		closestCenters.addAll(c.closestCenters);

	}

	public void stripExcessCenters(Corner c) {
		Set<Point> and = new HashSet<Point>(closestCenters);
		and.retainAll(c.closestCenters);
		if (and.size() > 2) {
			double diff = 0;
			Point badCenter = null;
			for (Point p : and) {
				double d = p.distance(x, y) - p.distance(c.x, c.y);
				if (Math.abs(d) > Math.abs(diff)) {
					if (d < 0 && c.closestCenters.size() < 4
							&& closestCenters.size() > 3)
						continue;
					else if (d > 0 && closestCenters.size() < 4
							&& c.closestCenters.size() > 3)
						continue;
					diff = d;
					badCenter = p;
				}
			}
			if (badCenter != null) {
				if (diff < 0) {
					c.closestCenters.remove(badCenter);
				} else {
					closestCenters.remove(badCenter);
				}
				stripExcessCenters(c);
			} else {
				System.out
						.println("We need to remove a center, but can't figure out which one "
								+ this + " and  " + c);
				diff = 0;
				badCenter = null;
				for (Point p : and) {
					System.out.println("Testing point " + p + " against "
							+ this + " and " + c);
					double d = p.distance(x, y) - p.distance(c.x, c.y);
					System.out.println("Diff = " + d);
					if (Math.abs(d) > Math.abs(diff)) {
						if (d < 0 && c.closestCenters.size() < 4
								&& closestCenters.size() > 3)
							continue;
						else if (d > 0 && closestCenters.size() < 4
								&& c.closestCenters.size() > 3)
							continue;
						System.out.println("Setting badCenter to " + p);
						diff = d;
						badCenter = p;
					}
				}
			}

		}

	}

	public boolean colocated(Corner d) {
		return x == d.x && y == d.y;
	}

	public void linkWith(Corner d) {
		if (linked != null && linked.contains(d))
			return;
		if (this != d && !addLink(d)) {
			double xsum = x;
			double ysum = y;
			int count = 1;
			for (Corner c : linked) {
				xsum += c.x;
				ysum += c.y;
				count++;
			}
			xsum /= count;
			ysum /= count;
			x = (int) Math.round(xsum);
			y = (int) Math.round(ysum);
			for (Corner c : linked) {
				c.x = (int) Math.round(xsum);
				c.y = (int) Math.round(ysum);
			}
		}

	}

	private boolean addLink(Corner d) {
		if (linked == null)
			linked = new HashSet<Corner>();
		if (linked.contains(d))
			return true;
		if (d.linked != null) {
			for (Corner c : d.linked) {
				linked.add(c);
			}
		}
		linked.add(d);
		addCenters(d);
		for (Corner c : new HashSet<Corner>(linked)) {
			c.linkWith(d);
		}
		d.linkWith(this);
		return false;

	}

}
