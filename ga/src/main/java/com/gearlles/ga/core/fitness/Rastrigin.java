package com.gearlles.ga.core.fitness;

public class Rastrigin extends FitnessFunction
{
    private static final double LOWER_LIMIT = -5.12d;
    private static final double UPPER_LIMIT = 5.12d;
    
    public Rastrigin()
    {
	super(LOWER_LIMIT, UPPER_LIMIT);
    }

    public double evaluate(double[] gene)
    {
	double res = 10 * gene.length;
	for (int i = 0; i < gene.length; i++)
		res += gene[i] * gene[i] - 10
				* Math.cos(2 * Math.PI * gene[i]);
	return res;
    }

}
