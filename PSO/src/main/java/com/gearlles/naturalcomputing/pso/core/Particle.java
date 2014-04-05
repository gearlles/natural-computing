package com.gearlles.naturalcomputing.pso.core;

/**
 * PSO particle.
 * 
 * @author Gearlles
 * 
 */
public class Particle {

	private double[] velocity;
	private double[] position;
	private double[] bestPosition;
	private double[] bestNeighborhoodPosition;

	public Particle(double[] velocity, double[] position,
			double[] bestPosition, double[] bestNeighborhoodPosition) {
		this.velocity = velocity;
		this.position = position;
		this.bestPosition = bestPosition;
		this.bestNeighborhoodPosition = bestNeighborhoodPosition;
	}

	public Particle() {
	}

	public double[] getVelocity() {
		return velocity;
	}

	public double[] getPosition() {
		return position;
	}

	public double[] getBestPosition() {
		return bestPosition;
	}

	public double[] getBestNeighborhoodPosition() {
		return bestNeighborhoodPosition;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}

	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	public void setBestNeighborhoodPosition(double[] bestNeighborhoodPosition) {
		this.bestNeighborhoodPosition = bestNeighborhoodPosition;
	}

	public void setBestPosition(double[] bestPosition) {
		this.bestPosition = bestPosition;
	}

}
