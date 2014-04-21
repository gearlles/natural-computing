package com.gearlles.fss.enhanced.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FSSSearch {
	
	private Logger logger = LoggerFactory.getLogger(FSSSearch.class);
	
	private List<Fish> school;
	private int dimensions;
	private int schoolSize;
	private double RANGE = 5.12;
	
	private Random rand = new Random();

	private double lastOverallWeight;
	private double bestFitness;

	private int BETA;
	private int C;
	
	public FSSSearch() {
		this.school = new ArrayList<Fish>();
		this.dimensions = 10;
		this.schoolSize = 20;
		this.lastOverallWeight = 0;
		this.bestFitness = Double.MAX_VALUE;
		
		initialize();
	}
	
	private void initialize() {
		for (int i = 0; i < schoolSize; i++) {
			double position[] = new double[dimensions];
			
			for (int j = 0; j < dimensions; j++) {
				position[j] = rand.nextDouble() * 2 * RANGE - RANGE;
			}
			
			Fish fish = new Fish();
			fish.setPosition(position);
			fish.setWeight(2500);
			
			school.add(fish);
		}
	}

	public void iterateOnce(int it) {
		
		for (int i = 1; i < school.size(); i++) {
			Fish fish = school.get(i);
			
			// 1. Evaluate fish displacement 
			double[] deltaPosition = new double[dimensions];
			for (int j = 0; j < dimensions; j++) {
				deltaPosition[j] = fish.getPosition()[j] - fish.getOldPosition()[j];
			}
			
			fish.setDeltaPosition(deltaPosition);
			
			// 2. Evaluate fitness variation using
			double deltaFitness = calculateFitness(fish.getPosition()) - calculateFitness(fish.getOldPosition());
			fish.setDeltaFitness(deltaFitness);
			
			// 3. Feed the fish
			fish.setOldWeight(fish.getWeight());
			fish.setWeight(fish.getWeight() + deltaFitness);
			
			// 4. Evaluate weight variation
			fish.setDeltaWeight(fish.getWeight() - fish.getOldWeight());
		}
		
		// 5. Calculate barycenter
		double[] barycenter = new double[dimensions];
		double totalWeight = 0;
		
		for (int i = 0; i < school.size(); i++) {
			Fish _fish = school.get(i);
			
			for (int j = 0; j < dimensions; j++) {
				barycenter[j] += _fish.getPosition()[j] * _fish.getWeight();
			}
			
			totalWeight += _fish.getWeight();
		}
		
		for (int i = 0; i < dimensions; i++) {
			barycenter[i] /= totalWeight;
		}
		
		// check whether the overall weight has increased
		double overallWeight = calculateOverallWeight();
		boolean overallWeightIncreased = overallWeight > lastOverallWeight;
		lastOverallWeight = overallWeight;
		
		// 6. Update fish position
		for (int i = 1; i < school.size(); i++) {
			Fish fish = school.get(i);
			
			double[] currentPosition = fish.getPosition();
			double[] individualMovementTerm = individualMovementTerm(fish);
			double[] instinctiveMovementTerm = collectiveInstinctiveMovementTerm(fish);
			double[] volitiveCollectiveMovementTerm = collectiveVolitiveMovementTerm(fish, barycenter);
			
			double[] newPosition = new double[dimensions];
			for (int j = 0; j < dimensions; j++) {
				if(!overallWeightIncreased) {
					volitiveCollectiveMovementTerm[j] *= -1;
				}
				newPosition[j] = currentPosition[j] + individualMovementTerm[j] + instinctiveMovementTerm[j] + volitiveCollectiveMovementTerm[j];
			}
			
			fish.setPosition(newPosition);
		}
	}
	
	private double[] individualMovementTerm(Fish fish) {
		double[] individualMovementTerm = new double[dimensions];
		double[] deltaPosition = fish.getDeltaPosition();
		for (int i = 0; i < dimensions; i++) {
			individualMovementTerm[i] = BETA * C * deltaPosition[i];
		}
		return individualMovementTerm;
	}
	
	private double[] collectiveInstinctiveMovementTerm(Fish fish) {
		double[] collectiveInstinctiveMovementTerm = new double[dimensions];
		double totalWeight = 0;
		
		for (int i = 0; i < school.size(); i++) {
			Fish _fish = school.get(i);
			
			for (int j = 0; j < dimensions; j++) {
				collectiveInstinctiveMovementTerm[j] += C * rand.nextDouble() * _fish.getDeltaPosition()[j] * _fish.getWeight();
			}
			
			totalWeight += _fish.getWeight();
		}
		
		for (int i = 0; i < dimensions; i++) {
			collectiveInstinctiveMovementTerm[i] /= totalWeight;
		}
		
		return collectiveInstinctiveMovementTerm;
	}
	
	private double[] collectiveVolitiveMovementTerm(Fish fish, double[] barycenter) {
		double result[] = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			result[i] = C * rand.nextDouble() * fish.getDeltaWeight() * (fish.getPosition()[i] - barycenter[i]);
		}
		return result;
	}
	
	private double calculateOverallWeight() {
		double overallWeight = 0;
		for (int i = 0; i < school.size(); i++) {
			overallWeight += school.get(i).getWeight();
		}
		return overallWeight;
	}
	
	private double calculateFitnessa(double[] inputs) {
		double res = 10 * inputs.length;
		for (int i = 0; i < inputs.length; i++)
			res += inputs[i] * inputs[i] - 10
					* Math.cos(2 * Math.PI * inputs[i]);
		return res;
	}
	
	private double calculateFitness(double[] inputs) {
		double res = 0;
		for (int i = 0; i < inputs.length; i++)
			res += Math.pow(inputs[i], 2);
		return res;
	}

	public List<Fish> getSchool() {
		return this.school;
	}
	
	public static void main(String[] args) {
		FSSSearch s = new FSSSearch();
		for (int i = 0; i < 1000; i++) {
			s.iterateOnce(i);
		}
	}

	public double getRANGE() {
		return RANGE;
	}
}
