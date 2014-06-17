package com.gearlles.ga.core.fitness;

public abstract class FitnessFunction
{
    public final double LOWER_LIMIT;
    public final double UPPER_LIMIT;
    
    public FitnessFunction (double lowerLimit, double upperLimit) {
	LOWER_LIMIT = lowerLimit;
	UPPER_LIMIT = upperLimit;
    }
    
    public abstract double evaluate(double[] gene);
}
