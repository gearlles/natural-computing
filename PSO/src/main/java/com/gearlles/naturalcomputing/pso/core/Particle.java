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
	private double fitness;

	public Particle(double[] velocity, double[] position,
			double[] bestPosition, double[] bestNeighborhoodPosition,
			double fitness) {
		this.velocity = velocity;
		this.position = position;
		this.bestPosition = bestPosition;
		this.bestNeighborhoodPosition = bestNeighborhoodPosition;
		this.fitness = fitness;
	}

	public Particle() {
		// TODO Auto-generated constructor stub
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

	public double getFitness() {
		return fitness;
	}
	
	public void setPosition(double[] position) {
		this.position = position;
	}

	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public void setBestNeighborhoodPosition(double[] bestNeighborhoodPosition) {
		this.bestNeighborhoodPosition = bestNeighborhoodPosition;
	}

}
