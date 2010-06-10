package com.coinjema.Deem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A Grid is just a space bounded by a Dimension object with a list of point
 * locations that represent the central position of each area in the space.
 * 
 * @author michaelstover
 * 
 */
public class Grid {
	private static ExecutorService exec = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors());

	protected final Point2D[] locations;
	private Collection<Corner> corners;
	private final Dimension bounds;

	/**
	 * The Grid must be created with the bounds and the list of center points.
	 * 
	 * @param bounds
	 * @param centers
	 */
	public Grid(Dimension bounds, Point2D... centers) {
		locations = centers;
		this.bounds = bounds;
		System.out.println("Done initializing grid");
	}

	/**
	 * Draw the Grid.
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight());
		g.setColor(Color.black);
		if (corners == null) {
			calcPoints();
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		for (Corner c : corners) {
			for (Corner adj : c.getAdjacentCorners()) {
				g2.drawLine(c.x, c.y, adj.x, adj.y);
			}
		}
		// g.setColor(Color.red);
		// for (Point2D p : locations) {
		// g.fillRect((int) p.getX() - 1, (int) p.getY() - 1, 3, 3);
		// }
	}

	/**
	 * Calculate all the points in concurrent fashion using all available
	 * processors. The algorithm is:
	 * 
	 * foreach point find closest center location if only 1 closest, do nothing
	 * if 2 or more are equally close, record the point
	 * 
	 * The recoreded points will be painted black, and by painting all such
	 * points black, lines will be drawn around the locations.
	 */
	private void calcPoints() {
		long time = System.currentTimeMillis();
		final List<Corner> coords = Collections
				.synchronizedList(new LinkedList<Corner>());
		DimensionIterator iter = new DimensionIterator(bounds);
		while (iter.hasNext()) {
			final int x = iter.nextX();
			final int y = iter.nextY();
			exec.execute(new Runnable() {
				public void run() {
					List<Tup> candidates = new ArrayList<Tup>();
					double threshhold = Double.MAX_VALUE;
					for (Point2D center : locations) {
						double lowest = candidates.isEmpty() ? Double.MAX_VALUE
								: candidates.get(0).dist;
						if (Math.abs(x - center.getX()) <= threshhold
								&& Math.abs(y - center.getY()) <= threshhold) {
							final double dist = center.distance(x, y);
							if (Math.abs(dist - lowest) < 1.1d) {
								candidates.add(dist < lowest ? 0 : 1, new Tup(
										center, dist));
								threshhold = lowest + 1.1d;
							} else if (dist < lowest) {
								lowest = dist;
								threshhold = lowest + 1.1d;
								candidates.clear();
								candidates.add(new Tup(center, dist));
							}
						}
					}
					if (candidates.size() > 2) {
						coords.add(new Corner(x, y, getCenters(candidates)));
					}
				}
			});
		}
		try {
			exec.shutdown();
			exec.awaitTermination(1, TimeUnit.HOURS);
			System.out.println("Grid calc took "
					+ (System.currentTimeMillis() - time) + " ms");
			corners = Collections.unmodifiableCollection(fillOut(coords));
			System.out.println("Grid calc took "
					+ (System.currentTimeMillis() - time) + " ms");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Collection<Corner> fillOut(Collection<Corner> corners) {
		System.out.println("Num corners coming in = " + corners.size());
		Set<Corner> unique = removeDupCorners(corners);
		System.out
				.println("After removing dups num corners = " + unique.size());
		for (Corner c : unique) {
			for (Corner d : unique) {
				if (c != d) {
					if (c.shareCenter(d)) {
						c.addAdjacent(d);
					}
				}
			}
		}
		for (Corner c : unique) {
			Set<Corner> line = new HashSet<Corner>();
			checkXLine(c.x, c, line);
			double sum = 0d;
			for (Corner t : line) {
				sum += t.x;
			}
			sum = sum / line.size();
			for (Corner t : line) {
				t.x = (int) Math.round(sum);
			}

			line = new HashSet<Corner>();
			checkYLine(c.y, c, line);
			sum = 0d;
			for (Corner t : line) {
				sum += t.y;
			}
			sum = sum / line.size();
			for (Corner t : line) {
				t.y = (int) Math.round(sum);
			}
		}
		System.out.println("Num corners going out = " + unique.size());
		return unique;
	}

	private Set<Corner> removeDupCorners(Collection<Corner> corners) {
		Set<Corner> unique = removeStrictDups(corners);
		for (Corner c : unique) {
			for (Corner d : unique) {
				if (c != d && !c.colocated(d)) {
					if (c.shareTooManyCenters(d)) {
						c.linkWith(d);
					}
				}
			}
		}

		return removeStrictDups(unique);
	}

	private Set<Corner> removeStrictDups(Collection<Corner> corners) {
		Set<Corner> unique = new HashSet<Corner>(corners);
		for (Corner c : corners) {
			if (unique.contains(c)) {
				for (Corner d : corners) {
					if (unique.contains(d) && c != d) {
						if (c.shareAllCenters(d)) {
							unique.remove(c.getWorse(d));
							if (!unique.contains(c))
								break;
						}
					}
				}
			}
		}
		return unique;
	}

	private void checkYLine(int baseY, Corner c, Set<Corner> onLine) {
		if (onLine.contains(c))
			return;
		if (Math.abs(c.y - baseY) < 3) {
			onLine.add(c);
			for (Corner adj : c.getAdjacentCorners()) {
				checkYLine(baseY, adj, onLine);
			}
		}
	}

	private void checkXLine(int baseX, Corner c, Set<Corner> onLine) {
		if (onLine.contains(c))
			return;
		if (Math.abs(c.x - baseX) < 3) {
			onLine.add(c);
			for (Corner adj : c.getAdjacentCorners()) {
				checkXLine(baseX, adj, onLine);
			}
		}
	}

	protected Point[] getCenters(List<Tup> candidates) {
		Point[] centers = new Point[candidates.size()];
		for (int i = 0; i < candidates.size(); i++) {
			centers[i] = new Point((int) candidates.get(i).center.getX(),
					(int) candidates.get(i).center.getY());
		}
		return centers;
	}

	private class Tup {
		final public double dist;
		public final Point2D center;

		public Tup(Point2D c, double d) {
			dist = d;
			center = c;
		}
	}

	private class DimensionIterator {
		private Dimension bounds;
		int x, y;

		public DimensionIterator(Dimension bounds) {
			this.bounds = bounds;
		}

		public int nextY() {
			return y;
		}

		public int nextX() {
			x += 1;
			if (x < bounds.getWidth())
				return x;
			else {
				x = 0;
				y += 1;
				return x;
			}
		}

		public boolean hasNext() {
			return (x + 1 < bounds.getWidth() || y + 1 < bounds.getHeight());
		}
	}

}