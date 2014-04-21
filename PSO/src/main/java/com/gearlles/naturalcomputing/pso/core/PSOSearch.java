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
	private double[] bestFitnessMed;

	private Random rand = new Random();

	private final double RANGE = 5.2f;
	private final double MAX_VELOCITY = 0.001f;

	private double W = 0f;
	private double C1 = 2.05f;
	private double C2 = 2.05f;

	private PSOVisualizer psoVisualizer;
	private boolean collision;

	public PSOSearch(PSOVisualizer psoVisualizer) {
		this.maxIterations = 500;
		this.dimensions = 10;
		this.numberOfParticles = 20;
		this.collision = false;
		
		this.psoVisualizer = psoVisualizer;
		this.bestFitnessMed = new double[maxIterations];
	}

	public void run(boolean headless, int velocity) {

		for (int k = 0; k <30; k++) {
			initializeSwarm();
//			W = 0.4 + (0.9 - 0.4) * rand.nextDouble();
			for (int i = 0; i < this.maxIterations; i++) {
				for (Particle particle : swarm) {
					particle.setVelocity(getNewVelocity(particle));
					particle.setPosition(getNewPosition(particle));
					
					double newFitness = calculateFitness(particle.getPosition());

					if (newFitness < calculateFitness(particle.getBestPosition())) {
						particle.setBestPosition(particle.getPosition());
					}

					double[] groupBestKnownPosition = getGroupBestKnownPosition(particle);

					if (newFitness < calculateFitness(groupBestKnownPosition)) {
						updateNeighborhood(particle);
						groupBestKnownPosition = particle.getPosition();
					}
					
					if (calculateFitness(groupBestKnownPosition) < calculateFitness(bestKnownPosition)) {
						bestKnownPosition = groupBestKnownPosition;
					}
				}

				bestFitnessMed[i] += calculateFitness(bestKnownPosition);
				
//				logger.debug(String.format("%f",
//						calculateFitness(bestKnownPosition)));

				if (!headless) {
					psoVisualizer.update(i);
					while (!psoVisualizer.isPaintComplete())
						;
				}

				try {
					Thread.sleep(velocity);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0; i < bestFitnessMed.length; i++) {
			bestFitnessMed[i] /= 30;
			logger.debug(String.format("%f",
					bestFitnessMed[i]));
		}
	}

	private double[] getGroupBestKnownPosition(Particle particle) {
		double[] bestGroupPosition = particle.getPosition();
		
		for (int i = 0; i < particle.getNeighbors().size(); i++) {
			Particle neighbor = particle.getNeighbors().get(i);
			
			if (neighbor.equals(particle)) continue;
			
			if (calculateFitness(neighbor.getPosition()) < calculateFitness(bestGroupPosition)) {
				bestGroupPosition = neighbor.getPosition();
			}
		}
		
		return bestGroupPosition;
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
			this.collision = newPosition[i] < -RANGE || newPosition[i] > RANGE;
			if (collision)
			{
				newPosition[i] = newPosition[i] > 0 ? RANGE : - RANGE;
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
			double inertia = W * oldVelocity[i];			//W -= 0.5/maxIterations;
			double selfMemory = C1 * R1 * (bestPosition[i] - position[i]); //C1 -= 1/maxIterations;
			double globalInfluence = C2 * R2
					* (bestNeighborhoodPosition[i] - position[i]);	//C2 += 1/maxIterations;
			newVelocity[i] = inertia + selfMemory + globalInfluence;
		}

		return newVelocity;
	}

	private void updateNeighborhood(Particle particle) {
		
		double bestFitness = Double.MAX_VALUE;
		double[] bestNeighborhoodPosition = null;
		
		for (int i = 0; i < particle.getNeighbors().size(); i++) {
			Particle _particle = particle.getNeighbors().get(i);
			double fitness = calculateFitness(_particle.getPosition());
			if (fitness < bestFitness) {
				bestFitness = fitness;
				bestNeighborhoodPosition = _particle.getPosition();
			}
			
		}
		
		for (int i = 0; i < particle.getNeighbors().size(); i++) {
			Particle _particle = particle.getNeighbors().get(i);
			_particle.setBestNeighborhoodPosition(bestNeighborhoodPosition);
		}
		
		if (bestKnownPosition == null) {
			bestKnownPosition = bestNeighborhoodPosition;
		}
		
//		double bestFitness = bestKnownPosition == null ? Double.MAX_VALUE
//				: calculateFitness(bestKnownPosition);
//		double[] bestNeighborhoodPosition = bestKnownPosition;
//		for (Particle _particle : particle.getNeighbors()) {
//			double fitness = calculateFitness(_particle.getPosition());
//			if (fitness < bestFitness) {
//				bestFitness = fitness;
//				bestNeighborhoodPosition = _particle.getPosition();
//			}
//		}
//
//		for (Particle _particle : swarm) {
//			_particle.setBestNeighborhoodPosition(bestNeighborhoodPosition);
//		}
//
//		bestKnownPosition = bestNeighborhoodPosition;
	}

	private void initializeSwarm() {
		this.swarm = new ArrayList<Particle>();
		bestKnownPosition = null;
		
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
		}
		
		for (int i = 0; i < swarm.size(); i++) {
			Particle particle = swarm.get(i);
			for (int j = 0; j < swarm.size(); j++) {
				particle.addNeighbor(swarm.get(j));
			}
//			int lastIndex = this.swarm.size() - 1;
//
//			if (i == 0)
//				particle.addNeighbor(this.swarm.get(lastIndex));
//			else
//				particle.addNeighbor(this.swarm.get(i - 1));
//
//			if (i == lastIndex)
//				particle.addNeighbor(this.swarm.get(0));
//			else
//				particle.addNeighbor(this.swarm.get(i + 1));

			updateNeighborhood(particle);
		}

	}

	public List<Particle> getSwarm() {
		return swarm;
	}

}
