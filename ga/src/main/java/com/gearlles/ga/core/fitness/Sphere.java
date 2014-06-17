package com.gearlles.ga.core.fitness;

public class Sphere extends FitnessFunction
{
    // FIXME adjust the limit
    private static final double LOWER_LIMIT = -10d;
    private static final double UPPER_LIMIT = 10d;
    
    public Sphere()
    {
	super(LOWER_LIMIT, UPPER_LIMIT);
    }

    public double evaluate(double[] gene)
    {
	double res = 0;
	for (int i = 0; i < gene.length; i++)
		res += Math.pow(gene[i], 2);
	return res;
    }

}
