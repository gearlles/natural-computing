package com.gearlles.naturalcomputing.pso.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class PSOFrame extends JFrame {
	
	private static final long serialVersionUID = -4493968643290416496L;
	private List<Point2D.Double> fillCells;
	private int iteration;

	public PSOFrame(String title) {
		super(title);
		fillCells = new ArrayList<Point2D.Double>();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (Point2D.Double fillCell : fillCells) {
			
			int cellX = (int) ((fillCell.x * this.getContentPane().getWidth()) / 10.4);
			int cellY = (int) ((fillCell.y * this.getContentPane().getHeight()) / 10.4);
			g.setColor(Color.RED);
			g.fillRect(cellX + 8, cellY + 30, 3, 3);
			g.drawString(String.format("Iteration %d", iteration), 18, 45);
		}
	}

	public void addDot(int iteration, double x, double y) {
		this.iteration = iteration;
		fillCells.add(new Point2D.Double(x, y));
		repaint();
	}

	public void cleanDots() {
		fillCells = new ArrayList<Point2D.Double>();
		repaint();
	}

}
