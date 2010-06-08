package com.coinjema.Deem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Grid {
	private static ExecutorService exec = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors());

	protected final Point2D[] locations;
	private List<Point> drawn;
	private final Dimension bounds;

	public Grid(Dimension bounds, Point2D... centers) {
		locations = centers;
		this.bounds = bounds;
		System.out.println("Done initializing grid");
	}

	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight());
		g.setColor(Color.black);
		if (drawn == null) {
			calcPoints();
		}
		for (Point p : drawn) {
			g.fillRect(p.x, p.y, 1, 1);
		}
	}

	private void calcPoints() {
		drawn = Collections.synchronizedList(new LinkedList<Point>());
		long time = System.currentTimeMillis();
		for (final Point2D point : iterate(bounds)) {
			exec.execute(
			new Runnable() {
				public void run() {
					double lowest = Double.MAX_VALUE;
					boolean multiple = false;
					for (Point2D center : locations) {
						double dist = point.distance(center);
						if (Math.abs(dist - lowest) < 1d) {
							lowest = Math.min(dist, lowest);
							multiple = true;
						} else if (dist < lowest) {
							lowest = dist;
							multiple = false;
						}
					}
					if (multiple) {
						drawn.add(new Point((int) point.getX(), (int) point
								.getY()));
					}
				}
			});
		}
		try {
			exec.shutdown();
			exec.awaitTermination(1,TimeUnit.HOURS);
			System.out.println("Grid calc took " + (System.currentTimeMillis() - time) + " ms");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Iterable<Point2D> iterate(final Dimension bounds) {
		// TODO Auto-generated method stub
		return new Iterable<Point2D>() {
			@Override
			public Iterator<Point2D> iterator() {
				// TODO Auto-generated method stub
				return new Iterator<Point2D>() {
					int x = -1;
					int y = 0;

					@Override
					public void remove() {
						// TODO Auto-generated method stub

					}

					@Override
					public Point2D next() {
						x += 1;
						if (x < bounds.getWidth()) {
							return new Point2D.Double(x, y);
						} else {
							x = 0;
							y += 1;
							return new Point2D.Double(x, y);
						}
					}

					@Override
					public boolean hasNext() {
						return x + 1 < bounds.getWidth()
								|| y + 1 < bounds.getHeight();
					}
				};
			}
		};
	}

}
