package com.gearlles.naturalcomputing.pso.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gearlles.naturalcomputing.pso.gui.PSOVisualizer;

public class PSOSearch {

	private Logger logger = LoggerFactory.getLogger(PSOSearch.class);

	private List<Particle> swarm;
	private int maxIterations;
	private int dimensions;
	private int numberOfParticles;

	private double[] bestKnownPosition;

	private Random rand = new Random();

	private final double RANGE = 5.2f;
	private final double MAX_VELOCITY = 0.002f;

	private double W = 0.9f;
	private double C1 = 2.05f;
	private double C2 = 1.05f;

	private PSOVisualizer psoVisualizer;

	private boolean updateVelocity;

	public PSOSearch(PSOVisualizer psoVisualizer) {
		this.maxIterations = 150;
		this.dimensions = 2;
		this.numberOfParticles = 200;
		this.updateVelocity = true;

		this.swarm = new ArrayList<Particle>();
		this.psoVisualizer = psoVisualizer;
	}

	public void run() {
		initializeSwarm();

		for (int i = 0; i < this.maxIterations; i++) {
			for (Particle particle : swarm) {
				particle.setVelocity(getNewVelocity(particle));
				particle.setPosition(getNewPosition(particle));

				double newFitness = calculateFitness(particle.getPosition());

				if (newFitness < calculateFitness(particle.getBestPosition())) {
					particle.setBestPosition(particle.getPosition());
				}

				if (newFitness < calculateFitness(bestKnownPosition)) {
					updateNeighborhood(particle);
					bestKnownPosition = particle.getPosition();
				}
			}

			logger.debug(String.format("Iteration #%s: global best fitness: %f", i, 
					calculateFitness(bestKnownPosition)));

			psoVisualizer.update(i);
			while (!psoVisualizer.isPaintComplete())
				;

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private double calculateFitness(double[] inputs) {
		double res = 10 * inputs.length;
		for (int i = 0; i < inputs.length; i++)
			res += inputs[i] * inputs[i] - 10
					* Math.cos(2 * Math.PI * inputs[i]);
		return res;
	}

	private double[] getNewPosition(Particle particle) {
		double[] newPosition = new double[dimensions];
		double[] oldPosition = particle.getPosition();
		double[] velocity = particle.getVelocity();

		for (int i = 0; i < dimensions; i++) {
			newPosition[i] = oldPosition[i] + velocity[i];
			if (newPosition[i] < -RANGE || newPosition[i] > RANGE) {
				this.updateVelocity = false;
			}
		}

		return newPosition;
	}

	private double[] getNewVelocity(Particle particle) {
		double[] newVelocity = new double[dimensions];
		double[] oldVelocity = particle.getVelocity();
		double[] bestPosition = particle.getBestPosition();
		double[] position = particle.getPosition();
		double[] bestNeighborhoodPosition = particle
				.getBestNeighborhoodPosition();

		for (int i = 0; i < dimensions; i++) {
			double R1 = rand.nextDouble();
			double R2 = rand.nextDouble();
			double inertia = W * oldVelocity[i];
			double selfMemory = C1 * R1 * (bestPosition[i] - position[i]);
			double globalInfluence = C2 * R2
					* (bestNeighborhoodPosition[i] - position[i]);
			newVelocity[i] = inertia + selfMemory + globalInfluence;
		}

		return newVelocity;
	}

	private void updateNeighborhood(Particle particle) {
		double bestFitness = bestKnownPosition == null ? Double.MAX_VALUE
				: calculateFitness(bestKnownPosition);
		double[] bestNeighborhoodPosition = bestKnownPosition;
		for (Particle _particle : swarm) {
			double fitness = calculateFitness(_particle.getPosition());
			if (fitness < bestFitness) {
				bestFitness = fitness;
				bestNeighborhoodPosition = _particle.getPosition();
			}
		}

		for (Particle _particle : swarm) {
			_particle.setBestNeighborhoodPosition(bestNeighborhoodPosition);
		}

		bestKnownPosition = bestNeighborhoodPosition;
	}

	private void initializeSwarm() {
		for (int i = 0; i < numberOfParticles; i++) {
			double[] position = new double[dimensions];
			double[] velocity = new double[dimensions];

			for (int j = 0; j < dimensions; j++) {
				position[j] = rand.nextDouble() * 2 * RANGE - RANGE;
				velocity[j] = rand.nextDouble() * MAX_VELOCITY;
			}

			Particle particle = new Particle();
			particle.setPosition(position);
			particle.setBestPosition(position);
			particle.setVelocity(velocity);

			swarm.add(particle);

			updateNeighborhood(particle);
		}

	}

	public List<Particle> getSwarm() {
		return swarm;
	}

}
