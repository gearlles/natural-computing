package com.gearlles.naturalcomputing.pso.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PSOSearch {

	private Logger logger = LoggerFactory.getLogger(PSOSearch.class);

	private List<Particle> swarm;
	private int maxIterations;
	private int dimensions;
	private int numberOfParticles;

	private Random rand = new Random();

	private final double RANGE = 5.2;
	private final double MAX_VELOCITY = 0.002;

	private double W = 0.9;
	private double C1 = 2.05;
	private double R1 = 2;
	private double C2 = 1.05;
	private double R2 = 2;

	public PSOSearch() {
		this.maxIterations = 5;
		this.dimensions = 10;
		this.numberOfParticles = 20;
		
		this.swarm = new ArrayList<Particle>();
	}

	public void run() {
		initializeSwarm();

		for (int i = 0; i < this.maxIterations; i++) {
			logger.debug(String.format("Generation #%d", i));

			double globalBestFitness = Double.MIN_VALUE;

			for (Particle particle : swarm) {
				particle.setVelocity(getNewVelocity(particle));
				particle.setPosition(getNewPosition(particle));

				double newFitness = getNewFitness(particle);

				if (newFitness > particle.getFitness()) {
					particle.setFitness(newFitness);
				}

				if (newFitness > globalBestFitness) {
					updateNeighborhood(particle, particle.getPosition());
					globalBestFitness = newFitness;
				}
			}

			logger.debug(String.format("Global best fitness: %f",
					globalBestFitness));
		}
	}

	private double getNewFitness(Particle particle) {
		double[] inputs = particle.getPosition();
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
			double inertia = W * oldVelocity[i];
			double selfMemory = C1 * R1 * (bestPosition[i] - position[i]);
			double globalInfluence = C2 * R2
					* (bestNeighborhoodPosition[i] - position[i]);
			newVelocity[i] = inertia + selfMemory + globalInfluence;
		}

		return newVelocity;
	}

	private void updateNeighborhood(Particle particle,
			double[] bestNeighborhoodPosition) {
		for (Particle _particle : swarm) {
			_particle.setBestNeighborhoodPosition(bestNeighborhoodPosition);
		}
	}

	private void initializeSwarm() {
		for (int i = 0; i < numberOfParticles; i++) {
			double[] position = new double[dimensions];
			double[] velocity = new double[dimensions];

			for (int j = 0; j < dimensions; j++) {
				position[j] = rand.nextDouble() * RANGE;
				velocity[j] = rand.nextDouble() * MAX_VELOCITY;
			}

			Particle particle = new Particle();
			particle.setPosition(position);
			particle.setVelocity(velocity);

			swarm.add(particle);
		}
	}

}
