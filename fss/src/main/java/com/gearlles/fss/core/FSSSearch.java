package com.gearlles.fss.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.omg.CORBA.INITIALIZE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FSSSearch {
	
	private Logger logger = LoggerFactory.getLogger(FSSSearch.class);
	
	private List<Fish> school;
	private int dimensions;
	private int schoolSize;
	private double RANGE = 5.12;
	
	private Random rand = new Random();

	private double STEP_IND;
	private double WEIGHT_SCALE;
	private double STEP_VOL;
	
	private double lastOverallWeight;
	private double bestFitness;
	
	public FSSSearch() {
		this.school = new ArrayList<Fish>();
		this.dimensions = 2;
		this.schoolSize = 20;
		this.lastOverallWeight = 0;
		this.bestFitness = Double.MAX_VALUE;
		
		this.STEP_IND = 2;
		this.STEP_VOL = 2 * this.STEP_IND;
		this.WEIGHT_SCALE = 5;
		
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
			fish.setWeight(0d);
			
			school.add(fish);
		}
	}

	public void iterateOnce() {
		
		double iterationBestFitness = Double.MAX_VALUE;
		
		for (int i = 0; i < school.size(); i++) {
			Fish fish = school.get(i);
			
			// 1. update position applying the individual operator
			double[] tempPosition = individualmovement(fish);
			double oldFitness = calculateFitness(fish.getPosition());
			double newFitness = calculateFitness(tempPosition);
			
			if (newFitness < oldFitness) {
				double[] deltaPosition = new double[dimensions];
				for (int j = 0; j < dimensions; j++) {
					deltaPosition[j] = tempPosition[j] - fish.getPosition()[j];
				}
				fish.setDeltaPosition(deltaPosition);
				fish.setDeltaFitness(newFitness - oldFitness);
				fish.setPosition(tempPosition);
				
				if (newFitness < iterationBestFitness) {
					iterationBestFitness = newFitness;
				}
				
			} else {
				fish.setDeltaPosition(new double[dimensions]);
				fish.setDeltaFitness(0);
			}
			
			// 2. applying feeding operator
			double newWeight = feedingOperator(fish);
			fish.setWeight(newWeight);
		}
		
		if (iterationBestFitness < bestFitness)
		{
			bestFitness = iterationBestFitness;
		}
		logger.debug(String.format("Best fitness: %f", bestFitness));
		
		double overallWeight = calculateOverallWeight();
		boolean overallWeightIncreased = overallWeight > lastOverallWeight;
		
		// 3. applying collective-instinctive movement
		collectiveInstinctiveMovement();
		
		// 4. applying collective-volitive movement
		collectiveVolitiveMovement(overallWeightIncreased);
	}
	
	private double[] individualmovement(Fish fish) {
		double[] newPosition = new double[dimensions];
		double[] oldPosition = fish.getPosition();
		double[] randArray = new double[dimensions];
		
		for (int i = 0; i < randArray.length; i++) {
			randArray[i] = rand.nextDouble(); 
		}
		
		for (int i = 0; i < dimensions; i++) {
			newPosition[i] = oldPosition[i] + randArray[i] * STEP_IND;
			
			boolean collision = newPosition[i] < -RANGE || newPosition[i] > RANGE;
			if (collision)
			{
				newPosition[i] = newPosition[i] > 0 ? RANGE : - RANGE;
			}
		}
		
		return newPosition;
	}
	
	private double feedingOperator(Fish fish) {
		double newWeight = Double.MIN_VALUE;
		double oldWeight = fish.getWeight();
		double bestDeltaFitness = Double.MAX_VALUE;
		
		// looking for the max delta fitness in the school
		// TODO should we handle negatives values?
		for (int i = 0; i < school.size(); i++) {
			double deltaFitness = school.get(i).getDeltaFitness();
			if (deltaFitness < bestDeltaFitness) {
				bestDeltaFitness = deltaFitness;
			}
		}
		
		newWeight = oldWeight + fish.getDeltaFitness() / bestDeltaFitness;
		
		// limit the weight according to Carmelo, 2008.
		if (newWeight < 1) {
			newWeight = 1;
		} else if (newWeight > WEIGHT_SCALE) {
			newWeight = WEIGHT_SCALE;
		}
		
		return newWeight;
	}
	
	// TODO review
	private void collectiveInstinctiveMovement() {
		double[] m = new double[dimensions];
		double totalFitness = 0;
		
		// calculating m, the weighted avarage of individual movements 
		for (int i = 0; i < school.size(); i++) {
			Fish _fish = school.get(i);
			
			for (int j = 0; j < dimensions; j++) {
				m[j] += _fish.getDeltaPosition()[j] * _fish.getDeltaFitness();
			}
			
			totalFitness += _fish.getDeltaFitness();
		}
		
		for (int i = 0; i < dimensions; i++) {
			m[i] /= totalFitness;
		}
		
		// applying m
		for (int i = 0; i < school.size(); i++) {
			Fish _fish = school.get(i);
			double[] schoolInstinctivePosition = new double[dimensions];
			
			for (int j = 0; j < dimensions; j++) {
				schoolInstinctivePosition[j] = _fish.getPosition()[j] + m[j];
				
				boolean collision = schoolInstinctivePosition[j] < -RANGE || schoolInstinctivePosition[j] > RANGE;
				if (collision)
				{
					schoolInstinctivePosition[j] = schoolInstinctivePosition[j] > 0 ? RANGE : - RANGE;
				}
			}
			
			_fish.setPosition(schoolInstinctivePosition);
		}
		
	}
	
	private void collectiveVolitiveMovement(boolean overallWeightIncreased) {
		double[] barycenter = new double[dimensions];
		double totalWeight = 0;
		
		// calculating barycenter
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
		
		// applying barycenter
		for (int i = 0; i < school.size(); i++) {
			Fish _fish = school.get(i);
			double[] schoolVolitivePosition = new double[dimensions];
			
			for (int j = 0; j < dimensions; j++) {
				double product = STEP_VOL * rand.nextDouble() * (_fish.getPosition()[j] - barycenter[j]);
				if (!overallWeightIncreased) product *= -1;
				schoolVolitivePosition[j] = _fish.getPosition()[j] + product;
				
				boolean collision = schoolVolitivePosition[j] < -RANGE || schoolVolitivePosition[j] > RANGE;
				if (collision)
				{
					schoolVolitivePosition[j] = schoolVolitivePosition[j] > 0 ? RANGE : - RANGE;
				}
			}
			
			_fish.setPosition(schoolVolitivePosition);
		}
	}
	
	private double calculateOverallWeight() {
		double overallWeight = 0;
		for (int i = 0; i < school.size(); i++) {
			overallWeight += school.get(i).getWeight();
		}
		return overallWeight / school.size();
	}
	
	private double calculateFitness(double[] inputs) {
		double res = 10 * inputs.length;
		for (int i = 0; i < inputs.length; i++)
			res += inputs[i] * inputs[i] - 10
					* Math.cos(2 * Math.PI * inputs[i]);
		return res;
	}

	public List<Fish> getSchool() {
		return this.school;
	}
	
	public static void main(String[] args) {
		FSSSearch s = new FSSSearch();
		for (int i = 0; i < 1000; i++) {
			s.iterateOnce();
		}
	}

	public double getRANGE() {
		return RANGE;
	}
}
