package com.coinjema.Deem;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

public class GridFrame extends JFrame {

	public Grid grid;

	public GridFrame() throws HeadlessException {
		super();
		// TODO Auto-generated constructor stub
	}

	public GridFrame(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	public GridFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}

	public GridFrame(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.translate(50, 50);
		try {
			grid.draw(g);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GridFrame mainWindow = new GridFrame("Canvas");
		mainWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosed(e);
				System.out.println("Closing window");
				System.exit(0);
			}
		});
		mainWindow.setPreferredSize(new Dimension(1200, 800));
		mainWindow.grid = new Grid(mainWindow.getPreferredSize(),
				getRandomCenters(100, 10, 1200, 800));
		mainWindow.pack();
		mainWindow.setVisible(true);
	}

	private static Point2D[] getHexCenters(int width, int height, int numPoints) {
		Point2D[] centers = new Point2D[numPoints];
		double xdiff = width / Math.sqrt(numPoints / Math.sin(Math.PI / 3));
		double ydiff = xdiff / Math.sin(Math.PI / 3);
		double x = -xdiff;
		double y = 0;
		boolean shift = false;
		for (int i = 0; i < numPoints; i++) {
			x += xdiff;
			if (x >= width) {
				x = 0;
				y += ydiff;
				shift = false;
				centers[i] = new Point2D.Double(Math.round(x), Math.round(y));
				shift = !shift;
			} else {
				centers[i] = new Point2D.Double(Math.round(x), Math.round(y
						+ (shift ? ydiff / 2 : 0)));
				shift = !shift;
			}
		}
		return centers;
	}

	private static Point2D[] getSquareCenters(int width, int height,
			int numPoints) {
		Point2D[] centers = new Point2D[numPoints];
		double xdiff = width / Math.sqrt(numPoints);
		double ydiff = height / Math.sqrt(numPoints);
		double x = 0;
		double y = ydiff / 2;
		for (int i = 0; i < numPoints; i++) {
			x += (x == 0 ? xdiff / 2 : xdiff);
			if (x >= width) {
				x = xdiff / 2;
				y += ydiff;
				centers[i] = new Point2D.Double(x, y);
			} else {
				centers[i] = new Point2D.Double(x, y);
			}
		}
		return centers;
	}

	private static Point2D[] getRandomCenters(int numFailures, double minDist,
			int maxX, int maxY) {
		List<Point2D> centers = new LinkedList<Point2D>();
		Random rand = new Random();
		int failures = 0;
		do {
			double x = rand.nextDouble() * maxX;
			double y = rand.nextDouble() * maxY;
			boolean fail = false;
			Point2D newPoint = new Point2D.Double(x, y);
			for (Point2D p : centers) {
				if (p.distance(newPoint) < minDist) {
					fail = true;
					break;
				}
			}
			if (fail) {
				failures++;
			} else {
				failures = 0;
				centers.add(newPoint);
			}
		} while (failures < numFailures);
		return centers.toArray(new Point2D[centers.size()]);
	}

}
