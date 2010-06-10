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

	public boolean shareAllCenter(Corner c) {
		Set<Point> and = new HashSet<Point>(closestCenters);
		and.retainAll(c.closestCenters);
		return and.size() >= 3;
	}

	public boolean shareAllCenter(Corner c, Corner d) {
		Set<Point> and = new HashSet<Point>(closestCenters);
		and.retainAll(c.closestCenters);
		and.retainAll(d.closestCenters);
		if (and.size() >= 2) {
			System.out.println("c = " + closestCenters);
			System.out.println("d = " + c.closestCenters);
			System.out.println("e = " + d.closestCenters);
			return true;
		}
		return false;

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

}
