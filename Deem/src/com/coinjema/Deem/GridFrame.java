package com.coinjema.Deem;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
		g.translate(50,50);
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
		mainWindow.setPreferredSize(new Dimension(1200,1000));
		mainWindow.grid = new HexGrid(30,30,40);
		mainWindow.pack();
		mainWindow.setVisible(true);
	}

}
