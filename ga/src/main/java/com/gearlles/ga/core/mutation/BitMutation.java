package com.gearlles.ga.core.mutation;

import java.util.Random;

import com.gearlles.ga.core.Chromosome;

public class BitMutation implements MutationInterface
{
    private static Random rand = new Random();

    public Chromosome mutate(Chromosome chromosome)
    {
	double lowerLimit = Chromosome.fitnessFunction.LOWER_LIMIT;
	double upperLimit = Chromosome.fitnessFunction.UPPER_LIMIT;

	double[] gene = chromosome.getGene();

	for (int i = 0; i < gene.length; i++)
	{
	    // 1/L is the bit mutation probabily (where L is the length of the gene)
	    if (rand.nextDouble() > 1d / gene.length)
	    {
		continue;
	    }

	    double delta = 0;
	    boolean negativeDelta = rand.nextBoolean();
	    
	    if (negativeDelta)
	    {
		delta = lowerLimit + (gene[i] - lowerLimit) * rand.nextDouble();
		delta *= -1;
	    } 
	    else
	    {
		delta = gene[i] + (upperLimit - gene[i]) * rand.nextDouble();
	    }
	    
	    gene[i] += delta;
	}

	return new Chromosome(gene);
    }

}
