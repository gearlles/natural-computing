package com.gearlles.fss.core;

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
	
	private Random rand = new Random();

	private double STEP_IND;
	private double WEIGHT_SCALE;
	
	public FSSSearch() {
		this.school = new ArrayList<Fish>();
		this.dimensions = 2;
		this.schoolSize = 20;
		this.STEP_IND = 2;
		this.WEIGHT_SCALE = 5;
	}
	
	public void iterate() {
		Fish a = new Fish();
	a.setPosition(new double[]{Math.random()*100, Math.random()*100});
	individualmovement(a);
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
	private double[] collectiveInstinctiveMovement(Fish fish) {
		double[] schoolInstinctivePosition = new double[dimensions];
		double[] m = new double[dimensions];
		double totalFitness = 0;
		
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
		
		for (int i = 0; i < dimensions; i++) {
			schoolInstinctivePosition[i] = fish.getPosition()[i] + m[i];
		}
		
		return schoolInstinctivePosition;
	}
	
	public List<Fish> getSchool() {
		List<Fish> toReturn = new ArrayList<Fish>();
		Fish a = new Fish();
		a.setPosition(new double[]{Math.random()*100, Math.random()*100});
		toReturn.add(a);
		
		Fish b = new Fish();
		b.setPosition(new double[]{Math.random()*100, Math.random()*100});
		
		toReturn.add(b);
		return toReturn;
	}
	
	private double fitness() {
		return 0d;
		
	}

}
