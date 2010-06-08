package com.coinjema.Deem;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Grid {

	protected final List<Area> locations;
	protected final int numCols, numRows;
	protected final double distanceBetweenCenters;

	public Grid(int numCols, int numRows, double distanceFromEach) {
		this.numCols = numCols;
		this.numRows = numRows;
		this.distanceBetweenCenters = distanceFromEach;
		locations = new ArrayList<Area>(numCols * numRows);
		initLocations(locations);
		System.out.println("Done initializing grid");
	}

	protected void initLocations(List<Area> locations) {
		for (int i = 0; i < getNumCols(); i++) {
			double x = (i * distanceBetweenCenters)
					+ (distanceBetweenCenters / 2);
			for (int j = 0; j < getNumRows(); j++) {
				double y = (j * distanceBetweenCenters)
						+ (distanceBetweenCenters / 2);
				locations.add(new Square(x, y, (distanceBetweenCenters / 2)));
			}
		}
	}

	protected int getNumCols() {
		return numCols;
	}

	protected int getNumRows() {
		return numRows;
	}

	public void draw(Graphics g) {
		for (Area a : locations) {
			for (Coord adj : getAdjacentCenters(a)) {
				Coord middle = new Coord((a.col + adj.col) / 2,
						(a.row + adj.row) / 2);
				g
						.drawLine(
								(int) middle.col,
								(int) middle.row,
								(int) (middle.col + ((adj.row - a.row) / distanceBetweenCenters)
										* getHalfBorderLength()),
								(int) (middle.row - ((adj.col - a.col) / distanceBetweenCenters)
										* getHalfBorderLength()));
				g
						.drawLine(
								(int) middle.col,
								(int) middle.row,
								(int) (middle.col - ((adj.row - a.row) / distanceBetweenCenters)
										* getHalfBorderLength()),
								(int) (middle.row + ((adj.col - a.col) / distanceBetweenCenters)
										* getHalfBorderLength()));
			}
		}
	}

	protected double getHalfBorderLength() {
		return distanceBetweenCenters / 2;
	}

	protected Iterable<Coord> getAdjacentCenters(Area a) {
		List<Coord> adj = new LinkedList<Coord>();
		adj.add(new Coord(a.col + distanceBetweenCenters, a.row));
		adj.add(new Coord(a.col - distanceBetweenCenters, a.row));
		adj.add(new Coord(a.col, a.row + distanceBetweenCenters));
		adj.add(new Coord(a.col, a.row - distanceBetweenCenters));
		return adj;
	}

}
