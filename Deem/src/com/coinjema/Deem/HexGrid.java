package com.coinjema.Deem;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

public class HexGrid extends Grid {

	public HexGrid(int numCols, int numRows, double distanceFromEach) {
		super(numCols, numRows, distanceFromEach);
	}


	
	protected void initLocations(List<Area> locations) {
		for(int i = 0;i < numCols;i++) {
			double x = (i * distanceBetweenCenters*Math.cos(Math.PI/6)) + ((distanceBetweenCenters/2)/Math.sin(Math.PI/3));
			for(int j = 0;j < numRows;j++) {
				double y = (j * distanceBetweenCenters) + (distanceBetweenCenters/2) + (i%2==1 ? (distanceBetweenCenters/2) : 0);
				locations.add(new Hex(x,y,(distanceBetweenCenters/2)));
			}
		}
	}
	
	protected Iterable<Coord> getAdjacentCenters(Area a) {
		List<Coord> adj = new LinkedList<Coord>();
		adj.add(new Coord(a.col+distanceBetweenCenters*Math.cos(Math.PI/6),a.row+distanceBetweenCenters/2));
		adj.add(new Coord(a.col+distanceBetweenCenters*Math.cos(Math.PI/6),a.row-distanceBetweenCenters/2));
		adj.add(new Coord(a.col-distanceBetweenCenters*Math.cos(Math.PI/6),a.row+distanceBetweenCenters/2));
		adj.add(new Coord(a.col-distanceBetweenCenters*Math.cos(Math.PI/6),a.row-distanceBetweenCenters/2));
		adj.add(new Coord(a.col,a.row+distanceBetweenCenters));
		adj.add(new Coord(a.col,a.row-distanceBetweenCenters));
		return adj;
	}

	protected double getHalfBorderLength() {
		return Math.tan(Math.PI/6) * (distanceBetweenCenters/2);
	}
}
