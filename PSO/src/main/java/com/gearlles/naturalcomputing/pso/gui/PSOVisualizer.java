package com.gearlles.naturalcomputing.pso.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.gearlles.naturalcomputing.pso.core.PSOSearch;
import com.gearlles.naturalcomputing.pso.core.Particle;

public class PSOVisualizer {

	private PSOFrame frame;
	private PSOSearch search;
	private boolean completed;
	
	public PSOVisualizer() {
		search = new PSOSearch(this);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					PSOVisualizer window = new PSOVisualizer();
					window.displayGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void displayGUI() {
		frame = new PSOFrame("PSO Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(500, 500));
//		frame.setResizable(false);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(500, 500));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		frame.setContentPane(panel);
		
		frame.pack();
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		frame.setVisible(true);
		this.completed = true;
	}
	
	public void start(boolean headless, int velocity) {
		search.run(headless, velocity);
	}

	public void update(int iteration) {
		this.completed = false;
		frame.cleanDots();
		for (int i = 0; i < search.getSwarm().size(); i++) {
			Particle particle = search.getSwarm().get(i);
			frame.addDot(iteration, particle.getPosition()[0] + 5.2, particle.getPosition()[1] + 5.2, getNeighborsPositions(particle));
		}
		this.completed = true;
	}
	
	public List<Double> getNeighborsPositions(Particle particle) {
		List<Point2D.Double> dots = new ArrayList<Point2D.Double>();
		for (Particle neighbor : particle.getNeighbors()) {
			dots.add(new Point2D.Double(neighbor.getPosition()[0] + 5.2, neighbor.getPosition()[1] + 5.2));
		}
		return dots;
	}

	public boolean isPaintComplete() {
		return this.completed;
	}
}
